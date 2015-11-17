package server;

import java.net.ServerSocket;

public class Server {

	public static void main(String[] args) throws Throwable {

		System.out.println("Start ChatServer");

		new ConnectionListener(new ServerSocket(12345));
	}
}
