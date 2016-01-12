package messagehandling;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class ThreadedMessageSender extends Thread implements MessageSender {

	private OutputStream out;
	private Queue<Message> messages;
	private Semaphore lock;

	public ThreadedMessageSender(OutputStream out) {
		this.out = out;
		messages = new LinkedList<>();
		lock = new Semaphore(1);
	}

	@Override
	public void sendMessage(Message message) {
		messages.add(message);
		if (!isAlive()) {
			start();
		}
	}

	@Override
	public void run() {
		while (messages.size() > 0) {
			Message message = messages.poll();
			try {
				lock.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}