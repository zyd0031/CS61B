package byog.Core;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;


import java.util.Random;

/**
 * random #rooms, #hallways
 * The locations of the rooms and hallways should be random.
 * The width and height of rooms should be random.
 * The length of hallways should be random.
 */

 /**
  * 1. Sprinkle a bunch of randomly located, non-overlapping rooms.
  * 2. Draw random corridors to connect them
  * To avoid a possible infinite loop, instead of trying until a certain number of rooms are successfully placed, 
  * I do a fixed number of attempts to place rooms. 
  */
public class MapGenerator {

    private int seed;
    private int Nrooms;
    private int Nhallways;

    public MapGenerator(int seed){
        this.seed = seed;
        
    }


    private void drawLTiles(){

    }


    public static void generateWorld(Random rand){

    }
}
