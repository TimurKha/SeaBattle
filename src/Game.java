/**
 *
 *
 *
 * @author	Timur Khairulin
 * @version
 */

public class Game {
    public int[][] arrPlayer;
    public int[][] arrComputer;
    public boolean compTurn; // Who going now (true - computer, false - player)
    // Signs of end game
    // (0-game going, 1-Player our win,2-Computer our win)
    public int endGame;


    public Game() {
        arrPlayer = new int[10][10];
        arrComputer = new int[10][10];
    }

    public void start() {
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                arrPlayer[i][j] = 0;
                arrComputer[i][j] = 0;
            }
        }
        endGame = 0;
        compTurn = false;
        arrangeShips(arrPlayer);
        arrangeShips(arrComputer);
    }

    //
    private void analysKilled(int[][]arr, int i, int j, int numDecks) {
        // number injures decks
        int numInjures = 0;
        // counting number of injures decks
        for (int k = i - (numDecks - 1); k <= i + (numDecks - 1); k++){
            for (int g = j - (numDecks - 1); g <= j + (numDecks - 1); g++){
                // If the wounded ship deck
                if (testArrPosition(k, g) && (arr[k][g] == numDecks + 7))
                    numInjures++;
            }
        }
        // If the number of wounded deck equal to the number of decks
        // The ship, it killed - add 7
        if (numInjures == numDecks) {
            for (int k = i - (numDecks - 1); k <= i + (numDecks - 1); k++){
                for (int g = j - (numDecks - 1); g <= j + (numDecks - 1); g++){
                    // If the wounded ship deck
                    if (testArrPosition(k, g) && (arr[k][g] == numDecks + 7)){
                        // mark the downed ship deck
                        arr[k][g] += 7;
                        // surround the deck of killed ship
                        circleShooted(arr, k, g);
                    }
                }
            }
        }
    }

    // check if the ship are killed
    private void testKilled(int[][] arr, int i, int j) {
        // If the single-deck
        if (arr[i][j] == 8){
            // make a shot
            arr[i][j] += 7;
            // surround the killed ship
            circleShooted(arr, i, j);
        }
        // two-deck ship
        else if (arr[i][j] == 9)
            analysKilled(arr, i, j, 2);
            // three-deck ship
        else if (arr[i][j] == 10)
            analysKilled(arr, i, j, 3);
            // four-deck ship
        else if (arr[i][j] == 11)
            analysKilled(arr, i, j, 4);
    }

    // Arrange ships
    private void arrangeShips(int[][] arr) {
        // Create one four-decker ships
        makeMulDeck(arr, 4);
        // Create two triple-decker ships
        for (int i = 1; i <= 2; i++)
            makeMulDeck(arr, 3);
        // Create three double-decker ships
        for (int i = 1; i <= 3; i++)
            makeMulDeck(arr, 2);
        // Create four single-decker ship
        makeOneDeck(arr);
    }

    // Player shot
    public void playerShot(int i, int j) {
        // when shot add 7
        arrComputer[i][j] += 7;
        // check if ship is killed
        testKilled(arrComputer, i, j);
        // check if the game end (all ships are killed)
        testEndGame();
        // If player miss - move turn computer
        if (arrComputer[i][j] < 8) {
            compTurn = true;
            // copmputer turn
            while (compTurn == true)
                compTurn = computerShot();
        }
    }

    // computer shot -
    // return true - if hit
    private boolean computerShot() {
        boolean res = false;
        boolean flag = false;
        _for1:
        // Runs through all of the playing field of the player
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // If find a injured deck
                if ((arrPlayer[i][j] >= 9) && (arrPlayer[i][j] <= 11)) {
                    flag = true;
                    // Check that you can make the shot
                    if (testArrPosition(i - 1, j) && (arrPlayer[i - 1][j] <= 4) && (arrPlayer[i - 1][j] != -2)) {
                        // make a shot
                        arrPlayer[i - 1][j] += 7;
                        // check that killed
                        testKilled(arrPlayer, i - 1, j);
                        // has been hit
                        if (arrPlayer[i - 1][j] >= 8)
                            res = true;
                        break _for1;
                    }
                    else if (testArrPosition(i + 1, j) && (arrPlayer[i + 1][j] <= 4) && (arrPlayer[i + 1][j] != -2)) {
                        arrPlayer[i + 1][j] += 7;
                        testKilled(arrPlayer, i + 1, j);
                        if (arrPlayer[i + 1][j] >= 8)
                            res = true;
                        break _for1;
                    }
                    if (testArrPosition(i, j - 1) && (arrPlayer[i][j - 1] <= 4) && (arrPlayer[i][j - 1] != -2)) {
                        arrPlayer[i][j - 1] += 7;
                        testKilled(arrPlayer, i, j - 1);
                        if (arrPlayer[i][j - 1] >= 8)
                            res = true;
                        break _for1;
                    }
                    else if (testArrPosition(i, j + 1) && (arrPlayer[i][j + 1] <= 4) && (arrPlayer[i][j + 1] != -2)) {
                        arrPlayer[i][j + 1] += 7;
                        testKilled(arrPlayer, i, j + 1);
                        if (arrPlayer[i][j + 1] >= 8)
                            res = true;
                        break _for1;
                    }
                }
            }
        }
        if (flag == false) {
            for (int l = 1; l <= 100; l++) {
                int i = (int) (Math.random() * 10);
                int j = (int) (Math.random() * 10);
                if ((arrPlayer[i][j] <= 4) && (arrPlayer[i][j] != -2)) {
                    arrPlayer[i][j] += 7;
                    testKilled(arrPlayer, i, j);
                    if (arrPlayer[i][j] >= 8)
                        res = true;
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                _for2: for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        // Check that can make shot
                        if ((arrPlayer[i][j] <= 4) && (arrPlayer[i][j] != -2)) {
                            // make a shot
                            arrPlayer[i][j] += 7;
                            testKilled(arrPlayer, i, j);
                            // if there has been hit
                            if (arrPlayer[i][j] >= 8)
                                res = true;
                            break _for2;
                        }
                    }
                }
            }
        }
        testEndGame();
        return res;
    }

    // Install one element surrounding the stricken ship
    private void setCircleHit(int[][] arr, int i, int j) {
        if (testArrPosition(i, j) == true) {
            if ((arr[i][j] == -1) || (arr[i][j] == 6))
                arr[i][j]--;
        }
    }

    // Surround a circle around the killed ship
    private void circleShooted(int[][] arr, int i, int j) {
        setCircleHit(arr, i - 1, j - 1);
        setCircleHit(arr, i - 1, j);
        setCircleHit(arr, i - 1, j + 1);
        setCircleHit(arr, i, j + 1);
        setCircleHit(arr, i + 1, j + 1);
        setCircleHit(arr, i + 1, j);
        setCircleHit(arr, i + 1, j - 1);
        setCircleHit(arr, i, j - 1);
    }

    // Checking if the game end
    private void testEndGame() {
        // test number = 15*4+16*2*3+17*3*2+18*4
        // when all decks are injured
        int testNumber = 330;
        int countComp = 0;
        int countPlay = 0;
        // counting all objects for two arrays
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // count the injure player decks
                if (arrPlayer[i][j] >= 15)
                    countPlay += arrPlayer[i][j];
                // count the injure computer decks
                if (arrComputer[i][j] >= 15)
                    countComp += arrComputer[i][j];
            }
        }
        if (countPlay == testNumber)
            endGame = 2; // Player are Winner
        else if (countComp == testNumber)
            endGame = 1; // Computer are Winner
    }

    // Checking if not going beyond the bounds of the array
    private boolean testArrPosition(int i, int j) {
        if (((i >= 0) && (i <= 9)) && ((j >= 0) && (j <= 9))) {
            return true;
        } else
            return false;
    }

    // Writing a value to array with array bounds checking
    private void setArrValue(int[][] arr, int i, int j, int val) {
        if (testArrPosition(i, j) == true) {
            arr[i][j] = val;
        }
    }

    // Install one element of the environment
    private void setCircle(int[][] arr, int i, int j, int val) {
        if (testArrPosition(i, j) && (arr[i][j] == 0))
            setArrValue(arr, i, j, val);
    }

    // Setting a cell around
    private void aroundBegin(int[][] arr, int i, int j, int val) {
        setCircle(arr, i - 1, j - 1, val);
        setCircle(arr, i - 1, j, val);
        setCircle(arr, i - 1, j + 1, val);
        setCircle(arr, i, j + 1, val);
        setCircle(arr, i + 1, j + 1, val);
        setCircle(arr, i + 1, j, val);
        setCircle(arr, i + 1, j - 1, val);
        setCircle(arr, i, j - 1, val);
    }

    // final environment
    private void finalCircle(int[][] arr) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (arr[i][j] == -2)
                    arr[i][j] = -1;
            }
        }
    }

    // Creating a four single-deck ships
    private void makeOneDeck(int[][] arr) {
        for (int k = 1; k <= 4; k++) {
            while (true) {
                int i = (int) (Math.random() * 10);
                int j = (int) (Math.random() * 10);
                if (arr[i][j] == 0) {
                    // Place a single-deck ship
                    arr[i][j] = 1;
                    aroundBegin(arr, i, j, -1);
                    break;
                }
            }
        }
    }

    // Checking the cell to be able to house the ship's deck
    private boolean testNewDeck(int[][] arr, int i, int j) {
        if (testArrPosition(i, j) == false)
            return false;
        // If this cell is 0 or 2, it we can use
        if ((arr[i][j] == 0) || (arr[i][j] == -2))
            return true;
        return false;
    }

    // Create a ship with several decks from 2 to 4
    private void makeMulDeck(int[][] arr, int numOfDecks) {
        while (true) {
            boolean flag = false;
            int i = 0, j = 0;
            // Select a random direction to construction of the ship
            i = (int) (Math.random() * 10);
            j = (int) (Math.random() * 10);
            // 0 - up, 1 - right, 2 - down, 3 - left
            int direction = (int) (Math.random() * 4);
            if (testNewDeck(arr, i, j) == true) {
                switch (direction){
                    case 0:
                        if (testNewDeck(arr, i - (numOfDecks - 1), j) == true)
                            flag = true;
                        break;
                    case 1:
                        if (testNewDeck(arr, i, j + (numOfDecks - 1)) == true)
                            flag = true;
                        break;
                    case 2:
                        if (testNewDeck(arr, i + (numOfDecks - 1), j) == true)
                            flag = true;
                        break;
                    case 3:
                        if (testNewDeck(arr, i, j - (numOfDecks- 1)) == true)
                            flag = true;
                        break;
                }
            }
            if (flag == true) {
                // Enter to cell number of decks
                arr[i][j] = numOfDecks;
                // around with -2
                aroundBegin(arr, i, j, -2);
                switch (direction){
                    case 0: // up
                        for (int k = numOfDecks - 1; k >= 1; k--) {
                            arr[i - k][j] = numOfDecks;
                            aroundBegin(arr, i - k, j, -2);
                        }
                        break;
                    case 1: // right
                        for (int k = numOfDecks - 1; k >= 1; k--) {
                            arr[i][j + k] = numOfDecks;
                            aroundBegin(arr, i, j + k, -2);
                        }
                        break;
                    case 2: // down
                        for (int k = numOfDecks - 1; k >= 1; k--) {
                            arr[i + k][j] = numOfDecks;
                            aroundBegin(arr, i + k, j, -2);
                        }
                        break;
                    case 3: // left
                        for (int k = numOfDecks - 1; k >= 1; k--) {
                            arr[i][j - k] = numOfDecks;
                            aroundBegin(arr, i, j - k, -2);
                        }
                        break;
                }
                break;
            }
        }
        finalCircle(arr);
    }
}