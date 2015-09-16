package app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
			filename = "work/batch1.dos.xml";
		}

		//if a filename was passed, try to create a batch from that file 
		try {
			Batch batch = BatchParser.parse(filename);
			
			//print out the batch for testing
			System.out.println("Batch: " + batch.getCommands().toString());
			executeBatch(batch);
		} 
		catch (ParserConfigurationException | SAXException | IOException
				| ProcessException e) {
			//error batch creation failed
			System.out.println("Error, failed to create batch:");
			System.out.println(e.getMessage());
			e.printStackTrace();
			//f
		}
	}
	
	//execute the batch, describe each command as it is executed
	public static void executeBatch(Batch batch) {
		Map<String, Command> commands = batch.getCommands();

		//to test the describe method, describe every command
		//for (Command command : commands.values()) {
		//	System.out.println(command.describe());
		//}
	}
}
