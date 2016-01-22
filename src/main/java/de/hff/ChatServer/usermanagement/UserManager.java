package de.hff.ChatServer.usermanagement;

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

import de.hff.ChatServer.messagehandling.Message;
import de.hff.ChatServer.messagehandling.MessageType;
import de.hff.ChatServer.messagehandling.messageinput.MessageListener;
import de.hff.ChatServer.messagehandling.messageoutput.MessageSender;
import de.hff.ChatServer.server.Connection;

public class UserManager {

	private List<User> userList;
	private List<User> onlineUsers;
	private String fileName;
	private int lastID;

	public UserManager(String fileName) {
		this.fileName = fileName;
		try {
			userList = readUserData();
			lastID = readLastID();
		} catch (IOException e) {
			e.printStackTrace();
		}
		onlineUsers = new ArrayList<>();
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
		lastID = 0;
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
		lastID++;
		saveUserData();
	}

	public User addUser(String userName, String password, int id) {
		User user = new User(userName, getSHA(password), id);
		addUser(user);
		return user;
	}

	public User addUser(String userName, String password) {
		return addUser(userName, password, lastID);
	}

	public boolean isUserOnline(User user) {
		return onlineUsers.contains(user);
	}

	public int getNextID() {
		return lastID;
	}

	public void logout(User user) {

		if (user.getMessageListener() != null) {

			try {
				if (user.getConnection().getOutputstream() != null) {
					user.getConnection().getOutputstream().close();
					user.getConnection().getInputstream().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			user.setConnection(null);
			user.setMessageListener(null);
			user.setMessageSender(null);
		}

		onlineUsers.remove(user);
		if (user.isLoggedIn()) {
			System.out.println("Der Benutzer " + user.getName() + " hat sich ausgeloggt!");
			sendToAllUsers(new Message("-> " + user.getName() + " hat sich ausgeloggt!", MessageType.CHAT_MESSAGE));
		} else {
			System.out.println("Client hat die Verbindung getrennt");
		}
		user.logout();
	}

	public User login(String username, String password, Connection connection, MessageListener messageListener,
			MessageSender messageSender) {

		User user = getUser(username);
		if (user != null && user.getPassword().equals(getSHA(password))) {
			if (onlineUsers.contains(user)) {
				throw new UserAlreadyLoggedInException("Benutzer ist bereits eingeloggt!");
			} else {
				user.setConnection(connection);
				user.setMessageListener(messageListener);
				user.setMessageSender(messageSender);
				onlineUsers.add(user);
				user.login();

				System.out.println("Der Benutzer " + user.getName() + " hat sich eingeloggt!");
				sendToAllUsers(new Message("-> " + username + " hat sich eingeloggt!", MessageType.CHAT_MESSAGE), user);
			}
		} else {
			throw new UserException("Benutzer nicht gefunden oder Passwort falsch!");
		}
		return user;
	}

	public void register(String username, String password) throws IOException {

		for (User user : userList) {
			if (user.getName().equalsIgnoreCase(username)) {
				throw new UserAlreadyExistsException("Benutzer bereits registriert!");
			}
		}

		if (username.length() > 10 || username.length() < 2) {
			throw new UserException("Benutzername muss zwischen 2 und 10 Zeichen lang sein!");
		}

		lastID++;
		userList.add(new User(username, getSHA(password), lastID));
		FileOutputStream out = new FileOutputStream(createEmtpyUserFile());
		writeUserData(out);
		out.close();
		System.out.println("User " + username + " hat sich soeben registriert!");
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
		sendToAllUsers(message, null);
	}

	public void sendToAllUsers(Message message, User user) {
		for (User u : onlineUsers) {
			if (!u.equals(user)) {
				u.getMessageSender().sendMessage(message);
			}
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