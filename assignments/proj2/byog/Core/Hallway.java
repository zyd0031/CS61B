package byog.Core;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
public class Hallway {

    private static class Edge{
        private Room sourceRoom;
        private Room targetRoom;
        private double weight;

        public Edge(Room sourceRoom, Room targetRoom){
            this.sourceRoom = sourceRoom;
            this.targetRoom = targetRoom;
            this.weight = calculateWeight(sourceRoom, targetRoom);
        }

        private double calculateWeight(Room sourceRoom, Room targetRoom){
            Position p1 = sourceRoom.getTopLeftPosition();
            Position p2 = targetRoom.getTopLeftPosition();
            return Math.hypot(p1.getX() - p2.getX(), p1.getY() - p2.getY());
        }

        public double getWeight(){
            return weight;
        }
    }

    private static List<Edge> primEdgesGenerator(TETile[][] world, List<Room> rooms){
        List<Edge> edges = new ArrayList<>();
        Set<Room> inTree = new HashSet<>();
        PriorityQueue<Edge> edgesAvailable =  new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));

        Room newlyAddedroom = rooms.get(0);
        inTree.add(newlyAddedroom);
        addEdges(newlyAddedroom, inTree, rooms, edgesAvailable);
        while (inTree.size() < rooms.size()){
            Edge edge = edgesAvailable.poll();
            Room newRoom = edge.targetRoom;
            if (!inTree.contains(newRoom)){
                inTree.add(newRoom);
                edges.add(edge);
                addEdges(newRoom, inTree, rooms, edgesAvailable);
            }
        }
        return edges;    
    }

    private static void addEdges(Room newlyAddedroom, Set<Room> inTree, List<Room> rooms, PriorityQueue<Edge> edgesAvailable){
        for (Room room : rooms) {
            if (!inTree.contains(room)){
                edgesAvailable.add(new Edge(newlyAddedroom, room));
            }
        }
    }

    private static Position createHallway(TETile[][] world, Edge edge, Random rand){
        Room r1 = edge.sourceRoom;
        Room r2 = edge.targetRoom;
        Position p1 = generateStarPosition(r1, rand);
        Position p2 = generateStarPosition(r2, rand);
        boolean horizontalFirst = rand.nextBoolean();
        Position turn;
        if (horizontalFirst){
            turn = new Position(p2.getX(), p1.getY());
        }else{
            turn = new Position(p1.getX(), p2.getY());
        }
        drawSingleHallway(world, p1, turn);
        drawSingleHallway(world, turn, p2);
        return turn;
    }

    private static void drawSingleHallway(TETile[][] world,Position start, Position end){
        int startX = start.getX();
        int startY = start.getY();
        int endX = end.getX();
        int endY = end.getY();
        if (startX == endX){  // vertical hallway
            int y1 = Math.min(startY, endY);
            int y2 = Math.max(startY, endY);
            for (int i = y1; i <= y2; i++) {
                world[startX][i] = Tileset.FLOOR;
            }
        }else{   // horizontal hallway
            int x1 = Math.min(startX, endX);
            int x2 = Math.max(startX, endX);
            for (int i = x1; i <= x2; i++) {
                world[i][startY] = Tileset.FLOOR;
            }
        }
    }

    private static Position generateStarPosition(Room room, Random rand){
        int [] bounds = room.getRoomBounds();
        int minX = bounds[0];
        int maxX = bounds[1];
        int minY = bounds[2];
        int maxY = bounds[3];
        int startX = rand.nextInt(maxX + 1 - minX) + minX;
        int startY;
        if (startX == minX || startX == maxX){
            startY = rand.nextInt(maxY + 1 - minY) + minY;
        }else{
            startY = rand.nextBoolean() ? minY : maxY;
        }
        return new Position(startX, startY);
    }

    private static int[][] drawHallway(TETile[][] world, List<Room> rooms, Random rand){
        int[][] hallwayCorner = new int[Game.WIDTH][Game.HEIGHT];
        List<Edge> edges = primEdgesGenerator(world, rooms);
        for (Edge edge : edges) {
            Position turn = createHallway(world, edge, rand);
            hallwayCorner[turn.getX()][turn.getY()] = 1;
        }
        return hallwayCorner;
    }

    private static void drawWall(TETile[][] world){
        for (int i = 0; i < Game.WIDTH; i++) {
            for (int j = 0; j < Game.HEIGHT; j++) {
                if (world[i][j] == Tileset.NOTHING){
                    if ( (i - 1) >= 0 && world[i - 1][j] == Tileset.FLOOR){
                        world[i][j] = Tileset.WALL;
                        continue;
                    }
                    if ( (i + 1) < Game.WIDTH && world[i + 1][j] == Tileset.FLOOR){
                        world[i][j] = Tileset.WALL;
                        continue;
                    }
                    if ( (j + 1) < Game.HEIGHT && world[i][j + 1] == Tileset.FLOOR){
                        world[i][j] = Tileset.WALL;
                        continue;
                    }
                    if ( (j - 1) >= 0 && world[i][j - 1] == Tileset.FLOOR){
                        world[i][j] = Tileset.WALL;
                        continue;
                    }
                }
            }
        }
    }

    private static int[][] roomCorner(List<Room> rooms){
        int[][] roomCorner = new int[Game.WIDTH][Game.HEIGHT];
        for (Room room : rooms) {
            int [] bounds = room.getRoomBounds();
            int minX = bounds[0];
            int maxX = bounds[1];
            int minY = bounds[2];
            int maxY = bounds[3];
            roomCorner[minX - 1][minY - 1] = 1;
            roomCorner[minX - 1][maxY + 1] = 1;
            roomCorner[maxX + 1][minY - 1] = 1;
            roomCorner[maxX + 1][maxY + 1] = 1;
        }
        return roomCorner;
    }

    private static void drawRoomCorner(TETile[][] world, List<Room> rooms){
        int[][] roomCorner = roomCorner(rooms);
        for (int i = 0; i < Game.WIDTH; i++) {
            for (int j = 0; j < Game.HEIGHT; j++){
                if (world[i][j] == Tileset.NOTHING && roomCorner[i][j] == 1){
                    world[i][j] = Tileset.WALL;
                }
            }
        }
    }

    private static void drawHallwayCorner(TETile[][] world, int[][] turns){
        for (int i = 0; i < Game.WIDTH; i++) {
            for (int j = 0; j < Game.HEIGHT; j++) {
                if (turns[i][j] == 1){
                    if ( (i - 1) >= 0 && (j - 1) >= 0 && world[i - 1][j - 1] == Tileset.NOTHING){
                        world[i - 1][j - 1] = Tileset.WALL;
                    }
                    if ( (i + 1) < Game.WIDTH && (j - 1) >= 0 && world[i + 1][j - 1] == Tileset.NOTHING){
                        world[i + 1][j - 1] = Tileset.WALL;
                    }
                    if ( (j + 1) < Game.HEIGHT && (i - 1) >= 0 && world[i - 1][j + 1] == Tileset.NOTHING){
                        world[i - 1][j + 1] = Tileset.WALL;
                    }
                    if ( (i + 1) < Game.WIDTH && (j + 1) < Game.HEIGHT && world[i + 1][j + 1] == Tileset.NOTHING){
                        world[i + 1][j + 1] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private static Position drawPlayer(TETile[][] world, Random rand){
        while(true){
            int x = rand.nextInt(Game.WIDTH);
            int y = rand.nextInt(Game.HEIGHT);
            if (world[x][y] == Tileset.FLOOR){
                world[x][y] = Tileset.PLAYER;
                return new Position(x, y);
            }
        }
    }

    public static Position drawHallways(TETile[][] world, List<Room> rooms, Random rand){
        int[][] turns = drawHallway(world, rooms, rand);
        drawWall(world);
        drawRoomCorner(world, rooms);
        drawHallwayCorner(world, turns);
        Position playerPosition =  drawPlayer(world, rand);
        return playerPosition;
    }
}
