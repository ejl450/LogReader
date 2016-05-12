/*
* Prosight Error Log
* Edmund Lynn
* Akash Shah
* 05/11/2016
 */




// Below are all the import that are used throughout this code as well as the package import.
package logreader;
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
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



// Below is listed the class definition for the entire code as well as some global constants and buttons that are used throughout the code.
public class LogReader extends JPanel implements ActionListener {
    protected JTextField textField;
    private final JMenuBar menuBar = new JMenuBar();
    protected JTextArea textArea;
    private final static String newline = "\n";
    private String File_Name_0;
    private int lineCount= 0;
    private int newLineCount= 0;
    private int delay = 0; //1 min(s)
    private Timer timer = new Timer(delay, this);
    static final int INT_min = 0;
    static final int INT_max = 60;
    static final int INT_init = 30;
    private String logType;
    
 
    
    // This class, "LogReader" is used to create the gridlayout for the text area as well as create the button layout in the application.
    // Also shown below is the code for each button and what happens when the button is pressed. The three buttons being used are Current
    // Date (Returns the current date), Input Date (Returns user input date), and Start (Starts the log).
    public LogReader() {
        super(new GridBagLayout());
        textArea = new JTextArea(30, 65);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
            

        JSlider timer_slider = new JSlider(JSlider.HORIZONTAL, INT_min, INT_max, INT_init);
        //Turn on labels at major tick marks.
        timer_slider.setMajorTickSpacing(5);
        timer_slider.setMinorTickSpacing(1);
        timer_slider.setPaintTicks(true);
        timer_slider.setPaintLabels(true);
        
        
        //Build File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem startMenuItem = new JMenuItem("Start Program");
        startMenuItem.addActionListener(this);
        JMenuItem dateMenuItem = new JMenuItem("Set Date");
        
        //In initialization code:
        //Create the radio buttons.
        JRadioButton functionLog = new JRadioButton("Function Log");
        functionLog.setMnemonic(KeyEvent.VK_A);
        functionLog.setActionCommand("function Log");
        JRadioButton projectBridgeLog = new JRadioButton("Project Bridge Log");
        projectBridgeLog.setMnemonic(KeyEvent.VK_B);
        projectBridgeLog.setActionCommand("Project Bridge Log");
        JRadioButton prosightLog = new JRadioButton("Prosight Log");
        prosightLog.setMnemonic(KeyEvent.VK_C);
        prosightLog.setActionCommand("Prosight Log");
        ButtonGroup group = new ButtonGroup();
        group.add(functionLog);
        group.add(projectBridgeLog);
        group.add(prosightLog);
        
        //Build Clear Menu
        JMenu clearField = new JMenu("Clear");
        JMenuItem clearTextItem = new JMenuItem("Clear Text Only");
        JMenuItem clearAllItem = new JMenuItem("Clear Text and Close");
        
        //Build Interval Menu
        JMenu intervalSlider = new JMenu("Refresh Interval");
        JMenuItem interval = new JMenuItem("Interval");
        
        
        fileMenu.add(startMenuItem);
        fileMenu.add(dateMenuItem);
        fileMenu.add(functionLog);
        fileMenu.add(projectBridgeLog);
        fileMenu.add(prosightLog); 
        clearField.add(clearTextItem);
        clearField.add(clearAllItem);
        intervalSlider.add(interval);
        menuBar.add(fileMenu);
        menuBar.add(clearField);
        menuBar.add(intervalSlider);

        
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        GridBagConstraints d = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        d.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(menuBar,c);
        //add(timer_slider, c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);

        
        functionLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logType = "function";
                textArea.append("The Log Type is now set to retrive the Function Log" + newline);
            }
        });  
        //Register a listener for the radio buttons.
        projectBridgeLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logType = "ProjectBridge";
                textArea.append("The Log Type is now set to retrive the Project Bridge Log" + newline);
            }
        });
        //Register a listener for the radio buttons.
        prosightLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logType = "prosight";
                textArea.append("The Log Type is now set to retrive the Prosight Log" + newline);
            }
        });
        dateMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAndShowDateMenuGUI();
            }
        });
        interval.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAndShowIntervalGUI();
            }
        });
        startMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                File_Name_0 = "\\\\cp-wpp-ap119d\\log\\" + logType + "_" + String.format("%02d", year) + "_" + String.format("%02d", month) + "_" + String.format("%02d", day) + ".log";
                textArea.append("Press Start to retrieve the Prosight Log for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year));
                delay = ((int)timer_slider.getValue())*1000*60;
                timer.setDelay(delay);
            }
        });
        clearAllItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText(null);
                System.exit(0);
            }
        });
        clearTextItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText(null);
            }
        });
    }
    
 
    
    // Below is the main part of the code which is the file input as well as error handling. Using the date which is determined from the user, the code
    // searches for the given days log and tries to input the log into the text area window. If the log does not exist, it displays an error message saying
    // so. Also displayed is the time the log was put onto the screen as well as the number of lines.
    public void actionPerformed(ActionEvent evt) {
        InputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String line = null;
        int i = 0;
                
        //Below is the code that allows for the current dates log to be shown as well as the error message if there is no log 
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
               
        
        File f = new File(File_Name_0);
        if(!f.isFile()){
            textArea.append(newline + "File " + File_Name_0 + " cannot be found, log is not generated for today!"+newline);               
        }
        else{
            try{
                fis = new FileInputStream(File_Name_0);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(new FileReader(File_Name_0));
                LineNumberReader reader = new LineNumberReader(br);

                while((i=fis.read())!=-1){
                   line = reader.readLine();
                   lineCount=reader.getLineNumber();

                    if(line==null || lineCount<=newLineCount){
                    timer.start();
                    reader.setLineNumber(lineCount);
                    
                    }else{

                       textArea.append(line+newline);
                       newLineCount=lineCount;
                    }
                }
                
            }catch(Exception ex){
            }finally{
                if(fis!=null)
                    try {
                        fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        textArea.append(newline+"Last time updated: " + dateFormat.format(date)+newline);
        //Code ends here
    }
 
    
   
    private static void createAndShowIntervalGUI(){
        //Create and set up the window.
        JFrame setIntervalFrame = new JFrame("Set Refresh Interval");
        //setDateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        
        //Add contents to the window.
        setIntervalFrame.add(new setIntervalFrame());
 
        
        //Display the window.
        setIntervalFrame.pack();
        setIntervalFrame.setVisible(true);
    }
    
    
    
    private static void createAndShowDateMenuGUI(){
        //Create and set up the window.
        JFrame setDateFrame = new JFrame("Set Date");
        //setDateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        
        //Add contents to the window.
        setDateFrame.add(new setDateFrame());
 
        
        //Display the window.
        setDateFrame.pack();
        setDateFrame.setVisible(true);
    }

    

    // Create the GUI and show it.  For thread safety, this method should be invoked from the
    // event dispatch thread.
    private static void createAndShowGUI(){
        //Create and set up the window.
        JFrame frame = new JFrame("PPM/P6 Log");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        
        //Add contents to the window.
        frame.add(new LogReader());
 
        
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
