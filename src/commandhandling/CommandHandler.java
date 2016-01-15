package commandhandling;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {

	private List<Command> commands = new ArrayList<>();

	public void addCommand(Command command) {
		commands.add(command);
	}

	public void handleCommand(String input) {
		input = input.trim();
		Command cmd = getCommand(input);
		if (cmd != null) {
			if (input.indexOf(" ") == -1) {
				cmd.execute("");
			} else {
				cmd.execute(input.substring(input.indexOf(' ') + 1));
			}
		}
	}

	private Command getCommand(String input) {

		if (input.contains("/")) {
			input = input.substring(1);
		}
		if (input.contains(" ")) {
			input = input.substring(0, input.indexOf(" "));
		}

		for (Command command : commands) {
			if (command.contains(input)) {
				return command;
			}
		}

		return null;
	}

	public boolean commandKnown(String input) {
		return getCommand(input) != null;
	}
}
