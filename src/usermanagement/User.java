package usermanagement;

import messagehandling.Connection;

public class User {

	private String name;
	private String password;
	private int id;
	private Connection connection;

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

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
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

}