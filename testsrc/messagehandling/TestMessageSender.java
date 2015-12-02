package messagehandling;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;

import server.Connection;

public class TestMessageSender {

	@Test
	public void testSending() {
		try {

			ServerSocket server = new ServerSocket(12300);

			byte[] expected = new byte[] { (byte) MessageType.CHAT_MESSAGE.getTypeNumber(), 1, 2, 3, 4, 5, 6, 7, 8, 9,
					10 };
			byte[] message = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

			new Thread() {
				@Override
				public void run() {
					SimpleMessageSender sender = new SimpleMessageSender(12300, "");
					sender.sendMessage(new Message(message, MessageType.CHAT_MESSAGE));
				};
			}.start();

			Socket socket = server.accept();

			byte[] actual = new byte[message.length + 1];

			DataInputStream in = new DataInputStream(socket.getInputStream());
			in.readFully(actual);

			Assert.assertArrayEquals(expected, actual);

			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMultiThreadedMessageSender() {
		try {

			ServerSocket server = new ServerSocket(12300);

			byte[] expected = new byte[] { (byte) MessageType.CHAT_MESSAGE.getTypeNumber(), 0, 0, 0, 10, 1, 2, 3, 4, 5,
					6, 7, 8, 9, 10 };
			byte[] message = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			byte[] actual = new byte[message.length + 5];

			new Thread() {
				@Override
				public void run() {
					try {
						Socket s = new Socket("", 12300);

						DataInputStream in = new DataInputStream(s.getInputStream());
						in.readFully(actual);

					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();

			Socket socket = server.accept();

			MultiThreadedMessageSender sender = new MultiThreadedMessageSender(new Connection(socket, null));
			sender.sendMessage(new Message(message, MessageType.CHAT_MESSAGE));

			Thread.sleep(20);
			Assert.assertArrayEquals(expected, actual);

			server.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
