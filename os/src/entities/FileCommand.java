package entities;

import org.w3c.dom.Element;

import errorLogging.ProcessException;

public class FileCommand extends Command {

	//FileCommand arguments
	private String path;  //path to file

	//create a new FileCommand by parsing through the elements
	public FileCommand(Element elem) throws ProcessException {

		//indicate that this command is a "file" command
		this.setCmdType("file");
		
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
	
	//getter and setter methods
	@Override
	public String getArguments() {
		//path is the only argument to return and is a mandatory argument
		return ("path: " + path);
	}
	public String getPath() { return path; }
}
