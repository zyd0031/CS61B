package byog.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import static byog.lab5.HexWorld.initializeTeTile;

public class HallwayTest {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static void main(String[] args) {
    TERenderer ter = new TERenderer();
    ter.initialize(WIDTH, HEIGHT);
    TETile[][] finalWorldFrame = initializeTeTile(WIDTH, HEIGHT);
    long seed = 123;
    Random rand = new Random(seed);
    List<Room> rooms = new ArrayList<>();
    rooms = Room.generateRooms(finalWorldFrame, rand, rooms);
    Hallway.drawHallways(finalWorldFrame, rooms, rand);
    ter.renderFrame(finalWorldFrame);

}
}
