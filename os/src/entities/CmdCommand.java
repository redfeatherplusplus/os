package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

import errorLogging.ProcessException;

public class CmdCommand extends Command {
	
	//CmdCommand arguments
	private String path;           //path to executable
	private List<String> cmdArgs;  //arguments passed to executable
	private String inID;           //input file for executable's stdin
	private String outID;          //output file for executable's stdout

	//create a new CmdCommand by parsing through the elements
	public CmdCommand(Element elem) throws ProcessException {
		//indicate that this command is a "cmd" command
		this.setCmdType("cmd");
		
		//set the id of the command
		String id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in CMD Command");
		}
		System.out.println("ID: " + id);
		this.setCmdId(id);
		
		//get the path of the executable
		this.path = elem.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in CMD Command");
		}
		System.out.println("Path: " + path);

		//get the executable's arguments as a list of strings 
		this.cmdArgs = new ArrayList<String>();
		String arg = elem.getAttribute("args");
		if (!(arg == null || arg.isEmpty())) {
			StringTokenizer st = new StringTokenizer(arg);
			while (st.hasMoreTokens()) {
				String tok = st.nextToken();
				cmdArgs.add(tok);
			}
		}
		for(String argi: cmdArgs) {
			System.out.println("Arg " + argi);
		}

		//get the file to be directed to the executable's stdin
		this.inID = elem.getAttribute("in");
		if (!(inID == null || inID.isEmpty())) {
			System.out.println("inID: " + inID);
		}

		//get the file to directed the executable's stdout to
		this.outID = elem.getAttribute("out");
		if (!(outID == null || outID.isEmpty())) {
			System.out.println("outID: " + outID);
		}
	}

	//getter and setter methods
	@Override
	public String getArguments() {
		return ("path: " + path + ", args: " + cmdArgs.toString() + 
				", inID: " + inID + ", outID: " + outID);
	}
}
