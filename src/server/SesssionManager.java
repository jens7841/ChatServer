package server;

import java.io.IOException;
import java.net.Socket;

import usermanager.User;
import usermanager.Usermanager;

public class SesssionManager implements Runnable {
	private User user;
	private Usermanager usermanager;
	private Socket socket;

	public SesssionManager(Usermanager usermanager, Socket socket) {
		this.usermanager = usermanager;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			socket.getOutputStream().write(100);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
