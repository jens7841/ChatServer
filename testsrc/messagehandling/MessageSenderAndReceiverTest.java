package messagehandling;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageSenderAndReceiverTest {

	private static boolean setupIsDone = false;
	private ServerSocket serverSocket;
	private Socket server;
	private Socket client;

	@Before
	public void setUp() {
		if (!setupIsDone) {
			try {
				serverSocket = new ServerSocket(12345);
				setupIsDone = true;

				new Thread() {
					@Override
					public void run() {
						try {
							client = new Socket("", 12345);
						} catch (UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					};
				}.start();

				server = serverSocket.accept();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void test() {
		MessageSender sender = new MultiThreadedMessageSender(new Connection(client));

		SimpleMessageHandler handler = new SimpleMessageHandler();
		MessageReceiver receiver = new MessageReceiver(handler, new Connection(server));
		receiver.start();

		byte[] message = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		sender.sendMessage(new Message(message, MessageType.CHAT_MESSAGE));

		Assert.assertArrayEquals(message, handler.getMessage().getBytes());

	}
}