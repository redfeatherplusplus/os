package entities;

import java.util.HashMap;
import java.util.Map;

public class Batch {
	
	private String workingDir;              //directory in which the batch executes
	private Map<String, Command> commands;  //map of commands to command id's
	
	//constructor
	public Batch() {
		commands = new HashMap<String, Command>();
	}
	
	//add a command to the list of commands
	public void addcommand(Command command) {
		commands.put(command.getCmdId(), command);
	}
	
	//getter and setter methods
	public String getWorkingDir() { return workingDir; }
	public Map<String, Command> getCommands() { return commands; }
}
