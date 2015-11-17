package server;

import java.net.ServerSocket;

public class Server {

	public static void main(String[] args) throws Throwable {

		System.out.println("Starte ChatServer");

		new VerbindungsListener(new ServerSocket(12345));
	}
}
