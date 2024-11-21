package edu.nyu.cs.assignment5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A program to play Sudoku and validate its correctness.
 * Complete the functions to perform the tasks indicated in the comments.
 * NOTE: Some of the functions should make use of the other functions, so read the provided notes!
 */
public class SudokuValidator {
    // Use this "global"-ish Scanner variable when getting keyboard input from the
    // user within any function; this avoids common problems using several different
    // Scanners within different functions.
    public static Scanner scn = new Scanner(System.in);

    /**
     * The main method is automatically called first in a Java program.
     * This method contains the main logic of the program that makes use of all
     * the other methods to solve the problem.
     * 
     * This main method makes use of the other methods to perform the tasks.
     * Those methods are:
     * - wonPuzzle() returns true if the puzzle is complete and valid
     * - getFilePathFromUser() asks the user for a CSV file
     * - printRemainingMoves() prints all empty fields in the puzzle
     * - remainingMoves() computes all empty fields in the puzzle
     * - getContentsOfFile() reads a CSV file and writes its content into a
     * two-dimensional array
     * - printPuzzle() prints the Sudoku puzzle to the command line
     * - printRemainingMoves() prints all remaining empty fields
     * - makeMove() adds a number to the puzzle if the number and position are valid
     * - validatePuzzle() checks if every number is at most once contained in every
     * row, column, and block
     *
     * @param args an array of any command-line arguments
     */
    public static void main(String[] args) throws Exception {
        // Get the filepath from user
        String filepath = getFilepathFromUser();

        // Get the puzzle contents from the file
        int[][] puzzle = getContentsOfFile(filepath);

        // Print the initial puzzle
        printPuzzle(puzzle);

        // Validate the puzzle
        if (validatePuzzle(puzzle)) {
            System.out.println("Puzzle is valid.");
        } else {
            System.out.println("Puzzle is invalid, exiting.");
            return;
        }

        // Continue until the puzzle is won or the user quits
        while (!wonPuzzle(puzzle)) {
            // Print the remaining moves
            printRemainingMoves(puzzle);

            // Break the loop if no remaining moves
            if (remainingMoves(puzzle).size() == 0) {
                break;
            }

            // Prompt the user for next move
            System.out.println("What is your next move?");
            String line = scn.nextLine();
            
            // Break the loop if the user quits
            if (line.equals("quit")) {
                break;
            } else {
                // Process the user's move
                String[] tokens = line.split(" ");
                try {
                    int row = Integer.parseInt(tokens[0]);
                    int col = Integer.parseInt(tokens[1]);
                    int value = Integer.parseInt(tokens[2]);

                    // Make the move and handle invalid moves
                    if (!makeMove(puzzle, row, col, value)) {
                        System.out.println("Try again!");
                    }
                } catch (Exception e) {
                    System.out.println("Did not understand command");
                }
            }

            // Print the updated puzzle
            printPuzzle(puzzle);
        }

        // Check if the puzzle is won or not
        if (wonPuzzle(puzzle)) {
            System.out.println("Congratulations!");
        } else {
            System.out.println("Condolences!");
        }
    }

    /**
     * Checks if the Sudoku puzzle is solved successfully, which means there are no
     * remaining moves and the puzzle is valid. If these conditions are met, it
     * indicates that the user has won.
     *
     * NOTE: THIS METHOD SHOULD MAKE USE OF `remainingMoves()` AND `validatePuzzle()`!
     *
     * @param puzzle the Sudoku puzzle
     * @return true if the puzzle is successfully completed and valid, false otherwise
     */
    public static boolean wonPuzzle(int[][] puzzle) {
        return ((validatePuzzle(puzzle) && remainingMoves(puzzle).size() == 0));
    }

    /**
     * Prompts the user to enter the path to the text file they want to analyze.
     * 
     * HINT:
     * - Use the "global"-ish Scanner variable `scn` to get the user's response,
     * rather than creating a new Scanner variable within this function.
     * - Do not close the "global"-ish Scanner so that you can use it in other
     * functions.
     *
     * @return the file path entered by the user, e.g. "sudoku_puzzle.csv"
     */
    public static String getFilepathFromUser() {
        System.out.println("What file would you like to open?");
        String filepath = scn.nextLine();
        return filepath;
    }

