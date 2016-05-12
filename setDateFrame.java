/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import static jdk.nashorn.internal.objects.NativeDate.setYear;

/**
 *
 * @author lynne
 */
class setDateFrame extends JPanel {
    
    //Labels and default values for user to input the date of log
    private JLabel yearLabel;
    private JLabel monthLabel;
    private JLabel dayLabel;
    private int defaultYear = 2016;
    private int defaultMonth = 05;
    private int defaultDay = 9;
    
    //set date String Labels
    private static String yearLabelString = "Year (yyyy): ";
    private static String monthLabelString = "Month (mm): ";
    private static String dayLabelString = "Day (dd): ";
    
    ////set date data entry fields
    private JFormattedTextField setYear;
    private JFormattedTextField setMonth;
    private JFormattedTextField setDay;
    
    //format the data entry fields
    private NumberFormat yearFormat;
    private NumberFormat monthFormat;
    private NumberFormat dayFormat;
    
    //Buttons
    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Cancel");
    
    public setDateFrame() {
        
        super(new BorderLayout());
        setUpFormats();
        //Create modified text area
        JTextArea dateMenuTextArea = new JTextArea(30, 65);
        
        //Create Labels
        yearLabel = new JLabel(yearLabelString);
        monthLabel = new JLabel(monthLabelString);
        dayLabel = new JLabel(dayLabelString);
        
        //Create and set up text fields
        setYear = new JFormattedTextField(yearFormat);
        setYear.setValue(new Integer(defaultYear));
        setYear.setColumns(10);
        //setYear.addPropertyChangeListener("value", (PropertyChangeListener) this);
        
        setMonth = new JFormattedTextField(monthFormat);
        setMonth.setValue(new Integer(defaultMonth));
        setMonth.setColumns(10);
        //setMonth.addPropertyChangeListener("value", (PropertyChangeListener) this);
        
        setDay = new JFormattedTextField(dayFormat);
        setDay.setValue(new Integer(defaultDay));
        setDay.setColumns(10);
        //setDay.addPropertyChangeListener("value", (PropertyChangeListener) this);
        
        //set up labels to appropriate text fields
        yearLabel.setLabelFor(setYear);
        monthLabel.setLabelFor(setMonth);
        dayLabel.setLabelFor(setDay);
        
        //Set up labels in a labelPane
        JPanel labelPane = new JPanel(new GridLayout(0,1));
        labelPane.add(yearLabel);
        labelPane.add(monthLabel);
        labelPane.add(dayLabel);
        labelPane.add(okButton,BorderLayout.SOUTH);
        
        //set up the text fields in a panel
        JPanel fieldPane = new JPanel(new GridLayout(0,1));
        fieldPane.add(setYear);
        fieldPane.add(setMonth);
        fieldPane.add(setDay);
        fieldPane.add(cancelButton,BorderLayout.SOUTH);
        
        //Format and add panels
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(labelPane, BorderLayout.CENTER);
        add(fieldPane, BorderLayout.LINE_END);

        
        
    }
    
    //Create and set up number formats. These objects also
    //parse numbers input by user.
    private void setUpFormats() {
        yearFormat = NumberFormat.getNumberInstance();
        yearFormat.setMinimumIntegerDigits(4);
        yearFormat.setMaximumIntegerDigits(4);
 
        monthFormat = NumberFormat.getNumberInstance();
        monthFormat.setMinimumIntegerDigits(2);
        monthFormat.setMaximumIntegerDigits(2);
       
 
        dayFormat = NumberFormat.getNumberInstance();
        dayFormat.setMinimumIntegerDigits(2);
        dayFormat.setMaximumIntegerDigits(2);
    }
}
    

