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
	
	//getter and setter methods
	@Override
	public String getArguments() {
		return ("path: " + path);
	}
}