    /**
     * Prints out a list of the remaining moves.
     * If no moves are left, print "No moves left!", otherwise print 
     * "Remaining moves:" followed by all possible moves.
     *
     * Possible output:
     * ```
     * Remaining moves:
     * (0,0) (4,2) (5,6)
     * ```
     * or
     * ```
     * No moves left!
     * ```
     *
     * NOTE: THIS METHOD SHOULD MAKE USE OF `remainingMoves()`!
     *
     * @param puzzle the Sudoku puzzle
     */
    public static void printRemainingMoves(int[][] puzzle) {
        if(!remainingMoves(puzzle).isEmpty()){ 
            System.out.println("Remaining moves:"); 
            System.out.println((Arrays.deepToString(remainingMoves(puzzle).toArray())));
        }
        else{
            System.out.println("No moves left!");
        }  

    }

    /**
     * Finds all of the remaining moves as an ArrayList of arrays, where each array
     * is of size 2. Any position in the Sudoku board where there is a 0 is a valid
     * move. Note that the moves should be sorted by smallest row and then smallest
     * column, i.e., lexicographical order.
     *
     * @param puzzle the Sudoku puzzle
     * @return ArrayList of all remaining moves
     */
    public static ArrayList<int[]> remainingMoves(int[][] puzzle) {
        ArrayList<int[]> indices = new ArrayList<>();
        
        for(int r = 0; r < puzzle.length; r++){
            for(int c = 0; c < puzzle[r].length; c++){
                if(puzzle[r][c] == 0){
                    indices.add(new int[] {r, c});
                }
            }

        }
        return indices;
    }

    /**
     * Opens the specified file and returns the text stored within. If the file cannot 
     * be opened, print out the message, "Oh no... can't find the file!"
     *
     * @param filename the path to a CSV file containing a Sudoku puzzle
     * @return a Sudoku puzzle represented by an int[][] array
     */
    public static int[][] getContentsOfFile(String filepath) {
        int[][] puzzle = new int[9][9];  // Create a 9x9 array for the puzzle
    
        try {
            Scanner fileScanner = new Scanner(new File(filepath));
    
            int row = 0;
            // Read each line of the file
            while (fileScanner.hasNextLine() && row < 9) {
                String line = fileScanner.nextLine();
                String[] values = line.split(",");  // Split line by commas
    
                for (int col = 0; col < 9; col++) {
                    puzzle[row][col] = Integer.parseInt(values[col].trim());  // Convert to integer
                }
                row++;
            }
            fileScanner.close();
            return puzzle;  // Return the populated puzzle
    
        } catch (FileNotFoundException e) {
            // Print error message if file is not found
            System.out.println("Oh no... can't find the file!");
        } catch (NumberFormatException e) {
            // Handle invalid number format in the CSV
            System.out.println("Error: Invalid number format in file.");
        }
    
        // Return null if the file couldn't be read or an error occurred
        return null;
    }
    

