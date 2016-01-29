package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import de.hff.ChatServer.usermanagement.User;
import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatShared.filehandling.FileManager;
import de.hff.ChatShared.filehandling.TransferFile;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class UploadRequestMessageHandler implements MessageHandler {

	private final int KB = 1024;
	private final int MB = KB * KB;
	private long maxFileSize;
	private int maxSimultaneosUploads;
	private final String TOO_BIG;
	private final String TOO_MANY_UPLOADS = "Es können nur max. " + maxSimultaneosUploads
			+ " Dateien gleichzeitig hochgeladen werden";
	private FileManager fileManager;
	private UserHandler userHandler;

	public UploadRequestMessageHandler(FileManager fileManager, UserHandler userHandler, long maxFileSize,
			int maxSimultaneosUploads) {
		this.fileManager = fileManager;
		this.userHandler = userHandler;

		this.maxFileSize = maxFileSize;
		this.maxSimultaneosUploads = maxSimultaneosUploads;

		TOO_BIG = "Datei darf " + maxFileSize / MB + "MB nicht �berschreiten";
	}

	@Override
	public void receiveMessage(Message message) {
		if (message.getType().equals(MessageType.UPLOAD_REQUEST)) {
			handleMessage(message);
		}
	}

	@Override
	public void handleMessage(Message message) {

		User user = userHandler.getUser();
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));
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

				} else if (user.getCurrentUploads() >= maxSimultaneosUploads) {
					out.writeInt(TOO_MANY_UPLOADS.length());
					out.write(TOO_MANY_UPLOADS.getBytes("UTF-8"));
					user.getMessageSender()
							.sendMessage(new Message(byteArrayOutputStream.toByteArray(), MessageType.UPLOAD_REJECT));

				} else {

					String fileName = new String(filenameBytes, "UTF-8");
					TransferFile transferFile = new TransferFile(
							new File(fileManager.getTempFolder().getAbsolutePath() + "/" + fileName), filesize,
							fileManager.getDownloadCounter());
					fileManager.addDownloadFile(transferFile);

					out.writeInt(transferFile.getId());

					user.getMessageSender().sendMessage(
							new Message(byteArrayOutputStream.toByteArray(), MessageType.UPLOAD_CONFIRMATION));
					user.setCurrentUploads(user.getCurrentUploads() + 1);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
