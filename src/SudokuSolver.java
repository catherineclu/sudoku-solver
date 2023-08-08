import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SudokuSolver {
    int[][] board; // 9x9 array representation of state of sudoku board

    public SudokuSolver(File world) throws FileNotFoundException {
        board = new int[9][9];

        Scanner sc = new Scanner(world);

        while (sc.hasNextInt()) { // fills array with starting values from file
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int value;

                    try { // ensures that input is formatted correctly - 81 just integers
                        value = sc.nextInt();
                    }
                    catch (InputMismatchException ime) {
                        board = null;
                        return;
                    }
                    catch (NoSuchElementException nse) {
                        board = null;
                        return;
                    }

                    if (value >= 0 && value <= 9) { // checks if integers are between 1 and 9
                        board[i][j] = value;
                    }
                    else {
                        board = null;
                        return;
                    }
                }
            }
        }
    }

    public boolean solve() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) { // traverses array to find first incomplete/empty tile
                    for (int possible = 1; possible < 10; possible++) { // attempts each number 1-9 to see if it could work
                        if (checkRow(row, possible) && checkColumn(col, possible) && checkSquare(row, col, possible)) {
                            board[row][col] = possible;
                            if (solve()) // recursively calls solve from this root until the board is complete
                                return true;
                            else { // if this number eventually wouldn't work/reaches a dead end, backtrack
                                board[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkRow(int currentX, int num) { // checks if the number already exists in that row
        for (int i = 0; i < 9; i++) {
            if (board[currentX][i] == num)
                return false;
        }
        return true;
    }

    private boolean checkColumn(int currentY, int num) { // checks if the number already exists in that column
        for (int i = 0; i < 9; i++) {
            if (board[i][currentY] == num)
                return false;
        }
        return true;
    }

    private boolean checkSquare(int currentX, int currentY, int num) { // checks if the number already exists in the 3x3 subgrid it's in
        // identifies which of the 9 subgrids it's in
        int startX = currentX/3*3;
        int startY = currentY/3*3;

        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                if (board[i][j] == num)
                    return false;
            }
        }
        return true;
    }

    @Override
    public String toString() { // prints board as numbers, for testing purposes
        String complete = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                complete += board[i][j] + " ";
            }
            complete += "\n";
        }
        return complete;
    }
}
