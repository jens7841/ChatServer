package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginHandler extends Thread {

	private Socket socket;

	public LoginHandler(Socket socket) {
		this.socket = socket;
		this.start();
	}

	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
			out.write(3);
			out.print("login");
			out.write(4);
			out.flush();

			InputStream in = new BufferedInputStream(socket.getInputStream());

			StringBuilder builder = new StringBuilder();

			int first = in.read();
			int read;
			while ((read = in.read()) != 4) {
				builder.append((char) read);
			}

			String usernamePasswort = builder.toString();

			String username = usernamePasswort.substring(0, usernamePasswort.indexOf((byte) 0));
			String passwort = usernamePasswort.substring(usernamePasswort.indexOf((byte) 0) + 1);

			System.out.println(username);
			System.out.println(passwort);

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
