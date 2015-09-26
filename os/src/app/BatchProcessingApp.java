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
	
	public static void main(String[] args) {
		String filename = null;
		
		//see if any arguments were passed into this program
		if(args.length > 0) {
			filename = args[0];
		}
		else {
			filename = "work/batch1.dos.xml";
		}

		//if a filename was passed, try to create a batch from that file 
		try {
			//parse the batch xml file into the singleton batch class
			BatchParser.parse(filename);
			
			//batch creation successful, execute the batch
			//note that this try block is nested inside the 
			//first try/catch since we only want to execute 
			//the batch if it was created successfully
			
			//get the batch singleton and execute the batch
			Batch batch = Batch.getSingleton();
			try {
				//attempt to execute the batch
				executeBatch(batch);
				
				//batch complete
				System.out.println("Batch Complete.");
			} 
			catch (InterruptedException | IOException | ProcessException e) {
				//error during batch execution
				System.out.println("Error, failure during batch execution: ");
				e.printStackTrace();
			}
		} 
		catch (ParserConfigurationException | SAXException | IOException
				| ProcessException e) {
			//error batch creation failed
			System.out.println("Error, failure during batch creation: ");
			e.printStackTrace();
		}
	}
	
	//execute the batch, describe each command as it is executed
	public static void executeBatch(Batch batch) 
			throws InterruptedException, IOException, ProcessException {
		//get the list of commands to execute
		List<Command> commandList = batch.getCommandList();
		
		//indicate that command execution is beginning
		System.out.println("Beginning execution of commands... \n");
		
		//describe and execute every command in the list sequentially
		for (Command command : commandList) {
			System.out.println(command.describe());
			
			//try to execute the current command in the list
			command.execute(batch.getWorkingDir());
			System.out.println();
		}
	}
}
