package app;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import entities.Batch;
import entities.Command;
import errorLogging.ProcessException;
import parser.BatchParser;

public class BatchProcessingApp {

	//note, try to use errorLogging.ProcessException for exceptions
	
	public static void main(String[] args) {
		String filename = null;
		
		//see if any arguments were passed into this program
		if(args.length > 0) {
			filename = args[0];
		}
		else {
			filename = "work/batch4.xml";
		}

		//if a filename was passed, try to create a batch from that file 
		try {
			//parse the batch xml file into the singleton batch class
			BatchParser.parse(filename);
			
			//get the current batch singleton and execute the batch
			Batch batch = Batch.getSingleton();
			executeBatch(batch);
		} 
		catch (ParserConfigurationException | SAXException | IOException
				| ProcessException e) {
			//error batch creation failed
			System.out.println("Error, failed to create batch:");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	//execute the batch, describe each command as it is executed
	public static void executeBatch(Batch batch) {
		//get the list of commands to execute
		List<Command> commandList = batch.getCommandList();
		
		//indicate that command execution is beginning
		System.out.println("Beginning execution of commands...");
		
		//describe and execute every command in the list sequentially
		for (Command command : commandList) {
		System.out.println(command.describe());
			try {
				command.execute(batch.getWorkingDir());
			} catch (InterruptedException | IOException e) {
				//error executing command
				System.out.println("Error, failure during command execution: "
						+ command.getCmdId());
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
