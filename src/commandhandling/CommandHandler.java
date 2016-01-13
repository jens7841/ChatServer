package commandhandling;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {

	private static List<Command> commands = new ArrayList<>();

	public static void addCommand(Command command) {
		commands.add(command);
	}

	public void handleCommand(String input) {
		Command cmd = getCommand(input);
		if (cmd != null) {
			cmd.execute(input.substring(input.indexOf(' ') + 1));
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
