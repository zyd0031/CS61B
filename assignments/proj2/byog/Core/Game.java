package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import static byog.lab5.HexWorld.initializeTeTile;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final String PATH = "savedworld.txt";
    private static final int STARTSCREENWIDTH = 600;
    private static final int STARTSCREENHEIGHT = 800;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        renderStartScreen();
        String input = processNavigationInput();
        if (input.equals("N")){
            newGame();
        }else if (input.equals("L")){
            WorldState worldState = null;
            try {
                worldState = loadWorld();
            } catch (ClassNotFoundException e) {
                System.out.println("Class WorldState not found.");
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            TETile[][] world = worldState.getWorld();
            Position playerPosition = worldState.getPlayerPosition();
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(world);
            playGame(world, playerPosition);
        }else if (input.equals("Q")){
            System.exit(0);
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        TETile[][] finalWorld = null;
        input = input.toLowerCase();
        char first = input.charAt(0);
        if (first == 'n'){
            finalWorld = newGame(input.substring(1, input.length()));
        }else if(first == 'l'){
            WorldState worldState = null;
            try {
                worldState = loadWorld();
            } catch (ClassNotFoundException e) {
                System.out.println("Class WorldState not found.");
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            finalWorld = worldState.getWorld();
            Position playerPosition = worldState.getPlayerPosition();
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(finalWorld);
            StdDraw.pause(750);
            input = input.replaceAll("\\d", "");
            playGame(finalWorld, playerPosition, input.substring(1, input.length()));
        }
        return finalWorld;
    }

    private TETile[][] newGame(String input){
        long seed = getSeed(input);
        Random rand = new Random(seed);
        WorldState newWorld= generateNewWorld(rand);
        TETile[][] world = newWorld.getWorld();
        Position playerPosition = newWorld.getPlayerPosition();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
        input = input.replaceAll("\\d", "");
        playGame(world, playerPosition, input);
        return world;
    }

    private void playGame(TETile[][] world, Position playerPosition, String input){
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
        StdDraw.pause(750);
        for (int i = 0; i < input.length(); i++) {
            char action = input.charAt(i);
            if (action == 'w'){
                walkUp(world, playerPosition);
                ter.renderFrame(world);
                StdDraw.pause(750);
            }else if (action == 's'){
                walkDown(world, playerPosition);
                ter.renderFrame(world);
                StdDraw.pause(750);
            }else if (action == 'd'){
                walkRight(world, playerPosition);
                ter.renderFrame(world);
                StdDraw.pause(750);
            }else if (action == 'a'){
                walkLeft(world, playerPosition);
                ter.renderFrame(world);
                StdDraw.pause(750);
            }else if (action == ':'){
                if (input.charAt(i + 1) == 'q'){
                    WorldState worldstate = new WorldState(playerPosition, world);
                    try {
                        saveWorld(worldstate);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
                break;
            }
        }    
    }

    private long getSeed(String input){
        StringBuilder seedBuilder = new StringBuilder();
        boolean digitFound = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)){
                seedBuilder.append(c);
                digitFound = true;
            }else if (digitFound){
                break;
            }
        }
        long seed = 0;
        if (seedBuilder.length() > 0){
            seed = Long.parseLong(seedBuilder.toString());
        }
        return seed;
    }

    private WorldState generateNewWorld(Random rand){
        TETile[][] finalWorldFrame = initializeTeTile(WIDTH, HEIGHT);
        List<Room> rooms = new ArrayList<>();
        rooms = Room.generateRooms(finalWorldFrame, rand, rooms);
        Position playerPosition = Hallway.drawHallways(finalWorldFrame, rooms, rand);
        return new WorldState(playerPosition, finalWorldFrame);
    }

    private void saveWorld(WorldState world) throws IOException{
        File file = new File(PATH);
        if (!file.exists()){
            file.createNewFile();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
            out.writeObject(world);
        }
    }

    private WorldState loadWorld() throws IOException, ClassNotFoundException {
        WorldState world;
        File file = new File(PATH);
        if (!file.exists()){
            throw new FileNotFoundException("No Saved World");
        }

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            world = (WorldState) in.readObject();
        }
        return world;
    }

    private void renderStartScreen(){
        StdDraw.setCanvasSize(STARTSCREENWIDTH, STARTSCREENHEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.enableDoubleBuffering();

        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.text(0.5, 0.8, "CS61B: THE GAME");

        Font samllFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(samllFont);
        StdDraw.text(0.5, 0.55, "New Game (N)");
        StdDraw.text(0.5, 0.5, "Load Game (L)");
        StdDraw.text(0.5, 0.45, "Quit (Q)");
        StdDraw.show();
    }

    private String processNavigationInput(){
        while(true){
            if (StdDraw.hasNextKeyTyped()){
                char input = StdDraw.nextKeyTyped();
                if ((input == 'N') || (input == 'L' || (input == 'Q'))){
                    return String.valueOf(input);
                }
            }
        }
    }

    private void newGame(){
        long seed = solicitSeed();
        Random rand = new Random(seed);
        WorldState worldstate = generateNewWorld(rand);
        TETile[][] world = worldstate.getWorld();
        Position playerPosition = worldstate.getPlayerPosition();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
        playGame(world, playerPosition);
    }

    private long solicitSeed(){
        String seed = "";
        drawSeedFrame(seed);

        while(true){
            if (StdDraw.hasNextKeyTyped()){
                char input = StdDraw.nextKeyTyped();
                if (input == 'S'){
                    String seedS = seed + "S";
                    drawSeedFrame(seedS);
                    return Long.valueOf(seed.toString());
                }else{
                    seed += String.valueOf(input);
                    drawSeedFrame(seed);
                }
            }
        }
    }

    private void drawSeedFrame(String seed){
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 25);
        StdDraw.setFont(font);
        StdDraw.text(0.5, 0.55, "Please enter a seed(End with 'S'):");
        StdDraw.text(0.5, 0.5, seed);
        StdDraw.show();
    }

    private void playGame(TETile[][] world, Position playerPosition){
        while(true){
            if(!StdDraw.hasNextKeyTyped()){
                continue;
            }
            char action = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (action == 'w'){
                walkUp(world, playerPosition);
                ter.renderFrame(world);
            }else if (action == 's'){
                walkDown(world, playerPosition);
                ter.renderFrame(world);
            }else if (action == 'd'){
                walkRight(world, playerPosition);
                ter.renderFrame(world);
            }else if (action == 'a'){
                walkLeft(world, playerPosition);
                ter.renderFrame(world);
            }else if (action == ':'){
                if (isQ()){
                    WorldState worldstate = new WorldState(playerPosition, world);
                    try {
                        saveWorld(worldstate);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                    System.exit(0);
                }
            }else if (action == 'q'){
                System.exit(0);
            }
        }
    }



    private void walkUp(TETile[][] world, Position playerPosition){
        int x = playerPosition.getX();
        int y = playerPosition.getY();
        if (world[x][y + 1].description().equals("floor")){
            world[x][y + 1] = Tileset.PLAYER;
            world[x][y] = Tileset.FLOOR;
            playerPosition.setY(y + 1);
        }
    }

    private void walkDown(TETile[][] world, Position playerPosition){
        int x = playerPosition.getX();
        int y = playerPosition.getY();
        if (world[x][y - 1].description().equals("floor")){
            world[x][y - 1] = Tileset.PLAYER;
            world[x][y] = Tileset.FLOOR;
            playerPosition.setY(y - 1);
        }
    }

    private void walkRight(TETile[][] world, Position playerPosition){
        int x = playerPosition.getX();
        int y = playerPosition.getY();
        if (world[x + 1][y].description().equals("floor")){
            world[x + 1][y] = Tileset.PLAYER;
            world[x][y] = Tileset.FLOOR;
            playerPosition.setX(x + 1);
        }
    }

    private void walkLeft(TETile[][] world, Position playerPosition){
        int x = playerPosition.getX();
        int y = playerPosition.getY();
        if (world[x - 1][y].description().equals("floor")){
            world[x - 1][y] = Tileset.PLAYER;
            world[x][y] = Tileset.FLOOR;
            playerPosition.setX(x - 1);
        }
    }

    private boolean isQ(){
        while(true){
            if (!StdDraw.hasNextKeyTyped()){
                continue;
            }
            if (Character.toLowerCase(StdDraw.nextKeyTyped()) == 'q'){
                return true;
            }else{
                return false;
            }
        }
    }

}




