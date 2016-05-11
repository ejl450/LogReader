/*
* Prosight Error Log
* Edmund Lynn
* Akash Shah
* 05/11/2016
 */

// Below are all the import that are used throughout this code as well as the package import.
package textdemo;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import javax.swing.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
 


// Below is listed the class definition for the entire code as well as some global constants and buttons that are used throughout the code.
public class TextDemo extends JPanel implements ActionListener {
    protected JTextField textField;
    private final JButton buttonStart = new JButton("Start");
    private final JButton CurrentDate = new JButton("Current Date");
    private final JButton InputQuestion = new JButton("Would you like to use the Current Date or Input a Date:");
    private final JButton InputDate = new JButton("Input Date");
    protected JTextArea textArea;
    private final static String newline = "\n";
    private String File_Name_0;
    private boolean start_true;
    
 
    // This class, "TextDemo" is used to create the gridlayout for the text area as well as create the button layout in the application.
    // Also shown below is the code for each button and what happens when the button is pressed. The three buttons being used are Current
    // Date (Returns the current date), Input Date (Returns user input date), and Start (Starts the log).
    public TextDemo() {
        super(new GridBagLayout());
        textArea = new JTextArea(30, 65);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        
        int delay = 100000; //millisecond
        buttonStart.addActionListener(this);
        new Timer(delay, this).start();
        
        
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        GridBagConstraints d = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        d.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(InputQuestion,c);
        add(CurrentDate,d);
        add(InputDate,d);
        add(buttonStart,c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
           
 
        InputDate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Scanner reader = new Scanner(System.in);
                System.out.println("Enter a month (mm): ");
                int month = reader.nextInt();             
                System.out.println("Enter a day (dd): ");
                int day = reader.nextInt();
                System.out.println("Enter a year (yyyy): ");
                int year = reader.nextInt();
                
              
                File_Name_0 = "\\\\cp-wpp-ap119d\\log\\prosight_" + String.format("%02d", year) + "_" + String.format("%02d", month) + "_" + String.format("%02d", day) + ".log";
                textArea.append("Press Start to retrieve the Prosight Log for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year));
                start_true = true;
            }
        });
        CurrentDate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                File_Name_0 = "\\\\cp-wpp-ap119d\\log\\prosight_" + String.format("%02d", year) + "_" + String.format("%02d", month) + "_" + String.format("%02d", day) + ".log";
                textArea.append("Press Start to retrieve the Prosight Log for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year));
                start_true = true;
            }
        });
    }
 
    
    // Below is the main part of the code which is the file input as well as error handling. Using the date which is determined from the user, the code
    // searches for the given days log and tries to input the log into the text area window. If the log does not exist, it displays an error message saying
    // so. Also displayed is the time the log was put onto the screen as well as the number of lines.
    public void actionPerformed(ActionEvent evt) {
        FileInputStream fis = null;
        int i = 0;
        char c;
      
        
        //Below is the code that allows for the current dates log to be shown as well as the error message if there is no log 
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        textArea.append(newline+"Last time updated 1: " + dateFormat.format(date)+newline);
          
        
        File f = new File(File_Name_0);
        if(!f.isFile()){
            textArea.append(newline + "File " + File_Name_0 + " cannot be found, log is not generated for today!"+newline);               
        }
        else{
            try{
                fis = new FileInputStream(File_Name_0);
                while((i=fis.read())!=-1){              
                    c=(char)i;
                    String log = Character.toString(c);
                    textArea.append(log);
                    //textArea.setCaretPosition(textArea.getDocument().getLength());
                    }
            }catch(Exception ex){
                ex.printStackTrace();
            }finally{
                if(fis!=null)
                    try{
                        fis.close();
                    }catch(IOException ex){
                        Logger.getLogger(TextDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        
        textArea.append(newline+"Last time updated 2: " + dateFormat.format(date));
        textArea.append("Line count: "+textArea.getLineCount());
        //Code ends here
    }
 
    
    // Create the GUI and show it.  For thread safety, this method should be invoked from the
    // event dispatch thread.
    private static void createAndShowGUI(){
        //Create and set up the window.
        JFrame frame = new JFrame("PPM Prosight Log");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        
        //Add contents to the window.
        frame.add(new TextDemo());
 
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    
    // The main class which runs the entire code.
    public static void main(String[]args){
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.        
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                createAndShowGUI();
            }
        });
    }
}
