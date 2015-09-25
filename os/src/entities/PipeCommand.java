package entities;

import org.w3c.dom.Element;

import errorLogging.ProcessException;

public class PipeCommand extends Command {
	
	//PipeCommand arguments
	String CmdElements;  //to be implemented
	
	//create a new PipeCommand by parsing through the elements
	public PipeCommand(Element elem) throws ProcessException {
		//indicate that this command is a "pipe" command
		this.setCmdType("pipe");
		
		//set the id of the command
		String id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in FILE Command");
		}
		System.out.println("ID: " + id);
		this.setCmdId(id);
	}
	
	//create a process to execute the current command
	@Override
	public void execute(String workingDir) {
		// TODO Auto-generated method stub
		
	}
	
	//return a string describing this command's arguments
	@Override
	public String getArguments() {
		return ("Pipe not yet implemented");
	}

	//getter and setter methods
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}
}

