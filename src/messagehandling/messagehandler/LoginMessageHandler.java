package messagehandling.messagehandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import messagehandling.Message;
import server.ConnectionHandler;
import usermanagement.UserManager;

public class LoginMessageHandler implements MessageHandler {

	private UserManager usermanager;

	public LoginMessageHandler(UserManager usermanager) {
		this.usermanager = usermanager;
	}

	@Override
	public void handleMessage(Message message, ConnectionHandler connectionHandler) {
		DataInputStream in = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(message.getBytes())));
		try {
			int length = in.readInt();
			byte[] usernameBytes = new byte[length];
			in.readFully(usernameBytes, 0, length);
			String username = new String(usernameBytes, "UTF-8");
			length = in.readInt();
			byte[] passwordBytes = new byte[length];
			in.readFully(passwordBytes, 0, length);
			String password = new String(passwordBytes, "UTF-8");

			usermanager.login(username, password, connectionHandler);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
