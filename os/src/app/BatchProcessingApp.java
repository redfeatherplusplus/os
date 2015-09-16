package app;

import parser.BatchParser;

public class BatchProcessingApp {
	public static void main(String[] args) {
		String filename = null;
		if(args.length > 0) {
			filename = args[0];
		}
		else {
			filename = "work/batch1.dos.xml";
			BatchParser.parse(filename);
			
			//note, try to utilize errorLogging in exceptions
		}
	}
}
