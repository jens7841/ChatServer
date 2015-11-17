package usermanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Usermanager {

	private ArrayList<User> user;
	private int lastID;
	private String filename;

	public Usermanager(String filename) {
		this.filename = filename;
		try {
			this.user = (ArrayList<User>) readUserData();
			this.lastID = readLastID();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeUserData(OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		writer.println(lastID);

		for (User user : user) {
			StringBuilder builder = new StringBuilder();
			builder.append(user.getID());
			builder.append(';');
			builder.append(user.getName());
			builder.append(';');
			builder.append(user.getPassword());
			writer.println(builder.toString());
		}

		writer.flush();
	}

	private List<User> readUserData() throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(createFile()));
		reader.readLine();
		List<User> user = new ArrayList<>();
		String s;
		while ((s = reader.readLine()) != null) {
			s.trim();
			if (!s.isEmpty()) {
				String[] userData = s.split(";");
				user.add(new User(userData[1], userData[2], Integer.parseInt(userData[0])));
			}
		}
		reader.close();
		return user;

	}

	private int readLastID() throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(createFile()));
		String s;
		if ((s = reader.readLine()) != null) {
			s.trim();
			if (!s.isEmpty()) {
				reader.close();
				return Integer.parseInt(s);
			}
		}
		reader.close();
		return 0;
	}

	private File createFile() throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			writer.println(0);
			writer.close();
		}
		return file;
	}

	public void userRegistration(String name, String password) throws UserAlreadyExistsException, IOException {
		for (User user : user) {
			if (user.getName().equals(name)) {
				throw new UserAlreadyExistsException();
			}
		}
		lastID++;
		System.out.println(user.size());
		user.add(new User(name, getMD5(password), lastID));
		writeUserData(new FileOutputStream(createFile()));

	}

	public String getMD5(String s) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
		} catch (NoSuchAlgorithmException e) {
		}
		return new BigInteger(1, m.digest()).toString(16);
	}

	public static void main(String[] args) throws Throwable {
		Usermanager mgr = new Usermanager("users.csv");

		for (int i = 0; i < 20; i++) {
			System.out.println(mgr.lastID);
			mgr.userRegistration("test" + i, "test");
		}

	}

	public void userLogin(String name, String password, Socket s) throws UserException {
		User o = new User(name, getMD5(password), -1);
		if (user.contains(o)) {
			User u = user.get(user.indexOf(o));
			u.login(s);
		} else {
			throw new UserException();
		}

	}
}
