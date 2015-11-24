package server;

import java.util.ArrayList;
import java.util.List;

import commands.Command;
import usermanager.User;

public class CommandHandler {

	private List<Command> commands = new ArrayList<Command>();

	public void addCommand(Command command) {
		commands.add(command);
	}

	public void handleCommand(User user, String commandString) {
		for (Command command : commands) {
			if (command.contains(commandString)) {
				System.out.println(user.getName() + ": /" + commandString);
				command.execute(user);
				return;
			}
		}
		System.out.println("Error. Command not found! " + user.getName() + ": /" + commandString);
	}
}
