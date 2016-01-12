package tests;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import messagehandling.Message;
import messagehandling.DefaultMessageSender;
import messagehandling.MessageType;

public class MessageSenderTest {

	@Test
	public void test() {
		ByteArrayOutputStream outputArray = new ByteArrayOutputStream();
		ByteArrayOutputStream out = outputArray;
		DataOutputStream output = new DataOutputStream(outputArray);
		byte[] arr = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		try {
			output.write(MessageType.CHAT_MESSAGE.getTypeNumber());
			output.writeInt(arr.length);
			output.write(arr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		DefaultMessageSender sender = new DefaultMessageSender(out);
		Message message = new Message(arr, MessageType.CHAT_MESSAGE);
		sender.sendMessage(message);
		Assert.assertArrayEquals(outputArray.toByteArray(), out.toByteArray());

	}

}
