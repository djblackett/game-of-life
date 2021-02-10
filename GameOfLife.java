package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameOfLife extends JFrame {
    Color aliveColor = Color.BLACK;
    Color deadColor = Color.WHITE;
    Color gridColor = Color.GRAY;
    Thread t1;
    int pausedGeneration;
    int generations = 30;
    int generationsLeft;
    int seed;
    public  static final int SLOW = 2000; // 1 frame every 2 seconds
    public static final int FAST = 500; // 2 FPS
    int speed = 600; // ms value for Thread.sleep()
    boolean isPaused = false;
    JPanel[][] panels;
    int size = 50;
    JLabel generationLabel;
    JLabel aliveLabel;
    JPanel grid1;
    public GameState state;


    public char[][] matrix;
    public char[][] nextGeneration;

    public GameOfLife() {
        setTitle("Game of Life");
        matrix = new char[size][size];
        nextGeneration = new char[size][size];
        state = new GameState();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 695);
        setVisible(true);
        setLocationRelativeTo(null);

        // Create container for components

        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());


        // Play and pause button
        JToggleButton playToggleButton = new JToggleButton();
        playToggleButton.setText("Start");
        playToggleButton.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        playToggleButton.setName("PlayToggleButton");
        playToggleButton.setMnemonic(KeyEvent.VK_SPACE);
        playToggleButton.setFocusPainted(true);
        playToggleButton.setActionCommand("playToggleButton");
        playToggleButton.addActionListener(e -> {

            if (e.getActionCommand().equals("playToggleButton")) {
                isPaused = false;
                t1 = new Thread(new GameThread());
                playToggleButton.setText("Pause");
                playToggleButton.setActionCommand("pause");
                t1.start();
            } else if (e.getActionCommand().equals("pause")){
                isPaused = true;
                playToggleButton.setText("Start");
                playToggleButton.setActionCommand("playToggleButton");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        pane.add(playToggleButton, gbc);

        // todo keyboard mnemonics don't work yet -- button not in focus by default


        // Restart Button
        JButton restartGame = new JButton("Restart");
        restartGame.setName("ResetButton");
        restartGame.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        restartGame.setActionCommand("restart");
        restartGame.addActionListener(e -> {
            if (e.getActionCommand().equals("restart")) {
                isPaused = false;
                playToggleButton.setText("Pause");
                playToggleButton.setActionCommand("pause");
                generationLabel.setText("Generation #0");
                aliveLabel.setText("Alive: 0");
                initializeGame();
                t1 = new Thread(new GameThread());
                t1.start();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(10, 10, 10, 10);

        pane.add(restartGame, gbc);


        // Generation label
        generationLabel = new JLabel();
        generationLabel.setName("GenerationLabel");
        generationLabel.setText("Generation #0");
        generationLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);

        pane.add(generationLabel, gbc);


        // Alive cells label
        aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("Alive: 0");
        aliveLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);

        pane.add(aliveLabel, gbc);


        // Slider definition and parameters
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, SLOW, FAST);
        speedSlider.setName("SpeedSlider");
        speedSlider.setMinorTickSpacing(125);
        speedSlider.setMajorTickSpacing(500);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(e -> speed = speedSlider.getValue());


        // Slider label
        JLabel sliderLabel = new JLabel("Milliseconds between each generation");
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(30, 10, 10, 10);

        pane.add(sliderLabel, gbc);

        // Slider layout
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 10, 10);
        pane.add(speedSlider, gbc);


        // Grid to house the cells for the game
        panels = new JPanel[size][size];
        grid1 = new JPanel(new GridLayout(size, size, 3, 3));
        grid1.setBackground(gridColor);
        grid1.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1)));


        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == 'O') {
                    panels[i][j] = new JPanel();
                    panels[i][j].setBackground(Color.BLACK);
                } else {
                    panels[i][j] = new JPanel();
                    panels[i][j].setBackground(Color.WHITE);
                }
                grid1.add(panels[i][j]);
            }
        }


        // Grid layout
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 50;
        gbc.gridheight = 50;
        gbc.insets = new Insets(0, 10, 10, 10);

        pane.add(grid1, gbc);


        // Color chooser
        final JButton button = new JButton("Change alive cell color");

        ActionListener actionListener = actionEvent -> {
            Color initialBackground = button.getBackground();
            Color background = JColorChooser.showDialog(null, "JColorChooser Sample", initialBackground);
            if (background != null) {
                aliveColor = background;
                grid1.repaint();
            }
        };
        button.addActionListener(actionListener);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(30, 10, 10, 10);
        pane.add(button, gbc);


        final JButton deadColorButton = new JButton("Change dead cell color");

        ActionListener actionListenerForDead = actionEvent -> {
            Color initialBackground = deadColorButton.getBackground();
            Color background = JColorChooser.showDialog(null, "JColorChooser Sample", initialBackground);
            if (background != null) {
                deadColor = background;
                grid1.repaint();
            }
        };
        deadColorButton.addActionListener(actionListenerForDead);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        pane.add(deadColorButton, gbc);

        final JButton gridColorButton = new JButton("Change grid color");

        ActionListener gridActionListener = actionEvent -> {
            Color initialBackground = gridColorButton.getBackground();
            Color background = JColorChooser.showDialog(null, "JColorChooser Sample", initialBackground);
            if (background != null) {
                //gridColor = background;
                grid1.setBackground(background);
            }
        };
        gridColorButton.addActionListener(gridActionListener);
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        pane.add(gridColorButton, gbc);


        JLabel colorLabel = new JLabel("Cell changes not visible while paused");
        aliveLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        pane.add(colorLabel, gbc);


        JLabel spinnerLabel = new JLabel("Number of generations");
        spinnerLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.insets = new Insets(30, 10, 10, 10);
        pane.add(spinnerLabel, gbc);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(30, 0 , 100, 1));
        spinner.addChangeListener(e -> generations = (int) spinner.getValue());

        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        pane.add(spinner, gbc);

        add(pane);
        setVisible(true);
    }



    public static void main(String[] args) {
        GameOfLife game = new GameOfLife();
        game.initializeGame();
    }

    public void runGame() throws InterruptedException {
        runGenerations();
    }

    public void refreshLabels() {
        generationLabel.setText("Generation #" + state.getGenerationCount());
        state.setAliveCells(GameEvolution.getAliveCells(getMatrix()));
        aliveLabel.setText("Alive: " + state.getAliveCells());
    }


    public void evolveGeneration() throws InterruptedException {

        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                List<Character> list = GameEvolution.getNeighbors(this.matrix, j, i);
                int aliveNeighbors = GameEvolution.countAliveNeighbors(list);
                if (this.matrix[i][j] == 'O' && (aliveNeighbors == 2 || aliveNeighbors == 3)) {
                    this.nextGeneration[i][j] = 'O';
                    this.panels[i][j].setBackground(aliveColor);
                    this.panels[i][j].repaint();
                } else if (this.matrix[i][j] == ' ' && aliveNeighbors == 3) {
                    this.nextGeneration[i][j] = 'O';
                    this.panels[i][j].setBackground(aliveColor);
                    this.panels[i][j].repaint();
                } else {
                    this.nextGeneration[i][j] = ' ';
                    this.panels[i][j].setBackground(deadColor);
                    this.panels[i][j].repaint();
                }
            }
        }

        state.setNextGeneration(this.nextGeneration);
        this.matrix = Arrays.stream(this.nextGeneration).map(char[]::clone).toArray(char[][]::new);
        this.repaint();
        this.refreshLabels();
        Thread.sleep(speed);

        state.generationCount++;
        state.generationsLeft--;


    }

    public void runGenerations() throws InterruptedException {
        for (int i = pausedGeneration; i < generationsLeft; i++) {
            if (isPaused) {
                pausedGeneration = i;
                break;
            }
                this.evolveGeneration();
        }
    }


    public void initializeGame() {
        state.setGenerationCount(0);
        GameEvolution.setSize(size);
        generationsLeft = generations;
        pausedGeneration = 0;
        Random random = new Random();
        seed = random.nextInt();
        nextGeneration = new char[size][size];

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
    }

    public char[][] getMatrix() {
        return this.matrix;
    }

    public class GameThread implements Runnable {

        @Override
        public void run() {
            try {
                runGame();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}