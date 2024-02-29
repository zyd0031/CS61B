package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    final static int[] HexagonNum = new int[]{3, 4, 5, 4, 3};

    private static class Position{
        private int x;
        private int y;
        public Position(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public static void addHexagon(TETile[][] world, Position p, int s, TETile t){
        int x = p.x;
        int y = p.y;
        /** draw the top half */
        for (int i = 0; i < s; i++) {
            int startX = x - i;
            int endX = startX + s + 2 * i;
            int currY = y - i;
            for(int j = startX; j < endX; j++){
                world[j][currY] = t;
            }
        }
        /** draw the bott0m half */
        for (int i = 0; i < s; i++) {
            int startX = x - (s - 1) + i;
            int endX = startX + s + 2 * (s - 1 - i);
            int currY = y - s - i;
            for(int j = startX; j < endX; j++){
                world[j][currY] = t;
            }
        }
    }

    private static int[] getXs(Position p, int s){
        int[] XPositions = new int[HexagonNum.length];
        int x = p.x;
        for (int i = 0; i < HexagonNum.length; i++) {
            int b = i - 2;
            XPositions[i] = x + b * s;
        }
        return XPositions;
    }

    private static int[][] getYs(Position p, int s){
        int[][] YPositions = new int[HexagonNum.length][];
        int y = p.y;
        for (int i = 0; i < YPositions.length; i++) {
            int numHexagon = HexagonNum[i];
            YPositions[i] = new int[numHexagon];
            int coef = (i - 2) * Integer.compare(2 - i, 0);
            for (int j = 0; j < numHexagon; j++) {
                YPositions[i][j] = y + coef * s - 2 * s * j;
            }
        }
        return YPositions;
    }

    public static void drawHexagonWorld(TETile[][] world, Position p, int s){
        int[] XPos = getXs(p, s);
        int[][] YPos = getYs(p, s);
        for (int i = 0; i < XPos.length; i++) {
            for (int j = 0; j < YPos[i].length; j++) {
                TETile t = RandomWorldDemo.randomTile();
                Position p_ = new Position(XPos[i], YPos[i][j]);
                addHexagon(world, p_, s, t);
            }
        }
    }

    public static TETile[][] initializeTeTile(int width, int height){
        TETile[][] world = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }

    public static void main(String[] args) {
        int width = 60;
        int height = 60;
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        Position p = new Position(30, 45);
        TETile[][] world = initializeTeTile(width, height);
        drawHexagonWorld(world, p, 4);
        ter.renderFrame(world);
    }
}
