import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
/**
 * Defines a GUI that displays details of a Gameplay object
 * and contains buttons enabling access to the required functionality.
 */
public class ITGUI extends JFrame implements ActionListener
{
    /** GUI JButtons */
    private JButton newGameButton, statisticsButton, submitButton, nextTurnButton;
    
    /** Label in front of the category drop down list*/
    private JLabel listLabel;
    
    /** Display card information */
    private JTextArea cardDisplay;
    
    /** Drop down list allowing the user to choose a category */
    private JComboBox categoriesList;
    
    /** List of available categories for the user to choose */
    private String[] categories = {"1", "2", "3", "4", "5"};
    
    /** GUI JPanels */
    private JPanel topPan, bottomPan;
    
    /** GamePlay object yet to be instantiated */
    private GamePlay game;
    
    private int roundCounter=0;
    
    public ITGUI() 
    {
        setTitle("Top Trumps Game");
        setSize(1400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        cardDisplay = new JTextArea();
        cardDisplay.setFont(new Font("Courier New", Font.PLAIN, 14));
        add(cardDisplay, BorderLayout.CENTER);
        cardDisplay.setEditable(false);
        
        layoutTop();
        layoutBottom();
        setVisible(true);
    }
    
    /**
     * Adds buttons and status label to top of GUI
     */
    public void layoutTop() 
    {
        //Instantiate panel for top of display (to show buttons)
        topPan = new JPanel();
        
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(this);
        topPan.add(newGameButton);
        
        statisticsButton = new JButton("Game Statistics");
        statisticsButton.addActionListener(this);
        topPan.add(statisticsButton);
        
        add(topPan, BorderLayout.NORTH);
        
        
    }
    
    /**
     * Adds label, drop down list and buttons to bottom of GUI
     */
    @SuppressWarnings("deprecation")
	public void layoutBottom() 
    {
        
        //Instantiate panel for bottom of display
        bottomPan = new JPanel();
        
        //Add label
        listLabel = new JLabel("Please select a category: ");
        bottomPan.add(listLabel);
        
        //Create a drop down list of categories that can be selected
        categoriesList = new JComboBox(categories);
        categoriesList.setSelectedIndex(0);
        categoriesList.enable();
        bottomPan.add(categoriesList);
        
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        bottomPan.add(submitButton);
        
        nextTurnButton = new JButton("Next Turn");
        nextTurnButton.addActionListener(this);
        bottomPan.add(nextTurnButton);
        add(bottomPan, BorderLayout.SOUTH);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().equals(newGameButton))
		{
			String filestring="";
			
			//prompt the user to select the deck they wish to use
			String[] DeckNames={ "Dinosaurs", "Dogs", "Simpsons" };
			String DeckName=(String) JOptionPane.showInputDialog(this, "Which deck would you like to use?",
			        "Deck Choice",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        DeckNames, 
			        DeckNames[0]);
			
			
			//get the number of computer players that the player would like to play with
			String[] playernumoptions={ "1", "2", "3", "4"};
			int playernumselection =Integer.parseInt((String) JOptionPane.showInputDialog(this, "How many computer players would you like to play with?",
			        "Number of Players",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        playernumoptions, 
			        playernumoptions[0]));
			
			//obtain the deck information from the file and store it in a string
			try 
			{
				//initialise the filereader object
				FileReader fin=new FileReader(DeckName + ".txt");//replace with the Decknames variable in future version
				
				//while loop to read the file character by character
				while(true)
				{
					int c=fin.read();
					if(c==-1)
					{
						break;
					}
					
					char cin=(char) c;
					filestring+=cin;
				}
				fin.close();
			}
			catch (IOException exception) 
			{
				exception.printStackTrace();
			}
			
			//split the file string up by newline characters to get each individual line as a separate entry in PrelimCardArray
			String[] PrelimCardArray=filestring.split("[\r\n]+");
			Card[] cardArray=new Card[40];
			
			//initialise the card array using the lines from PrelimCardArray
			for(int i=1; i<41; i++)
			{
				cardArray[i-1]=new Card(PrelimCardArray[i]);
				System.out.println(PrelimCardArray[i]);
				cardArray[i-1].setDescriptions(PrelimCardArray[0]);
			}
			System.out.println("-----------------------");
			
			//initialise game
			game=new GamePlay(playernumselection, cardArray);

			submitButton.setEnabled(false);
			
			//add preplay information to the display string
			String display="";
			display+=game.status();
			display+="\n\nYour current card:\n\n"+game.showUsersCard();
			
			//set the characteristic to the default value (maximum)
			game.setCharacteristicIndex(game.bestCharacteristicToPlay());
			
			//enable the nextTurnButton since after a first play it will have been disabled
			nextTurnButton.setEnabled(true);
			
			//if the user is playing then the submit button is enabled so they can select the characteristic and disable the nextTurnButton so they cannot progress the round before submitting a characteristic
			if(game.isItUsersPLay())
			{
				submitButton.setEnabled(true);
				nextTurnButton.setEnabled(false);
			}
			cardDisplay.setText(display);
		}
		else if(e.getSource().equals(submitButton))
		{
			//this section of code obtains the users choice of card category when they press the submit button
			int categoryindex=Integer.parseInt((String)categoriesList.getSelectedItem());
			game.setCharacteristicIndex(categoryindex-1);
			
			//re-enable the next turn button and disable the submit button
			nextTurnButton.setEnabled(true);
			submitButton.setEnabled(false);
		}
		else if(e.getSource().equals(nextTurnButton))
		{
			//advance the round counter by one, used to keep a track of which stage of a round we are in: pre-play or post-play
			roundCounter++;
			String display="";
			
			//if roundCounter is an even number then we display the pre-play information
			if((roundCounter%2)==0)
			{
				//add preplay information to the display string
				display+=game.status();
				display+="\n\nYour current card:\n\n"+game.showUsersCard();
				
				//set the characteristic to the default best value (maximum)
				game.setCharacteristicIndex(game.bestCharacteristicToPlay());
				
				//if the user is playing then the submit button is enabled so they can select the characteristic
				if(game.isItUsersPLay())
				{
					submitButton.setEnabled(true);
					nextTurnButton.setEnabled(false);
				}
				
			}
			else//if roundCounter is an odd number then we display the post-play information
			{
				//show the top cards prior to redistributing them
				display+=game.showRoundCards();
				
				//Show user and computer selection of chararacteristic - testing
				System.out.println("The characteristic played on this round was: "+game.getCharacteristicPlayed());
				System.out.println("---------------------------------------------------------");
				
				//check if there is an overall winner in this round
				if(game.isThereAWinner())
				{
					submitButton.setEnabled(false);
					nextTurnButton.setEnabled(false);
					display+="\nThis is the end of the game.";
					int reply = JOptionPane.showConfirmDialog(null, "This is the end of the game. Do you want to save the results?", "End of Game", JOptionPane.YES_NO_OPTION);
			        if (reply == JOptionPane.YES_OPTION) 
			        {
			        	JDBC.saveGameResults(JDBC.getAvailableID(), game.getHumanWinCount(), game.getComputerWinCount(), game.getRoundsCount(), game.getRoundsWonByPlayerByIndex(0), 
			            		game.getRoundsWonByPlayerByIndex(1), game.getRoundsWonByPlayerByIndex(2), game.getRoundsWonByPlayerByIndex(3), game.getRoundsWonByPlayerByIndex(4), game.getDrawsCount());
			        }
			        
			        return;
				}
				
				//play the round
				game.findWinnerOrDraw();
				game.transferTopCardsToCommunalPile();
				game.finishRound();
				
				//show the result of the round
				display+="\nThe characteristic played on this round was: "+game.getCharacteristicPlayed()+"\n";
				//add the winner of the round to the display
				if(game.getWinner()==0)
				{
					display+="The Winner of this round is: You!";
				}
				else if(game.getWinner()==-1)
				{
					display+="This round was a draw!";
				}
				else
				{
					display+="The winner of this round is: Player "+game.getWinner();
				}
			}
			//write to the display
			cardDisplay.setText(display);
			
		}
		else if(e.getSource().equals(statisticsButton))
		{
			StatisticsFrame statistics = new StatisticsFrame();
		}
	}
}