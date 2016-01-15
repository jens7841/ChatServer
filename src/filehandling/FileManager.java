package filehandling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import usermanagement.User;

public class FileManager {

	private List<UploadedFile> files;
	private int lastID;
	private final String tempPath;

	public FileManager(String tempPath) {
		this.tempPath = tempPath;

		new File(tempPath).mkdir();
		files = new ArrayList<>();
		lastID = Integer.MIN_VALUE;
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

	public UploadedFile addFile(String fileName, User user) {
		File savePath = new File(tempPath + "/" + user.getName() + "/");

		System.out.println(savePath.mkdirs());

		savePath = new File(savePath.getAbsolutePath() + "/" + fileName);

		UploadedFile file = new UploadedFile(savePath, lastID, user);
		lastID++;
		files.add(file);
		return file;
	}

	public void savePackage(byte[] data, UploadedFile file) {
		BufferedOutputStream out = null;
		try {

			out = new BufferedOutputStream(new FileOutputStream(file.getFile(), true));
			out.write(data);
			out.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
