package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import usermanager.User;

public class MessageListener extends Thread {
	public static final int CHAT_MESSAGE = 1;
	public static final int LOGIN = 3;
	public static final int END_OF_MESSAGE = 4;

	private Socket socket;
	private User user;

	public MessageListener(Socket socket) {
		this.socket = socket;
		this.start();
	}

	@Override
	public void run() {

		while (!socket.isClosed()) {

			try {
				InputStream in = new BufferedInputStream(socket.getInputStream());
				int first = in.read();

				StringBuilder builder = new StringBuilder();
				int read;
				while ((read = in.read()) != END_OF_MESSAGE) {
					builder.append((char) read);
				}

				switch (first) {
				case CHAT_MESSAGE:

					break;
				case LOGIN:
					String[] split = builder.toString().split("\\" + (char) ((byte) 0));

					break;
				}

			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e1) {
				}
			}

		}
	}
}
