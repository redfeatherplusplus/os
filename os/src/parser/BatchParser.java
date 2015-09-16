package parser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import entities.CmdCommand;
import entities.Command;
import entities.FileCommand;
import entities.PipeCommand;
import entities.WDCommand;
import errorLogging.ProcessException;

/**
 * Example of using Java's built-in DOM XML parsing to
 * parse one of the XML batch files. 
 */
public class BatchParser
{

	public static void parse(String filename)
	{
		try {
			if(!filename.equals(null)) {
				//we have received a filename, attempt to open and read that file
				System.out.println("Opening " + filename);
				File f = new File(filename);
				
				FileInputStream fis = new FileInputStream(f);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fis);

				Element pnode = doc.getDocumentElement();
				NodeList nodes = pnode.getChildNodes();
				for (int idx = 0; idx < nodes.getLength(); idx++) {
					Node node = nodes.item(idx);
					
					//for each line of the batch file parse the command
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element elem = (Element) node;
						parseCommand(elem);
					}
				}
			}
			else {
				//print error, possibly use ProcessException.java?
				System.out.println("Error, recieved empty string as filename.");
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void parseCommand(Element elem) throws ProcessException
	{
		String cmdName = elem.getNodeName();
		
		//note: rather than make an empty command class, and then immediately parse an element 
		//with that command class, we chose to have each individual command class's constructor
		//parse the given element. In other words, a command is created by parsing an element.
		
		if (cmdName == null) {
			throw new ProcessException("unable to parse command from " + elem.getTextContent());
		}
		else if ("wd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing wd");
			Command cmd = new WDCommand(elem);
		}
		else if ("file".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing file");
			Command cmd = new FileCommand(elem);
		}
		else if ("cmd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing cmd");
			Command cmd = new CmdCommand(elem);
			//parseCmd(elem); // Example of parsing a cmd element
		}
		else if ("pipe".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing pipe");
			//Command cmd = PipeCommand.parse(elem);
		}
		else {
			throw new ProcessException("Unknown command " + cmdName + " from: " + elem.getBaseURI());
		}
	}

	/* 
	 * An example of parsing a CMD element 
	 * THIS LOGIC BELONGS IN INDIVIDUAL Command subclasses
	 *
	public static void parseCmd(Element elem) throws ProcessException
	{
		String id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in CMD Command");
		}
		System.out.println("ID: " + id);
		
		String path = elem.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in CMD Command");
		}
		System.out.println("Path: " + path);

		// Arguments must be passed to ProcessBuilder as a list of
		// individual strings. 
		List<String> cmdArgs = new ArrayList<String>();
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

		String inID = elem.getAttribute("in");
		if (!(inID == null || inID.isEmpty())) {
			System.out.println("inID: " + inID);
		}

		String outID = elem.getAttribute("out");
		if (!(outID == null || outID.isEmpty())) {
			System.out.println("outID: " + outID);
		}
	}
	*/
}
