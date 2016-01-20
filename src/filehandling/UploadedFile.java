package filehandling;

import java.io.File;

import usermanagement.User;

public class UploadedFile {

	private int id;
	private File file;
	private User user;
	private long expectedLength;

	public UploadedFile(File file, int id, User user, long expectedLength) {
		this.file = file;
		this.id = id;
		this.user = user;
		this.expectedLength = expectedLength;
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

	public long getExpectedLength() {
		return expectedLength;
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
