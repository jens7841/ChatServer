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
import server.UserHandler;
import usermanagement.User;

public class UploadRequestMessageHandler implements MessageHandler {

	private final int KB = 1024;
	private final int MB = KB * KB;
	private final int MAX_FILESIZE = MB * 200;
	private final int MAX_SIMULTANEOS_UPLOADS = 1;
	private final String TOO_BIG = "Datei darf " + MAX_FILESIZE / MB + "MB nicht überschreiten";
	private final String TOO_MANY_UPLOADS = "Es können nur max. " + MAX_SIMULTANEOS_UPLOADS
			+ " Dateien gleichzeitig hochgeladen werden";
	private FileManager fileManager;

	public UploadRequestMessageHandler(FileManager fileManager) {
		this.fileManager = fileManager;
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
				String filename = new String(filenameBytes, "UTF-8");
				long filesize = in.readLong();

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(byteArrayOutputStream);

				out.writeInt(length);
				out.write(filenameBytes);
				out.writeLong(filesize);

				if (filesize > MAX_FILESIZE) {
					out.writeInt(TOO_BIG.length());
					out.write(TOO_BIG.getBytes("UTF-8"));
					user.getMessageSender()
							.sendMessage(new Message(byteArrayOutputStream.toByteArray(), MessageType.UPLOAD_REJECT));

				} else if (user.getSimultaneosUploads() >= MAX_SIMULTANEOS_UPLOADS) {
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
