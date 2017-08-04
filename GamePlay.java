import java.util.Random;

/**
 * Defines the class of methods and game status variables necessary to carry out a full game and recording the results of the game
 * @author Sean
 *
 */
public class GamePlay {

	//instance variable declaration
	//stores the shuffled deck
	private Card[] deck=new Card[40];
	//stores the decks of all the players
	private Card[][] playerDecks;
	//stores the indices of the top card for every player
	private int[] playerTopCards;
	//stores the number of players in the game
	private int playerNum;
	//stores the index of the currentplayer
	private int currentPlayerIndex;
	//stores the cards of the communal pile
	private Card[] communalPile=new Card[40];
	//keeps a count of the number of communal cards and acts as a kind of stack pointer
	private int communalPileCount=0;
	//stores the current choice of characteristic
	private int characteristicIndex;
	//default user index is zero
	private int USERINDEX=0;
	//stores the playing status of each player
	private boolean[] playersStillPlaying;
	//stores the winner of the current winner
	private int winnerIndex;
	
	private int gamePlayID = JDBC.getAvailableID();
	
	//Initialise all counters to 0 by default
	private int humanWinCount = 0; //add 1 to this when human collects all the cards
	private int computerWinCount = 0; //add 1 to this as soon as the human loses all his cards
	private int roundsCount = 0; //add 1 after each round
	private int[] roundsWonByPlayer=new int[5]; //add 1 if human wins the round
	private int drawsCount = 0; //add 1 if a draw happens

	/**
	 * Constructor
	 * @param playernum
	 * @param deckinput
	 */
	public GamePlay(int playernum, Card[] deckinput)
	{
		//set the values of the instance variables using the constructor parameters
		this.playerNum=playernum+1;
		this.deck=shuffle(deckinput);
		
		//pick a random player to begin
		Random random=new Random();
		currentPlayerIndex=random.nextInt(this.playerNum);
		
		//initialise the communal pile
		 communalPile=new Card[40];
		
		//initialiase the decks and deal the carsds among the players
		setUpPlayers();
		dealCards();
	}
	
	/**
	 * Initialises the player decks and other variables
	 */
	public void setUpPlayers()
	{
		//initialise the player decks array and the array storing the top card index
		playerDecks=new Card[this.playerNum][40];
		playerTopCards=new int[this.playerNum];
		playersStillPlaying=new boolean[playerNum];
		
		for(int j=0; j<playerNum; j++)
		{
			playersStillPlaying[j]=true;
		}
		
		for(int i=0; i<this.playerNum; i++)
		{
			playerTopCards[i]=0;
		}
		
		communalPileCount=0;
		
	}
	
	/**
	 * Deals the cards among the players cyclically
	 */
	public void dealCards()
	{
		for(int i=0; i<40; i++)
		{
			playerDecks[i%(playerNum)][playerTopCards[i%playerNum]]=deck[i];//this line assigns the cards to each player deck cyclically
			playerTopCards[i%playerNum]++;//this line keeps track of the top card for each player
		}
		//Showing the deck of user and computers players after the allocation - testing
		for(int i=0; i<playerNum; i++){
			for(int j=0;j<40;j++){
				if(i==USERINDEX){
					if(playerDecks[i][j]!=null){
						System.out.println("User deck after the allocation of cards: "+playerDecks[i][j].getType());
						}
				}else{
					if(playerDecks[i][j]!=null){
						System.out.println("Computer deck after the allocation of cards: "+playerDecks[i][j].getType());
						}
				}
				
			}
		}
		System.out.println("---------");
	}
	
	/**
	 * Sets the current characteristic
	 * @param index
	 */
	public void setCharacteristicIndex(int index)
	{
		this.characteristicIndex=index;
	}
	
	/**
	 * Sets the current player index
	 * @param index
	 */
	public void setCurrentPlayer(int index)
	{
		this.currentPlayerIndex=index;
	}
	
