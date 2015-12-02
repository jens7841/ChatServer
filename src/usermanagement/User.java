package usermanagement;

public class User {

	private String name;
	private String password;
	private int id;

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

	public int getId() {
		return id;
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