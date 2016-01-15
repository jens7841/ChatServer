package filehandling;

import java.io.File;

import usermanagement.User;

public class UploadedFile {

	private int id;
	private File file;
	private User user;
	private boolean uploadFinished;

	public UploadedFile(File file, int id, User user) {
		this.file = file;
		this.id = id;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public File getFile() {
		return file;
	}

	public User getUser() {
		return user;
	}

	public void setUploadFinished() {
		uploadFinished = true;
	}

	public boolean isUploadFinished() {
		return uploadFinished;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UploadedFile) {

			UploadedFile file = (UploadedFile) obj;
			if (file.getUser().equals(getUser()) && file.getId() == getId() && file.getFile().equals(getFile())) {
				return true;
			}
		}
		return false;
	}

}