	/**
	 * Returns the number of players playing in the current game
	 * @return
	 */
	public int getPlayerNum()
	{
		return playerNum;
	}
	/**
	 * Returns the index of the current player
	 * @return
	 */
	public int getCurrentPlayer()
	{
		return currentPlayerIndex;
	}
	/**
	 * Returns the index of the current round winner
	 * @return
	 */
	public int getWinner()
	{
		return winnerIndex;
	}
	/**
	 * Returns 1 if the human player won the current game
	 * @return
	 */
	public int getHumanWinCount()
	{
		return humanWinCount;
	}
	/**
	 * Returns 1 if a computer player won the current game
	 * @return
	 */
	public int getComputerWinCount()
	{
		return computerWinCount;
	}
	/**
	 * Returns the number of rounds played
	 * @return
	 */
	public int getRoundsCount()
	{
		return roundsCount;
	}
	/**
	 * Returns the number of rounds won by a particular player labelled by the index input
	 * @param index
	 * @return
	 */
	public int getRoundsWonByPlayerByIndex(int index)
	{
		return roundsWonByPlayer[index];
	}
	/**
	 * Returns the number of draws
	 * @return
	 */
	public int getDrawsCount()
	{
		return drawsCount;
	}
	/**
	 * Returns the characteristic played in this round
	 * @return
	 */
	public String getCharacteristicPlayed()
	{
		return playerDecks[currentPlayerIndex][playerTopCards[currentPlayerIndex]-1].getDescriptionCharByIndex(characteristicIndex);
	}
	
	/**
	 * Returns the best characteristic to play for the computer player
	 * @return
	 */
	public int bestCharacteristicToPlay()
	{
		int max=0;
		int maxindex=0;
		for(int i=0; i<5; i++)
		{
			if(max<playerDecks[currentPlayerIndex][playerTopCards[currentPlayerIndex]-1].getCharacteristicByIndex(i))
			{
				max=playerDecks[currentPlayerIndex][playerTopCards[currentPlayerIndex]-1].getCharacteristicByIndex(i);
				maxindex=i;
			}
		}
		return maxindex;
	}
	
	/**
	 * Returns the formatted String displaying the top cards of all players
	 * @return
	 */
	public String showRoundCards()
	{
		
		String displayString="";
		for(int j=0; j<playerNum; j++)
		{
			if(j==USERINDEX)
			{
				String temp="You:";
				displayString+=String.format("%-32s", temp);
			}
			else
			{
				String temp="player "+j+":";
				displayString+=String.format("%-32s", temp);
			}
		}
		displayString+="\n";
		for(int i=0; i<6; i++)
		{
			for(int j=0; j<playerNum; j++)
			{
				if(playersStillPlaying[j])
				{
					//label the each card by the player who played it
					if(i==0)
					{
						displayString+=String.format("Type: %-26s", playerDecks[j][playerTopCards[j]-1].getType());//shows the name of the card
					}
					else
					{
						displayString+=String.format("%d. %-12s: %-12d\t", i,playerDecks[j][playerTopCards[j]-1].getDescriptionCharByIndex(i-1), playerDecks[j][playerTopCards[j]-1].getCharacteristicByIndex(i-1));
					}
				}
				else
				{
					displayString+=String.format("%-32s", "");
				}
			}
			displayString+="\n";
		}
		//display top card of user player and computer players - testing
		System.out.println(displayString);
		System.out.println("---------------------------------------------------------------");
		
		return displayString;
	}
	/**
	 * Returns the formatted String indicating the status of the game
	 * @return
	 */
	public String status()
	{
		String result="";
		
		
		if(currentPlayerIndex==USERINDEX)
		{
			result+="Current Player: You\nCard count:\n";
		}
		else
		{
			result+="Current Player: "+currentPlayerIndex+"\nCard count:\n";
		}
		
		for(int i=0; i<playerNum; i++)
		{
			if(i==USERINDEX)
			{
				result+="You: "+playerTopCards[i]+"\t";
			}
			else
			{
				result+="Player "+i+": "+playerTopCards[i]+"\t";
			}
		}
		result+="\nNumber of cards in the communal pile: "+communalPileCount;
		return result;
	}
	
