package messagehandling.messageoutput;

import java.io.IOException;
import java.io.OutputStream;

import messagehandling.Message;

public class DefaultMessageSender implements MessageSender {

	private OutputStream out;

	public DefaultMessageSender(OutputStream out) {
		this.out = out;
	}

	@Override
	public void sendMessage(Message message) {

		try {
			MessageOutputstream output = new MessageOutputstream(out);

			output.writeMessage(message);
			output.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}