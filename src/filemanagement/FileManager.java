package filemanagement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

	private List<UploadedFile> files;
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
		path.delete();
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
