package byog.Core;

import java.io.Serializable;

import byog.TileEngine.TETile;

public class WorldState implements Serializable{
    private static final long serialVersionUID = 123234266345L;
    private Position playerPosition;
    private TETile[][] world;

    public WorldState(Position playerPosition, TETile[][] world){
        this.playerPosition = playerPosition;
        this.world = world;
    }

    public Position getPlayerPosition(){
        return playerPosition;
    }

    public TETile[][] getWorld(){
        return world;
    }
}
