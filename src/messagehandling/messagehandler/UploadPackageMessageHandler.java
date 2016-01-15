package messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import filehandling.FileManager;
import filehandling.UploadedFile;
import messagehandling.Message;
import server.UserHandler;

public class UploadPackageMessageHandler implements MessageHandler {

	private FileManager fileManager;

	public UploadPackageMessageHandler(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	private static int i = 0;

	@Override
	public void handleMessage(Message message, UserHandler userHandler) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));
		try {
			int id = in.readInt();
			int dataLength = in.readInt();
			i += dataLength;

			UploadedFile file = fileManager.getFile(id);

			if (file != null) {

				byte[] data = new byte[dataLength];
				in.read(data);

				fileManager.savePackage(data, file);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(i);
	}

}
