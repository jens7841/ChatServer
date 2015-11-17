package benutzerverwaltung;

import java.io.IOException;
import java.net.Socket;

public class User {

	private String name;
	private String passwort;
	private int id;
	private Socket socket;

	public User(String name, String passwort, int id) {
		this.name = name;
		this.passwort = passwort;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getPasswort() {
		return passwort;
	}

	public int getId() {
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

	public boolean istOnline() {
		return socket != null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			if (((User) obj).getName().equals(getName()) && ((User) obj).getPasswort().equals(getPasswort())) {
				return true;
			}
		}
		return false;
	}
}
