package messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import filehandling.FileManager;
import messagehandling.Message;
import server.UserHandler;

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
			int packageLength = in.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
