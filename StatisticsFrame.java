import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

	/**
	 * Class to define window in which game statistics are displayed.
	 */
	public class StatisticsFrame extends JFrame implements ActionListener
	{
	    /** Display of statistics */
	    private JTextArea statisticsDisplay;
	    
	    /** Variable to hold statistics retrieved from the database */
	    private String retrievedStatistics;
	    
	    /** Button to save statistics */
	    private JButton saveStatistics;
	    
	    /** JPanels for holding elements */
	    private JPanel buttonPan, displayPan;
	    
	    /** Output text file */
		private final String statsOutFile = "Statistics.txt";
	    
	    public StatisticsFrame()
	    {
	        
	        //Set window parameters
	        setTitle("Statistics");
	        setSize(1000, 250);
	        
	        //Panels for holding elements
	        buttonPan = new JPanel();
	        displayPan = new JPanel();
	        
	        saveStatistics = new JButton("Save to File");
	        saveStatistics.addActionListener(this);
	        buttonPan.add(saveStatistics);
	        
	        //Create JTextArea
	        statisticsDisplay = new JTextArea();
	        statisticsDisplay.setFont(new Font("Courier New", Font.PLAIN, 12));
	        statisticsDisplay.setEditable(false);
	        displayPan.add(statisticsDisplay);
	        
	        //Add contents to JTextArea
	        retrievedStatistics = JDBC.getStatistics();
	        statisticsDisplay.append(retrievedStatistics);
	        
	        //Add panels to window
	        add(buttonPan, "North");
	        add(displayPan, "Center");
	     	        
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        setVisible(true);
	        
	    }
	    
	    public void actionPerformed(ActionEvent e)
	    {
	    	if(e.getSource() == saveStatistics)
			{
	    		PrintWriter outputFile = null;
	    		
	    		try 
	    		{
	    			outputFile = new PrintWriter(statsOutFile);
	    		}
	    		catch (FileNotFoundException x)
	    		{
	    			System.out.println("Could not write to file."); 
	    		}
	    		
	    		outputFile.print(retrievedStatistics);
	    		outputFile.close();
			}
	    }
	    
	}
	
