package messagehandling;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import chatshared.Messages;
import server.Connection;

public class MessageSender extends Thread {

	private Connection connection;

	public MessageSender(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void run() {

		try {
			OutputStream out = new BufferedOutputStream(connection.getSocket().getOutputStream());

			while (!connection.getSocket().isClosed()) {
				try {
					Message message = connection.getBuffer().poll(5, TimeUnit.SECONDS);

					if (message != null) {
						out.write(message.getType().getTypeNumber());

						out.write(message.getMessage());

						out.write(Messages.END_OF_MESSAGE);

						out.flush();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