	/**
	 * Finds the winner of the current round based on the chosen characteristic and the current top cards
	 * Sets the winnerIndex variable accordingly or if there was a draw, sets the winnerIndex to the flag -1, indicating the occurrence of a draw
	 */
	public void findWinnerOrDraw()
	{	
		int max=0;
		int winnerIndex=-1;
		
		//find the index of the winner
		for(int j=0; j<playerNum; j++)
		{
			if(playersStillPlaying[j])
			{
				if((playerDecks[j][playerTopCards[j]-1].getCharacteristicByIndex(characteristicIndex)>max))
				{
					max=playerDecks[j][playerTopCards[j]-1].getCharacteristicByIndex(characteristicIndex);//update the max value accordingly
					winnerIndex=j;
				}
			}
		}
	
		//check if there is a draw
		int drawCount=0;
		
		for(int k=0; k<playerNum; k++)
		{
			if(playersStillPlaying[k])
			{
				if((max==playerDecks[k][playerTopCards[k]-1].getCharacteristicByIndex(characteristicIndex)))
				{
					drawCount++;//increment the drawcount variable for every time the maximum value turns up in the current player's characteristic values if there is more than one incidence of the value then we have a draw
				}
			}
		}
	
		//if there is a draw reset winnerIndex to invalid index -1
		if(drawCount!=1)
		{
			winnerIndex=-1;
		}
		
		//set the instance variable of the winner
		this.winnerIndex=winnerIndex;
	}
	/**
	 * This method transfers all the top cards from the player decks to the communal pile.
	 * This action is performed every round, but the cards are only retained in the communal pile if there was a draw
	 */
	public void transferTopCardsToCommunalPile()
	{
		//this section of code transfers cards from the players decks to the communal pile
		for(int i=0; i<playerNum; i++)
		{
			if(playersStillPlaying[i])
			{
				//add top cards to the communal pile
				communalPile[communalPileCount]=playerDecks[i][playerTopCards[i]-1];
				communalPileCount++;
				
				//remove top cards and update the index of each player's top card
				playerDecks[i][playerTopCards[i]-1]=null;
				playerTopCards[i]--;
			}
		}
		//show the contents of the communal pile once cards are added - testing
		for(int i=0; i<communalPileCount;i++)
		{
			System.out.println("The communal pile is added with the card: "+ communalPile[i].getType());
		}
		System.out.println("--------------");

	}
	/**
	 * This method determines if there is an overall winner and returns a boolean indicating whether or not this has happened
	 * @return
	 */
	public boolean isThereAWinner()
	{
		//check that there is a winner
		int CheckIfThereisAWinner=0;
		
		for(int n=0; n<playerNum; n++)
		{
			if(playersStillPlaying[n])
			{
				CheckIfThereisAWinner++;
			}
		}
		if(CheckIfThereisAWinner==1)
		{
			if(currentPlayerIndex==0)
			{
				humanWinCount++;
			}
			else
			{
				computerWinCount++;
			}
			//print the winner - testing
			System.out.println("The winner is: "+currentPlayerIndex);
			System.out.println("------------------------------------------------");
			return true;
		}
		else
		{
			return false;
		}
		
	}
	/**
	 * This method transfers the cards from the communal pile to the winner's deck if there was a winner. If there was a draw the cards are retained in the communal pile.
	 * The method also determines if a player needs to be kicked out of the game and updates the status accordingly and also advances the currentPlayerIndex appropriately
	 */
	public void finishRound()
	{
		
		//if there was a winner then transfer the cards from the communal pile into their deck
		if(winnerIndex!=-1)
		{
			Card[] tempCardArray=new Card[40];
			
			//put the winning player's deck into the temporary card array shifted according to how many cards are in the communal pile
			for(int i=0; i<playerTopCards[winnerIndex]; i++)
			{
				tempCardArray[i+communalPileCount]=playerDecks[winnerIndex][i];
			}
			//put the communal pile cards in the bottom of the temporary card array
			for(int j=0; j<communalPileCount; j++)
			{
				tempCardArray[j]=communalPile[j];
				
			}
			
			//print the contents of communal pile once cars has been removed - testing
			for(int l=0; l<communalPileCount;l++){
				System.out.println("The communal pile has removed the card: "+communalPile[l].getType());
			}
			System.out.println("-----------------------------");
			
			
			//set the new winner deck
			for(int k=0; k<40; k++)
			{
				playerDecks[winnerIndex][k]=tempCardArray[k];
			}
			
			for(int i=0; i<playerNum; i++)
			{
				System.out.println("Player: "+i);
				for(int j=0; j<40; j++)
				{
					if(playerDecks[i][j]!=null)
					{
						//print the cards of of each deck in a round 
						System.out.println(playerDecks[i][j].getType());
						
					}
				}
				System.out.println("-------------------------------------");
			}
			//update the top card index
			playerTopCards[winnerIndex]+=communalPileCount;
			//reset the communal pile card count
			communalPileCount=0;

			//this section of code checks which players are still playing
			for(int k=0; k<playerNum; k++)
			{
				if(playerTopCards[k]==0)
				{
					playersStillPlaying[k]=false;
				}
			}
		}
		//set the index of the current player to the winners index or increment if there was a draw
		if(winnerIndex!=-1)
		{
			currentPlayerIndex=winnerIndex;
			roundsWonByPlayer[winnerIndex]++;
			System.out.println("Rounds won by player "+winnerIndex+": "+roundsWonByPlayer[winnerIndex]);
		}
		else
		{
			drawsCount++;
			System.out.println("DrawsCount: "+drawsCount);
			//increment the currentplayerindex until it lands on a player that is still playing
			do
			{
				currentPlayerIndex++;
				currentPlayerIndex%=playerNum;//keeps the index cycling through values from 0 to playerNum-1
			}
			while(!playersStillPlaying[currentPlayerIndex]);
			
		}
		roundsCount++;
		System.out.println("RoundsCount: "+roundsCount);
	}

	
	/**
	 * Returns a boolean flag indicating whether or not the user is playing currently
	 * @return
	 */
	public boolean isItUsersPLay()
	{
		return currentPlayerIndex==USERINDEX;
	}
	/**
	 * Returns a formatted String displaying the user's top card
	 * @return
	 */
	public String showUsersCard()
	{
		if(playersStillPlaying[USERINDEX])
		{
			String displayString="";
				displayString+="Type: "+playerDecks[USERINDEX][playerTopCards[USERINDEX]-1].getType()+"\n";
				for(int i=0; i<5; i++)
				{
					displayString+=String.format("%d. %-12s: %d\n", i+1,playerDecks[USERINDEX][playerTopCards[USERINDEX]-1].getDescriptionCharByIndex(i), playerDecks[USERINDEX][playerTopCards[USERINDEX]-1].getCharacteristicByIndex(i));
				}
			return displayString;
		}
		return "You are out of luck buddy";
	}
	
