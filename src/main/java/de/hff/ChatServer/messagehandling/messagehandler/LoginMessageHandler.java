package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import de.hff.ChatServer.messagehandling.Message;
import de.hff.ChatServer.messagehandling.MessageType;
import de.hff.ChatServer.server.UserHandler;
import de.hff.ChatServer.usermanagement.User;
import de.hff.ChatServer.usermanagement.UserAlreadyExistsException;
import de.hff.ChatServer.usermanagement.UserException;
import de.hff.ChatServer.usermanagement.UserManager;

public class LoginMessageHandler implements MessageHandler {

	private UserManager usermanager;

	public LoginMessageHandler(UserManager usermanager) {
		this.usermanager = usermanager;
	}

	@Override
	public void handleMessage(Message message, UserHandler userHandler) {
		if (userHandler.getUser().isLoggedIn()) {
			try {
				userHandler.getUser().getMessageSender()
						.sendMessage(new Message("Bereits eingeloggt!".getBytes("UTF-8"), MessageType.LOGIN_ERROR));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			DataInputStream in = new DataInputStream(
					new BufferedInputStream(new ByteArrayInputStream(message.getBytes())));
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

						userHandler.getUser().getMessageSender().sendMessage(
								new Message("Erfolgreich Registriert!".getBytes("UTF-8"), MessageType.SUCCESS));

					} catch (UserException e) {
						userHandler.getUser().getMessageSender()
								.sendMessage(new Message(e.getMessage(), MessageType.LOGIN_ERROR));
					}
				}
				try {

					User oldUser = userHandler.getUser();
					userHandler.setUser(usermanager.login(username, password, oldUser.getConnection(),
							oldUser.getMessageListener(), oldUser.getMessageSender()));
					sendLoginSuccess(userHandler);

				} catch (UserAlreadyExistsException e) {

					userHandler.getUser().getMessageSender().sendMessage(
							new Message("Benutzer bereits eingeloggt!".getBytes("UTF-8"), MessageType.LOGIN_ERROR));

				} catch (UserException e) {
					userHandler.getUser().getMessageSender()
							.sendMessage(new Message(e.getMessage().getBytes("UTF-8"), MessageType.LOGIN_ERROR));

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendLoginSuccess(UserHandler connectionHandler) {
		try {
			connectionHandler.getUser().getMessageSender()
					.sendMessage(new Message("Erfolgreich eingeloggt!".getBytes("UTF-8"), MessageType.LOGIN_SUCCESS));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
