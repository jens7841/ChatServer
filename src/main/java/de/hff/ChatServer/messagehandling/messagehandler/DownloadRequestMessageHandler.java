package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatShared.filehandling.FileManager;
import de.hff.ChatShared.filehandling.TransferFile;
import de.hff.ChatShared.filehandling.Uploader;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class DownloadRequestMessageHandler implements MessageHandler {

	private static final int BUFFER_SIZE = 1024 * 1024;
	private FileManager fileManager;
	private UserHandler userHandler;

	public DownloadRequestMessageHandler(FileManager fileManager, UserHandler userHandler) {
		this.fileManager = fileManager;
		this.userHandler = userHandler;
	}

	@Override
	public void receiveMessage(Message message) {
		if (message.getType().equals(MessageType.DOWNLOAD_REQUEST)) {
			handleMessage(message);
		}
	}

	@Override
	public void handleMessage(Message message) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));
		int id = 0;
		try {
			id = in.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		TransferFile transferFile = fileManager.getDownloadFile(id);
		if (transferFile != null) {
			byte[] fileNameBytes = transferFile.getFile().getName().getBytes();
			byte[] msg = ByteBuffer.allocate(16 + fileNameBytes.length).putInt(fileNameBytes.length).put(fileNameBytes)
					.putLong(transferFile.getFile().length()).putInt(id).array();
			System.out.println("-> Download Request von " + userHandler.getUser().getName() + " confirmed!");
			try {
				userHandler.getUser().getMessageSender()
						.sendMessage(new Message(msg, MessageType.DOWNLOAD_CONFIRMATION));
			} catch (IOException e) {
				e.printStackTrace();
			}
			new Uploader(userHandler.getUser().getMessageSender(), transferFile, BUFFER_SIZE).start();
		} else {
			byte[] msg = ByteBuffer.allocate(4).putInt(id).array();
			System.out.println("-> Download Request von " + userHandler.getUser().getName() + " rejected!");
			try {
				userHandler.getUser().getMessageSender().sendMessage(new Message(msg, MessageType.DOWNLOAD_REJECT));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
