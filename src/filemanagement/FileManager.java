package filemanagement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import usermanagement.User;

public class FileManager {

	private List<UploadedFile> files;
	private Map<UploadedFile, FileSaver> uploads;
	private int lastID;
	private String tempPath;

	public FileManager(String tempPath) {
		this.tempPath = tempPath;
		this.uploads = new HashMap<>();

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
		path.delete();
	}

	public void addFile(UploadedFile file) {
		lastID++;
		files.add(file);
	}

	public FileSaver saveFile(String filename, long size, User from) {

		UploadedFile file = new UploadedFile(new File(tempPath + "/" + filename), lastID, from);
		addFile(file);

		FileSaver saver = new FileSaver(file, size);

		saver.start();

		uploads.put(file, saver);

		return saver;
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

	public FileSaver getSaver(UploadedFile file) {
		return uploads.get(file);
	}
}
