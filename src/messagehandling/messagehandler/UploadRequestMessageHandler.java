package messagehandling.messagehandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import filehandling.FileManager;
import messagehandling.Message;
import messagehandling.MessageType;
import server.Server;
import server.UserHandler;
import usermanagement.User;

public class UploadRequestMessageHandler implements MessageHandler {

	private final int KB = 1024;
	private final int MB = KB * KB;
	private int maxFileSize;
	private int maxSimultaneosUploads;
	private final String TOO_BIG;
	private final String TOO_MANY_UPLOADS = "Es können nur max. " + maxSimultaneosUploads
			+ " Dateien gleichzeitig hochgeladen werden";
	private FileManager fileManager;

	public UploadRequestMessageHandler(FileManager fileManager) {
		this.fileManager = fileManager;
		try {
			maxFileSize = MB * Integer.parseInt(Server.PROPERTIES.getProperty("max.file.size"));
		} catch (NumberFormatException e) {
			maxFileSize = MB * 200;
		}

		try {
			maxSimultaneosUploads = Integer.parseInt(Server.PROPERTIES.getProperty("max.sim.uploads"));
		} catch (NumberFormatException e) {
			maxSimultaneosUploads = 1;
		}
		TOO_BIG = "Datei darf " + maxFileSize / MB + "MB nicht überschreiten";
	}

	@Override
	public void handleMessage(Message message, UserHandler userHandler) {

		User user = userHandler.getUser();
		DataInputStream in = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(message.getBytes())));
		if (user != null) {

			System.out.println("-> Upload Request von " + user.getName());

			try {

				int length = in.readInt();
				byte[] filenameBytes = new byte[length];
				in.readFully(filenameBytes, 0, length);
				long filesize = in.readLong();

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(byteArrayOutputStream);

				out.writeInt(length);
				out.write(filenameBytes);
				out.writeLong(filesize);

				if (filesize > maxFileSize) {
					out.writeInt(TOO_BIG.length());
					out.write(TOO_BIG.getBytes("UTF-8"));
					user.getMessageSender()
							.sendMessage(new Message(byteArrayOutputStream.toByteArray(), MessageType.UPLOAD_REJECT));

				} else if (user.getSimultaneosUploads() >= maxSimultaneosUploads) {
					out.writeInt(TOO_MANY_UPLOADS.length());
					out.write(TOO_MANY_UPLOADS.getBytes("UTF-8"));
					user.getMessageSender()
							.sendMessage(new Message(byteArrayOutputStream.toByteArray(), MessageType.UPLOAD_REJECT));

				} else {
					out.writeInt(fileManager.getLastID());
					user.getMessageSender().sendMessage(
							new Message(byteArrayOutputStream.toByteArray(), MessageType.UPLOAD_CONFIRMATION));
					user.setSimultaneosUploads(user.getSimultaneosUploads() + 1);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
