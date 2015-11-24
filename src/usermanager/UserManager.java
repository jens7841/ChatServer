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

public class UserManager {

	private List<User> userList;
	private int lastID;
	private String filename;

	public UserManager(String filename) {
		this.filename = filename;
		try {
			this.userList = readUserData();
			this.lastID = readLastID();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeUserData(OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		writer.println(lastID);

		for (User user : userList) {
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
		LineNumberReader reader = new LineNumberReader(new FileReader(createEmtpyUserFile()));
		reader.readLine();
		List<User> user = new ArrayList<>();
		String s;
		while ((s = reader.readLine()) != null) {
			if (!s.isEmpty()) {
				String[] userData = s.split(";");
				if (userData.length == 3) {
					user.add(new User(userData[1], userData[2], Integer.parseInt(userData[0])));
				}
			}
		}
		reader.close();
		return user;

	}

	private int readLastID() throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(createEmtpyUserFile()));
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

	private File createEmtpyUserFile() throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			writer.println(0);
			writer.close();
		}
		return file;
	}

	public void registerUser(String name, String password)
			throws UserAlreadyExistsException, UserException, IOException {
		for (User user : userList) {
			if (user.getName().equals(name)) {
				throw new UserAlreadyExistsException();
			}
		}

		if (name.length() > 10 || name.length() < 2) {
			throw new UserException("Benutzername muss zwischen 2 und 10 Zeichen lang sein!");
		}

		lastID++;
		userList.add(new User(name, getSHA(password), lastID));
		FileOutputStream out = new FileOutputStream(createEmtpyUserFile());
		writeUserData(out);
		out.close();
		System.out.println("User " + name + " hat sich soeben registriert!");

	}

	public User loginUser(String name, String password, Socket s) throws UserException {
		User o = new User(name, getSHA(password), -1);
		User u;
		if (userList.contains(o)) {
			if (!(u = userList.get(userList.indexOf(o))).isOnline()) {
				u.setSocket(s);
				System.out.println("User: " + name + " hat sich eingeloggt!");
				return u;
			} else {
				throw new UserAlreadyLoggedInException();
			}
		} else {
			throw new UserException("Username/password wrong or user not registered");
		}
	}

	public List<User> getUserList() {
		return userList;
	}

	public String getSHA(String s) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("SHA-512");
			m.update(s.getBytes(), 0, s.length());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new BigInteger(1, m.digest()).toString(16);
	}

}
