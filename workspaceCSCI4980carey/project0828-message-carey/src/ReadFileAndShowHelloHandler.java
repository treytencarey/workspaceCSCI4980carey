import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ReadFileAndShowHelloHandler {
	@Execute
	public void execute(Shell shell) {
		MessageDialog.openInformation(shell, "Title", "Hello " + testReadFile("name"));
	}
	
	public String testReadFile(String conf) {
      // A format of file "config.txt" consists of a key-value pair.
      List<String> contents = readFile("C:/Users/Treyten/workspaceCSCI4980/workspaceCSCI4980carey/project0828-message-carey/config.txt");
      for (int i = 0; i < contents.size(); i++) {
         String line = contents.get(i);
         // System.out.println("[DBG] Line " + i + " - " + line);

         if (line.indexOf(":") >= 0)
         {
	         String lineConf = line.split(":")[0].trim();
	         String name = line.split(":")[1].trim();
	         // System.out.println("[DBG] \tConf: " + lineConf + ", " + "Name: " + name);
	         
	         if (lineConf.equals(conf))
	         {
	        	 return name;
	         }
         }
      }
      return "{Unknown}";
   }

   public static List<String> readFile(String filePath) {
      List<String> contents = new ArrayList<String>();
      File file = new File(filePath);
      Scanner scanner = null;
      try {
         scanner = new Scanner(file);
         while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            contents.add(line);
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } finally {
         if (scanner != null)
            scanner.close();
      }
      return contents;
   }
}