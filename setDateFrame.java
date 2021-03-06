/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logreader;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author lynne
 */
class setDateFrame extends JPanel implements PropertyChangeListener {
    //Labels and default values for user to input the date of log
    private final JLabel yearLabel;
    private final JLabel monthLabel;
    private final JLabel dayLabel;
    public final static Calendar now = Calendar.getInstance();
    public static int defaultYear = now.get(Calendar.YEAR);
    public static int defaultMonth = now.get(Calendar.MONTH) + 1;
    public static int defaultDay = now.get(Calendar.DAY_OF_MONTH);
    
    //set date String Labels
    private final static String yearLabelString = "Year (yyyy): ";
    private final static String monthLabelString = "Month (mm): ";
    private final static String dayLabelString = "Day (dd): ";
    
    ////set date data entry fields
    private final JFormattedTextField setYear;
    private final JFormattedTextField setMonth;
    private final JFormattedTextField setDay;
    
    //format the data entry fields
    private NumberFormat yearFormat;
    private NumberFormat monthFormat;
    private NumberFormat dayFormat;
    
    //Buttons
    public static JButton okButton = new JButton("OK");
    public static JButton cancelButton = new JButton("Cancel");
    
    
    
    public setDateFrame() {
        super(new BorderLayout());
        setUpFormats();

        
        //Create Labels
        yearLabel = new JLabel(yearLabelString);
        monthLabel = new JLabel(monthLabelString);
        dayLabel = new JLabel(dayLabelString);
        
        
        //Create and set up text fields
        setYear = new JFormattedTextField(yearFormat);
        setYear.setValue(defaultYear);
        setYear.setColumns(10);
        setYear.addPropertyChangeListener("value", this);
        setMonth = new JFormattedTextField(monthFormat);
        setMonth.setValue(defaultMonth);
        setMonth.setColumns(10);
        setMonth.addPropertyChangeListener("value", this);
        setDay = new JFormattedTextField(dayFormat);
        setDay.setValue(defaultDay);
        setDay.setColumns(10);
        setDay.addPropertyChangeListener("value", this);
        
        
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
        
        
        okButton.addActionListener((ActionEvent e) -> {
            int inputMonth = defaultMonth;
            int inputDay = defaultDay;
            int inputYear = defaultYear;
            Window dialog = SwingUtilities.windowForComponent(okButton);
            dialog.dispose();
        });   
        cancelButton.addActionListener((ActionEvent e) -> {
            Window dialog = SwingUtilities.windowForComponent(cancelButton);
            dialog.dispose();
        });   
    }
    
    
    
    //Create and set up number formats. These objects also
    //parse numbers input by user.
    private void setUpFormats() {
        yearFormat = NumberFormat.getNumberInstance();
        yearFormat.setGroupingUsed(false);
        yearFormat.setMinimumIntegerDigits(4);
        yearFormat.setMaximumIntegerDigits(4);
 
        monthFormat = NumberFormat.getNumberInstance();
        monthFormat.setMinimumIntegerDigits(2);
        monthFormat.setMaximumIntegerDigits(2);
       
 
        dayFormat = NumberFormat.getNumberInstance();
        dayFormat.setMinimumIntegerDigits(2);
        dayFormat.setMaximumIntegerDigits(2);
    }
   
    
    
    @Override
    public void propertyChange(PropertyChangeEvent e){
        Object source = e.getSource();
        
        if(source==setYear){
            defaultYear=((Number)setYear.getValue()).intValue();          
        }else if(source==setMonth){
            defaultMonth=((Number)setMonth.getValue()).intValue();
        }else if(source==setDay){
            defaultDay=((Number)setDay.getValue()).intValue();
        }
        
    }
}
