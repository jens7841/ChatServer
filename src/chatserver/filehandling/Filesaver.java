package chatserver.filehandling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Filesaver extends Thread {

	private final OutputStream out;
	private boolean running;
	private BlockingQueue<byte[]> buffer;
	private long expectedLength;
	private File file;

	public Filesaver(File file, long expectedLength) throws FileNotFoundException {
		this.out = new BufferedOutputStream(new FileOutputStream(file));
		this.expectedLength = expectedLength;
		System.out.println("Expected Length: " + expectedLength);
		this.file = file;
		this.buffer = new LinkedBlockingQueue<>();
	}

	@Override
	public synchronized void start() {
		this.running = true;
		super.start();
	}

	public void savePackage(byte[] pack) throws IOException {
		if (!running)
			throw new IllegalStateException("Filesaver is not running");
		buffer.add(pack);
	}

	public void endSave() {
		this.running = false;
	}

	@Override
	public void run() {

		try {
			while (running) {
				out.write(buffer.take());

				if (file.length() == expectedLength) {
					out.flush();
					out.close();
					running = false;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

}