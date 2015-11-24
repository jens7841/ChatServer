package commands;

public abstract class Command {
	protected String command;
	protected String[] aliases;

	public Command(String command, String... aliases) {
		this.command = command;
		this.aliases = aliases;
	}

	public boolean contains(String command) {
		return false;

	}

	public abstract void execute();
}
