package server;

import java.io.IOException;

import usermanager.User;

public class Test2 extends Thread {

	private User user;

	public Test2(User user) {
		this.user = user;
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			try {
				user.getOutputStream().write(i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
