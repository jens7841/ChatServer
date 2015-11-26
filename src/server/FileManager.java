package server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import usermanager.UserManager;

public class FileManager {

	private List<UploadedFile> files;
	private static final String filename = "files.csv";
	int lastID;
	private UserManager userManager;
	private String tempPath = "tempFiles";

	public FileManager(UserManager userManager) {
		this.userManager = userManager;
		try {
			files = loadData();
			lastID = readLastID();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File createEmtyFile() throws IOException {

		File file = new File(filename);
		if (!file.exists()) {
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			writer.println(0);
			writer.close();
			System.out.println("hi");
		}
		return file;
	}

	public void writeUserData(OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		writer.println(lastID);

		for (UploadedFile file : files) {
			StringBuilder builder = new StringBuilder();
			builder.append(file.getId());
			builder.append(';');
			builder.append(file.getFile().toPath().toString());
			builder.append(';');
			builder.append(file.getFrom().getName());
			writer.println(builder.toString());
		}

		writer.flush();
	}

	public void addFile(UploadedFile file) {
		lastID++;
		files.add(file);
		try {
			writeUserData(new BufferedOutputStream(new FileOutputStream(new File(filename))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

	private int readLastID() throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(createEmtyFile()));
		String s;
		if ((s = reader.readLine()) != null) {
			s.trim();
			if (!s.isEmpty()) {
				reader.close();
				return Integer.parseInt(s);
			}
		}
		reader.close();
		return 0;
	}

	private List<UploadedFile> loadData() throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(createEmtyFile()));
		reader.readLine();
		List<UploadedFile> uploadedFiles = new ArrayList<>();
		String s;
		while ((s = reader.readLine()) != null) {
			if (!s.isEmpty()) {
				String[] fileData = s.split(";");
				if (fileData.length == 3) {
					uploadedFiles.add(new UploadedFile(new File(fileData[1]), Integer.parseInt(fileData[0]),
							userManager.getUser(fileData[2])));
				}
			}
		}
		reader.close();
		return uploadedFiles;
	}

	public String getTempPath() {
		return tempPath;
	}

}
