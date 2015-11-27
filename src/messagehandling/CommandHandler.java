package messagehandling;

import java.util.ArrayList;
import java.util.List;

import commands.Command;

public class CommandHandler {

	private List<Command> commands;

	public CommandHandler() {
		commands = new ArrayList<>();
	}

	public boolean handleCommand(Message message) {

		return false;
	}

	public void addCommand(Command cmd) {
		commands.add(cmd);
	}

}
