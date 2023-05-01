import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;

    public static void battleshipGame() {
        // testing
        String str, regex; // used for converting str to int array
        int i, j; // index

        System.out.println("Enter the board size");
        // correct format for input: "nXm" n rows and m cols
        str = scanner.nextLine();

        regex = "X"; // regex relevant for this input line
        // send string to function to get int array
        int[] boardSizeIntArrayArray = stringToIntArray(str, regex);

        int ROWS = boardSizeIntArrayArray[0];
        int COLS = boardSizeIntArrayArray[1];
        String[][] gameBoard = new String[ROWS][COLS];

        // building boards using function
        String[][] playerGameBoard = buildGameBoard(gameBoard);
        String[][] playerGuessBoard = buildGameBoard(gameBoard);
        String[][] computerGameBoard = buildGameBoard(gameBoard);
        String[][] computerGuessBoard = buildGameBoard(gameBoard);

        System.out.println("Enter battleship sizes");
        String battleshipSizes = scanner.nextLine();
        // "n1Xs1... nkXsk"

        String[] battleshipSizesArray = battleshipSizes.split(" ");
        // array of n1Xs1, .., nkXsk

        String[][] battleshipStringArray = new String[battleshipSizesArray.length][2];
        for (i = 0; i < battleshipStringArray.length; i++) {
            battleshipStringArray[i] = battleshipSizesArray[i].split("X");
        }
        // string array of [n1][s1], ..., [nk][sk]
        int[][] battleshipArray = new int[battleshipStringArray.length][battleshipStringArray[0].length];
        for (i = 0; i < battleshipStringArray.length; i++) {
            for (j = 0; j < battleshipStringArray[0].length; j++) {
                battleshipArray[i][j] = Integer.parseInt(battleshipStringArray[i][j]);
            }
        }

        // declaring for next section
        int X;
        int Y;
        int ORIENTATION;
        int S;

        for (i = 0; i < battleshipArray.length; i++) {
            System.out.println("Enter location and orientation for battleship of size " + battleshipArray[i][1]);
            String locationOrientation = scanner.nextLine();
            // format "x, y, orientation"
            // orientation 0 for horizontal, 1 for vertical
            // horizontal size s from (x,y) to (x, y+s-1)
            // vertical size s from (x,y) to (x+s-1, y)
            X = locationOrientation.charAt(0);
            Y = locationOrientation.charAt(3);
            ORIENTATION = locationOrientation.charAt(6);
            S = battleshipArray[i][1];

            if (ORIENTATION != 0 && ORIENTATION != 1) {
                System.out.println("Illegal orientation, try again!");
                locationOrientation = scanner.nextLine();
            } else if (ORIENTATION == 0 && (X+S-1) > ROWS) {
                System.out.println("Illegal tile, try again!");
                locationOrientation = scanner.nextLine();
            } else if (ORIENTATION == 1 && (Y+S-1) > COLS) {
                System.out.println("Illegal tile, try again!");
                locationOrientation = scanner.nextLine();
            } else if (X > ROWS || Y > COLS) {
                System.out.println("Battleship exceeds the boundaries of the board, try again!");
                locationOrientation = scanner.nextLine();
            } else if (overlapTest) {
                System.out.println("Battleship overlaps another battleship, try again!");
                locationOrientation = scanner.nextLine();
            } else if (adjacentTest) {
                System.out.println("Adjacent battleship detected, try again!");
                locationOrientation = scanner.nextLine();
            }
        }

    }


    /** Function for conversion of string to int array */
    public static int[] stringToIntArray(String str, String regex) {
        String[] stringArray = str.split(regex);
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }


    /** Function for checking overlapping battleships while filling board */
    public static boolean overlapTest() {
        for
        return true;
    }


    /** Function for checking adjacent battleships while filling board */
    public static boolean adjacentTestTest() {
        return true;
    }

    public static String[][] buildGameBoard(String[][] gameBoard) {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                gameBoard[i][j] = "–";
                }
            }
        return gameBoard;
        }


    public static void printGameBoard(String[][] gameBoard) {
        for (String[] row : gameBoard) {
            for (String c : row) {
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