    /**
     * Prints out the Sudoku puzzle to the command line with a line of "===" above
     * and vertical "|" between numbers
     * Refer to the example outputs for the expected format.
     *
     * - Use the given char[][] `board` and replace the X's by the puzzle numbers.
     * - If the number is 0, print an empty space (' ') instead of the number.
     * - Do not forget to convert the number into the correct ASCII character!
     * - The output must match exactly the format from the example outputs.
     *
     * @param puzzle the Sudoku puzzle
     */
    public static void printPuzzle(int[][] puzzle) {
        char[][] board = {
            "╔═══════════════════════════════════╗".toCharArray(),
            "║ X │ X │ X ║ X │ X │ X ║ X │ X │ X ║".toCharArray(),
            "║───┼───┼───║───┼───┼───║───┼───┼───║".toCharArray(),
            "║ X │ X │ X ║ X │ X │ X ║ X │ X │ X ║".toCharArray(),
            "║───┼───┼───║───┼───┼───║───┼───┼───║".toCharArray(),
            "║ X │ X │ X ║ X │ X │ X ║ X │ X │ X ║".toCharArray(),
            "║═══════════╬═══════════╬═══════════║".toCharArray(),
            "║ X │ X │ X ║ X │ X │ X ║ X │ X │ X ║".toCharArray(),
            "║───┼───┼───║───┼───┼───║───┼───┼───║".toCharArray(),
            "║ X │ X │ X ║ X │ X │ X ║ X │ X │ X ║".toCharArray(),
            "║───┼───┼───║───┼───┼───║───┼───┼───║".toCharArray(),
            "║ X │ X │ X ║ X │ X │ X ║ X │ X │ X ║".toCharArray(),
            "║═══════════╬═══════════╬═══════════║".toCharArray(),
            "║ X │ X │ X ║ X │ X │ X ║ X │ X │ X ║".toCharArray(),
            "║───┼───┼───║───┼───┼───║───┼───┼───║".toCharArray(),
            "║ X │ X │ X ║ X │ X │ X ║ X │ X │ X ║".toCharArray(),
            "║───┼───┼───║───┼───┼───║───┼───┼───║".toCharArray(),
            "║ X │ X │ X ║ X │ X │ X ║ X │ X │ X ║".toCharArray(),
            "╚═══════════════════════════════════╝".toCharArray()};
    
        // Iterate over each cell in the puzzle and insert into the board
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int boardRow = 1 + row * 2;
                int boardCol = 2 + col * 4;
                
                if (puzzle[row][col] != 0) {
                    board[boardRow][boardCol] = (char) ('0' + puzzle[row][col]);
                } else {
                    board[boardRow][boardCol] = ' ';
                }
            }
        }
    
        // Print the board
        for (char[] line : board) {
            System.out.println(line);
        }
    }
    

    /**
     * Tries to insert the given value into the puzzle. Validates that the given
     * position (row,col) is within the bounds of the puzzle, the position is not already
     * occupied, and the value is within the expected range.
     *
     * Also, use `validatePuzzle()` to ensure that this move does not break the
     * game. If it does, set the value in this position back to 0.
     *
     * @param puzzle the Sudoku puzzle
     * @param row    the row-index where the value should be placed
     * @param col    the column-index where the value should be placed
     * @param value  the value that should be placed at the given (row,col)
     * @return true if the move was successful, false otherwise
     */
    public static boolean makeMove(int[][] puzzle, int row, int col, int value) {
        // Check if the position is within bounds, the position is empty, and the value is valid
        if (0 <= row && row <= 8 && 0 <= col && col <= 8 && puzzle[row][col] == 0 && value >= 1 && value <= 9) {
            
            // Place the value temporarily
            puzzle[row][col] = value;
            
            // Check if the puzzle is still valid with this move
            if (validatePuzzle(puzzle)) {
                return true;  // The move is valid
            } else {
                // Reset the position if the move is invalid
                puzzle[row][col] = 0;
                return false;  // The move was invalid
            }
        }
        
        // If the position is out of bounds, already occupied, or the value is invalid
        return false;
    }
    

    /**
     * Checks that none of the entries in `counts` is larger than one.
     * The entry at index 0 is ignored. The input array must have a length of 10.
     *
     * @param counts array of length 10
     * @return true if none of the entries is larger than 1 (ignoring index 0), false otherwise
     */
    public static boolean validateCountData(int[] counts) {
        assert (counts.length == 10);
        boolean countData = true;
        for(int i = 1; i < counts.length; i++){
            if(counts[i] > 1){
                countData = false;
            }
            
        }
        return countData;
        
    }

    /**
     * Validates the given row by ensuring that no number other than 0 appears
     * more than once.
     *
     * NOTE: THIS METHOD SHOULD MAKE USE OF `validateCountData()`!
     *
     * @param puzzle the Sudoku puzzle
     * @param row    the row that should be validated
     * @return true if no number besides 0 appears more than once in the row, false otherwise
     */
    public static boolean validateRow(int[][] puzzle, int row) {
        int[] counts = new int[10];
        for (int j = 0; j < puzzle[row].length; j++) {
            int num = puzzle[row][j];
            if (num != 0) {
                counts[num]++;
            }
        }
        return validateCountData(counts);
        
    }

    /**
     * Validates the given column by ensuring that no number other than 0 appears
     * more than once.
     *
     * NOTE: THIS METHOD SHOULD MAKE USE OF `validateCountData()`!
     *
     * @param puzzle the Sudoku puzzle
     * @param col    the column that should be validated
     * @return true if no number besides 0 appears more than once in the column, false otherwise
     */
    public static boolean validateColumn(int[][] puzzle, int col) {
        int[] counts = new int[10];
        for (int i = 0; i < puzzle.length; i++) {
            int num = puzzle[i][col];
            if (num != 0) {
                counts[num]++;
            }
        }
        return validateCountData(counts);

    }

    /**
     * Validates the given block by ensuring that no number other than 0 appears
     * more than once. 
     * 
     * Make sure each 3x3 block has at most one of any number other than 0.
     *
     * The `blockRow` and `blockCol` must be indices in the range [0,3), where
     * blockRow==0 corresponds to the top block row,
     * blockCol==0 corresponds to the left block column,
     * blockRow==2 corresponds to the bottom block row,
     * blockCol==2 corresponds to the right block column.
     *
     * NOTE: THIS METHOD SHOULD MAKE USE OF `validateCountData()`!
     *
     * @param puzzle   the Sudoku puzzle
     * @param blockRow the block row
     * @param blockCol the block column
     * @return true if no number besides 0 appears more than once in the block, otherwise false
     */
    public static boolean validateBlock(int[][] puzzle, int blockRow, int blockCol) {
        int[] counts = new int[10];
        int startRow = blockRow * 3;
        int startCol = blockCol * 3;
    
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                int num = puzzle[startRow + r][startCol + c];
                if (num != 0) {
                    counts[num]++;
                }
            }
        }
        
        return validateCountData(counts);
    }
    

    /**
     * Validates all rows in the Sudoku puzzle.
     *
     * NOTE: THIS METHOD SHOULD MAKE USE OF `validateRow()`!
     *
     * @param puzzle the Sudoku puzzle
     * @return true if all rows are valid, false otherwise
     */
    public static boolean validateRows(int[][] puzzle) {
        for(int i = 0; i < 9; i++){
            if(!validateRow(puzzle, i)){
                return false;
            
            }            
        }
        return true;
    }

    /**
     * Validates all columns in the Sudoku puzzle.
     *
     * NOTE: THIS METHOD SHOULD MAKE USE OF `validateColumn()`!
     *
     * @param puzzle the Sudoku puzzle
     * @return true if all columns are valid, false otherwise
     */
    public static boolean validateColumns(int[][] puzzle) {
        for(int c = 0; c < 9; c++){
            if(!validateColumn(puzzle, c)){
                    return false;
                }
            }
            return true;
        }
        
    

    /**
     * Validates all 3x3 blocks in the Sudoku puzzle.
     *
     * NOTE: THIS METHOD SHOULD MAKE USE OF `validateBlock()`!
     *
     * @param puzzle the Sudoku puzzle
     * @return true if all blocks are valid, false otherwise
     */
    public static boolean validateBlocks(int[][] puzzle) {
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                if(!validateBlock(puzzle, r, c)){
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * Validates the whole Sudoku puzzle by validating all rows, columns and blocks.
     * A puzzle is considered valid if none of the non-zero entries appears twice
     * in its column, row, and block.
     *
     * @param puzzle the Sudoku puzzle
     * @return true if all rows, columns, and blocks are valid, false otherwise
     */
    public static boolean validatePuzzle(int[][] puzzle) {
        if(validateBlocks(puzzle) && validateColumns(puzzle) && validateRows(puzzle)){
            return true;
        }
        else{
            return false;
        }
        
    }

} // end of class
