package messagehandling;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import usermanagement.UserManager;

public class LoginMessageHandler implements MessageHandler {

	private UserManager usermanager;

	public LoginMessageHandler(UserManager usermanager) {
		this.usermanager = usermanager;
	}

	@Override
	public void handleMessage(Message message, Connection con) {
		DataInputStream in = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(message.getBytes())));
		try {
			int length = in.readInt();
			byte[] usernameBytes = new byte[length];
			in.readFully(usernameBytes, 0, length);
			String username = usernameBytes.toString();
			length = in.readInt();
			byte[] passwordBytes = new byte[length];
			in.readFully(passwordBytes, 0, length);
			String password = passwordBytes.toString();

			usermanager.login(username, password, con);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
