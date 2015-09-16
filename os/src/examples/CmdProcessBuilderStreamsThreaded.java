package examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Example of using a JavaSE 7 ProcessBuilder to execute a command that reads
 * and writes to/from a stream. This will be needed to implement the pipe needed
 * for batch4.xml.
 * 
 * NOTE: This version uses a thread to copy the contents of the input stream 
 * to an output stream. See the static method copy();
 */
public class CmdProcessBuilderStreamsThreaded
{
	public static void main(String args[]) throws InterruptedException, IOException
	{
		List<String> command = new ArrayList<String>();
		command.add("sort"); // DOS SORT cmd
		command.add("/r"); // reverse sort

		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File("work"));
		File wd = builder.directory();

		final Process process = builder.start();

		OutputStream os = process.getOutputStream();
		FileInputStream fis = new FileInputStream(new File(wd, "randomwords.txt"));
		copyStreams(fis, os);
		
		File outfile = new File(wd, "sortedwords.txt");
		FileOutputStream fos = new FileOutputStream(outfile);
		InputStream is = process.getInputStream();
		copyStreams(is, fos);
		
		System.out.println("Program terminated!");
	}

	/**
	 * Copy the contents of the input stream to the output stream in
	 * separate thread. The thread ends when an EOF is read from the 
	 * input stream.   
	 */
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
}

