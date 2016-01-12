package usermanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import messagehandling.Connection;
import messagehandling.Message;
import messagehandling.MultiThreadedMessageSender;

public class UserManager {

	private List<User> userList;
	private List<User> onlineUsers;
	private String fileName;
	private int nextID;

	public UserManager(String fileName) {
		this.fileName = fileName;
		try {
			userList = readUserData();
			nextID = readLastID();
		} catch (IOException e) {
			e.printStackTrace();
		}
		onlineUsers = new ArrayList<>();
	}

	public void writeUserData(OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		writer.println(nextID);

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

	private void saveUserData() {
		File file = new File(fileName);

		try {
			FileOutputStream out = new FileOutputStream(file);
			writeUserData(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<User> readUserData() throws IOException {

		LineNumberReader reader = new LineNumberReader(new FileReader(getUserFile()));
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

	private File getUserFile() {
		File file = new File(fileName);

		if (!file.exists()) {
			createEmtpyUserFile();
		}

		return file;
	}

	public void clearUserList() {
		userList.clear();
		onlineUsers.clear();
		nextID = 0;
		createEmtpyUserFile();
	}

	private File createEmtpyUserFile() {
		File file = new File(fileName);

		try {

			PrintWriter writer = new PrintWriter(new FileWriter(file));
			writer.println(0);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private int readLastID() throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(getUserFile()));
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

	public void addUser(User user) {
		if (getUser(user.getName()) != null) {
			throw new UserAlreadyExistsException("User " + user.getName() + " does already exists!");
		}
		userList.add(user);
		nextID++;
		saveUserData();
	}

	public User addUser(String userName, String password, int id) {
		User user = new User(userName, getSHA(password), id);
		addUser(user);
		return user;
	}

	public User addUser(String userName, String password) {
		return addUser(userName, password, nextID);
	}

	public boolean isUserOnline(User user) {
		return onlineUsers.contains(user);
	}

	public int getNextID() {
		return nextID;
	}

	public void logout(User user) {

		if (user.getConnection() != null) {

			try {
				if (user.getConnection().getOut() != null) {
					user.getConnection().getOut().close();
					user.getConnection().getIn().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			user.setConnection(null);
		}

		onlineUsers.remove(user);
	}

	public void login(String username, String password, Connection con) {

		User user = getUser(username);
		if (user != null && user.getPassword().equals(password)) {
			user.setConnection(con);
			onlineUsers.add(user);
		}
	}

	public User getUser(Connection connection) {

		for (User user : userList) {
			if (connection.equals(user.getConnection())) {
				return user;
			}
		}

		return null;
	}

	public User getUser(String name) {

		for (User user : userList) {
			if (user.getName().equalsIgnoreCase(name)) {
				return user;
			}
		}

		return null;
	}

	public void sendToAllUsers(Message message) {
		for (User user : onlineUsers) {
			new MultiThreadedMessageSender(user.getConnection()).sendMessage(message);
		}
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

	public List<User> getOnlineUsers() {
		return onlineUsers;
	}

}