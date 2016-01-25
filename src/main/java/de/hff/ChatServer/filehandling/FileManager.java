package de.hff.ChatServer.filehandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hff.ChatServer.usermanagement.User;

public class FileManager {

	private List<UploadedFile> files;
	private Map<UploadedFile, Filesaver> uploadingFiles;

	private int lastID;
	private final String tempPath;

	public FileManager(String tempPath) {
		this.tempPath = tempPath;
		this.uploadingFiles = new HashMap<>();

		new File(tempPath).mkdir();
		files = new ArrayList<>();
		lastID = 1;
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

	public UploadedFile addFile(String fileName, User user, long expectedLength) {
		File savePath = new File(tempPath + "/" + user.getName() + "/");

		savePath.mkdirs();
		savePath = new File(savePath.getAbsolutePath() + "/" + fileName);

		UploadedFile file = new UploadedFile(savePath, lastID, user, expectedLength);
		lastID++;
		files.add(file);
		try {
			Filesaver saver = new Filesaver(savePath);
			uploadingFiles.put(file, saver);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return file;
	}

	public void savePackage(byte[] data, UploadedFile file) {

		Filesaver filesaver = uploadingFiles.get(file);
		if (filesaver != null) {
			if (!filesaver.isRunning()) {
				filesaver.start();
			}
			try {
				filesaver.savePackage(data);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	public UploadedFile getFile(int id) {
		for (UploadedFile file : files) {
			if (file.getId() == id) {
				return file;
			}
		}
		return null;
	}

}
