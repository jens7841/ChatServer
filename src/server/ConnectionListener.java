package server;

import java.net.ServerSocket;

import usermanager.Usermanager;

public class ConnectionListener {

	ServerSocket server;

	private Usermanager mgr = new Usermanager("users.csv");

	public ConnectionListener(ServerSocket server) throws Throwable {
		this.server = server;
		waitForConnection();

	}

	public static int i = 1;

	private void waitForConnection() throws Exception {
		i++;
		mgr.userRegistration("test" + i, "test");
		mgr.userLogin("test" + i, "test", server.accept());

		System.out.println("Waiting for new connection...");
		waitForConnection();

	}
}
