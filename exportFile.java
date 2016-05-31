package logreader;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import static logreader.LogReader.fileNavigator;
import static logreader.LogReader.newline;
import static logreader.LogReader.textArea;



public class exportFile extends LogReader{
    static void exportWithNotes(){
            logreader.LogReader.fileNavigator.setSelectedFile(new File(logreader.LogReader.logType + " log" + "_" + logreader.LogReader.month + "_" + logreader.LogReader.day + "_" + logreader.LogReader.year + "_" + logreader.LogReader.serverType + "_" + logreader.LogReader.directoryType +".xml"));
            int returnVal = fileNavigator.showSaveDialog(logreader.LogReader.exportNotesMenuItem);
            File file = fileNavigator.getSelectedFile();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                BufferedWriter bw;
                
                //Prosight Log Export with Notes
                if ("prosight".equals(logType)){
                    try {
                        bw = new BufferedWriter(new FileWriter(file));
                        if ("prosight".equals(logreader.LogReader.logType)){
                            String andSymbol = "&";
                            String ampersand = "&amp;";
                            textArea.setText(textArea.getText().replaceAll(andSymbol, ampersand));
                        }
                        String clientAppend = "Client=\"\"";
                        String notesAppend = "Notes=\"\"";
                        textArea.setText(textArea.getText().replaceAll(clientAppend, notesAppend));

                        String exportText = textArea.getText();
                        Pattern p = Pattern.compile(notesAppend);
                        Matcher m = p.matcher(exportText);
                        String pattern1 = "<Trace Type=";
                        String pattern2 = "</Trace>";
                        StringBuffer sb = new StringBuffer();
                        Pattern p1 = Pattern.compile(Pattern.quote(pattern1) + "(?s)(.*?)" + Pattern.quote(pattern2));
                        Matcher m1 = p1.matcher(exportText);
                        String errorText = null;
                        String userNotes;
                        while((m.find())) {
                            if (m1.find()) {
                            errorText = m1.group(1);
                            }
                            UIManager.put("OptionPane.okButtonText", "Next/Skip");
                            String inputNotes = JOptionPane.showInputDialog("Input Notes for the Given Error:" + newline + newline + errorText + newline + newline);
                            userNotes = "Notes=\"" + inputNotes + "\"";
                            if (inputNotes.equals("null")){
                                System.exit(0);
                                break;
                            }
                            m.appendReplacement(sb, userNotes);
                        }
                        m.appendTail(sb);

                        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>" + sb + "</data>");

                        bw.flush();
                        bw.close();
                    }               
                    catch (IOException e1)
                    {
                    }
                }
                
                //Project Bridge Log Export with Notes
                else if ("ProjectBridge".equals(logType)){
                    try {
                        bw = new BufferedWriter(new FileWriter(file));

                        String clientAppend = "</description>";
                        String notesAppend = "</description><notes></notes>";
                        textArea.setText(textArea.getText().replaceAll(clientAppend, notesAppend));

                        String exportText = textArea.getText();
                        Pattern p = Pattern.compile(notesAppend);
                        Matcher m = p.matcher(exportText);
                        String pattern1 = "<entry>";
                        String pattern2 = "</entry>";
                        StringBuffer sb = new StringBuffer();
                        Pattern p1 = Pattern.compile(Pattern.quote(pattern1) + "(?s)(.*?)" + Pattern.quote(pattern2));
                        Matcher m1 = p1.matcher(exportText);
                        String errorText = null;

                        while(m.find()) {
                            if (m1.find()) {
                            errorText = m1.group(1);
                            }
                            String inputNotes = JOptionPane.showInputDialog("Input Notes for the Given Error:" + newline + newline + errorText + newline + newline);
                            String userNotes = "</description>" + "<notes>" + inputNotes + "</notes>";
                            if (inputNotes.equals("null")){
                                System.exit(0);
                                break;
                            }
                            m.appendReplacement(sb, userNotes);
                        }
                        m.appendTail(sb);

                        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>" + sb + "</data>");

                        bw.flush();
                        bw.close();
                    }               
                    catch (IOException e1)
                    {
                    }
                }
                
                //Function Log Export with Notes
                else{
                    try {
                        bw = new BufferedWriter(new FileWriter(file));

                        String clientAppend = "</FunctionExecution>";
                        String notesAppend = "<NOTES></NOTES></FunctionExecution>";
                        textArea.setText(textArea.getText().replaceAll(clientAppend, notesAppend));

                        String exportText = textArea.getText();
                        Pattern p = Pattern.compile(notesAppend);
                        Matcher m = p.matcher(exportText);
                        String pattern1 = "<FunctionExecution TYPE=\"error\">";
                        String pattern2 = "</FunctionExecution>";
                        StringBuffer sb = new StringBuffer();
                        Pattern p1 = Pattern.compile(Pattern.quote(pattern1) + "(?s)(.*?)" + Pattern.quote(pattern2));
                        Matcher m1 = p1.matcher(exportText);
                        String errorText = null;

                        while(m.find()) {
                            if (m1.find()) {
                            errorText = m1.group(1);
                            }
                            String inputNotes = JOptionPane.showInputDialog("Input Notes for the Given Error:" + newline + newline + errorText + newline + newline);
                            String userNotes = "<NOTES>" + inputNotes + "</NOTES>" + "</FunctionExecution>";
                            if (inputNotes.equals("null")){
                                System.exit(0);
                                break;
                            }
                            m.appendReplacement(sb, userNotes);
                        }
                        m.appendTail(sb);

                        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>" + sb + "</data>");

                        bw.flush();
                        bw.close();
                    }               
                    catch (IOException e1)
                    {
                    }
                }
            }
            textArea.setCaretPosition(textArea.getDocument().getLength());
            Desktop dt = Desktop.getDesktop();
            try {
                dt.open(file);
            } catch (IOException ex) {
                Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    static void exportFile(){
            fileNavigator.setSelectedFile(new File(logType + " log" + "_" + month + "_" + day + "_" + year + "_" + serverType + "_" + directoryType +".xml"));
            int returnVal = fileNavigator.showSaveDialog(exportMenuItem);
            File file = fileNavigator.getSelectedFile();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                BufferedWriter bw;
                try {
                    bw = new BufferedWriter(new FileWriter(file));
                    if ("prosight".equals(logType)){
                        String andSymbol = "&";
                        String ampersand = "&amp;";
                        textArea.setText(textArea.getText().replaceAll(andSymbol, ampersand));
                    }    
                    String exportText = textArea.getText();
                    bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>" + exportText + "</data>");
                    
                    bw.flush();
                    bw.close();
                }               
                catch (IOException e1)
                {
                }
            }
            textArea.setCaretPosition(textArea.getDocument().getLength());
            Desktop dt = Desktop.getDesktop();
            try {
                dt.open(file);
            } catch (IOException ex) {
                Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
