
/*
* Edmund Lynn
* 5/4/2016
 */
 

 
package components;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileInputStream;
import javax.swing.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
 

public class TextDemo extends JPanel implements ActionListener {
    protected JTextField textField;
    private JButton buttonStart = new JButton("Start");
    protected JTextArea textArea;
    private final static String newline = "\n";
    
 
    public TextDemo() {
        super(new GridBagLayout());
        
        int delay = 100000; //millisecond
        
 
        buttonStart.addActionListener(this);
 
        textArea = new JTextArea(30, 65);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
           
        new Timer(delay, this).start();
        
        
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
 
        c.fill = GridBagConstraints.HORIZONTAL;
        add(buttonStart, c);
 
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
    }
 
    public void actionPerformed(ActionEvent evt) {
        FileInputStream fis = null;
        int i = 0;
        char c;
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        textArea.append(newline+"Last time updated 1: " + dateFormat.format(date)+newline+newline);
        
        try{
            fis = new FileInputStream("\\\\CP-WVS-AP119D\\log\\prosight_2016_05_04.log");
            
            
            
            
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
                try {
                    fis.close();
            } catch (IOException ex) {
                Logger.getLogger(TextDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        textArea.append(newline+"Last time updated 2: " + dateFormat.format(date));
        textArea.append("Line count: "+textArea.getLineCount());
        
        
 

    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("PPM Prosight Log");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        frame.add(new TextDemo());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        

        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}


