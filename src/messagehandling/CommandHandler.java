package messagehandling;

import java.util.ArrayList;
import java.util.List;

import commands.Command;
import usermanagement.User;

public class CommandHandler {

	private List<Command> commands;

	public CommandHandler() {
		commands = new ArrayList<>();
	}

	public boolean handleCommand(Message message, User user) {

		String input = message.toString();

		if (input.startsWith("/")) {
			input = input.substring(1);
		}

		Command command = getCommand(input.replaceAll("\\s\\s+", " ").split("\\ ")[0]);

		if (command != null) {
			command.execute(user, input);
			return true;
		}

		return false;
	}

	public Command getCommand(String commandName) {
		for (Command command : commands) {
			if (command.contains(commandName)) {
				return command;
			}
		}
		return null;
	}

	public void addCommand(Command cmd) {
		commands.add(cmd);
	}

}
