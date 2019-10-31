import java.util.Random;

public class Gameplay {

    private Random rand = new Random();
    private int[] dice = new int[] {6,6,6,6,6};
    private boolean[] reroll = new boolean[5];
    private int score = 0;

    
    
    private boolean[] excludes;

    public Gameplay(){
        boolean aces = false;
        boolean twos = false;
        boolean threes = false;
        boolean fours = false;
        boolean fives = false;
        boolean sixes = false;
        boolean threeOfKind = false;
        boolean fourOfKind = false;
        boolean fullHouse = false;
        boolean smStraight = false;
        boolean lgStraight = false;
        boolean yahtzee = false;
        //These variables just meant to show what parts of the array are what, didnt feel like learning enum stuff
        excludes = new boolean[] {
                aces,twos,threes,fours,fives,sixes,threeOfKind,
                fourOfKind,fullHouse,smStraight,lgStraight,yahtzee
        };
    }
    
    public void setUp() {
        reroll = new boolean[5];
        for (int counter = 0; counter < 5; counter++) {
            dice[counter] = rand.nextInt(6) + 1;
        }
        //reroller();
    }

    public void setReroll(boolean[] reroll) {
        this.reroll = reroll;
    }


    public void reroller() {



        for (int i = 0; i < 5; i++){
            if(!reroll[i])
                dice[i] = rand.nextInt(6) + 1;
            System.out.println("Dice: "+ i + "Is " + reroll[i]);
                //dice[i] = 5;
        }
        setter();
    }

    public boolean[] getExcludes() {
        return excludes;
    }

    public int[] setter() {
        return dice;
    }

    public int[] getPossibilities(){
        String[] textOptions = new String[]{
                "Aces","Twos","Threes","Fours","Fives","Sixes","Three Of A Kind",
                "Four Of A Kind","Full House","Small Straight","Large Straight","Yahtzee"
        };
        for (int i = 0; i < excludes.length; i++) {
            if (excludes[i]){
                System.out.println(textOptions[i]);
            }
        }
        Logic logic = new Logic(dice);
        for (int i = 0; i < dice.length; i++) {
            System.out.println("Die "+ i + ": "+ dice[i]);
        }
        int[] possibilities = logic.getPossibilities(excludes);
        return possibilities;
    }


    public boolean endGame(){

        boolean end  = true;

        for (boolean bool:
             excludes) {
            if(!bool){
                end = false;
                break;
            }
        }

        return end;

    }

    public void excludeOption(int option){
        excludes[option] = true;
    }

}