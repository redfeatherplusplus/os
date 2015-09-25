package entities;

import org.w3c.dom.Element;

import errorLogging.ProcessException;

public class WDCommand extends Command {

	//WDCommand arguments
	private String path;

	//create a new WDCommand by parsing through the elements
	public WDCommand(Element elem) throws ProcessException {
		
		//indicate that this command is a "WD" command
		this.setCmdType("wd");
		
		//set the id of the command
		String id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in FILE Command");
		}
		System.out.println("ID: " + id);
		this.setCmdId(id);
		
		//get the path of the executable
		this.path = elem.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in FILE Command");
		}
		System.out.println("Path: " + path);
	}
	
	//create a process to execute the current command
	@Override
	public void execute(String workingDir) {
		//note that no actual execution was done
		System.out.println("Nothing to execute for command: " + this.getCmdId());
	}
	
	//return a string describing this command's arguments
	@Override
	public String getArguments() {
		//path is the only argument to return and is a mandatory argument
		return ("path: " + path);
	}
	
	//getter and setter methods
	@Override
	public String getPath() { return path; }
}
