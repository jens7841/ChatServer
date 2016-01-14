package messagehandling.messagehandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import messagehandling.Message;
import messagehandling.MessageType;
import server.UserHandler;
import usermanagement.User;

public class UploadRequestMessageHandler implements MessageHandler {

	private static final int KB = 1024;
	private static final int MB = KB * KB;
	private static final int MAX_FILESIZE = MB * 200;
	private int id = Integer.MIN_VALUE;

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

				if (filesize <= MAX_FILESIZE && user.getSimultaneosUploads() <= 3) {
					out.writeInt(id);
					user.getMessageSender().sendMessage(
							new Message(byteArrayOutputStream.toByteArray(), MessageType.UPLOAD_CONFIRMATION));
					user.setSimultaneosUploads(user.getSimultaneosUploads() + 1);
				} else {
					user.getMessageSender()
							.sendMessage(new Message(byteArrayOutputStream.toByteArray(), MessageType.UPLOAD_REJECT));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
