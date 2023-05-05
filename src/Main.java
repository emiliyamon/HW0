import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;

    public static void battleshipGame() {

        String input, regex; // used for converting str to int array
        int i, j; // index

        System.out.println("Enter the board size");
        // correct format for input: "nXm" n rows and m cols
        input = scanner.nextLine();

        regex = "X"; // regex relevant for this input line
        // send string to function to get int array
        int[] boardSizeIntArray = stringToIntArray(input, regex);

        int ROWS = boardSizeIntArray[0];
        int COLS = boardSizeIntArray[1];

        // building boards using function
        char[][] playerGameBoard = new char[ROWS][COLS];
        buildGameBoard(playerGameBoard);
        char[][] playerGuessBoard = new char[ROWS][COLS];
        buildGameBoard(playerGuessBoard);
        char[][] computerGameBoard = new char[ROWS][COLS];
        buildGameBoard(computerGameBoard);
        char[][] computerGuessBoard = new char[ROWS][COLS];
        buildGameBoard(computerGuessBoard);

        // for keeping track of boats
        int[][] playerBoatBoard = new int[ROWS][COLS];
        buildBoatBoard(playerBoatBoard);
        int[][] computerBoatBoard = new int[ROWS][COLS];
        buildBoatBoard(computerBoatBoard);

        System.out.println("Enter battleship sizes");
        String battleshipSizes = scanner.nextLine();
        // "n1Xs1...nkXsk"

        String[] battleshipSizesArray = battleshipSizes.split(" ");
        // array of ["n1Xs1",...,"nkXsk"]

        String[][] battleshipStringArray = new String[battleshipSizesArray.length][2];
        for (i = 0; i < battleshipStringArray.length; i++) {
            battleshipStringArray[i] = battleshipSizesArray[i].split("X");
        }
        // string matrix of [["n1","s1"],...,["nk","sk"]]

        int[][] battleshipArray = new int[battleshipStringArray.length][battleshipStringArray[0].length];
        for (i = 0; i < battleshipStringArray.length; i++) {
            for (j = 0; j < battleshipStringArray[0].length; j++) {
                battleshipArray[i][j] = Integer.parseInt(battleshipStringArray[i][j]);
            }
        }
        // change the matrix from string matrix to integer matrix, called battleshipArray

        // for keeping track of boats:
        int numOfBoats = 0;
        for (i = 0; i < battleshipArray.length; i++) {
            numOfBoats = numOfBoats + battleshipArray[i][0];
        }

        int[] playerBoatArray = new int[numOfBoats];
        int[] computerBoatArray = new int[numOfBoats];
        int count;

        System.out.println("Your current game board:");
        printGameBoard(playerGameBoard);

        int X, Y, ORIENTATION, S, AMOUNT;
        int[] locationOrientationArray = new int[3]; //למה זה מושחר? אולי כי בשורה 94 אנחנו מקבלים מערך של אינטים מהפונק' המרה שלנו, אז לא צריך להגדיר פה מערך אינטים חדש?

        // filling player game board
        for (i = 0; i < battleshipArray.length; i++) {

            S = battleshipArray[i][1];
            AMOUNT = battleshipArray[i][0];
            count = 1;

            System.out.println("Enter location and orientation for battleship of size " + S);
            input = scanner.nextLine();
            // format "x, y, orientation"
            regex = ", ";
            // orientation 0 for horizontal, 1 for vertical
            // horizontal size s from (x,y) to (x, y+s-1)
            // vertical size s from (x,y) to (x+s-1, y)
            // send string to function to get int array
            locationOrientationArray = stringToIntArray(input, regex);


            while (AMOUNT > 0) {
                X = locationOrientationArray[0];
                Y = locationOrientationArray[1];
                ORIENTATION = locationOrientationArray[2];
                boolean overlapTestResult = false;
                boolean adjacentTestResult = false;

                if (ORIENTATION != 0 && ORIENTATION != 1) {
                    System.out.println("Illegal orientation, try again!");
                    input = scanner.nextLine();
                    locationOrientationArray = stringToIntArray(input, regex);
                    continue;
                } else if (X < 0 || Y < 0) {
                    System.out.println("Illegal tile, try again!");
                    input = scanner.nextLine();
                    locationOrientationArray = stringToIntArray(input, regex);
                    continue;
                } else if (X >= ROWS || Y >= COLS) { //מאחר ומדובר במשבצת ההתחלה של הספינה, זה כן יהיה תקין במקרה שאורך הספינה הוא 1
                    System.out.println("Illegal tile, try again!");
                    input = scanner.nextLine();
                    locationOrientationArray = stringToIntArray(input, regex);
                    continue;
                } else if (ORIENTATION == 0 && (Y + S - 1) >= COLS) {
                    System.out.println("Battleship exceeds the boundaries of the board, try again!");
                    input = scanner.nextLine();
                    locationOrientationArray = stringToIntArray(input, regex);
                    continue;
                } else if (ORIENTATION == 1 && (X + S - 1) >= ROWS) {
                    System.out.println("Battleship exceeds the boundaries of the board, try again!");
                    input = scanner.nextLine();
                    locationOrientationArray = stringToIntArray(input, regex);
                    continue;
                }

                overlapTestResult = overlapTest(X, Y, ORIENTATION, S, playerGameBoard); //checking if there's already ship there
                adjacentTestResult = adjacentTest(X, Y, ORIENTATION, S, playerGameBoard); //checking if all around is clear

                if (overlapTestResult) {
                    System.out.println("Battleship overlaps another battleship, try again!");
                    input = scanner.nextLine();
                    locationOrientationArray = stringToIntArray(input, regex);
                    continue;
                } else if (adjacentTestResult) {
                    System.out.println("Adjacent battleship detected, try again!");
                    input = scanner.nextLine();
                    locationOrientationArray = stringToIntArray(input, regex);
                    continue;
                }

                fillGameBoard(playerGameBoard, X, Y, ORIENTATION, S);
                fillBoatBoard(playerBoatBoard, X, Y, ORIENTATION, S, playerBoatArray, count);

                System.out.println("Your current game board:");
                printGameBoard(playerGameBoard);

                AMOUNT--;
                count++;
            }
        }


        // filling computer game board
        for (i = 0; i < battleshipArray.length; i++) {
            X = rnd.nextInt(ROWS);
            Y = rnd.nextInt(COLS);
            ORIENTATION = rnd.nextInt(2);
            S = battleshipArray[i][1];
            AMOUNT = battleshipArray[i][0];
            count = 1;


            // orientation 0 for horizontal, 1 for vertical
            // horizontal size s from (x,y) to (x, y+s-1)
            // vertical size s from (x,y) to (x+s-1, y)

            while (AMOUNT > 0) {
                boolean overlapTestResult = false;
                boolean adjacentTestResult = false;

                if (X >= ROWS || Y >= COLS) {
                    X = rnd.nextInt(ROWS);
                    Y = rnd.nextInt(COLS);
                    ORIENTATION = rnd.nextInt(2);
                    continue;
                } else if (ORIENTATION == 0 && (Y + S - 1) >= COLS) {
                    X = rnd.nextInt(ROWS);
                    Y = rnd.nextInt(COLS);
                    ORIENTATION = rnd.nextInt(2);
                    continue;
                } else if (ORIENTATION == 1 && (X + S - 1) >= ROWS) {
                    X = rnd.nextInt(ROWS);
                    Y = rnd.nextInt(COLS);
                    ORIENTATION = rnd.nextInt(2);
                    continue;
                }

                overlapTestResult = overlapTest(X, Y, ORIENTATION, S, computerGameBoard);
                adjacentTestResult = adjacentTest(X, Y, ORIENTATION, S, computerGameBoard);

                if (overlapTestResult) {
                    X = rnd.nextInt(ROWS);
                    Y = rnd.nextInt(COLS);
                    ORIENTATION = rnd.nextInt(2);
                    continue;
                } else if (adjacentTestResult) {
                    X = rnd.nextInt(ROWS);
                    Y = rnd.nextInt(COLS);
                    ORIENTATION = rnd.nextInt(2);
                    continue;
                }

                fillGameBoard(computerGameBoard, X, Y, ORIENTATION, S);
                fillBoatBoard(computerBoatBoard, X, Y, ORIENTATION, S, computerBoatArray, count);
                AMOUNT--;
                count++;
            }
        }

        int rComputer = numOfBoats;
        int rPlayer = numOfBoats;

        while (rComputer != 0 && rPlayer != 0) {

            // attacking - player round

            System.out.println("Your current guessing board:");
            printGameBoard(playerGuessBoard);

            System.out.println("Enter a tile to attack");
            input = scanner.nextLine();
            int[] guessingTileIntArray = new int[2];

            boolean flag = true;
            int boatId;

            while (flag) {
                // send string to function to get int array
                guessingTileIntArray = stringToIntArray(input, ", ");
                // checking the input
                X = guessingTileIntArray[0];
                Y = guessingTileIntArray[1];

                if (X >= ROWS || Y >= COLS) {
                    System.out.println("Illegal tile, try again!");
                    input = scanner.nextLine();
                } else if (X < 0 || Y < 0) {
                    System.out.println("Illegal tile, try again!");
                    input = scanner.nextLine();
                } else if (playerGuessBoard[X][Y] != '–') {
                    System.out.println("Tile already attacked, try again!");
                    input = scanner.nextLine();
                } else {
                    flag = false;
                }
            }

            // not sure if next two line are necessary, added because of an error message
            X = guessingTileIntArray[0];
            Y = guessingTileIntArray[1];

            if (computerGameBoard[X][Y] == '–') {
                System.out.println("That is a miss!");
                playerGuessBoard[X][Y] = 'X';
            }
            if (computerGameBoard[X][Y] == '#') {
                System.out.println("That is a hit!");
                playerGuessBoard[X][Y] = 'V';
                computerGameBoard[X][Y] = 'X';

                boatId = computerBoatBoard[X][Y];
                computerBoatArray[boatId - 1] = computerBoatArray[boatId - 1] - 1;

                for (i = 0; i < computerBoatArray.length; i++) {
                    if (computerBoatArray[i] == 0) {
                        rComputer = (rComputer - 1);
                        System.out.println("The computer's battleship has been drowned, " + rComputer + " more battleships to go!");
                        computerBoatArray[i] = -1;
                    }
                }
                if (rComputer == 0) {
                    break;
                }
            }


            // attacking - computer round
            X = rnd.nextInt(ROWS);
            Y = rnd.nextInt(COLS);
            flag = true;

            while (flag) {
                if (computerGuessBoard[X][Y] != '–') {
                    X = rnd.nextInt(ROWS);
                    Y = rnd.nextInt(COLS);
                } else {
                    flag = false;
                    System.out.println("The computer attacked (" + X + "," + Y + ")");
                }
            }

            if (playerGameBoard[X][Y] == '–') {
                System.out.println("That is a miss!");
                computerGuessBoard[X][Y] = 'X';
            }
            if (playerGameBoard[X][Y] == '#') {
                System.out.println("That is a hit!");
                computerGuessBoard[X][Y] = 'V';
                playerGameBoard[X][Y] = 'X';

                boatId = playerBoatBoard[X][Y];
                playerBoatArray[boatId - 1] = playerBoatArray[boatId - 1] - 1;
                for (i = 0; i < playerBoatArray.length; i++) {
                    if (playerBoatArray[i] == 0) {
                        rPlayer = (rPlayer - 1);
                        System.out.println("Your battleship has been drowned, you have left " + rPlayer + " more battleships!");
                        playerBoatArray[i] = -1;
                    }
                }
                if (rPlayer == 0) {
                    break;
                }
            }

            System.out.println("Your current game board:");
            printGameBoard(playerGameBoard);

        }


        // determine the winner
        if (rComputer == 0) {
            System.out.println("You won the game!");
        } else {
            System.out.println("You lost ):");
        }

        // end of main battleship game
    }

    //זה דוקומינטציה תקינה מה שקורה מתחתיי?

    /**
     * Function for conversion of string to int array
     *
     * @param input - string input from the user
     * @param regex - indication where to split the string
     * @return - returns the int array
     */
    public static int[] stringToIntArray(String input, String regex) {
        String[] stringArray = input.split(regex);
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }


    /**
     * Function for game and guess board building
     *
     * @param gameBoard - gets the relevant game board and fills it with the correct sign
     */
    public static void buildGameBoard(char[][] gameBoard) {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                gameBoard[i][j] = '–';
            }
        }
    }


    /**
     * Function for keeping track board building
     *
     * @param boatBoard - gets the relevant game board and fills it with the numbers to indicate boat location
     */
    public static void buildBoatBoard(int[][] boatBoard) {
        for (int i = 0; i < boatBoard.length; i++) {
            for (int j = 0; j < boatBoard[0].length; j++) {
                boatBoard[i][j] = 0;
            }
        }
    }


    /**
     * Function for checking overlapping battleships while filling board
     *
     * @param X               - x coordinate
     * @param Y               - y coordinate
     * @param ORIENTATION     - orientation 0 or 1
     * @param S               - size of boat
     * @param playerGameBoard - the game board to check in
     * @return - true or false according to the test
     */
    public static boolean overlapTest(int X, int Y, int ORIENTATION, int S, char[][] playerGameBoard) {
        char boatLocationMarker = '#';
        boolean overlapTestResult = false;

        switch (ORIENTATION) {
            case 0:
                for (int j = Y; j < Y + S; j++) {
                    if (playerGameBoard[X][j] == boatLocationMarker) {
                        overlapTestResult = true;
                    }
                }
                break;
            case 1:
                for (int i = X; i < X + S; i++) {
                    if (playerGameBoard[i][Y] == boatLocationMarker) {
                        overlapTestResult = true;
                    }
                }
                break;
        }
        return overlapTestResult;
    }

    /**
     * Function for checking adjacent battleships while filling board
     *
     * @param X               - x coordinate
     * @param Y               - y coordinate
     * @param ORIENTATION     - orientation 0 or 1
     * @param S               - size of boat
     * @param playerGameBoard - the game board to check in
     * @return - true or false according to the test
     */
    public static boolean adjacentTest(int X, int Y, int ORIENTATION, int S, char[][] playerGameBoard) {
        char boatLocation = '#';
        switch (ORIENTATION) {
            case 0:
                for (int i = Math.max(0, X - 1); i <= Math.min(playerGameBoard.length - 1, X + 1); i++) {
                    for (int j = Math.max(0, Y - 1); j <= Math.min(playerGameBoard[0].length - 1, Y + S); j++) {
                        if (playerGameBoard[i][j] == boatLocation) {
                            return true;
                        }
                    }
                }
                break;
            case 1:
                for (int i = Math.max(0, X - 1); i <= Math.min(playerGameBoard.length - 1, X + S); i++) {
                    for (int j = Math.max(0, Y - 1); j <= Math.min(playerGameBoard[0].length - 1, Y + 1); j++) {
                        if (playerGameBoard[i][j] == boatLocation) {
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }


    /**
     * function for filling the game board
     *
     * @param playerGameBoard- the game board to fill
     * @param X                - x coordinate
     * @param Y                - y coordinate
     * @param ORIENTATION      - orientation 0 or 1
     * @param S                - size of boat
     */
    public static void fillGameBoard(char[][] playerGameBoard, int X, int Y, int ORIENTATION, int S) {
        char boatLocation = '#';
        switch (ORIENTATION) {
            case 0:
                for (int j = Y; j <= Y + S - 1; j++) {
                    playerGameBoard[X][j] = boatLocation;
                }
                break;
            case 1:
                for (int i = X; i <= X + S - 1; i++) {
                    playerGameBoard[i][Y] = boatLocation;
                }
                break;
        }
    }


    /**
     * function for keeping track of battleships
     * @param boatBoard   - relevant boat board
     * @param X           - x coordinate
     * @param Y           - y coordinate
     * @param ORIENTATION - orientation 0 or 1
     * @param S           - size of boat
     * @param boatArray   - array of all boats of the player
     * @param count       - number of boat
     */
    public static void fillBoatBoard(int[][] boatBoard, int X, int Y, int ORIENTATION, int S, int[] boatArray, int count) {
        int boatLocationMarker = count; // count represents number of boat on the board
        switch (ORIENTATION) {
            case 0:
                for (int j = Y; j <= Y + S - 1; j++) {
                    boatBoard[X][j] = boatLocationMarker;
                }
                break;
            case 1:
                for (int i = X; i <= X + S - 1; i++) {
                    boatBoard[i][Y] = boatLocationMarker;
                }
                break;
        }
        boatArray[count - 1] = S; // in the array the number of the boat is the index-1 and the value is its length
    }


    /**
     * function for printing the game board
     * @param gameBoard - the relevant game board to print
     */
    public static void printGameBoard(char[][] gameBoard) {
        int ROWS = gameBoard.length; //num of rows in game board. exp: 11X11 -> ROWS = 11
        int COLS = gameBoard[0].length;//num of columns in game board

        String numLastRow = Integer.toString(ROWS - 1); //"10" הפכתי לסטרינג כדי לאתחל משנה של אורך המספר
        int len = numLastRow.length(); //2אורך מספר השורה האחרונה

        //printing the spaces before col numbers
        for (int i = 0; i < len + 1; i++) {
            System.out.print(" ");
        }
        // printing col numbers, without the last one (because of space after number)
        for (int i = 0; i < COLS - 1; i++) {
            System.out.print(i +" ");
        }
        // printing the last col number, and ln
        System.out.println(COLS - 1);

        for (int i = 0; i < ROWS; i++) {
            String new_i = Integer.toString(i); //to check the length of row number
            int len_i = new_i.length();
            int counter = checkingSpaces(ROWS); //got the number of digits of last row number
            int space = counter - len_i; //spaces = ההפרש
            for (int k = 0; k < space; k++) {
                System.out.print(" ");
            }
        System.out.print(i + " ");
        for (int j = 0; j < COLS; j++) {
                    System.out.print(gameBoard[i][j] + " ");
                }
        System.out.println();
        }

        System.out.println();
        }


    /**
     * function for checking spaces
     * @param ROWS - number of rows
     * @return - length of longest digits
     */
    public static int checkingSpaces(int ROWS) {
        ROWS -= 1;
        int counter = 0;
        while (((ROWS) % 10) != (ROWS)) {
            counter += 1;
            ROWS /= 10;
        }
        return counter + 1;
    }



 // end of functions


    public static void main(String[] args) throws IOException {
        String path = args[0];
        scanner = new Scanner(new File(path));
        int numberOfGames = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Total of " + numberOfGames + " games.");

        for (int i = 1; i <= numberOfGames; i++) {
            scanner.nextLine();
            int seed = scanner.nextInt();
            rnd = new Random(seed);
            scanner.nextLine();
            System.out.println("Game number " + i + " starts.");
            battleshipGame();
            System.out.println("Game number " + i + " is over.");
            System.out.println("------------------------------------------------------------");
        }
        System.out.println("All games are over.");
    }
}



