package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import usermanager.User;

public class UserMessageListener extends Thread {

	private User user;

	public UserMessageListener(User user) {
		this.user = user;
		this.start();
	}

	@Override
	public void run() {

		while (user.isOnline()) {

			InputStream in;
			try {
				in = new BufferedInputStream(user.getSocket().getInputStream());
				int read;
				while ((read = in.read()) != -1) {
					System.out.println(read);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
