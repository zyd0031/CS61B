package byog.Core;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Maze {
    private void mazeGenerator(TETile[][] world){

        // starts with 1 and ends with XX - 1 to leave place for walls.
        for (int i = 1; i < world.length -1 ; i += 2) {
            for (int j = 1; j < world[0].length - 1; j += 2) {
                if (world[i][j] == Tileset.NOTHING){
                    Position p = new Position(i, j);
                    growMaze(p);
                }
            }
        }
    }



    private static void growMaze(TETile[][] world, Position p){

    }

    /** return true if not out of bounds */
    private boolean notOutOfBounds(TETile[][] world,Position p){
        int xP = p.getX();
        int yP = p.getY();
        int maxX = world.length - 2;
        int maxY = world[0].length - 2;
        int minX = 1;
        int minY = 1;
        return xP <= maxX && xP >= minX && yP >= minY && yP <= maxY;
    }

    private ArrayList<Position> growthDirection(TETile[][] world, Position P){
        int xP = P.getX();
        int yP = P.getY();
        ArrayList<Position> possibleDirections = new ArrayList<>();
        possibleDirections.add(new Position(xP + 1, yP));
        possibleDirections.add(new Position(xP, yP + 1));
        possibleDirections.add(new Position(xP, yP - 1));
        possibleDirections.add(new Position(xP - 1, yP));
        Iterator<Position> positionIterator = possibleDirections.iterator();
        while(positionIterator.hasNext()){
            Position position = positionIterator.next();
            if (!notOutOfBounds(world, position)){
                positionIterator.remove();
                continue;
            }

        }



    }
}
