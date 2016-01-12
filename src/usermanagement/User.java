package usermanagement;

import server.ConnectionHandler;

public class User {

	private String name;
	private String password;
	private int id;

	private ConnectionHandler connectionHandler;

	public User(String name, String password, int id) {
		this.name = name;
		this.password = password;
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

	public ConnectionHandler getConnectionHandler() {
		return connectionHandler;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof User) {
			User user = (User) obj;
			if (user.getName().equals(this.getName()) && user.getPassword().equals(this.getPassword())) {
				return true;
			}
		}

		return false;
	}

	public void setConnectionHandler(ConnectionHandler con) {
		this.connectionHandler = con;
	}

}