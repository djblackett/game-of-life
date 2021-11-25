package life;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameEvolution {

    public static int size;

    public static List<Character> getNeighbors(char[][] matrix, int x, int y) {
        List<Character> neighbors = new ArrayList<>();

        for (int i = y-1; i < y+2; i++) {
            for (int j = x-1; j < x+2; j++) {
                int adjustedX = adjustX(j);
                int adjustedY = adjustY(i);
                if (j == x && i == y) {
                    continue;
                }
                neighbors.add(matrix[adjustedY][adjustedX]);
            }
        }
        return neighbors;
    }

    public static int adjustX(int x) {
        if (x < 0) {
            return size - 1;
        } else if (x >= size) {
            return 0;
        } else {
            return x;
        }
    }

    public static int adjustY(int y) {
        if (y < 0) {
            return size - 1;
        } else if (y >= size) {
            return 0;
        } else {
            return y;
        }
    }

    public static int countAliveNeighbors(List<Character> neighbors) {
        return Collections.frequency(neighbors, 'O');
    }


    public static int getAliveCells(char[][] matrix) {
        int count = 0;
        for (char[] chars : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (chars[j] == 'O') {
                    count++;
                }
            }
        }
        return count;
    }

    public static void setSize(int newSize) {
        size = newSize;
    }

}
