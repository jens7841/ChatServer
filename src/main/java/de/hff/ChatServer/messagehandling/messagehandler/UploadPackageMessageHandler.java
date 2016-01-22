package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import de.hff.ChatServer.filehandling.FileManager;
import de.hff.ChatServer.filehandling.UploadedFile;
import de.hff.ChatServer.messagehandling.Message;
import de.hff.ChatServer.server.UserHandler;

public class UploadPackageMessageHandler implements MessageHandler {

	private FileManager fileManager;

	public UploadPackageMessageHandler(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	@Override
	public void handleMessage(Message message, UserHandler userHandler) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));
		try {
			int id = in.readInt();
			int dataLength = in.readInt();

			// System.out.println("ID (UPMH): " + id);
			// System.out.println("Data length: " + dataLength);

			UploadedFile file = fileManager.getFile(id);

			if (file != null) {

				byte[] data = new byte[dataLength];
				in.read(data);

				// System.out.println("UploadPackageMessageHandler.handleMessage()");

				fileManager.savePackage(data, file);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(i);
	}

}
