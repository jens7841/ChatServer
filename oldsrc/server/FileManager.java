package server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import usermanager.UserManager;

public class FileManager {

	private List<UploadedFile> files;
	private int lastID;
	private UserManager userManager;
	private String tempPath;

	public FileManager(UserManager userManager, String tempPath) {
		this.userManager = userManager;
		this.tempPath = tempPath;

		new File(tempPath).mkdir();

		files = new ArrayList<>();
		lastID = 0;

		clearTempFolder();
	}

	private void clearTempFolder() {
		File tempFolder = new File(tempPath);
		tempFolder.delete();
		tempFolder.mkdir();
	}

	public void addFile(UploadedFile file) {
		lastID++;
		files.add(file);
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
