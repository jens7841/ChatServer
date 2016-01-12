package messagehandling.messageoutput;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import messagehandling.Message;

public class MessageOutputstream extends FilterOutputStream {

	public MessageOutputstream(OutputStream out) {
		super(out);
	}

	public void writeMessage(Message message) throws IOException {
		DataOutputStream output = new DataOutputStream(out);
		output.write(message.getType().getTypeNumber());
		output.writeInt(message.getBytes().length);
		output.write(message.getBytes());
		output.flush();
	}

}
