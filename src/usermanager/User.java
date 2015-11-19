package usermanager;

import java.io.IOException;
import java.net.Socket;

public class User {

	private String name;
	private String password;
	private int id;
	private Socket socket;

	public User(String name, String passwort, int id) {
		this.name = name;
		this.password = passwort;
		this.id = id;
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

	public void login(Socket s) {
		this.socket = s;
	}

	public void logout() throws IOException {
		try {
			socket.close();
		} finally {
			socket = null;
		}
	}

	public boolean isOnline() {
		if (getSocket() != null)
			return !getSocket().isConnected();
		return false;
	}

	public Socket getSocket() {
		return socket;
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
