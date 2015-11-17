package benutzerverwaltung;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Benutzerverwaltung {

	private ArrayList<User> benutzer;
	private int letzteId;

	public Benutzerverwaltung(List<User> benutzer) {
		this.benutzer = new ArrayList<>();

		this.benutzer.addAll(benutzer);
	}

	public Benutzerverwaltung() {
		this(new ArrayList<>());
	}

	public void schreibeUserDaten(OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		writer.println(letzteId);

		for (User user : benutzer) {
			StringBuilder builder = new StringBuilder();
			builder.append(user.getId());
			builder.append(';');
			builder.append(user.getName());
			builder.append(';');
			builder.append(user.getPasswort());
			writer.println(builder.toString());
		}

		writer.flush();
	}

	public void benutzerRegistrieren(String name, String passwort) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(passwort.getBytes(), 0, passwort.length());
		String verschluesseltesPW = new BigInteger(1, m.digest()).toString(16);
		benutzer.add(new User(name, verschluesseltesPW, benutzer.size()));
	}
}
