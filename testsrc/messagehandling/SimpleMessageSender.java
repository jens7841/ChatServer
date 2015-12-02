package messagehandling;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleMessageSender implements MessageSender {

	private Socket socket;

	public SimpleMessageSender(int recieverPort, String recieverIP) {
		try {

			this.socket = new Socket(recieverIP, recieverPort);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessage(Message message) {
		try {
			socket.getOutputStream().write(message.getType().getTypeNumber());
			socket.getOutputStream().write(message.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}