package de.hff.ChatServer.usermanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.hff.ChatShared.connectionhandling.Connection;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.messageinput.MessageReceiver;
import de.hff.ChatShared.messagehandling.messageoutput.MessageSender;

public class UserManager {

	private String fileName;

	private int lastID;

	private List<User> userList;
	private List<User> onlineUsers;

	public UserManager(String fileName) {
		this.fileName = fileName;
		this.userList = new ArrayList<>();
		this.onlineUsers = new ArrayList<>();
	}

	public void saveUserData() throws FileNotFoundException, IOException {
		PrintWriter writer = new PrintWriter(new FileOutputStream(getUserFile()));

		writer.println(lastID);

		for (User user : userList) {
			StringBuilder builder = new StringBuilder();
			builder.append(user.getId());
			builder.append(';');
			builder.append(user.getName());
			builder.append(';');
			builder.append(user.getPassword());
			writer.println(builder.toString());
		}

		writer.close();

	}

	public void loadUsersFromFile() throws FileNotFoundException, IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(getUserFile()));

		lastID = Integer.parseInt(reader.readLine());
		userList = new ArrayList<>();
		String line;
		while ((line = reader.readLine()) != null) {
			if (!line.isEmpty()) {
				String[] userData = line.split("\\;");
				if (userData.length == 3) {
					userList.add(new User(userData[1], userData[2], Integer.parseInt(userData[0])));
				}
			}
		}
		reader.close();

	}

	private File getUserFile() throws IOException {
		File userDatabase = new File(fileName);
		if (userDatabase.isDirectory())
			throw new IOException("UserFile is a directory!");

		if (!userDatabase.exists())
			createEmptyUserFile(userDatabase);

		return userDatabase;
	}

	public void createEmptyUserFile(File file) throws IOException {
		PrintWriter writer = new PrintWriter(new FileWriter(file));
		writer.println(0);
		writer.close();
	}

	public void logout(User user) {
		if (user.getConnection() != null) {
			if (user.getConnection().getOutputstream() != null) {
				try {
					user.getConnection().getOutputstream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (user.getConnection().getInputstream() != null) {
				try {
					user.getConnection().getInputstream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		user.setConnection(null);
		user.setMessageReceiver(null);
		user.setMessageSender(null);
		onlineUsers.remove(user);

		// SYSO
		System.out.println("-> Logout: " + user.getName());

	}

	public User loginUser(String username, String password, Connection connection, MessageReceiver messageReceiver,
			MessageSender messageSender) {

		password = getHash(password);

		if (onlineUsers.contains(getUser(username)))
			throw new UserAlreadyLoggedInException("Benutzer bereits eingeloggt!");

		for (User user : userList) {
			if (user.getName().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
				User newUser = new User(username, password, lastID, connection, messageReceiver, messageSender);
				onlineUsers.add(newUser);
				return newUser;
			}
		}
		return null;
	}

	public void registerUser(String username, String password) throws FileNotFoundException, IOException {

		if (username.length() > 10 | username.length() < 2) {
			throw new UserException("Benutzername muss zwischen 2 und 10 Zeichen lang sein!");
		}

		for (User user : userList) {
			if (user.getName().equalsIgnoreCase(username)) {
				throw new UserAlreadyExistsException("Benutzer bereits registriert!");
			}
		}

		lastID++;
		userList.add(new User(username, getHash(password), lastID));
		saveUserData();
	}

	public boolean isUserOnline(User user) {
		return onlineUsers.contains(user);
	}

	public User getUser(String username) {
		for (User user : userList) {
			if (user.getName().equalsIgnoreCase(username))
				return user;
		}

		return null;
	}

	public void sendToAllUsers(Message message, User from, User... blacklist) throws IOException {
		for (User user : onlineUsers) {
			if (blacklist.length == 0) {
				user.getMessageSender().sendMessage(message);
			} else {

				for (int i = 0; i < blacklist.length; i++) {
					User listedUser = blacklist[i];

					if (user.equals(listedUser))
						break;
					if (i == blacklist.length - 1) {
						user.getMessageSender().sendMessage(message);
					}
				}

			}
		}
	}

	public String getHash(String input) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("SHA-512");
			m.update(input.getBytes(), 0, input.length());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return new BigInteger(1, m.digest()).toString(16);
	}

}