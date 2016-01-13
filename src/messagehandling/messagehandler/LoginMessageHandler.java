package messagehandling.messagehandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import messagehandling.Message;
import messagehandling.MessageType;
import server.UserHandler;
import usermanagement.User;
import usermanagement.UserAlreadyExistsException;
import usermanagement.UserException;
import usermanagement.UserManager;

public class LoginMessageHandler implements MessageHandler {

	private UserManager usermanager;

	public LoginMessageHandler(UserManager usermanager) {
		this.usermanager = usermanager;
	}

	@Override
	public void handleMessage(Message message, UserHandler connectionHandler) {
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

			if (usermanager.getUser(username) == null) {

				try {
					usermanager.register(username, password);

					connectionHandler.getUser().getMessageSender().sendMessage(
							new Message("Erfolgreich Registriert!".getBytes("UTF-8"), MessageType.SUCCESS_MESSAGE));

				} catch (UserException e) {
					connectionHandler.getUser().getMessageSender()
							.sendMessage(new Message(e.getMessage(), MessageType.LOGIN_ERROR_MESSAGE));
				}
			}
			try {

				User oldUser = connectionHandler.getUser();
				connectionHandler.setUser(usermanager.login(username, password, oldUser.getConnection(),
						oldUser.getMessageListener(), oldUser.getMessageSender()));
				sendLoginSuccess(connectionHandler);

			} catch (UserAlreadyExistsException e) {

				connectionHandler.getUser().getMessageSender().sendMessage(
						new Message("Benutzer bereits eingeloggt!".getBytes("UTF-8"), MessageType.LOGIN_ERROR_MESSAGE));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendLoginSuccess(UserHandler connectionHandler) {
		try {
			connectionHandler.getUser().getMessageSender().sendMessage(
					new Message("Erfolgreich eingeloggt!".getBytes("UTF-8"), MessageType.LOGIN_SUCCESS_MESSAGE));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
