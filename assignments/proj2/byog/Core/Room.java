package byog.Core;
import java.util.List;
import java.util.Random;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Room {

    private Position TopLeftPosition; //TL position of the room
    private int width;
    private int height;
    private Position TopRightPosition;
    private Position BottemLeftPosition;
    private Position BottemRightPosition;

    public Room(Position p, int width, int height){
        this.TopLeftPosition = p;
        int TopLeftX = TopLeftPosition.getX();
        int TopLeftY = TopLeftPosition.getY();
        this.width = width;
        this.height = height;
        this.TopRightPosition = new Position(TopLeftX + width - 1, TopLeftY);
        this.BottemLeftPosition = new Position(TopLeftX, TopLeftY - height + 1);
        this.BottemRightPosition = new Position(TopLeftX + width - 1, TopLeftY - height + 1);
    }

    public Position getTopLeftPosition(){
        return this.TopLeftPosition;
    }


    /**
     * return true if Position is out of bounds
     * if p is outside the world, return true
     * if p is inside the world, return false
     */
    private static boolean isOutOfBound(int[] bounds, Position p){
        int x = p.getX();
        int y = p.getY();
        int minX = bounds[0];
        int maxX = bounds[1];
        int minY = bounds[2];
        int maxY = bounds[3];
        return x < minX || x  > maxX  || y < minY  || y > maxY;     
    }

    /** if cannot add room --> return true */
    private static boolean  isOutOfBoundRoom(Room room){
        int maxX = Game.WIDTH - 2;
        int maxY = Game.HEIGHT - 2;
        int[] bounds = new int[]{1, maxX, 1, maxY};
        return isOutOfBound(bounds, room.BottemLeftPosition) || 
               isOutOfBound(bounds, room.BottemRightPosition) || 
               isOutOfBound(bounds, room.TopLeftPosition) || 
               isOutOfBound(bounds, room.TopRightPosition);
    }

    public int[] getRoomBounds(){
        int[] bounds = new int[4]; 
        bounds[0] = BottemLeftPosition.getX();
        bounds[1] = BottemRightPosition.getX();
        bounds[2] = BottemLeftPosition.getY();
        bounds[3] = TopLeftPosition.getY();
        return bounds; // [minX, maxX, minY, maxY]
    }

    /* return true if overlap */
    private boolean isOverlap(Room room){
        int[] myBounds = this.getRoomBounds();
        int[] roomBounds = room.getRoomBounds();
        boolean isVerticalAdjacent = Math.max(myBounds[2], roomBounds[2]) <= Math.min(myBounds[3], roomBounds[3]);
        boolean isHorizontalOneOff = (myBounds[1] + 1 == roomBounds[0]) || (roomBounds[1] + 1 == myBounds[0]);
        boolean isHorizontalAdjacent = Math.max(myBounds[0], roomBounds[0]) <= Math.min(myBounds[1], roomBounds[1]);
        boolean isVerticalOneOff = (myBounds[3] + 1 == roomBounds[2]) || (roomBounds[3] + 1 == myBounds[2]);
        boolean isOverlap = isVerticalAdjacent && isHorizontalAdjacent;
        boolean isVerticalOneOffAdjacent = isVerticalAdjacent && isHorizontalOneOff;
        boolean isHorizontalOneOffAdjacent = isVerticalOneOff && isHorizontalAdjacent;
        return isOverlap || isVerticalOneOffAdjacent || isHorizontalOneOffAdjacent;
    }

    /** generate a random room */
    private static Room generateRoom(Random rand){
        int width = rand.nextInt(3) * 2 + 3;
        int height = rand.nextInt(3) * 2 + 3;
        int x = rand.nextInt(Game.WIDTH - 2) + 1;
        int y = rand.nextInt(Game.HEIGHT - 2) + 1;
        return new Room(new Position(x, y), width, height);
    }

    /** return true if can add room */
    private static boolean canAddRoom(TETile[][] world, Room room, List<Room> rooms){
        if (isOutOfBoundRoom(room)){
            return false;
        }
        for (Room r : rooms) {
            if (room.isOverlap(r)){
                return false;
            }
        }
        return true;
    }
    /** generate random number of rooms */
    private static List<Room> addRooms(TETile[][] world, Random rand, List<Room> rooms){
        int numRoomTries = rand.nextInt(200);
        for (int i = 0; i < numRoomTries; i++) {
            Room newRoom = generateRoom(rand);
            if (canAddRoom(world, newRoom, rooms)){
                rooms.add(newRoom);
            }
        }
        return rooms;
    }

    /** generate a room to the world */
    private static void generateSingleRoom(TETile[][] world, Room room){
        int[] bounds = room.getRoomBounds();
        int minX = bounds[0];
        int maxX = bounds[1];
        int minY = bounds[2];
        int maxY = bounds[3];

        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
}
    
    /** show the rooms */
    public static List<Room> generateRooms(TETile[][] world, Random rand, List<Room> rooms){
        rooms = addRooms(world, rand, rooms);
        for (Room room : rooms) {
            generateSingleRoom(world, room);
        }
        return rooms;
    }


}
