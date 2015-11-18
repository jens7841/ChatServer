package server;

import java.io.IOException;

public class Test {

	public static void main(String[] args) {
		Server s = new Server(12345, "users.csv");
		try {
			s.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
