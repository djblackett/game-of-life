package life;

public class GameState {
    public static int generationCount;
    public int aliveCells;

    public int getAliveCells() {
        return aliveCells;
    }

    char[][] originalGeneration;
    char[][] nextGeneration;

    public static void setGenerationCount(int generationCount) {
        GameState.generationCount = generationCount;
    }

    public void setAliveCells(int aliveCells) {
        this.aliveCells = aliveCells;
    }

    public void setOriginalGeneration(char[][] originalGeneration) {
        this.originalGeneration = originalGeneration;
    }

    public void setNextGeneration(char[][] nextGeneration) {
        this.nextGeneration = nextGeneration;
    }

    public GameState() {

    }

    public static int getGenerationCount() {
        return generationCount;
    }

    public char[][] getOriginalGeneration() {
        return originalGeneration;
    }

    public char[][] getNextGeneration() {
        return nextGeneration;
    }
}
