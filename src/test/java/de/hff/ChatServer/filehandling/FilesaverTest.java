package de.hff.ChatServer.filehandling;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class FilesaverTest {

	@Test
	public void test() throws Exception {
		String fileText = "Das ist ein Test File mit einem Text.... ";
		int quantity = 10000;

		File expected = new File("testFileSource.txt");

		OutputStream out = new BufferedOutputStream(new FileOutputStream(expected));

		for (int i = 0; i < quantity; i++) {
			out.write(fileText.getBytes());
		}

		out.close();

		File actual = new File("testFile.txt");

		Filesaver filesaver = new Filesaver(actual, fileText.length());
		filesaver.start();
		while (!filesaver.isAlive()) {
		}

		for (int i = 0; i < quantity; i++) {
			filesaver.savePackage(fileText.getBytes());
		}

		filesaver.endSave();

		while (filesaver.isAlive()) {
			Thread.sleep(20);
		}

		Thread.sleep(100);

		Assert.assertEquals(expected.length(), actual.length());

		InputStream readActual = new BufferedInputStream(new FileInputStream(actual));
		InputStream readExpected = new BufferedInputStream(new FileInputStream(expected));

		for (int i = 0; i < quantity * fileText.length(); i++) {
			Assert.assertEquals(readActual.read(), readExpected.read());
		}

		readActual.close();
		readExpected.close();

	}

}