/*
* PPM Error Log
* Edmund Lynn
* Akash Shah
* 06/01/2016
* VERSION 1.0.0
 */




// Below are all the import that are used throughout this code as well as the package import.
package logreader;

import java.awt.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;



// Below is listed the class definition for the entire code as well as some global constants and buttons that are used throughout the code.
public class LogReader extends JPanel implements ActionListener {
    //create GUI fields 
    protected static JTextField textField;
    public static final JMenuBar menuBar = new JMenuBar();
    protected static JTextArea textArea;
    public static JTextField lastUpdatedField = new JTextField(40);
    public static JTextField intervalField = new JTextField();
    public static JFileChooser fileNavigator = new JFileChooser();
    public static JMenuItem exportNotesMenuItem = new JMenuItem("Export with Notes");
    public static JMenuItem exportMenuItem = new JMenuItem("Export");  
    //Misc
    public static final String versionNumber = "Version 1.0.0";
    public final static String newline = "\n";
    public static String fileName;
    public static int lineCount= 0;
    public static int newLineCount= 0;
    public static String logType = "prosight";
    public static String serverType = "wpp";
    public static String directoryType = "d";
    //Timer and Delay
    public static int delay = 0;
    public static int calculationDelay = 0;
    public Timer timer = new Timer(delay, this);    
    //Date Information
    public final static Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    public static int day = calendar.get(Calendar.DATE);
    public static int month = calendar.get(Calendar.MONTH) + 1;
    public static int year = calendar.get(Calendar.YEAR);
    public final static int todayDay = calendar.get(Calendar.DATE);
    public final static int todayMonth = calendar.get(Calendar.MONTH) + 1;
    public final static int todayYear = calendar.get(Calendar.YEAR);
    //Search Info
    public static int pos;
    
    
    
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
        JMenuItem helpMenuItem = new JMenuItem("Help");
        //Clear Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem findMenuItem = new JMenuItem("Find");
        JMenuItem clearMenuItem = new JMenuItem("Clear Text");
        JMenuItem restartMenuItem = new JMenuItem("Restart");   
        //Option Menu
        JMenu optionMenu = new JMenu("Options");
        JMenuItem dateMenuItem = new JMenuItem("Set Date");
        JMenuItem intervalMenuItem = new JMenuItem("Interval");
        JMenu logTypeMenu = new JMenu("Log Type");
        JMenu serverTypeMenu = new JMenu("Server Type");
        JMenu directoryTypeMenu = new JMenu("Directory");  
        //Create the radio buttons.
        JRadioButton functionLog = new JRadioButton("Function Log");
        functionLog.setActionCommand("function Log");
        JRadioButton projectBridgeLog = new JRadioButton("Project Bridge Log");
        projectBridgeLog.setActionCommand("Project Bridge Log");
        JRadioButton prosightLog = new JRadioButton("Prosight Log");
        prosightLog.setActionCommand("Prosight Log");
        prosightLog.setSelected(true);
        ButtonGroup group0 = new ButtonGroup();
        group0.add(functionLog);
        group0.add(projectBridgeLog);
        group0.add(prosightLog);
        JRadioButton productionServer = new JRadioButton("Production Server");
        productionServer.setActionCommand("Production Server");
        JRadioButton testServer = new JRadioButton("Test Server");
        testServer.setActionCommand("Test Server");
        productionServer.setSelected(true);
        ButtonGroup group1 = new ButtonGroup();
        group1.add(productionServer);
        group1.add(testServer);
        JRadioButton cDirectory = new JRadioButton("Front End Server");
        cDirectory.setActionCommand("Front End Server");
        JRadioButton dDirectory = new JRadioButton("Back End Server");
        dDirectory.setActionCommand("Back End Server");
        dDirectory.setSelected(true);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(cDirectory);
        group2.add(dDirectory);

        
        //Group Components Together
        fileMenu.add(startMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exportMenuItem);
        fileMenu.add(exportNotesMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(helpMenuItem);
        optionMenu.add(dateMenuItem);
        optionMenu.add(intervalMenuItem);
        optionMenu.add(logTypeMenu);
        logTypeMenu.add(functionLog);
        logTypeMenu.add(projectBridgeLog);
        logTypeMenu.add(prosightLog);
        optionMenu.add(serverTypeMenu);
        serverTypeMenu.add(productionServer);
        serverTypeMenu.add(testServer);
        optionMenu.add(directoryTypeMenu);
        directoryTypeMenu.add(cDirectory);
        directoryTypeMenu.add(dDirectory);
        editMenu.add(findMenuItem);
        editMenu.addSeparator();
        editMenu.add(clearMenuItem);
        editMenu.add(restartMenuItem);  
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
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
                
        
        //Set menu item and radio button hints
        startMenuItem.setToolTipText("Start the log reader");
        exportMenuItem.setToolTipText("Export log into .XML format");
        exportNotesMenuItem.setToolTipText("Export log into .XML format wiht notes appended to each error");
        helpMenuItem.setToolTipText("Information/Tutorials on how to use the log reader");
        findMenuItem.setToolTipText("Search through the log");
        clearMenuItem.setToolTipText("Clear text from the log program");
        restartMenuItem.setToolTipText("Clear and stop running the log program");
        intervalMenuItem.setToolTipText("Select number of minutes before refreshing the log");
        dateMenuItem.setToolTipText("Select the date of the log to be read");
        prosightLog.setToolTipText("Set the log type to Prosight");
        functionLog.setToolTipText("Set the log type to Function");
        projectBridgeLog.setToolTipText("Set the log type to Project Bridge");
        productionServer.setToolTipText("Set the server type to Production");
        testServer.setToolTipText("Set the server type to Test");
        //Setup shortcut keys for quick actions
        helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
        findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        startMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        restartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        clearMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        intervalMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        dateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        exportNotesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        
        
        //Setup action listeners
        exportNotesMenuItem.addActionListener((ActionEvent e) -> {
            logreader.exportFile.exportWithNotes(); 
        });
        helpMenuItem.addActionListener((ActionEvent e) -> {
            logreader.helpAndFind.helpItem();
        });
        cDirectory.addActionListener((ActionEvent e) -> {
            directoryType = "c";
            lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server in the " + directoryType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
        });
        dDirectory.addActionListener((ActionEvent e) -> {
            directoryType = "d";
            lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server in the " + directoryType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
        });
        findMenuItem.addActionListener((ActionEvent e) -> {
            logreader.helpAndFind.findItems();
        });
        testServer.addActionListener((ActionEvent e) -> {
            serverType = "wvs";
            lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server in the " + directoryType +  " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
        });
        productionServer.addActionListener((ActionEvent e) -> {
            serverType = "wpp";
            lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server in the " + directoryType +  " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
        });  
        functionLog.addActionListener((ActionEvent e) -> {
            logType = "function";
            lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server in the " + directoryType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
        });  
        projectBridgeLog.addActionListener((ActionEvent e) -> {
            logType = "ProjectBridge";
            lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server in the " + directoryType +  " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
        });
        prosightLog.addActionListener((ActionEvent e) -> {
            logType = "prosight";
            lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server in the " + directoryType +  " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
        });
        dateMenuItem.addActionListener((ActionEvent e) -> {
            createAndShowDateMenuGUI();
        });
        exportMenuItem.addActionListener((ActionEvent e) -> {
            logreader.exportFile.exportFile();
        });
        intervalMenuItem.addActionListener((ActionEvent e) -> {
            createAndShowIntervalGUI();
        });
        startMenuItem.addActionListener((ActionEvent e) -> {
            fileName = "\\\\cp-" + serverType + "-ap119" + directoryType + "\\log\\" + logType + "_" + String.format("%02d", year) + "_" + String.format("%02d", month) + "_" + String.format("%02d", day) + ".log";
            lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
            calculationDelay = logreader.setIntervalFrame.setIntervalFrameDelay;
            delay = calculationDelay*1000*60;
            timer.setDelay(delay);
        });
        restartMenuItem.addActionListener((ActionEvent e) -> {
            //reset GUI
            lastUpdatedField.setText("Text cleared and program stopped");
            textArea.setText(null);
            lineCount=0;
            newLineCount=0;
            timer.stop();
            UIManager.put("OptionPane.okButtonText", "OK");
            JOptionPane.showMessageDialog(null, "The program has been restarted successfully!");             
        });
        clearMenuItem.addActionListener((ActionEvent e) -> {
            lastUpdatedField.setText("Text cleared");
            textArea.setText(null);
            UIManager.put("OptionPane.okButtonText", "OK");
            JOptionPane.showMessageDialog(null, "The text area has been cleared successfully!");
        });
        //Reference setDateFrame 'okButton'
        logreader.setDateFrame.okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //set date
                day = logreader.setDateFrame.defaultDay;
                month = logreader.setDateFrame.defaultMonth;
                year = logreader.setDateFrame.defaultYear;

                //set file name
                fileName = "\\\\cp-" + serverType + "-ap119" + directoryType + "\\log\\" + logType + "_" + String.format("%02d", year) + "_" + String.format("%02d", month) + "_" + String.format("%02d", day) + ".log";
                lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType +  " server in the " + directoryType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
                
                //set delay
                calculationDelay = logreader.setIntervalFrame.setIntervalFrameDelay;
                delay = calculationDelay*1000*60;
                timer.setDelay(delay);
                
                //run program
                startMenuItem.addActionListener(this);
           }
        });
    }
    
 
    
    // Below is the main part of the code which is the file input as well as error handling. Using the date which is determined from the user, the code
    // searches for the given days log and tries to input the log into the text area window. If the log does not exist, it displays an error message saying
    // so. Also displayed is the time the log was put onto the screen as well as the number of lines.
    @Override
    public void actionPerformed(ActionEvent evt) {
        InputStream fis = null;
        InputStreamReader isr;
        BufferedReader br;
        String line;
        int i = 0;
        boolean skipUpdate = false;        
        //Below is the code that allows for the current dates log to be shown as well as the error message if there is no log 
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
               
        File f = new File(fileName);
        if(!f.isFile()){
            UIManager.put("OptionPane.okButtonText", "OK");
            JOptionPane.showMessageDialog(null, "File " + fileName + " cannot be found, log is not generated for the date!","Error", JOptionPane.ERROR_MESSAGE);
            lastUpdatedField.setText("File " + fileName + " cannot be found, log is not generated for the date!");
            skipUpdate = true;
        }
        else if((day != todayDay) || (month != todayMonth) || (year != todayYear)){
            try{
                timer.stop();
                skipUpdate = true;
                fis = new FileInputStream(fileName);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(new FileReader(fileName));
                LineNumberReader reader = new LineNumberReader(br);
                while((i=fis.read())!=-1){
                   line = reader.readLine();
                   lineCount=reader.getLineNumber();
                    if(line==null || lineCount<=newLineCount){
                        reader.setLineNumber(lineCount);
                    }else{

                       textArea.append(line+newline);
                       newLineCount=lineCount;
                    }
                }
                fis.close();
                lastUpdatedField.setText(newline + fileName + newline);
                lineCount=0;
                newLineCount=0;
            }catch(Exception ex){
                Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                if(fis!=null){
                    try {
                        fis.close();
                    }catch (IOException ex) {
                        Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (skipUpdate == false){
            lastUpdatedField.setText(newline+ "Last time updated: " + dateFormat.format(date) + newline);
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
    private static void createAndShowGUI(JFrame frame){
        //Create and set up the window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new LogReader());
 
        //Display the window.
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }
 
    
    
    public static void splashScreen() throws MalformedURLException{
        JWindow window = new JWindow();
        JLabel splashScreen = new JLabel(versionNumber, new ImageIcon(new URL("http://projects.pepcoholdings.biz/sites/cbciwcpm/PPM%20Support/PPM/Error%20Log%20Files/Splash%20Screen.jpg")), SwingConstants.CENTER);
        splashScreen.setHorizontalTextPosition(JLabel.CENTER);
        splashScreen.setVerticalTextPosition(JLabel.BOTTOM);
        splashScreen.setOpaque(true);
        splashScreen.setBackground(Color.black);
        splashScreen.setForeground(Color.white);
        window.getContentPane().add(splashScreen); 
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
        }
        window.setVisible(false);
        window.dispose();
        UIManager.put("OptionPane.okButtonText", "OK");
        JOptionPane.showMessageDialog(null, "Welcome to Primervera Portfolio Management Error Log Reader!" + newline + "For information on how to use the program, go to the file menu." + newline + "Press OK if ready to begin." ,"Welcome", JOptionPane.INFORMATION_MESSAGE);
        try {
            Thread.sleep(1200);
        } catch (InterruptedException ex) {
            Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    // The main class which runs the entire code.
    public static void main(String[]args) throws MalformedURLException{
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.        
        splashScreen();
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Primavera Portfolio Management Log Reader " + versionNumber);
            createAndShowGUI(frame);
        });
    }
}
