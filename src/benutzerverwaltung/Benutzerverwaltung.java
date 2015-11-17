package benutzerverwaltung;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Benutzerverwaltung {

	private ArrayList<User> benutzer;
	private int letzteId;
	private String dateiname;

	public Benutzerverwaltung(String dateiname) {
		this.dateiname = dateiname;
		try {
			this.benutzer = (ArrayList<User>) liesUserDaten();
			this.letzteId = liesLetzteId();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.benutzer.addAll(benutzer);
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

	private List<User> liesUserDaten() throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(dateiErstellen()));
		reader.readLine();
		List<User> user = new ArrayList<>();
		String s;
		while ((s = reader.readLine()) != null) {
			s.trim();
			String[] userDaten = s.split(";");
			user.add(new User(userDaten[1], userDaten[2], Integer.parseInt(userDaten[0])));
		}
		reader.close();
		return user;

	}

	private int liesLetzteId() throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(dateiErstellen()));
		String s;
		if ((s = reader.readLine()) != null) {
			s.trim();
			reader.close();
			return Integer.parseInt(s);
		}
		reader.close();
		return 0;
	}

	private File dateiErstellen() throws IOException {
		File datei = new File(dateiname);
		if (!datei.exists()) {
			datei.createNewFile();
		}
		return datei;
	}

	public void benutzerRegistrieren(String name, String passwort) throws BenutzerSchonVorhandenException {
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
		} catch (NoSuchAlgorithmException e) {
		}
	}

	public String getMD5(String s) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(), 0, s.length());
		return new BigInteger(1, m.digest()).toString(16);
	}

	public static void main(String[] args) throws Throwable {
		Benutzerverwaltung verw = new Benutzerverwaltung("users.csv");

		verw.benutzerRegistrieren("test", "test");

		verw.benutzerLogin("test", "test", null);

	}

	public void benutzerLogin(String name, String passwort, Socket s) throws UserException {
		User o = new User(name, passwort, -1);
		if (benutzer.contains(o)) {
			User u = benutzer.get(benutzer.indexOf(o));
			u.login(s);
		} else {
			throw new UserException();
		}

	}
}
