package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hff.ChatServer.filehandling.FileManager;
import de.hff.ChatServer.filehandling.UploadedFile;
import de.hff.ChatServer.server.Server;
import de.hff.ChatServer.usermanagement.User;
import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageType;

public class UploadRequestMessageHandler implements MessageHandler {

	private final int KB = 1024;
	private final int MB = KB * KB;
	private long maxFileSize;
	private int maxSimultaneosUploads;
	private final String TOO_BIG;
	private final String TOO_MANY_UPLOADS = "Es k�nnen nur max. " + maxSimultaneosUploads
			+ " Dateien gleichzeitig hochgeladen werden";
	private FileManager fileManager;

	public UploadRequestMessageHandler(FileManager fileManager) {
		this.fileManager = fileManager;
		try {
			maxFileSize = MB * Long.parseLong(Server.PROPERTIES.getProperty("max.file.size"));
		} catch (NumberFormatException e) {
			maxFileSize = MB * 200;
		}

		try {
			maxSimultaneosUploads = Integer.parseInt(Server.PROPERTIES.getProperty("max.sim.uploads"));
		} catch (NumberFormatException e) {
			maxSimultaneosUploads = 1;
		}
		TOO_BIG = "Datei darf " + maxFileSize / MB + "MB nicht �berschreiten";
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

					String fileName = new String(filenameBytes, "UTF-8");
					UploadedFile file = fileManager.addFile(fileName, userHandler.getUser(), filesize);

					out.writeInt(file.getId());

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
