package life;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameOfLife extends JFrame {
    JButton[][] cells;
    public static JPanel cards = new JPanel(new CardLayout());
    public static CardLayout cl;
    AbstractPanel[][] panels;
    static int size = 15;
    JLabel generationLabel;
    JLabel aliveLabel;
    JPanel grid1;
    public static GameState state = new GameState();

    public static char[][] matrix;

    public static char[][] getMatrix() {
        return matrix;
    }


    public GameOfLife() {
        GameState state = new GameState();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450);
        setVisible(true);
        setLocationRelativeTo(null);

        generationLabel = new JLabel();
        aliveLabel = new JLabel();
        generationLabel.setName("GenerationLabel");
        aliveLabel.setName("AliveLabel");
        JPanel labels = new JPanel();

        labels.add(generationLabel);
        labels.add(aliveLabel);
        labels.setLayout(new GridLayout(2, 1));
        setVisible(true);
        generationLabel.setText("Generation #0");
        aliveLabel.setText("Alive: 0");

        //make grid layout
        //int size = Main.getSize(); Using hardcoded size for testing
        JPanel grid = new JPanel(new GridLayout(size, size));
        cells = new JButton[size][size];
        char[][] matrix = getMatrix();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new JButton();
                grid.add(cells[i][j]);
                if (matrix[i][j] == 'O') {
                    cells[i][j].setBackground(Color.BLACK);
                } else {
                    // cells[i][j].setBackground(Col);
                }
            }
        }


        panels = new AbstractPanel[size][size];
        grid1 = new JPanel(new GridLayout(size, size, 0, 0));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == 'O') {
                    panels[i][j] = new AlivePanel();
                } else if (matrix[i][j] == ' ') {
                    panels[i][j] = new DeadPanel();
                }
                grid1.add(panels[i][j]);
            }
        }

        JPanel card1 = new JPanel();
        card1.add(labels);
        card1.add(grid1);
        cards.add(card1);

        cl = (CardLayout)(cards.getLayout());
        cl.next(cards);

        add(labels);
        add(grid);
        //add(grid1);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setVisible(true);
    }

    public void refreshLabels() {
        generationLabel.setText("Generation #" + GameState.getGenerationCount());
        state.setAliveCells(GameEvolution.getAliveCells(getMatrix()));
        aliveLabel.setText("Alive: " + state.getAliveCells());
    }

    public static void main(String[] args) throws InterruptedException {


        //Scanner sc = new Scanner(System.in);

        size = 15;
        int generations = 20;
        Random random = new Random();
        int seed = random.nextInt();
        char[][] nextGeneration = new char[size][size];

        matrix = new char[size][size];
        state.setOriginalGeneration(matrix);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                boolean isAlive = random.nextBoolean();
                if (isAlive) {
                    matrix[i][j] = 'O';
                } else {
                    matrix[i][j] = ' ';
                }
            }
        }

        GameOfLife game = new GameOfLife();

        for (int k = 0; k < generations; k++) {


            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    List<Character> list = GameEvolution.getNeighbors(matrix, j, i);
                    int aliveNeighbors = GameEvolution.countAliveNeighbors(list);
                    if (matrix[i][j] == 'O' && (aliveNeighbors == 2 || aliveNeighbors == 3)) {
                        nextGeneration[i][j] = 'O';
                        game.cells[i][j].setBackground(Color.BLACK);
                    } else if (matrix[i][j] == ' ' && aliveNeighbors == 3) {
                        nextGeneration[i][j] = 'O';
                        game.cells[i][j].setBackground(Color.BLACK);
                    } else {
                        nextGeneration[i][j] = ' ';
                        game.cells[i][j].setBackground(Color.WHITE);
                    }
                }
            }

            state.setNextGeneration(nextGeneration);

//            for (int i = 0; i < matrix.length; i++) {
//                for (int j = 0; j < matrix[0].length; j++) {
//                matrix[i][j] = nextGeneration[i][j];
//                }
//            }
            matrix = Arrays.stream(nextGeneration).map(char[]::clone).toArray(char[][]::new);

            Thread.sleep(600);

//            try {
//                if (System.getProperty("os.name").contains("Windows"))
//                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//                else
//                    Runtime.getRuntime().exec("clear");
//            } catch (IOException | InterruptedException e) {
//            }
            GameState.generationCount++;
           // game.createCard();
            game.refreshLabels();
            game.refreshPanels();
            //game.removeAll();
            game.repaint();
            game.revalidate();
            game.update(game.getGraphics());
        }

    }

    public void refreshPanels() {
        JPanel grid = new JPanel(new GridLayout(size, size));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == 'O') {
                    panels[i][j] = new AlivePanel();
                    panels[i][j].setForeground(Color.BLACK);
                    panels[i][j].repaint();
                    panels[i][j].revalidate();
                } else if (matrix[i][j] == ' ') {
                    panels[i][j] = new DeadPanel();
                    panels[i][j].setForeground(Color.WHITE);
                    panels[i][j].repaint();
                    panels[i][j].revalidate();

                }
                //panels[i][j].repaint();
                    //grid.add(panels[i][j]);
            }
        }
        grid1.repaint();
        setVisible(true);
        //add(grid);
        this.update(getGraphics());
    }

    public void createCard() {
        JPanel labels = new JPanel();

        labels.add(generationLabel);
        labels.add(aliveLabel);
        labels.setLayout(new GridLayout(2, 1));
        setVisible(true);
        generationLabel.setText("Generation #0");
        aliveLabel.setText("Alive: 0");

        refreshLabels();
        refreshPanels();

        panels = new AbstractPanel[size][size];
        grid1 = new JPanel(new GridLayout(size, size, 0, 0));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == 'O') {
                    panels[i][j] = new AlivePanel();

                } else if (matrix[i][j] == ' ') {
                    panels[i][j] = new DeadPanel();
                }
                grid1.add(panels[i][j]);
            }
        }

        JPanel card1 = new JPanel();
        card1.add(labels);
        card1.add(grid1);
        cards.add(card1);
        cl.next(cards);
    }



}