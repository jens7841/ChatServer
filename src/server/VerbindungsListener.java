package server;

import java.net.ServerSocket;

import benutzerverwaltung.Benutzerverwaltung;

public class VerbindungsListener {

	ServerSocket server;

	private Benutzerverwaltung verw = new Benutzerverwaltung("users.csv");

	public VerbindungsListener(ServerSocket server) throws Throwable {
		this.server = server;
		warteAufVerbindung();

	}

	public static int i = 1;

	private void warteAufVerbindung() throws Exception {
		i++;
		verw.benutzerRegistrieren("test" + i, "test");
		verw.benutzerLogin("test" + i, "test", server.accept());

		System.out.println("Warte auf neue verbindung...");
		warteAufVerbindung();

	}
}
