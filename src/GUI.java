import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class GUI {
    SudokuSolver s;
    JFrame frame;
    JPanel mainPanel;
    JPanel buttonPanel; // interactive portion
    JPanel gridPanel; // display portion
    JButton solveButton; // solves current sudoku
    JButton restartButton; // empties grid
    JButton chooseFileButton; // choose sudoku to solve

    public GUI() {
        frame = new JFrame();
        frame.setTitle("Sudoku Solver");
        frame.setSize(480, 550);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();

        buttonPanel = new JPanel();
        createButtons();
        gridPanel = new JPanel();
        fillGrid();


        frame.add(mainPanel);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.validate();
        frame.repaint();
    }

    public void refresh() { // updates interface with current state of sudoku board
        mainPanel.remove(gridPanel);
        gridPanel = new JPanel();

        fillGrid();

        frame.setSize(480,550);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    private void fillGrid() {
        gridPanel.setLayout(new GridLayout(9, 9, 0, 0));

        for (int i = 0; i < 9; i++) { // traverses board array to fill visual grid
            for (int j = 0; j < 9; j++) {
                String value;
                if (s != null) {
                    if (s.board[i][j] != 0)
                        value = String.valueOf(s.board[i][j]);
                    else // since empty spaces are represented as 0 in the array, replace w actual space
                        value = " ";
                }
                else value = " ";

                JLabel label = new JLabel(value, JLabel.CENTER);
                label.setOpaque(true);

                // formatting box size, font, color, borders
                label.setPreferredSize(new Dimension(50, 50));
                label.setFont(new Font("Times New Roman", Font.BOLD, 25));
                if (i >= 3 && i <= 5 && !(j >= 3 && j <= 5))
                    label.setBackground(new Color(211, 211, 211));
                else if (j >= 3 && j <= 5 && !(i >= 3 && i <= 5))
                    label.setBackground(new Color(211,211,211));
                label.setBorder(BorderFactory.createLineBorder(Color.black));
                gridPanel.add(label);
            }
        }
        mainPanel.add(gridPanel);
    }

    private void createButtons() {
        solveButton = new JButton("Solve");
        restartButton = new JButton("Restart");
        chooseFileButton = new JButton("Choose File");
        setSolve();
        setRestart();
        setChooseFile();
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
    }

    private void setSolve() {
        solveButton.addActionListener(e -> {
            s.solve();
            if (!s.solve()) {
                setError();
            }
            refresh();
        });

        buttonPanel.add(solveButton);
    }

    private void setError() { // if unsolvable
        mainPanel.remove(gridPanel);
        JPanel errorPanel = new JPanel();
        JLabel errorLabel;
        if (s.board != null)
            errorLabel = new JLabel("Unable to Solve! Try Again", JLabel.CENTER);
        else
            errorLabel = new JLabel("Incorrect File Format! Try Again", JLabel.CENTER);
        errorPanel.add(errorLabel);
        mainPanel.add(errorPanel);

        frame.setSize(480,600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    private void setRestart() {
        restartButton.addActionListener(e -> new GUI());
        buttonPanel.add(restartButton);
    }

    private void setChooseFile() { // user chooses file from device that fits constraints (9x9 numbers (1-9) separated by spaces and line breaks)
        chooseFileButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jfc.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                try {
                    s = new SudokuSolver(selectedFile); // started display is empty, but after choosing file, displays initial conditions of board
                    if (s.board != null) { // invalid starting numbers
                        refresh();
                    }
                    else
                        setError();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(chooseFileButton);
    }
}

