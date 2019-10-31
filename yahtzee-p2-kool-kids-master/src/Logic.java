public class Logic {

    int score = 0;
    int yahztees = 0;
    Gameplay GameplayObject = new Gameplay();
    private int[] dice;
    
    public Logic(int[] dice){
        this.dice = dice;
    }

    private int ones(){
        int score = 0;
        for (int i = 0; i < 5; i++){
            if (dice[i]  == 1){
                score = score+1;
            }
        }
        return score;
    }

    private int twos(){
        int score = 0;
        for (int i = 0; i < 5; i++){
            if (dice[i] == 2){
                score = score + 2;
            }
        }
        return score;
    }

    private int threes(){
        int score = 0;
        for (int i = 0; i < 5; i++){
            if (dice[i] == 3){
                score = score + 3;
            }
        }
        return score;
    }

    private int fours(){
        int score = 0;
        for (int i = 0; i < 5; i++){
            if (dice[i] == 4){
                score = score + 4;
            }
        }
        return score;
    }

    private int fives(){
        int score = 0;
        for (int i = 0; i < 5; i++){
            if (dice[i] == 5){
                score = score + 5;
            }
        }
        return score;
    }

    private int sixes(){
        int score = 0;
        for (int i = 0; i < 5; i++){
            if (dice[i] == 6){
                score = score + 6;
            }
        }
        return score;
    }

    private void bonus(){
        if (score>62)
            //add 25 to the actual score
            score = score + 25;
    }

    private int threeOfaKind(){
        int score = 0;
        /*int counter;
        for (int a = 6; a >= 0; a--) {
            counter = 0;
            for (int i = 0; i < 5; i++) {
                if (dice[i] == a){
                    score = score + dice[i];
                    counter++;
                }
            }
                if (counter == 3) {
                    return score;
                }
                else score = 0;
        }*/
        int [] numOfEachNum = new int[6];
        for (int i = 1; i < 7; i++) {
            for (int die : dice) {
                if (die == i){
                    ++numOfEachNum[i-1];
                }
            }
        }
        for (int i = 0; i < numOfEachNum.length; i++) {
            if (numOfEachNum[i] >= 3){
                score = (i+1)*numOfEachNum[i];
            }
        }
        return score;
    }

    private int fourOfaKind(){
        int score = 0;
        //int counter;
        int [] numOfEachNum = new int[6];
        for (int i = 1; i < 7; i++) {
            for (int die : dice
            ) {
                if (die == i){
                    ++numOfEachNum[i-1];
                }
            }
        }
        for (int i = 0; i < numOfEachNum.length; i++) {
            if (numOfEachNum[i] >= 4){
                score = (i+1)*numOfEachNum[i];
            }
        }
        return score;
    }

    private int fullHouse(){
        int score = 0;
        /*int gg = 0;
        int counter = 0;
        int c = 0;
        int skip;
        for (int a = 6; a >= 0; a--) {
            counter = 0;
            for (int i = 0; i < 5; i++) {
                if (dice[i] == a){
                    score = score + dice[i];
                    counter++;
                }
                if (counter==3) {
                    skip = a;
                    for (int b = 6; b >= 0; b--) {
                        if (b == skip)
                            b = skip + 1;
                        else{
                            c = 0;
                            for (int f = 0; f < 5; f++) {
                                if (dice[f] == b){
                                    gg = gg + dice[f];
                                    c++;
                                }
                                else gg = 0;
                            }
                        }
                    }
                }
                else score = 0;
            }
        }

        if(counter == 3 && c == 2){
            return score + gg;
        }*/

        int [] numOfEachNum = new int[6];

        boolean[] fullHouse = new boolean[]{
                false, false
        };

        for (int i = 1; i < 7; i++) {
            for (int die : dice
            ) {
                if (die == i){
                    ++numOfEachNum[i-1];
                }
            }
        }
        for (int num:numOfEachNum) {
            if(num == 2){
                fullHouse[0] = true;
            }
            if (num == 3){
                fullHouse[1] = true;
            }
        }
        if (fullHouse[0] && fullHouse[1]){
            score = 25;
        }
        System.out.println("Fullhouse 3 of kind: " + fullHouse[1]);
        System.out.println("Fullhouse 2 of kind: " + fullHouse[0]);
        return score;

    //return 0;
    }

    private int yahtzee(){
        score = 0;
        int [] numOfEachNum = new int[6];
        for (int i = 1; i < 7; i++) {
            for (int die : dice
            ) {
                if (die == i){
                    ++numOfEachNum[i-1];
                }
            }
        }
        for (int num:numOfEachNum) {
            if (num == 5){
                score = 50;
                break;
            }
        }
        return score;
    //return 0;
    }

    /*private int chance(){
        int score = 0;
            for (int i = 0; i < 6; i++){
                score = score + dice[i];
            }
    return score;
    }*/

    private int smStraight(){
        //int[] dice = dice;
        int lowestDie = 7;
        for (int die: dice) {
            if (die < lowestDie){
                lowestDie = die;
            }
        }
        int counter = 0;
        for (int die: dice) {
            if (die == (lowestDie+1)){
                ++ counter;
                ++ lowestDie;
            }
        }
        if (counter >= 3){
            return 30;
        }
        else{
            return 0;
        }
    }

    private int lgStraight(){
        //int[] dice = dice;
        int lowestDie = 7;
        for (int die: dice) {
            if (die < lowestDie){
                lowestDie = die;
            }
        }
        int counter = 0;
        for (int die: dice) {
            if (die == (lowestDie+1)){
                ++ counter;
                ++ lowestDie;
            }
        }
        if (counter == 4){
            return 40;
        }
        else{
            return 0;
        }
    }

    public int[] getPossibilities(boolean[] excludes){
        int[] possibleScores = new int[]{
                0,0,0,0,
                0,0,0,0,
                0,0,0,0
        };
        if (!excludes[0]){
            possibleScores[0] = ones();
        }
        if (!excludes[1]){
            possibleScores[1] = twos();
        }
        if (!excludes[2]){
            possibleScores[2] = threes();
        }
        if (!excludes[3]){
            possibleScores[3] = fours();
        }
        if (!excludes[4]){
            possibleScores[4] = fives();
        }
        if (!excludes[5]){
            possibleScores[5] = sixes();
        }
        if (!excludes[6]){
            possibleScores[6] = threeOfaKind();
        }
        if (!excludes[7]){
            possibleScores[7] = fourOfaKind();
        }
        if (!excludes[8]){
            possibleScores[8] = fullHouse();
        }
        if (!excludes[9]){
            possibleScores[9] = smStraight();
        }
        if (!excludes[10]){
            possibleScores[10] = lgStraight();
        }
        possibleScores[11] = yahtzee();
        for (int num: possibleScores
             ) {
            System.out.println("Score: " + num);
        }
        for (int num: dice) {
            System.out.println("Die: " + num);
        }
        return possibleScores;
    }

  public void yahtzeeCounter(){
      yahztees++;
  }
}
