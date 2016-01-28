package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import de.hff.ChatServer.usermanagement.User;
import de.hff.ChatServer.usermanagement.UserAlreadyExistsException;
import de.hff.ChatServer.usermanagement.UserException;
import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatServer.usermanagement.UserManager;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class LoginMessageHandler implements MessageHandler {

	private UserManager usermanager;
	private UserHandler userHandler;

	public LoginMessageHandler(UserManager usermanager, UserHandler userHandler) {
		this.usermanager = usermanager;
		this.userHandler = userHandler;
	}

	private void sendLoginSuccess(UserHandler connectionHandler) {
		try {
			userHandler.getUser().getMessageSender()
					.sendMessage(new Message("Erfolgreich eingeloggt!".getBytes("UTF-8"), MessageType.LOGIN_SUCCESS));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void receiveMessage(Message message) {
		if (message.getType() == MessageType.LOGIN) {
			handleMessage(message);
		}
	}

	@Override
	public void handleMessage(Message message) {

		if (usermanager.isUserOnline(userHandler.getUser())) {
			try {
				userHandler.getUser().getMessageSender()
						.sendMessage(new Message("Bereits eingeloggt!".getBytes("UTF-8"), MessageType.LOGIN_ERROR));
			} catch (IOException e) {
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
						usermanager.registerUser(username, password);

						userHandler.getUser().getMessageSender().sendMessage(
								new Message("Erfolgreich Registriert!".getBytes("UTF-8"), MessageType.SUCCESS));

					} catch (UserException e) {
						userHandler.getUser().getMessageSender()
								.sendMessage(new Message(e.getMessage(), MessageType.LOGIN_ERROR));
					}
				}
				try {

					User oldUser = userHandler.getUser();
					userHandler.setUser(usermanager.loginUser(username, password, oldUser.getConnection(),
							oldUser.getMessageReceiver(), oldUser.getMessageSender()));
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

}
