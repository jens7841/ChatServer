package benutzerverwaltung;

import java.util.ArrayList;
import java.util.List;

public class Benutzerverwaltung {

	private ArrayList<User> benutzer;

	public Benutzerverwaltung(List<User> benutzer) {
		this.benutzer = new ArrayList<>();

		this.benutzer.addAll(benutzer);
	}

	public Benutzerverwaltung() {
		this(new ArrayList<>());
	}
}
