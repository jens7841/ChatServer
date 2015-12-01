package filemanagement;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Semaphore;

public class FileSaver extends Thread {

	private UploadedFile file;
	private long size;
	private List<byte[]> packages;
	private Semaphore lock;

	public FileSaver(UploadedFile file, long size) {
		this.file = file;
		this.size = size;
		this.lock = new Semaphore(1);
	}

	public void addPackgage(byte[] data) {
		packages.add(data);
		lock.release();
	}

	@Override
	public void run() {

		try {
			lock.acquire();

			OutputStream out = new BufferedOutputStream(new FileOutputStream(file.getFile()));

			while (!file.isUploadFinished()) {

				while (packages.size() > 0) {
					out.write(packages.get(0));
					packages.remove(0);
				}
				if (file.getFile().length() == size) {
					System.out.println("JAAAAA!!!!111111 FileSaver funktioniert!!!11111");
					file.setUploadFinished();
					break;
				}
				lock.acquire();
			}

			out.close();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
