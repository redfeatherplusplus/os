package entities;

import java.util.List;
import java.util.Map;

public class Batch {
	
	private String workingDir;                    //directory in which the batch executes
	private List<Map<String, Command>> commands;  //list of commands the batch executes
	
	//constructor?
	
	//add a command to the list of commands
	public void addcommand(Command command) {
		
	}
	
	//getter and setter methods
	public String getWorkingDir() { return workingDir; }
	public List<Map<String, Command>> getCommands() { return commands; }
}
