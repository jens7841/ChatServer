package benutzerverwaltung;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
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

	public void liesUserDaten() throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(dateiErstellen()));
		int id = Integer.parseInt(reader.readLine().trim());

	}

	public File dateiErstellen() throws IOException {
		File file = new File("users.csv");
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	public void benutzerRegistrieren(String name, String passwort)
			throws BenutzerSchonVorhandenException, FileNotFoundException {
		try {
			for (User user : benutzer) {
				if (user.getName().equals(name)) {
					throw new BenutzerSchonVorhandenException();
				}
			}
			letzteId++;
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(passwort.getBytes(), 0, passwort.length());
			benutzer.add(new User(name, new BigInteger(1, m.digest()).toString(16), letzteId));
			try {
				schreibeUserDaten(new FileOutputStream(dateiErstellen()));
			} catch (IOException e) {
			}
		} catch (NoSuchAlgorithmException e) {
		}
	}
}
