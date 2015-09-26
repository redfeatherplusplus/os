package entities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
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
		
		//set the id of all PipeCommands to be "pipe"
		this.setCmdId("pipe");
		
		//get a list of filters to connect with pipes
		NodeList myNodeList = elem.getChildNodes();
		for (int idx = 0; idx < myNodeList.getLength(); idx++) {
			Node node = myNodeList.item(idx);
			
			//add each CmdCommand to the list of filters
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element filter = (Element) node;
				System.out.println("Command found: ");
				filters.add(new CmdCommand(filter));
			}
		}
	}
	
	//create a process to execute the current command
	@Override
	public void execute(String workingDir) 
			throws IOException, InterruptedException, ProcessException {
		//for each filter create an executable ProcessBuilder
		//then add that executable to a list of executables
		LinkedList<ProcessBuilder> executables = new LinkedList<ProcessBuilder>();
		for(CmdCommand filter : filters) {
			ProcessBuilder executable = filter.generateExecutable(workingDir);
			executables.add(executable);
		}
		
		//for a given pipe there will always be at least two executables
		if (executables.size() == 2){
			//base case of two executables
			ProcessBuilder sourceExecutable = executables.getFirst();
			ProcessBuilder destinationExecutable = executables.getLast();
			
			//get id of each process for logging purposes
			String pid_first = filters.get(0).getCmdId();
			String pid_last = filters.get(executables.size() - 1).getCmdId();
			
			//start each executable
			System.out.println("Starting process: " + pid_first);
			final Process source = sourceExecutable.start();
			
			System.out.println("Starting process: " + pid_last);
			final Process destination = destinationExecutable.start();
			
			//create a pipe of between the two executables via streams
			System.out.println("Linking processes via I/O streams...");
			InputStream sourceStream = source.getInputStream();
			OutputStream destinationStream = destination.getOutputStream();
			copyStreams(sourceStream, destinationStream);
			
			//wait for processes to finish execution, then close streams
			source.waitFor();
			sourceStream.close();
			System.out.println("Process '" + pid_first + "' terminated.");
			
			destination.waitFor();
			destinationStream.close();
			System.out.println("Process '" + pid_last + "' terminated.");

			//execution successful, print out message indicating so
			System.out.println("Pipe Complete!");
		}
		else {
			//for n>2 executables, to be implemented
		}
	}
	
	//copy the inputstream's data into the outputstream in a thread
	static void copyStreams(final InputStream is, final OutputStream os) {
		Runnable copyThread = (new Runnable() {
			@Override
			public void run()
			{
				try {
					int achar;
					while ((achar = is.read()) != -1) {
						os.write(achar);
					}
					os.close();
				}
				catch (IOException ex) {
					throw new RuntimeException(ex.getMessage(), ex);
				}
			}
		});
		new Thread(copyThread).start();
	}
	
	//return a string describing this command's arguments
	@Override
	public String getArguments() {
		//throw all filter id's into a single stringbuilder
		StringBuilder arguments = new StringBuilder();
		for(CmdCommand filter : filters) {
			arguments.append(filter.getCmdId() + ", ");
		}
		
		//remove leading comma and return arguments
		arguments.delete(arguments.length() - 2, arguments.length());
		return (arguments.toString());
	}

	//getter and setter methods
	@Override
	public String getPath() { return null; }
}

