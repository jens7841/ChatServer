package filemanagement;

import java.io.File;

import usermanagement.User;

public class UploadedFile {

	private int id;
	private File file;
	private User from;

	public UploadedFile(File file, int id, User from) {
		this.file = file;
		this.id = id;
		this.from = from;
	}

	public int getId() {
		return id;
	}

	public File getFile() {
		return file;
	}

	public User getFrom() {
		return from;
	}

}
