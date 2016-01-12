package messagehandling;

import java.io.InputStream;
import java.io.OutputStream;

public class Connection {

	private InputStream in;
	private OutputStream out;

	public Connection(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}

	public InputStream getIn() {
		return in;
	}

	public OutputStream getOut() {
		return out;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Connection) {
			Connection con = (Connection) obj;
			if (con.getIn() != null && con.getIn().equals(this.getIn())) {
				if (con.getOut() != null && con.getOut().equals(this.getOut())) {
					return true;
				}
			}
		}
		return false;
	}

}