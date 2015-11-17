package benutzerverwaltung;

public class User {

	private String name;
	private String passwort;
	private int id;

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
}
