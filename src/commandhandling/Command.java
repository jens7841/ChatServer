package commandhandling;

public abstract class Command {
	protected String command;
	protected String[] aliases;

	public Command(String command, String... aliases) {
		this.command = command;
		this.aliases = aliases;
	}

	public boolean contains(String command) {
		if (command.equalsIgnoreCase(this.command)) {
			return true;
		}
		for (String alias : aliases) {
			if (command.equalsIgnoreCase(alias)) {
				return true;
			}
		}
		return false;
	}

	public abstract void execute(String msg);
}
