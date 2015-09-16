package app;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import entities.Batch;
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
			//for testing, assume the filename is as follows (debug later)
			filename = "work/batch1.dos.xml";
		}

		//if a filename was passed, try to create a batch from that file 
		try {
			Batch batch = BatchParser.parse(filename);
			
			//print out the batch for testing
			System.out.println("Batch: " + batch.getCommands().toString());
		} 
		catch (ParserConfigurationException | SAXException | IOException
				| ProcessException e) {
			//error batch creation failed
			System.out.println("Error, failed to create batch:");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		//note, try to utilize errorLogging in exceptions
	}
}
