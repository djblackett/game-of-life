package life;

public class GameState {
    public int generationCount;
    public int aliveCells;
    char[][] originalGeneration;
    char[][] nextGeneration;
    public int generationsLeft;

    public int getAliveCells() {
        return aliveCells;
    }



    public void setGenerationCount(int newGenerationCount) {
        this.generationCount = newGenerationCount;
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

    public int getGenerationCount() {
        return this.generationCount;
    }



    public char[][] getOriginalGeneration() {
        return originalGeneration;
    }

    public char[][] getNextGeneration() {
        return nextGeneration;
    }
}
