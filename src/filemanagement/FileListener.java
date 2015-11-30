package filemanagement;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.ServerSocket;

public class FileListener extends Thread {

	private ServerSocket fileSocket;
	private FileManager fileManager;

	public FileListener(ServerSocket fileSocket, FileManager fileManager) {
		this.fileSocket = fileSocket;
		this.fileManager = fileManager;
	}

	@Override
	public void run() {
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(fileSocket.accept().getInputStream()));
			while (!fileSocket.isClosed()) {

				int fileNameLength = in.readInt();

				byte[] fileNameBytes = new byte[fileNameLength];
				in.readFully(fileNameBytes);
				String fileName = new String(fileNameBytes, "UTF-8");

				long fileSize = in.readLong();

				File file = new File(fileManager.getTempPath() + "/" + fileName);
				Writer fileWriter = new BufferedWriter(new FileWriter(file));
				for (int i = 0; i < fileSize; i++) {
					fileWriter.write(in.readByte());
				}
				fileWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
