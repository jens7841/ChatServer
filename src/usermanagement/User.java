package usermanagement;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import server.Connection;

public class User {

	private String name;
	private String password;
	private int id;
	private Connection connection;
	private boolean isUploadingFile;

	public User(String name, String passwort, int id) {
		this.name = name;
		this.password = passwort;
		this.id = id;
	}

	public synchronized OutputStream getOutputStream() {
		try {
			return getSocket().getOutputStream();
		} catch (IOException e) {
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public int getID() {
		return id;
	}

	public Socket getSocket() {
		return connection.getSocket();
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void logout() throws IOException {
		System.out.println("User: " + this.getName() + " hat sich ausgeloggt!");
		try {
			connection.getSocket().close();
		} finally {
			connection = null;
		}
	}

	public boolean isOnline() {
		if (connection != null)
			return !connection.getSocket().isClosed();
		return false;
	}

	public boolean isUploadingFile() {
		return isUploadingFile;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User user = (User) obj;
			if (user.getName().equals(getName()) && user.getPassword().equals(getPassword())) {
				return true;
			}
		}
		return false;
	}
}
