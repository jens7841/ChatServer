package de.hff.ChatServer.filehandling;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.hff.ChatServer.messagehandling.messageoutput.MessageSender;
import de.hff.ChatServer.server.Server;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageType;

public class Uploader extends Thread {

	private UploadedFile file;
	private MessageSender sender;

	public Uploader(MessageSender sender, UploadedFile file) {
		this.file = file;
		this.sender = sender;
	}

	@Override
	public void run() {
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(file.getFile()));
			int bufferSize = 1024 * 1024;
			try {
				bufferSize = Integer.parseInt(Server.PROPERTIES.getProperty("buffer.size", "1024")) * 1024;
			} catch (NumberFormatException e) {
				System.out.println("property buffer.size nicht verfügbar, wird auf default gesetzt (1024KB)");
			}
			byte[] buffer = new byte[bufferSize];
			double expectedPackages = (double) file.getFile().length() / (double) bufferSize;

			int packages = 0;

			do {

				for (int i = 0; i < buffer.length; i++) {
					buffer[i] = -1;
				}

				int readBytes = in.read(buffer);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(byteArrayOutputStream);
				out.writeInt(file.getId());
				out.writeInt(readBytes);
				out.write(buffer);

				sender.sendMessage(new Message(byteArrayOutputStream.toByteArray(), MessageType.DOWNLOAD_PACKAGE));
				packages++;

			} while (packages < expectedPackages);

			in.close();

			byte[] msg = ByteBuffer.allocate(4).putInt(file.getId()).array();
			sender.sendMessage(new Message(msg, MessageType.DOWNLOAD_END));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
