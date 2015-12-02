package messagehandling;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import server.Connection;

public class MultiThreadedMessageSender extends Thread implements MessageSender {

	private Connection connection;
	private Queue<Message> messages;
	private Semaphore lock;

	public MultiThreadedMessageSender(Connection connection) {
		this.messages = new LinkedList<>();
		this.connection = connection;
		this.lock = new Semaphore(1);
	}

	@Override
	public void sendMessage(Message message) {
		messages.add(message);
		if (!isAlive()) {
			start();
		}
		lock.release();
	}

	@Override
	public void run() {
		try {
			DataOutputStream out = new DataOutputStream(
					new BufferedOutputStream(connection.getSocket().getOutputStream()));

			while (!connection.getSocket().isClosed()) {
				while (messages.size() > 0) {

					Message message = messages.poll();

					if (message != null) {
						out.write(message.getType().getTypeNumber());
						out.writeInt(message.getMessage().length);
						out.write(message.getMessage());
						out.flush();
					}
				}
				lock.acquire();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
