package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

import de.hff.ChatServer.usermanagement.User;
import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatShared.connectionhandling.Connection;
import de.hff.ChatShared.filehandling.FileManager;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageType;
import de.hff.ChatShared.messagehandling.messageinput.MessageInputStream;
import de.hff.ChatShared.messagehandling.messageoutput.MessageOutputstream;
import de.hff.ChatShared.messagehandling.messageoutput.MessageSender;

public class UploadRequestMessageHandlerTest {

	private static final String TEMP_FOLDER = "src/test/resources/tempFolder/";
	private static final String FILE_NAME = "test.txt";
	private static final int MAX_FILE_SIZE = 100;

	@Test
	public void testUploadConfirmation() throws IOException {

		ByteArrayOutputStream byteArrayMessageOutputstream = new ByteArrayOutputStream();
		MessageOutputstream messageOutputstream = new MessageOutputstream(byteArrayMessageOutputstream);

		FileManager fileManager = new FileManager(TEMP_FOLDER);
		Connection connection = new Connection(messageOutputstream, null);
		UserHandler userHandler = new UserHandler(connection);
		userHandler.setUser(
				new User("userName", "passwort", 1, connection, null, new MessageSender(connection.getOutputstream())));

		UploadRequestMessageHandler messageHandler = new UploadRequestMessageHandler(fileManager, userHandler,
				MAX_FILE_SIZE, 1);

		messageHandler.receiveMessage(getUploadRequestMessage());
		messageHandler.receiveMessage(getUploadRequestMessage());
		byte[] byteArray = byteArrayMessageOutputstream.toByteArray();

		MessageInputStream in = new MessageInputStream(new ByteArrayInputStream(byteArray));

		Message actual = readMessage(in);

		Assert.assertEquals(MessageType.UPLOAD_CONFIRMATION, actual.getType());

	}

	@Test
	public void testUploadReject() throws UnsupportedEncodingException, IOException {

		ByteArrayOutputStream byteArrayMessageOutputstream = new ByteArrayOutputStream();
		MessageOutputstream messageOutputstream = new MessageOutputstream(byteArrayMessageOutputstream);

		FileManager fileManager = new FileManager(TEMP_FOLDER);
		Connection connection = new Connection(messageOutputstream, null);
		UserHandler userHandler = new UserHandler(connection);
		userHandler.setUser(
				new User("userName", "passwort", 0, connection, null, new MessageSender(connection.getOutputstream())));

		UploadRequestMessageHandler messageHandler = new UploadRequestMessageHandler(fileManager, userHandler,
				MAX_FILE_SIZE, 0);

		messageHandler.receiveMessage(getUploadRequestMessage());
		ByteArrayInputStream in = new ByteArrayInputStream(byteArrayMessageOutputstream.toByteArray());

		Message actual = readMessage(new MessageInputStream(in));
		Assert.assertEquals(MessageType.UPLOAD_REJECT, actual.getType());

	}

	private Message getUploadRequestMessage() throws IOException, UnsupportedEncodingException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(out);

		dataOut.writeInt(FILE_NAME.length());
		dataOut.write(FILE_NAME.getBytes("UTF-8"));
		dataOut.writeLong(99);

		return new Message(out.toByteArray(), MessageType.UPLOAD_REQUEST);
	}

	private Message readMessage(MessageInputStream messageInputStream) throws IOException {

		return messageInputStream.readMessage();
	}

}