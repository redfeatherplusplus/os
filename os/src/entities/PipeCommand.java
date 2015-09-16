package entities;

import org.w3c.dom.Element;

import errorLogging.ProcessException;

public class PipeCommand extends Command {
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
}

