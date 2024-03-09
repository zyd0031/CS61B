package byog.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import static byog.lab5.HexWorld.initializeTeTile;

public class RoomTest {

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] finalWorldFrame = initializeTeTile(WIDTH, HEIGHT);
        long seed = 123;
        Random rand = new Random(seed);
        List<Room> rooms = new ArrayList<>();
        Room.generateRooms(finalWorldFrame, rand, rooms);
        ter.renderFrame(finalWorldFrame);

    }
}
