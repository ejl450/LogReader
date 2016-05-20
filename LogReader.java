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
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;



// Below is listed the class definition for the entire code as well as some global constants and buttons that are used throughout the code.
public class LogReader extends JPanel implements ActionListener {
    //create GUI fields
    protected JTextField textField;
    private final JMenuBar menuBar = new JMenuBar();
    protected JTextArea textArea;
    public static JTextField lastUpdatedField = new JTextField(40);
    public static JTextField intervalField = new JTextField();
    public static JFileChooser fileNavigator = new JFileChooser();
    
    //Misc
    private final static String newline = "\n";
    private String fileName;
    private int lineCount= 0;
    private int newLineCount= 0;
    private String logType;
    private String serverType;
    
    //Timer and Delay
    static int delay = 0;
    static int calculationDelay = 0;
    private Timer timer = new Timer(delay, this);
    
    //Date Information
    public static Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    public static int day = calendar.get(Calendar.DATE);
    public static int month = calendar.get(Calendar.MONTH) + 1;
    public static int year = calendar.get(Calendar.YEAR);


    
    // This class, "LogReader" is used to create the gridlayout for the text area as well as create the button layout in the application.
    // Also shown below is the code for each button and what happens when the button is pressed. The three buttons being used are Current
    // Date (Returns the current date), Input Date (Returns user input date), and Start (Starts the log).
    public LogReader() {
        super(new GridBagLayout());
        textArea = new JTextArea(30, 65);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        
        lastUpdatedField.setEditable(false);
        lastUpdatedField.setHorizontalAlignment(10);
        intervalField.setEditable(false);
        intervalField.setHorizontalAlignment(10);
        intervalField.setHorizontalAlignment(SwingConstants.RIGHT);
        intervalField.setText("Refresh Interval (mins): " + String.format("%02d", logreader.setIntervalFrame.setIntervalFrameDelay));  
  
        //Build File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem startMenuItem = new JMenuItem("Start Program");
        startMenuItem.addActionListener(this);
        JMenuItem exportMenuItem = new JMenuItem("Export");
        //Option Menu
        JMenu optionMenu = new JMenu("Options");
        JMenuItem dateMenuItem = new JMenuItem("Set Date");
        JMenuItem intervalMenuItem = new JMenuItem("Interval");
        JMenu logTypeMenu = new JMenu("Log Type");
        JMenu serverTypeMenu = new JMenu("Server Type");
        JMenuItem clearMenu = new JMenu("Clear");
        JMenuItem clearTextSubMenuItem = new JMenuItem("Clear Text Only");
        JMenuItem clearAllSubMenuItem = new JMenuItem("Clear Text and Stop");
        
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
        ButtonGroup group0 = new ButtonGroup();
        group0.add(functionLog);
        group0.add(projectBridgeLog);
        group0.add(prosightLog);
        JRadioButton productionServer = new JRadioButton("Production Server");
        functionLog.setMnemonic(KeyEvent.VK_A);
        functionLog.setActionCommand("Production Server");
        JRadioButton testServer = new JRadioButton("Test Server");
        projectBridgeLog.setMnemonic(KeyEvent.VK_B);
        projectBridgeLog.setActionCommand("Test Server");
        ButtonGroup group1 = new ButtonGroup();
        group1.add(productionServer);
        group1.add(testServer);

        //Group Components Together
        fileMenu.add(startMenuItem);
        fileMenu.add(exportMenuItem);
        optionMenu.add(dateMenuItem);
        optionMenu.add(intervalMenuItem);
        optionMenu.add(logTypeMenu);
        logTypeMenu.add(functionLog);
        logTypeMenu.add(projectBridgeLog);
        logTypeMenu.add(prosightLog);
        optionMenu.add(serverTypeMenu);
        serverTypeMenu.add(productionServer);
        serverTypeMenu.add(testServer);
        optionMenu.add(clearMenu);
        clearMenu.add(clearTextSubMenuItem);
        clearMenu.add(clearAllSubMenuItem);  
        menuBar.add(fileMenu);
        menuBar.add(optionMenu);
     
        
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints d = new GridBagConstraints();
        d.gridwidth = GridBagConstraints.REMAINDER;
        d.fill = GridBagConstraints.HORIZONTAL;
        add(menuBar);
        add(lastUpdatedField,c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
        add(intervalField,d);

        
        testServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serverType = "wvs";
                lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
            }
        });
        productionServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serverType = "wpp";
                lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
            }
        });  
        functionLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logType = "function";
                lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
            }
        });  
        projectBridgeLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logType = "ProjectBridge";
                lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
            }
        });
        prosightLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logType = "prosight";
                lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
            }
        });
        dateMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAndShowDateMenuGUI();
            }
        });   
        exportMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileNavigator.setSelectedFile(new File(logType + " log" + "_" + month + "_" + day + "_" + year + "_" + serverType +".xml"));
                int returnVal = fileNavigator.showSaveDialog(exportMenuItem);
                File file = fileNavigator.getSelectedFile();
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    BufferedWriter bw;
                try {
                    bw = new BufferedWriter(new FileWriter(file));
                    bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>" + textArea.getText() + "</data>");
                    bw.flush();
                    bw.close();
                }               
                catch (IOException e1)
                {
                    e1.printStackTrace();
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
        });
        intervalMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAndShowIntervalGUI();
            }
        });
        startMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileName = "\\\\cp-" + serverType + "-ap119d\\log\\" + logType + "_" + String.format("%02d", year) + "_" + String.format("%02d", month) + "_" + String.format("%02d", day) + ".log";
                lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
                calculationDelay = logreader.setIntervalFrame.setIntervalFrameDelay;
                delay = calculationDelay*1000*60;
                timer.setDelay(delay);
            }
        });
        clearAllSubMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //reset GUI
                lastUpdatedField.setText("Text cleared and program stopped");
                textArea.setText(null);
                lineCount=0;
                newLineCount=0;
                timer.stop();
            }
        });
        clearTextSubMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lastUpdatedField.setText("Text cleared");
                textArea.setText(null);
            }
        });
        //Reference setDateFrame 'okButton'
        logreader.setDateFrame.okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //set date
                day = logreader.setDateFrame.defaultDay;
                month = logreader.setDateFrame.defaultMonth;
                year = logreader.setDateFrame.defaultYear;

                //set file name
                fileName = "\\\\cp-" + serverType + "-ap119d\\log\\" + logType + "_" + String.format("%02d", year) + "_" + String.format("%02d", month) + "_" + String.format("%02d", day) + ".log";
                lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
                
                //set delay
                calculationDelay = logreader.setIntervalFrame.setIntervalFrameDelay;
                delay = calculationDelay*1000*60;
                timer.setDelay(delay);
                
                //run program
                startMenuItem.addActionListener(this);
                //createAndShowDateMenuGUI(); 
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
        boolean skipUpdate = false;        
        //Below is the code that allows for the current dates log to be shown as well as the error message if there is no log 
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
              
        
        File f = new File(fileName);
        if(!f.isFile()){
            lastUpdatedField.setText(newline + "File " + fileName + " cannot be found, log is not generated for the date!"+newline);
            skipUpdate = true;
        }
        else{
            try{
                fis = new FileInputStream(fileName);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(new FileReader(fileName));
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
                Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                if(fis!=null)
                    try {
                        fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (skipUpdate == false){
        lastUpdatedField.setText(newline+"Last time updated: " + dateFormat.format(date)+newline);
        //Code ends here
        }
    }
 
    
   
    private static void createAndShowIntervalGUI(){
        //Create and set up the window.
        JFrame setIntervalFrame = new JFrame("Set Refresh Interval");
 
        
        //Add contents to the window.
        setIntervalFrame.add(new setIntervalFrame());
 
        //Display the window.
        setIntervalFrame.pack();
        setIntervalFrame.setVisible(true);
    }
    
    
    
    private static void createAndShowDateMenuGUI(){
        //Create and set up the window.
        JFrame setDateFrame = new JFrame("Set Date");
 
        
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