	/**
	 * Takes an ordered deck and returns a shuffled deck
	 * @param deckin
	 * @return
	 */
	public Card[] shuffle(Card[] deckin)
	{
		Random random=new Random();
		int[] orderedindexarray=new int[40];
		int[] randomarrayindices=new int[40];
		boolean[] cardtaken=new boolean[40];
		
		//initialise the ordered index array
		for(int i=0; i<40; i++)
		{
			orderedindexarray[i]=i;
		}
		//initialise the cardtaken array
		for(int i=0; i<40; i++)
		{
			cardtaken[i]=false;
		}
		

		int i=0;
		int currentrandom;//variable for the current random index accessing the ordered index array
		while(i<40)
		{
			currentrandom=random.nextInt(40);//generate a random index
			
			if(!cardtaken[currentrandom])//check if the card at the randomly generated index has already been taken
			{
				//if the card at this random index has not been taken we update the random index in randomindices and move on to the next entry in randomindices
				//we also update this entry of the card taken array
				randomarrayindices[i]=orderedindexarray[currentrandom];
				cardtaken[currentrandom]=true;
				i++;
			}
		}
		Card[] deck=new Card[40];
		
		//use the randomly ordered indices to create a shuffled version of the deck
		//print the  deck after it has been shuffled for testing
		for(int j=0; j<40; j++)
		{
			deck[j]=deckin[randomarrayindices[j]];
			System.out.println(deck[j].getType());
		}
		System.out.println("--------");
		return deck;

	}
	

	
}