package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import chatshared.Messages;

public class TestClient {

	public static void main(String[] args) throws Exception {
		System.out.println("Client");
		Socket s = new Socket("localhost", 12345);
		int first;
		int read;

		InputStream in = new BufferedInputStream(s.getInputStream());
		PrintWriter w = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));

		String user = "test";
		String passwort = "test";

		w.write(Messages.LOGIN);
		w.print(user);
		w.write(Messages.DELIMITER);
		w.print(passwort);
		w.write(Messages.END_OF_MESSAGE);

		w.flush();

		first = in.read();
		StringBuilder builder = new StringBuilder();
		while ((read = in.read()) != Messages.END_OF_MESSAGE) {
			builder.append((char) read);
		}

		if (first == Messages.ERROR_MESSAGE) {
			System.out.println("FEHLER: " + builder);
		} else if (first == Messages.SUCCESS_MESSAGE) {
			System.out.println("Erfolgreich: " + builder);
		}

		w.write(Messages.CHAT_MESSAGE);
		w.print("Hi wie geht's?");
		w.write(Messages.END_OF_MESSAGE);
		w.flush();

		first = in.read();
		while ((read = in.read()) != Messages.END_OF_MESSAGE) {
			System.out.print((char) read);
		}
		System.out.println();
		first = in.read();
		while ((read = in.read()) != Messages.END_OF_MESSAGE) {
			System.out.print((char) read);
		}
		System.out.println();
		first = in.read();
		while ((read = in.read()) != Messages.END_OF_MESSAGE) {
			System.out.print((char) read);
		}
		System.out.println();
		first = in.read();
		while ((read = in.read()) != Messages.END_OF_MESSAGE) {
			System.out.print((char) read);
		}
		System.out.println();

	}
}