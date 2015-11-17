package server;

import java.net.ServerSocket;
import java.net.Socket;

import usermanager.Usermanager;

public class VerbindungsListener {

	ServerSocket server;

	private Usermanager verw = new Usermanager("users.csv");

	public VerbindungsListener(ServerSocket server) throws Throwable {
		this.server = server;
		warteAufVerbindung();

	}

	public static int i = 1;

	private void warteAufVerbindung() throws Exception {
		Socket s = server.accept();

		s.getOutputStream().write(1);

		s.getOutputStream().write(-1);
		System.out.println("Warte auf neue verbindung...");
		warteAufVerbindung();

	}
}
