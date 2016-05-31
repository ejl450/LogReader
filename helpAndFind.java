package logreader;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import static logreader.LogReader.newline;
import static logreader.LogReader.textArea;



public class helpAndFind extends LogReader{
    static void findItems(){
            final String inputValue = JOptionPane.showInputDialog("Find What?");
            int offset = textArea.getText().indexOf(inputValue);
            int  length= inputValue.length();
            String text = textArea.getText();
            Highlighter h = textArea.getHighlighter();
            h.removeAllHighlights();
            final Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);
            
            if (offset == -1){
                JOptionPane.showMessageDialog(null, "Search Value Not Found", "Error", JOptionPane.ERROR_MESSAGE);
            }
            int index = text.indexOf(inputValue);

            while ( index >= 0 ) {
                try {
                    int len = inputValue.length();
                    h.addHighlight(index, index+len, painter);
                    index = text.indexOf(inputValue, index+len);
                } catch (BadLocationException ex) {
                    Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            textArea.select(offset, offset+length);
    }
    static void helpItem(){
            String tutorial = "Tutorial: " + newline + "In order to start the program to be able to read the logs, first you need to setup the log to be looked at. This requires steps such " + newline +
                    "as applying the date to be looked at, the log type to be searched for, the server type to grab, and the directory type to be set to. " + newline +
                    "After all of this has been accomplished, the program can be ran by pressing the Start Program item under the file menu." + newline + newline;
            String export = "Export: " + newline + "In order to export the program to an .XML File, the program first has to be ran to have the log in the text area. After this is accomplished, " + newline +
                    "the program can be exported by pressing the Export Item under the file menu. When exporting the file, a file dialog box will show up which " + newline +
                    "will allow you to pick where to save the exported file as well as the file name which is already predefined. After exporting, the file will " + newline +
                    "automatically open to the chosen default program to view .XML files in. It is suggested to set the default program to excel to have the proper " + newline +
                    "formatted table." + newline + newline;
            String exportNotes = "Export with Notes: " + newline + "This feature is similar to the export command. The only difference is that for each error in a given log, a user has the option " + newline +
                    "to append notes about the error such as what the error means, when it was fixed, and how to fix the error so when the .XML file is opened up, " + newline +
                    "the notes will be appended to each error in a table like format for easy visibility for any user to read. " + newline + newline + newline;
            String authorInfo = "Authors: " + newline + "Edmund Lynn" + newline + "Akash Shah" + newline + newline + newline;
            String versionInfo = newline + versionNumber + newline;
            JOptionPane.showMessageDialog(null, (tutorial + export + exportNotes + authorInfo + versionInfo), "Help", JOptionPane.INFORMATION_MESSAGE);
    }
}
