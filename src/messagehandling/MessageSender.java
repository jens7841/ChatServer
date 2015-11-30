package messagehandling;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import server.Connection;

public class MessageSender extends Thread {

	private Connection connection;
	private List<Message> messages;
	private Semaphore lock;

	public MessageSender(Connection connection) {
		this.connection = connection;
		this.messages = new ArrayList<>();
		this.lock = new Semaphore(1);
	}

	@Override
	public void run() {

		try {
			lock.acquire();

			DataOutputStream out = new DataOutputStream(
					new BufferedOutputStream(connection.getSocket().getOutputStream()));

			while (!connection.getSocket().isClosed()) {
				while (messages.size() > 0) {
					Message message = messages.get(0);
					messages.remove(0);

					if (message != null) {
						out.write(message.getType().getTypeNumber());

						out.writeInt(message.getMessage().length);

						out.write(message.getMessage());

						out.flush();
					}
				}

				lock.acquire();
			}
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void send(Message message) {
		messages.add(message);
		lock.release();
	}
}
