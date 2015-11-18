package server;

import java.net.Socket;

import usermanager.User;
import usermanager.Usermanager;

public class SesssionManager implements Runnable {
	private User user;
	private Usermanager usermanager;
	private Socket socket;

	public SesssionManager(Usermanager usermanager) {
		this.usermanager = usermanager;
	}

	@Override
	public void run() {

	}

}
