/*
* PPM Error Log
* Edmund Lynn
* Akash Shah
* 06/15/2016
* VERSION 2.0.1
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
import static java.lang.String.join;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static logreader.setLogType.inputDirectory;
import static logreader.setLogType.inputLog;
import static logreader.setLogType.inputServer;
import static logreader.setLogType.setDirectory;
import static logreader.setLogType.setLog;
import static logreader.setLogType.setServer;



// Below is listed the class definition for the entire code as well as some global constants and buttons that are used throughout the code.
public class LogReader extends JPanel implements ActionListener {
    //create GUI fields 
    public static JFrame setLogType;
    public static JFrame frontLogAnalysis;
    public static JFrame backLogAnalysis;
    public static JFrame setDateFrame;
    public static JFrame setIntervalFrame;
    protected static JTextField textField;
    public static final JMenuBar menuBar = new JMenuBar();
    protected static JTextArea textArea;
    public static JTextField lastUpdatedField = new JTextField();
    public static JTextField intervalField = new JTextField();
    public static JTextField bridgeField = new JTextField();
    public static JFileChooser fileNavigator = new JFileChooser();
    public static JMenuItem exportNotesMenuItem = new JMenuItem("Export with Notes");
    public static JMenuItem exportMenuItem = new JMenuItem("Export"); 
    //Misc
    public static final String versionNumber = "Version 2.1.0";
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
    public static int hours = 0;
    public static int minutes = 0;
    public static int numwind = 0;
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
        textArea.setForeground(Color.gray);
        textArea.setBackground(Color.black);
        JScrollPane scrollPane = new JScrollPane(textArea);    
        lastUpdatedField.setEditable(false);
        lastUpdatedField.setHorizontalAlignment(SwingConstants.CENTER);
        intervalField.setEditable(false);
        intervalField.setHorizontalAlignment(SwingConstants.CENTER);
        intervalField.setText("--Set Frame Width to be this wide by default--");
        intervalField.setText("Refresh Interval (Minutes): " + String.format("%02d", logreader.setIntervalFrame.setIntervalFrameDelay));
        bridgeField.setEditable(false);
        bridgeField.setHorizontalAlignment(SwingConstants.CENTER);
        bridgeField.setText("--Set Frame Width to be this wide by default--");
        bridgeField.setText("Duration: " + String.format("%02d", hours) + " hr(s) and " + String.format("%02d", minutes) + " min(s)");

        
        //Set Initial File Name
        fileName = "\\\\cp-" + serverType + "-ap119" + directoryType + "\\log\\" + logType + "_" + String.format("%02d", year) + "_" + String.format("%02d", month) + "_" + String.format("%02d", day) + ".log";
  
        
        //Build File menu
        JMenu fileMenu = new JMenu(" File  ");
        JMenuItem startMenuItem = new JMenuItem("Start Program");
        startMenuItem.addActionListener(this);
        JMenuItem helpMenuItem = new JMenuItem("Tutorial");
        //Clear Menu
        JMenu editMenu = new JMenu(" Edit  ");
        JMenuItem findMenuItem = new JMenuItem("Find");
        JMenuItem clearMenuItem = new JMenuItem("Clear Text");
        JMenuItem restartMenuItem = new JMenuItem("Restart");   
        //Option Menu
        JMenu optionMenu = new JMenu(" Setup  ");
        JMenuItem dateMenuItem = new JMenuItem("Set Date");
        JMenuItem intervalMenuItem = new JMenuItem("Set Interval");
        JMenuItem logMenuItem = new JMenuItem("Set Log Type");
        
        //Tools
        JMenu toolsMenu = new JMenu(" Tools  ");
        JMenuItem frontAnalysisMenuItem = new JMenuItem("Front End Log Analysis (ap119c)");
        JMenuItem backAnalysisMenuItem = new JMenuItem("Back End Log Analysis (ap119d)");
        JMenuItem openDirectoryMenuItem = new JMenuItem("Open File Directory");
        
        
        //Group Components Together
        fileMenu.add(startMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exportMenuItem);
        fileMenu.add(exportNotesMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(helpMenuItem);
        optionMenu.add(dateMenuItem);
        optionMenu.add(intervalMenuItem);
        optionMenu.add(logMenuItem);
        editMenu.add(findMenuItem);
        editMenu.addSeparator();
        editMenu.add(clearMenuItem);
        editMenu.add(restartMenuItem);
        toolsMenu.add(frontAnalysisMenuItem);
        toolsMenu.add(backAnalysisMenuItem);
        toolsMenu.addSeparator();
        toolsMenu.add(openDirectoryMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(optionMenu);  
        menuBar.add(toolsMenu);
        //Add Components to this panel.
        GridBagConstraints b = new GridBagConstraints();
        b.gridwidth = GridBagConstraints.REMAINDER;
        b.fill = GridBagConstraints.BOTH;
        b.weightx = 1.0;
        b.weighty = 1.0;
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints d1 = new GridBagConstraints();
        d1.anchor = GridBagConstraints.FIRST_LINE_START;
        d1.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints d2 = new GridBagConstraints();
        d2.anchor = GridBagConstraints.LINE_START;
        GridBagConstraints d3 = new GridBagConstraints();
        d3.anchor = GridBagConstraints.LINE_END;
        add(menuBar,d1);
        add(lastUpdatedField,c);
        add(scrollPane,b);
        add(bridgeField,d2);
        add(intervalField,d3);
                
        
        //Set menu item and radio button hints
        startMenuItem.setToolTipText("Start the log reader");
        exportMenuItem.setToolTipText("Export log into .XML format");
        exportNotesMenuItem.setToolTipText("Export log into .XML format with notes appended to each error");
        frontAnalysisMenuItem.setToolTipText("Shows brief analysis of two front end production logs");
        backAnalysisMenuItem.setToolTipText("Shows brief analysis of three back end production logs");
        helpMenuItem.setToolTipText("Information/Tutorials on how to use the log reader");
        findMenuItem.setToolTipText("Search through the log");
        clearMenuItem.setToolTipText("Clear text from the log program");
        restartMenuItem.setToolTipText("Clear and stop running the log program");
        intervalMenuItem.setToolTipText("Select number of minutes before refreshing the log");
        dateMenuItem.setToolTipText("Select the date of the log to be read");
        logMenuItem.setToolTipText("Select the type of log to be read");
        openDirectoryMenuItem.setToolTipText("Open current file location in Explorer");
        //Setup shortcut keys for quick actions
        helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        frontAnalysisMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
        backAnalysisMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
        findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        startMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        restartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        clearMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
        intervalMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        dateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        logMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        exportNotesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        openDirectoryMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        
        //Setup action listeners
        openDirectoryMenuItem.addActionListener((ActionEvent e) -> {
            try { 
                Runtime.getRuntime().exec("explorer.exe /select," + fileName);
            } catch (IOException ex) {
                Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        exportNotesMenuItem.addActionListener((ActionEvent e) -> {
            logreader.exportFile.exportWithNotes(); 
        });
        helpMenuItem.addActionListener((ActionEvent e) -> {
            logreader.helpAndFind.helpItem();
        });
        frontAnalysisMenuItem.addActionListener((ActionEvent e) -> {
            createAndShowFrontLogAnalysisGUI();
        });
        backAnalysisMenuItem.addActionListener((ActionEvent e) -> {
            createAndShowBackLogAnalysisGUI();
        });
        findMenuItem.addActionListener((ActionEvent e) -> {
            logreader.helpAndFind.findItems();
        });
        logMenuItem.addActionListener((ActionEvent e) -> {
            createAndShowLogMenuGUI();
            setLog.setText(inputLog);
            setServer.setText(inputServer);
            setDirectory.setText(inputDirectory);
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
            String logType0 = inputLog;
            String serverType0 = inputServer;
            String directoryType0 = inputDirectory;
            
            if(logType0 != null || serverType0 != null || directoryType0 != null){
                logType = inputLog;
                serverType = inputServer;
                directoryType = inputDirectory;
            }
            fileName = "\\\\cp-" + serverType + "-ap119" + directoryType + "\\log\\" + logType + "_" + String.format("%02d", year) + "_" + String.format("%02d", month) + "_" + String.format("%02d", day) + ".log";
            lastUpdatedField.setText("Press Start to retrieve the " + logType + " log in the " + serverType + " server for the following date: " + String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%02d", year)+newline);
            calculationDelay = logreader.setIntervalFrame.setIntervalFrameDelay;
            delay = calculationDelay*1000*60;
            timer.setDelay(delay);
        });
        restartMenuItem.addActionListener((ActionEvent e) -> {
            lastUpdatedField.setText("Text cleared and program stopped");
            textArea.setText(null);
            lineCount=0;
            newLineCount=0;
            timer.stop();
            UIManager.put("OptionPane.okButtonText", "OK");
            logType = "prosight";
            serverType = "wpp";
            directoryType = "d";
            logreader.setLogType.inputLog = logType;
            logreader.setLogType.inputServer = serverType;
            logreader.setLogType.inputDirectory = directoryType;
            logreader.setDateFrame.defaultDay = calendar.get(Calendar.DATE);
            logreader.setDateFrame.defaultMonth = calendar.get(Calendar.MONTH) + 1;
            logreader.setDateFrame.defaultYear = calendar.get(Calendar.YEAR);
            day = calendar.get(Calendar.DATE);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            hours = 0;
            minutes = 0;
            numwind = 0;
            fileName = "\\\\cp-" + serverType + "-ap119" + directoryType + "\\log\\" + logType + "_" + String.format("%02d", year) + "_" + String.format("%02d", month) + "_" + String.format("%02d", day) + ".log";
            bridgeField.setText("Duration: " + String.format("%02d", hours) + " hr(s) and " + String.format("%02d", minutes) + " min(s)");
            logreader.backLogAnalysis.pbhours = 0;
            logreader.backLogAnalysis.pbminutes = 0;
            logreader.backLogAnalysis.pbstarthours = 0;
            logreader.backLogAnalysis.pbstartminutes = 0;
            logreader.backLogAnalysis.pbendhours = 0;
            logreader.backLogAnalysis.pbendminutes = 0;
            logreader.backLogAnalysis.funhours = 0;
            logreader.backLogAnalysis.funminutes = 0;
            logreader.backLogAnalysis.funstarthours = 0;
            logreader.backLogAnalysis.funstartminutes = 0;
            logreader.backLogAnalysis.funendhours = 0;
            logreader.backLogAnalysis.funendminutes = 0;
            logreader.backLogAnalysis.prohours = 0;
            logreader.backLogAnalysis.prominutes = 0;
            logreader.backLogAnalysis.prostarthours = 0;
            logreader.backLogAnalysis.prostartminutes = 0;
            logreader.backLogAnalysis.proendhours = 0;
            logreader.backLogAnalysis.proendminutes = 0;
            logreader.frontLogAnalysis.pbhours = 0;
            logreader.frontLogAnalysis.pbminutes = 0;
            logreader.frontLogAnalysis.pbstarthours = 0;
            logreader.frontLogAnalysis.pbstartminutes = 0;
            logreader.frontLogAnalysis.pbendhours = 0;
            logreader.frontLogAnalysis.pbendminutes = 0;
            logreader.frontLogAnalysis.prohours = 0;
            logreader.frontLogAnalysis.prominutes = 0;
            logreader.frontLogAnalysis.prostarthours = 0;
            logreader.frontLogAnalysis.prostartminutes = 0;
            logreader.frontLogAnalysis.proendhours = 0;
            logreader.frontLogAnalysis.proendminutes = 0;
            JOptionPane.showMessageDialog(null, "The program has been restarted successfully!");             
        });
        clearMenuItem.addActionListener((ActionEvent e) -> {
            lastUpdatedField.setText("Text cleared");
            textArea.setText(null);
            UIManager.put("OptionPane.okButtonText", "OK");
            JOptionPane.showMessageDialog(null, "The text area has been cleared successfully!");
        });
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
        InputStreamReader isr = null;
        BufferedReader br = null;
        String line;
        int i = 0;
        boolean skipUpdate = false;        
        //Below is the code that allows for the current dates log to be shown as well as the error message if there is no log 
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try{       
            File f = new File(fileName);
            long bytes = f.length();
            if (bytes>10000000){    
                int result = JOptionPane.showConfirmDialog(null, "The chosen log is over 10,000 KB which will take time to load. Would you like to continue?", "File Size to Big", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                if ((result == JOptionPane.CANCEL_OPTION) || (result == JOptionPane.CLOSED_OPTION)){
                    f = null;
                }
            }
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
                    isr.close();
                    br.close();
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
                            isr.close();
                            br.close();
                        }catch (IOException ex) {
                            Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            if(skipUpdate == false){
                lastUpdatedField.setText(newline+ "Last time updated: " + dateFormat.format(date) + newline);
            }
            if("ProjectBridge".equals(logType)){
                if(f.isFile()){
                    String logText = textArea.getText();
                    String timeTag = "<time>";
                    String timeEndTag = "</time>";
                    String testValue = "<time>xx:xx:xx XX</time>";
                    String timeValue;
                    ArrayList<String> timeMatrix = new ArrayList<>();
                    int len = testValue.length();
                    int index = logText.indexOf(timeTag);
                    while ( index >= 0 ) {
                        timeValue = logText.substring(index, index + len);
                        timeMatrix.add(timeValue);
                        index = logText.indexOf(timeTag, index+len);
                    }
                    int sizeof = timeMatrix.size();
                    String firstTime = timeMatrix.get(0);
                    String lastTime = timeMatrix.get(sizeof-1);
                    int firstHour = Integer.parseInt(firstTime.substring(6, 8));
                    int lastHour = Integer.parseInt(lastTime.substring(6, 8));
                    int firstMin = Integer.parseInt(firstTime.substring(9, 11));
                    int lastMin = Integer.parseInt(lastTime.substring(9, 11));
                    hours = lastHour - firstHour;
                    minutes = lastMin - firstMin;
                    if (minutes < 0){
                        minutes+=60;
                        hours--;
                    }
                    if (hours < 0){
                        hours+=12;
                    }

                    bridgeField.setText("Duration: " + String.format("%02d", hours) + " hr(s) and " + String.format("%02d", minutes) + " min(s)");
                    if(hours >= 3){
                        numwind++;
                        if(numwind == 1){
                            emailClient();
                        }
                    }
                }
            }
            if("function".equals(logType)){
                if(f.isFile()){
                    String logText = textArea.getText();
                    String timeTag = "<TIME>";
                    String testValue = "<TIME>xx:xx:xx</TIME>";
                    String timeValue;
                    ArrayList<String> timeMatrix = new ArrayList<>();
                    int len = testValue.length();
                    int index = logText.indexOf(timeTag);
                    while ( index >= 0 ) {
                        timeValue = logText.substring(index, index + len);
                        timeMatrix.add(timeValue);
                        index = logText.indexOf(timeTag, index+len);
                    }
                    int sizeof = timeMatrix.size();
                    String firstTime = timeMatrix.get(0);
                    String lastTime = timeMatrix.get(sizeof-1);
                    int firstHour = Integer.parseInt(firstTime.substring(6, 8));
                    int lastHour = Integer.parseInt(lastTime.substring(6, 8));
                    int firstMin = Integer.parseInt(firstTime.substring(9, 11));
                    int lastMin = Integer.parseInt(lastTime.substring(9, 11));
                    hours = lastHour - firstHour;
                    minutes = lastMin - firstMin;
                    if (minutes < 0){
                        minutes+=60;
                        hours--;
                    }
                    bridgeField.setText("Duration: " + String.format("%02d", hours) + " hr(s) and " + String.format("%02d", minutes) + " min(s)");
                }
            }
            if("prosight".equals(logType)){
                if(f.isFile()){
                    String logText = textArea.getText();
                    String pattern0 = "Time=.*Class";
                    Pattern timePattern = Pattern.compile(pattern0);
                    Matcher timeMatcher = timePattern.matcher(logText);
                    ArrayList<String> timeMatrix = new ArrayList<>();
                    while(timeMatcher.find()){
                        timeMatrix.add(timeMatcher.group());
                    }
                    int sizeof = timeMatrix.size();
                    String firstTime = timeMatrix.get(0).replaceAll("[^0-9: ]", "");
                    String lastTime = timeMatrix.get(sizeof-1).replaceAll("[^0-9: ]", "");
                    int firsthour0 = firstTime.indexOf(" ");
                    int firsthour1 = firstTime.indexOf(":");
                    int lasthour0 = lastTime.indexOf(" ");
                    int lasthour1 = lastTime.indexOf(":");
                    int firstminute0 = firstTime.indexOf(":", firsthour1 + 1);
                    int lastminute0 = lastTime.indexOf(":", lasthour1 + 1);
                    int firstHour = Integer.parseInt(firstTime.substring(firsthour0, firsthour1).replaceAll("[^0-9]",""));
                    int lastHour = Integer.parseInt(lastTime.substring(lasthour0, lasthour1).replaceAll("[^0-9]",""));
                    int firstMin = Integer.parseInt(firstTime.substring(firsthour1, firstminute0).replaceAll("[^0-9]",""));
                    int lastMin = Integer.parseInt(lastTime.substring(lasthour1, lastminute0).replaceAll("[^0-9]",""));
                    hours = lastHour - firstHour;
                    minutes = lastMin - firstMin;
                    if (minutes < 0){
                        minutes+=60;
                        hours--;
                    }
                    bridgeField.setText("Duration: " + String.format("%02d", hours) + " hr(s) and " + String.format("%02d", minutes) + " min(s)");
                }
            }
        } catch(HeadlessException | NumberFormatException e){
            UIManager.put("OptionPane.okButtonText", "OK");
            JOptionPane.showMessageDialog(null, "An error has occured while reading the log!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    public static void emailClient(){
        try{
            UIManager.put("OptionPane.okButtonText", "OK");
            int result = JOptionPane.showConfirmDialog(null, "The duration of the PPM Bridge Log is over 3 hours!\nDo you want to send an email?", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if ((result == JOptionPane.OK_OPTION)){
                ArrayList<String> monthMatrix = new ArrayList<>();
                monthMatrix.add("January");
                monthMatrix.add("February");
                monthMatrix.add("March");
                monthMatrix.add("April");
                monthMatrix.add("May");
                monthMatrix.add("June");
                monthMatrix.add("July");
                monthMatrix.add("August");
                monthMatrix.add("September");
                monthMatrix.add("October");
                monthMatrix.add("November");
                monthMatrix.add("December");

                ArrayList<String> recepMatrix = new ArrayList<>();
                recepMatrix.add("Cheryl.Crist@pepcoholdings.com");
                recepMatrix.add("Kim.Deely@pepcoholdings.com");
                recepMatrix.add("Edmund.Lynn@pepcoholdings.com");
                recepMatrix.add("Elizabeth.May@pepcoholdings.com");
                recepMatrix.add("Akash.Shah@pepcoholdings.com");
                String subject = "Bridge Log Over 3 Hours";
                String body = "Hello,\n\nThe Bridge Log Duration for the date of " + monthMatrix.get(month-1) + " " + day + ", " + year + ", is " + hours + " hour(s) and " + minutes
                    + " minute(s). This duration is above the mark of 3 hours and should be looked at.\n\n\n"
                    + "This is an automatically prepopulated email through the PPM Log Reader Application!\n"
                    + "OPPM Log Reader Application\n" + "Pepco Holdings Inc.\n" + "A JAVA Application\n";
                String uriStr = String.format("mailto:%s?subject=%s&body=%s",join(";", recepMatrix),
                URLEncoder.encode(subject, "UTF-8").replace("+", "%20"), URLEncoder.encode(body, "UTF-8").replace("+", "%20"));
                Desktop.getDesktop().browse(new URI(uriStr));
            }
        }catch (IOException | URISyntaxException ex) {
            Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    
    
    private static void createAndShowIntervalGUI(){
        //Create and set up the window.
        setIntervalFrame = new JFrame("Set Refresh Interval");
 
        
        //Add contents to the window.
        setIntervalFrame.add(new setIntervalFrame());
 
        
        //Display the window.
        setIntervalFrame.pack();
        setIntervalFrame.setVisible(true);
    }
    
    
    
    private static void createAndShowDateMenuGUI(){
        //Create and set up the window.
        setDateFrame = new JFrame("Set Date");
 
        
        //Add contents to the window.
        setDateFrame.add(new setDateFrame());
 
        
        //Display the window.
        setDateFrame.pack();
        setDateFrame.setVisible(true);
    }
    
    
    
    private static void createAndShowLogMenuGUI(){
        //Create and set up the window.
        setLogType = new JFrame("Set Log Types");
        
        
        //Add contents to the window.
        setLogType.add(new setLogType());
        
        
        //Display the window.
        setLogType.pack();
        setLogType.setVisible(true);
    }
    
    
    
    private static void createAndShowFrontLogAnalysisGUI(){
        //Create and set up the window.
        frontLogAnalysis = new JFrame("Oracle Primavera Front End Log Analysis");
        frontLogAnalysis.getContentPane().setSize(475,300);
        
        
        //Add contents to the window.
        frontLogAnalysis.add(new frontLogAnalysis());
        
        
        //Display the window.
        frontLogAnalysis.pack();
        frontLogAnalysis.setVisible(true);
    }
    
    
    
    private static void createAndShowBackLogAnalysisGUI(){
        //Create and set up the window.
        backLogAnalysis = new JFrame("Oracle Primavera Back End Log Analysis");
        backLogAnalysis.getContentPane().setSize(475,300);
        
        
        //Add contents to the window.
        backLogAnalysis.add(new backLogAnalysis());
        
        
        //Display the window.
        backLogAnalysis.pack();
        backLogAnalysis.setVisible(true);
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
    }
    
    
    
    // The main class which runs the entire code.
    public static void main(String[]args) throws MalformedURLException{
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        splashScreen();
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Oracle Primavera Log Reader " + versionNumber);
            createAndShowGUI(frame);
        });
    }
}
