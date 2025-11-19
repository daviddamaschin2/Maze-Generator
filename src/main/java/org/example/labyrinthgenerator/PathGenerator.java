package org.example.labyrinthgenerator;

import java.util.List;
import java.util.ArrayList;

public class PathGenerator {
    public enum Direction{
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private int fieldHeight;
    private int fieldWidth;
    private int startX;
    private int endX;

    List<Direction> finalPath;

    public PathGenerator(int fieldHeight, int fieldWidht, int startX, int endX){
        this.fieldWidth = fieldWidht;
        this.fieldHeight = fieldHeight;
        this.startX = startX;
        this.endX = endX;
        finalPath = new ArrayList<Direction>();
    }

    private void generatePath(){
        finalPath.clear();
        int attempts = 100;
        for(int i = 0; i < attempts; i++){

        }
    }

    private boolean createRecursivePath(int currentX, int currentY, List<Direction> path){
        if(currentY == 0 && currentX == endX){
            return true;
        }
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        return false;
    }

    private void shuffleArray(Direction[] dirs){
        Direction[] temp = dirs.clone();
        int firstPos = (int)(Math.random()*4);
    }
}
