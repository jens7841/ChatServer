package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import de.hff.ChatShared.filehandling.FileManager;
import de.hff.ChatShared.filehandling.Filesaver;
import de.hff.ChatShared.filehandling.TransferFile;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class UploadPackageMessageHandler implements MessageHandler {

	private FileManager fileManager;

	public UploadPackageMessageHandler(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	@Override
	public void receiveMessage(Message message) {
		if (message.getType() == MessageType.UPLOAD_PACKAGE) {
			handleMessage(message);
		}
	}

	@Override
	public void handleMessage(Message message) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));
		try {
			int id = in.readInt();
			int dataLength = in.readInt();

			// System.out.println("ID (UPMH): " + id);
			// System.out.println("Data length: " + dataLength);

			TransferFile transferFile = fileManager.getDownloadFile(id);

			if (transferFile != null) {

				byte[] data = new byte[dataLength];
				in.read(data);

				// System.out.println("UploadPackageMessageHandler.handleMessage()");

				Filesaver filesaver = fileManager.getFilesaver(transferFile);

				if (filesaver != null) {
					filesaver.savePackage(data);
					if (filesaver.getReceivedBytes() >= transferFile.getExpectedLength()) {
						filesaver.endSave();
					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(i);
	}

}
