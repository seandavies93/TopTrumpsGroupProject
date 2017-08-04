/**
 * The class Card represents the Card object, it is suppossed to 
 * be universal for every topic like Simpsons, Dinosaurs e.t.c.
 */
public class Card
{
    //type - e.g. TRex
    private String type = " ";
    //char1Desc - description of characteristic 1, e.g. height 
    private String[] charDesc=new String[5];
    //characteristic1 - value for characteristic 1, e.g. height=6
    private int[] characteristic = new int[5];

    
    //default constructor
    public Card(){
        
    }
    /**constructor
    *takes as parameter a String in the format TRex 6 6 12 9 9
    *and instantiates a Card object*/
    public Card(String line){
        String[] parts = line.split(" ");
        type = parts[0];
        
        for(int i=0; i<5; i++)
        {
        	characteristic[i]=Integer.parseInt(parts[i+1]);
        } 
        
    }
    
    /**method setDesciption
    *parameter String, takes a line as description height weight length ferocity intelligence
    *and inputs the description for each characteristic. 
    *It ignores the first word of the line:description (not needed) */
    public void setDescriptions(String line){
        String[] parts = line.split(" ");
        for(int i=0; i<5; i++)
        {
        	charDesc[i]=parts[i+1];
        }
    }
    
    /**
     * Sets the type String
     * @param type
     */
    public void setType(String type){
        this.type = type;
    }
    
    /**
     * Takes a value and an index and sets the value of the entry at that index
     * @param index
     * @param value
     */
    public void setCharacteristicByIndex(int index, int value){
        characteristic[index] = value;
    }

    /**
     * 
     * @param index
     * @return
     */
    public int getCharacteristicByIndex(int index){
            return characteristic[index];
    }

    /**
     * Returns the type of the card
     * @return
     */
    public String getType(){
        return type;
    }
    
    /**
     * Returns the name of the characteristic at the chosen index
     * @param index
     * @return
     */
    public String getDescriptionCharByIndex(int index){
        return charDesc[index];
    }
}
    