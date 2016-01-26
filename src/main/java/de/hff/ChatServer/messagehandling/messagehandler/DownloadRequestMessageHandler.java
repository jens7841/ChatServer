package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import de.hff.ChatServer.filehandling.FileManager;
import de.hff.ChatServer.filehandling.UploadedFile;
import de.hff.ChatServer.filehandling.Uploader;
import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageType;

public class DownloadRequestMessageHandler implements MessageHandler {

	private FileManager fileManager;

	public DownloadRequestMessageHandler(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	@Override
	public void handleMessage(Message message, UserHandler userHandler) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));
		int id = 0;
		try {
			id = in.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		UploadedFile file = fileManager.getFile(id);
		if (file != null) {
			new Uploader(userHandler.getUser().getMessageSender(), file).start();
		} else {
			byte[] msg = ByteBuffer.allocate(4).putInt(id).array();
			userHandler.getUser().getMessageSender().sendMessage(new Message(msg, MessageType.DOWNLOAD_REJECT));
		}
	}

}
