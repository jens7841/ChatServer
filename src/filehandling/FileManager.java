package filehandling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import usermanagement.User;

public class FileManager {

	private List<UploadedFile> files;
	private List<byte[]> packages;
	private int lastID;
	private String tempPath;

	public FileManager(String tempPath) {
		this.tempPath = tempPath;

		new File(tempPath).mkdir();
		files = new ArrayList<>();
		lastID = 0;
		clearTempFolder();
	}

	private void clearTempFolder() {
		File tempFolder = new File(tempPath);
		deleteFolder(tempFolder);
		tempFolder.mkdir();
	}

	private void deleteFolder(File path) {
		for (File file : path.listFiles()) {
			if (file.isDirectory()) {
				deleteFolder(file);
			}
			file.delete();
		}
	}

	public void addFile(UploadedFile file) {
		lastID++;
		files.add(file);
	}

	public UploadedFile saveFile(String filename, long size, User user) {
		UploadedFile file = new UploadedFile(new File(tempPath + "/" + filename), lastID, user);
		addFile(file);

		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(file.getFile(), true));

			while (!file.isUploadFinished()) {

				while (packages.size() > 0) {
					out.write(packages.get(0));
					packages.remove(0);
					if (file.getFile().length() >= size) {
						file.setUploadFinished();
						break;
					}
				}

			}

			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}

	public UploadedFile getFile(int id) {
		for (UploadedFile file : files) {
			if (file.getId() == id) {
				return file;
			}
		}
		return null;
	}

	public String getTempPath() {
		return tempPath;
	}

	public int getLastID() {
		return lastID;
	}

}
