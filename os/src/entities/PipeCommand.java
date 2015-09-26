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
				filters.add(new CmdCommand(filter));
			}
		}
	}
	
	//create a process to execute the current command
	@Override
	public void execute(String workingDir) 
			throws IOException, InterruptedException {
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
			
			//start each executable
			final Process source = sourceExecutable.start();
			final Process destination = destinationExecutable.start();
			
			//create a pipe of between the two executables via streams
			InputStream is = source.getInputStream();
			OutputStream os = destination.getOutputStream();
			copyStreams(is,os);
			
			//wait for processes to finish execution, then close streams
			source.waitFor();
			destination.waitFor();
			is.close();
			os.close();

			System.out.println("Pipe terminated!");
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

