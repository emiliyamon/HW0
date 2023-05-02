import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;

    public static void battleshipGame() {
        // happy birthday shaked
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
        char[][] gameBoard = new char[ROWS][COLS];

        // building boards using function
        char[][] playerGameBoard = buildGameBoard(gameBoard);
        char[][] playerGuessBoard = buildGameBoard(gameBoard);
        char[][] computerGameBoard = buildGameBoard(gameBoard);
        char[][] computerGuessBoard = buildGameBoard(gameBoard);

        System.out.println("Enter battleship sizes");
        String battleshipSizes = scanner.nextLine();
        // "n1Xs1... nkXsk"

        String[] battleshipSizesArray = battleshipSizes.split(" ");
        // array of ["n1Xs1", ..,"nkXsk"]

        String[][] battleshipStringArray = new String[battleshipSizesArray.length][2];
        for (i = 0; i < battleshipStringArray.length; i++) {
            battleshipStringArray[i] = battleshipSizesArray[i].split("X");
        }
        // string array of ["n1","s1"], ..., ["nk","sk"]
        // check later if working

        int[][] battleshipArray = new int[battleshipStringArray.length][battleshipStringArray[0].length];
        for (i = 0; i < battleshipStringArray.length; i++) {
            for (j = 0; j < battleshipStringArray[0].length; j++) {
                battleshipArray[i][j] = Integer.parseInt(battleshipStringArray[i][j]);
            }
        }

        printGameBoard(playerGameBoard);

        // filling player game board
        for (i = 0; i < battleshipArray.length; i++) {
            System.out.println("Enter location and orientation for battleship of size " + battleshipArray[i][1]);
            input = scanner.nextLine();
            // format "x, y, orientation"
            regex = ", ";
            // orientation 0 for horizontal, 1 for vertical
            // horizontal size s from (x,y) to (x, y+s-1)
            // vertical size s from (x,y) to (x+s-1, y)

            // send string to function to get int array
            int[] locationOrientationArray = stringToIntArray(input, regex);

            int X = locationOrientationArray[0];
            int Y = locationOrientationArray[1];
            int ORIENTATION = locationOrientationArray[2];
            int S = battleshipArray[i][1];
            int AMOUNT = battleshipArray[i][0];

            while (AMOUNT > 0) {
                boolean overlapTestResult = false;
                boolean adjacentTestResult = false;

                if (ORIENTATION != 0 && ORIENTATION != 1) {
                    System.out.println("Illegal orientation, try again!");
                    input = scanner.nextLine();
                } else if (X > ROWS || Y > COLS) {
                    System.out.println("Illegal tile, try again!");
                    input = scanner.nextLine();
                } else if (ORIENTATION == 1 && (Y+S-1) > COLS) {
                    System.out.println("Battleship exceeds the boundaries of the board, try again!");
                    input = scanner.nextLine();
                } else if (ORIENTATION == 0 && (X+S-1) > ROWS) {
                    System.out.println("Battleship exceeds the boundaries of the board, try again!");
                    input = scanner.nextLine();
                }

                overlapTestResult = overlapTest(X, Y, ORIENTATION, S, playerGameBoard);
                adjacentTestResult = adjacentTest(X, Y, ORIENTATION, S, playerGameBoard);

                if (overlapTestResult) {
                    System.out.println("Battleship overlaps another battleship, try again!");
                    input = scanner.nextLine();
                } else if (adjacentTestResult) {
                    System.out.println("Adjacent battleship detected, try again!");
                    input = scanner.nextLine();
                }

                fillGameBoard(playerGameBoard, X,Y, ORIENTATION, S);
                printGameBoard(playerGameBoard);
                AMOUNT--;
            }
        }


        // end of main battleship game
    }


    /** Function for conversion of string to int array */
    public static int[] stringToIntArray(String input, String regex) {
        String[] stringArray = input.split(regex);
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }


    /** Function for board building */
    public static char[][] buildGameBoard(char[][] gameBoard) {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                gameBoard[i][j] = '–';
            }
        }
        return gameBoard;
    }


    /** Function for checking overlapping battleships while filling board */
    public static boolean overlapTest(int X, int Y, int ORIENTATION, int S, char[][] playerGameBoard) {
        char boatLocation = '#';
        switch (ORIENTATION) {
            case 0:
                for (int j = Y; j < Y+S; j++) {
                    if (playerGameBoard[X][j] == boatLocation) {
                        return true;
                    }
                }
            case 1:
                for (int i = X; i < X+S; i++) {
                    if (playerGameBoard[i][Y] == boatLocation) {
                        return true;
                    }
                }
        }
        return false;
    }


    /** Function for checking adjacent battleships while filling board */
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
            case 1:
                for (int i = Math.max(0, X - 1); i <= Math.min(playerGameBoard.length - 1, X + S); i++) {
                    for (int j = Math.max(0, Y - 1); j <= Math.min(playerGameBoard[0].length - 1, Y + 1); j++) {
                        if (playerGameBoard[i][j] == boatLocation) {
                            return true;
                        }
                    }
                }
        }
        return false;
    }


    /** function for filling the game board */
    public static void fillGameBoard(char[][] playerGameBoard, int X, int Y, int ORIENTATION, int S) {
        char boatLocation = '#';
        switch (ORIENTATION) {
            case 0:
                for (int j = Y; j <= Y + S - 1; j++) {
                    playerGameBoard[X][j] = boatLocation;
                }
            case 1:
                for (int i = X; i <= X + S - 1; i++) {
                    playerGameBoard[i][Y] = boatLocation;
                }
        }
    }


    /** function for printing the game board */
    public static void printGameBoard(char[][] gameBoard) {
        System.out.println("Your current game board:");
        for (char[] row : gameBoard) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }


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



