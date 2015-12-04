package messagehandling;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.Before;
import org.junit.Test;

import usermanagement.User;
import usermanagement.UserManager;

public class ServerMessageHandlerTest {

	private static boolean isSetup = false;
	private static ServerMessageHandler handler;
	private static SimpleMessageSender sender;
	private static MessageReceiver receiver;

	@Before
	public void setup() {
		if (!isSetup) {

			try {
				ServerSocket serverSocket = new ServerSocket(12345);
			} catch (IOException e) {
				e.printStackTrace();
			}

			UserManager userManager = new UserManager("users.csv");
			User user = userManager.addUser("Georg", "1234");
			handler = new ServerMessageHandler(userManager);
			sender = new SimpleMessageSender(12345, "localhost");
			userManager.login(user, user.getConnection(), sender);
			receiver = new MessageReceiver(handler, user.getConnection());
			isSetup = true;
		}
	}

	@Test
	public void chatMessageTest() {
		sender.sendMessage(new Message("hi", MessageType.CHAT_MESSAGE));

	}

}
