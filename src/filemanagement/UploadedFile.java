package filemanagement;

import java.io.File;

import usermanagement.User;

public class UploadedFile {

	private int id;
	private File file;
	private User from;
	private boolean uploadFinished;

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

	public void setUploadFinished() {
		uploadFinished = true;
	}

	public boolean isUploadFinished() {
		return uploadFinished;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UploadedFile)) {
			return false;
		}
		UploadedFile file = (UploadedFile) obj;
		if (file.getFrom().equals(getFrom()) && file.getId() == getId() && file.getFile().equals(getFile())) {
			return true;
		}
		return false;
	}

}
