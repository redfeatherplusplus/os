package entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	
	//create a process to execute the current command
	@Override
	public void execute(String workingDir) 
			throws InterruptedException, IOException, ProcessException {
		//prepare an executable process using ProcessBuilder
		ProcessBuilder executable = generateExecutable(workingDir);
		
		//execute the process
		System.out.println("Starting process: " + this.getCmdId());
		Process process = executable.start();
		process.waitFor();

		//execution successful, print out message indicating so
		System.out.println("Terminated! ExitValue: " + process.exitValue());
	}
	
	//return an executable ProcessBuilder
	public ProcessBuilder generateExecutable(String workingDir) throws ProcessException {
		//get the current command map to enable ID pairing
		Map<String, Command> commands = Batch.getSingleton().getCommands();
		
		//create a list that maintains the given command and its arguments
		List<String> command = new ArrayList<String>();
		command.add(path);
		for (String arg : cmdArgs)  {
			command.add(arg);
		}

		//create a new process that executes this command
		ProcessBuilder executable = new ProcessBuilder(command);
		executable.directory(new File(workingDir));
		File wd = executable.directory();
		
		//set input file if desired
		if (!(inID == null || inID.isEmpty())) {
			//inID is not null, verify that the given inID exists
			if (commands.get(inID) != null) {
				File inFile = new File(wd, commands.get(inID).getPath());	
				executable.redirectInput(inFile);
			}
			else {
				throw new ProcessException("Unable to map inID '" + inID 
						+ "' in command '" + this.getCmdId() + "' to a FileCommand.");
			}
		}

		//set output file if desired
		if (!(outID == null || outID.isEmpty())) {
			//outID is not null, verify that the given outID exists
			if (commands.get(outID) != null) {
				File outFile = new File(wd, commands.get(outID).getPath());	
				executable.redirectOutput(outFile);
			}
			else {
				throw new ProcessException("Unable to map outID '" + outID 
						+ "' in command '" + this.getCmdId() + "' to a FileCommand.");
			}
		}
		
		return(executable);
	}
	
	//return a string describing this command's arguments
	@Override
	public String getArguments() {
		//throw all non-null string arguments into a string builder
		StringBuilder arguments = new StringBuilder();
		
		//add the path of the executable
		arguments.append("path: " + path + ", ");

		//add the executable's arguments 
		if (!cmdArgs.isEmpty()) {
			arguments.append("args: " + cmdArgs.toString() + ", ");
		}

		//add the file to be directed to the executable's stdin
		if (!(inID == null || inID.isEmpty())) {
			arguments.append("inID: " + inID + ", ");
		}

		//add the file to direct the executable's stdout to
		if (!(outID == null || outID.isEmpty())) {
			arguments.append("outID: " + outID + ", ");
		}
		
		//remove leading comma and return arguments
		arguments.delete(arguments.length() - 2, arguments.length());
		return (arguments.toString());
	}

	//getter and setter methods
	@Override
	public String getPath() { return path; }
}
