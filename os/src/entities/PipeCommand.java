package entities;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import errorLogging.ProcessException;

public class PipeCommand extends Command {
	
	//PipeCommand arguments
	List<CmdCommand> filters = new ArrayList<CmdCommand>();
	
	//create a new PipeCommand by parsing through the elements
	public PipeCommand(Element elem) throws ProcessException {
		//indicate that this command is a "pipe" command
		this.setCmdType("pipe");
		
		//get a list of filters to connect with pipes
		NodeList myNodeList = elem.getChildNodes();
		for (int idx = 0; idx < myNodeList.getLength(); idx++) {
			Node node = myNodeList.item(idx);
			
			//add each CmdCommand to the list of filters
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element filter = (Element) node;
				filters.add(new CmdCommand(filter));
			}
		}
		
		//describe the cmd commands
		for (CmdCommand filter : filters) {
			System.out.println(filter.describe());
		}
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

