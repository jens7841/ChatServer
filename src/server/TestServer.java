package server;

import java.io.IOException;

public class TestServer {

	public static void main(String[] args) {
		System.out.println("Server");
		Server s = new Server(12345, "users.csv");
		try {
			s.startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
