package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Note that certain commands require access to the current batch
//and that moreover we always have exactly one instance of batch.
//Because of this, we have chosen to make batch a singleton.

public class Batch {
	
	private static final Batch singleton = new Batch();  //batch is a singleton class
	private String workingDir;                           //working directory of the batch 
	private List<Command> commandList;                   //ordered list of commands                  
	private Map<String, Command> commands;               //map of commands to command id's
	
	//constructor
	private Batch() {
		commandList = new ArrayList<Command>();
		commands = new HashMap<String, Command>();
	}
	
	//add a command to the list and map of commands
	public void addcommand(Command command) {
		commandList.add(command);
		commands.put(command.getCmdId(), command);
	}
	
	//getter and setter methods
	public void setWorkingDir(String workingDir) { this.workingDir = workingDir; }
	public static Batch getSingleton() { return singleton; }
	public String getWorkingDir() { return workingDir; }
	public List<Command> getCommandList() { return commandList; }
	public Map<String, Command> getCommands() { return commands; }
}
