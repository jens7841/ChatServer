package messagehandling;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DefaultMessageSender implements MessageSender {

	private OutputStream out;

	public DefaultMessageSender(OutputStream out) {
		this.out = out;
	}

	@Override
	public void sendMessage(Message message) {

		try {
			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(out));

			output.write(message.getType().getTypeNumber());
			output.writeInt(message.getBytes().length);
			output.write(message.getBytes());
			output.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}