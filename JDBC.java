import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC 
{

	/** Connection details */
    private static String dbname = "postgres"; //m_16_2273269b
    private static String username = "postgres"; //m_16_2273269b
    private static String password = "postgres"; //2273269b
    
    /** JDBC variables */
    static Connection connection = null;
    static Statement stmt = null;
    static ResultSet rs = null;
    static String query = "";
    
    
    /** Connect to the database */
    public static void makeConnection()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" +
            dbname,username, password);
            //jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/
        }
        catch (SQLException e)
        {
            System.err.println("Connection Failed!");
            e.printStackTrace();
        return;
        }
    }
    
    
    /** Close the database connection */
    public static void closeConnection()
    {
        try
        {
            connection.close();
            System.out.println("Connection closed");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Connection could not be closed – SQL exception");
        }
    }
    
    /** Save game results to database */
    public static void saveGameResults(int gameID, int humanWinner, int computerWinner, int rounds, int roundsWonByHuman, 
    		int roundsWonByComputer1, int roundsWonByComputer2, int roundsWonByComputer3, int roundsWonByComputer4, int draws)
	{
		makeConnection();
		query = "INSERT INTO GameStatistics (gameID, humanWinner, computerWinner, rounds, roundsWonByHuman, roundsWonByComputer1, "
				+ "roundsWonByComputer2, roundsWonByComputer3, roundsWonByComputer4, draws) VALUES "
				+ "(" + gameID + ", " + humanWinner + ", " + computerWinner + ", " + rounds + ", " + roundsWonByHuman + 
				", " + roundsWonByComputer1 + ", " + roundsWonByComputer2 + ", " + roundsWonByComputer3 + ", " + roundsWonByComputer4 + ", " + draws + ");";
		
		try 
		{
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
			System.out.println("Results saved");
		}
		
		catch (SQLException e )
		{
			e.printStackTrace();
			System.out.println("Error executing query " + query);
		}
		
		closeConnection();
	}
    
    /** Retrieve statistics from the database and add them to the text area */
    public static String getStatistics()
    {
    	String statisticsString="";
    	
    	makeConnection();
		query = "SELECT COUNT(gameID) AS numGames, SUM(humanWinner) AS humanWins, SUM(computerWinner) AS computerWins, "
				+ "AVG(draws) AS averageDraws, MAX(rounds) AS maxRounds FROM GameStatistics";
    	
		try 
		{
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
		
			while (rs.next())
			{
			 
				int numGames = rs.getInt("numGames");
				int humanWins = rs.getInt("humanWins");
				int computerWins = rs.getInt("computerWins");
				int averageDraws = rs.getInt("averageDraws");
				int maxRounds = rs.getInt("maxRounds");
				
				statisticsString+="Number of games played: " + numGames +"\n";
				statisticsString+="Human has won " + humanWins +" games \n";
				statisticsString+="Computer has won " + computerWins +" games \n";
				statisticsString+="Average number of draws: " + averageDraws +"\n";
				statisticsString+="Largest number of rounds played in a single game: " + maxRounds +"\n";
			}
		}
		
		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("Error executing query " + query);
		}
		
		closeConnection();
		
		return statisticsString;
	}
        
    /** Retrieve latest (highest) gameID and return the next available ID */
    public static int getAvailableID()
    {
    	int availableID = 0;
    	
    	makeConnection();
		query = "SELECT MAX(gameID) AS highestID FROM GameStatistics";
    	
		try 
		{
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
		
			while (rs.next())
			{
			 
				int highestID = rs.getInt("highestID");
				
				availableID = highestID + 1;
			}
		}
		
		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("Error executing query " + query);
		}
		
		closeConnection();
		
		return availableID;
	}
}
