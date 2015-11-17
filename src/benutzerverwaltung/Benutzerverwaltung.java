package benutzerverwaltung;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
			if (!s.isEmpty()) {
				String[] userDaten = s.split(";");
				user.add(new User(userDaten[1], userDaten[2], Integer.parseInt(userDaten[0])));
			}
		}
		reader.close();
		return user;

	}

	private int liesLetzteId() throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(dateiErstellen()));
		String s;
		if ((s = reader.readLine()) != null) {
			s.trim();
			if (!s.isEmpty()) {
				reader.close();
				return Integer.parseInt(s);
			}
		}
		reader.close();
		return 0;
	}

	private File dateiErstellen() throws IOException {
		File datei = new File(dateiname);
		if (!datei.exists()) {
			PrintWriter writer = new PrintWriter(new FileWriter(datei));
			writer.println(0);
			writer.close();
		}
		return datei;
	}

	public void benutzerRegistrieren(String name, String passwort) throws BenutzerSchonVorhandenException, IOException {
		for (User user : benutzer) {
			if (user.getName().equals(name)) {
				throw new BenutzerSchonVorhandenException();
			}
		}
		letzteId++;
		System.out.println(benutzer.size());
		benutzer.add(new User(name, getMD5(passwort), letzteId));
		schreibeUserDaten(new FileOutputStream(dateiErstellen()));

	}

	public String getMD5(String s) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
		} catch (NoSuchAlgorithmException e) {
		}
		return new BigInteger(1, m.digest()).toString(16);
	}

	public static void main(String[] args) throws Throwable {
		Benutzerverwaltung verw = new Benutzerverwaltung("users.csv");

		for (int i = 0; i < 20; i++) {
			System.out.println(verw.letzteId);
			verw.benutzerRegistrieren("test" + i, "test");
		}

	}

	public void benutzerLogin(String name, String passwort, Socket s) throws UserException {
		User o = new User(name, getMD5(passwort), -1);
		if (benutzer.contains(o)) {
			User u = benutzer.get(benutzer.indexOf(o));
			u.login(s);
		} else {
			throw new UserException();
		}

	}
}
