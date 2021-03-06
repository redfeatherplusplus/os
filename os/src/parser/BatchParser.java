package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import entities.Batch;
import entities.CmdCommand;
import entities.Command;
import entities.FileCommand;
import entities.PipeCommand;
import entities.WDCommand;
import errorLogging.ProcessException;

public class BatchParser
{
	//parse the given xml file into the batch singleton
	public static void parse(String filename) 
			throws ParserConfigurationException, SAXException, IOException, ProcessException
	{
		if(!filename.equals(null)) {
			//we have received a filename, attempt to open and read that file
			System.out.println("Opening " + filename + "\n");
			File f = new File(filename);
			
			//file opened successfully, add commands to the batch singleton
			Batch batch = Batch.getSingleton();
			
			//extract xml elements from the input file
			FileInputStream fis = new FileInputStream(f);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fis);

			//for every xml element, extract a command
			Element pnode = doc.getDocumentElement();
			NodeList nodes = pnode.getChildNodes();
			for (int idx = 0; idx < nodes.getLength(); idx++) {
				Node node = nodes.item(idx);
				
				//for each line of the batch file parse the command
				//after a command is added, add that command to the batch
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) node;
					Command cmd = parseCommand(elem);
					batch.addcommand(cmd);
					System.out.println();
				}
			}
		}
		else {
			//received empty string as filename, print error accordingly
			throw new ProcessException("Error, recieved empty string as filename.");
		}
	}

	private static Command parseCommand(Element elem) throws ProcessException
	{
		String cmdName = elem.getNodeName();
		Command cmd;
		
		//note: rather than make an empty command class, and then immediately parse an element 
		//with that command class, we chose to have each individual command class's constructor
		//parse the given element. In other words, a command is created by parsing an element.
		
		//check if the element contains a command
		if (cmdName == null) {
			throw new ProcessException("unable to parse command from " + elem.getTextContent());
		}
		else if ("wd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing wd");
			cmd = new WDCommand(elem);
			
			//wd parsed, set the working directory of the batch singleton
			Batch.getSingleton().setWorkingDir(cmd.getPath());
			System.out.println("Working Directory set to: " +
					Batch.getSingleton().getWorkingDir());
		}
		else if ("file".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing file");
			cmd = new FileCommand(elem);
		}
		else if ("cmd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing cmd");
			cmd = new CmdCommand(elem);
		}
		else if ("pipe".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing pipe");
			cmd = new PipeCommand(elem);
		}
		else {
			throw new ProcessException("Unknown command " + cmdName + " from: " + elem.getBaseURI());
		}

		//we have successfully parsed a command, return the command
		return(cmd);
	}
}
