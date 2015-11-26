package usermanager;

import java.io.IOException;
import java.io.OutputStream;
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
		return socket;
	}

	public void setSocket(Socket s) {
		this.socket = s;
	}

	public void logout() throws IOException {
		System.out.println("User: " + this.getName() + " hat sich ausgeloggt!");
		try {
			socket.close();
		} finally {
			socket = null;
		}
	}

	public boolean isOnline() {
		if (socket != null)
			return !socket.isClosed();
		return false;
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
