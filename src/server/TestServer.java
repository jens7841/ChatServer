package server;

public class TestServer {

	public static void main(String[] args) throws Throwable {

		new Server(12345, "users.csv", "tmpFiles").startServer();

	}
}
