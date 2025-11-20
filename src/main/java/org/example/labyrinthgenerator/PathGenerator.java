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

    public PathGenerator(int fieldHeight, int fieldWidth, int startX, int endX){
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.startX = startX;
        this.endX = endX;
        finalPath = new ArrayList<Direction>();
    }

    public void generatePath(){
        finalPath.clear();
        int attempts = 100;
        boolean solutionFound = false;

        //finding a possible path
        for(int i = 0; i < attempts && !solutionFound; i++){
            List<Direction> path = new ArrayList<Direction>();
            boolean[][] visited = new boolean[fieldHeight][fieldWidth];

            visited[startX][fieldHeight-1] = true;

            if(createRecursivePath(startX, fieldHeight - 1, path, visited)){
                solutionFound = true;
                finalPath = path;
            }
        }

        //returning the path if it has been found
        if(!solutionFound) finalPath = null;
    }

    private boolean createRecursivePath(int currentX, int currentY, List<Direction> path, boolean visited[][]){
        if(currentY == 0 && currentX == endX){
            return true;
        }
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        shuffleArray(directions);

        for(Direction dir : directions){
            int nextX = currentX;
            int nextY = currentY;

            switch(dir){
                case NORTH -> nextY--;
                case SOUTH -> nextY++;
                case WEST -> nextX--;
                case EAST -> nextX++;
            }

            // Check bounds and visited status
            if(nextX >= 0 && nextX < fieldWidth &&
                    nextY >= 0 && nextY < fieldHeight &&
                    !visited[nextY][nextX]){

                path.add(dir);
                visited[nextY][nextX] = true;

                if(createRecursivePath(nextX, nextY, path, visited)){
                    return true;
                }

                // Backtrack
                path.removeLast();
                visited[nextY][nextX] = false;
            }
        }
        return false;
    }

    public static void shuffleArray(Direction[] dirs){
        List<Direction> directions = new ArrayList<Direction>();
        directions.add(Direction.NORTH);
        directions.add(Direction.SOUTH);
        directions.add(Direction.WEST);
        directions.add(Direction.EAST);

        int randomDir = (int)(Math.random()*4);
        dirs[0] = directions.get(randomDir);
        directions.remove(randomDir);

        randomDir = (int)(Math.random()*3);
        dirs[1] = directions.get(randomDir);
        directions.remove(randomDir);

        randomDir = (int)(Math.random()*2);
        dirs[2] = directions.get(randomDir);
        directions.remove(randomDir);

        randomDir = (int)(Math.random()*1);
        dirs[3] = directions.get(randomDir);
        directions.remove(randomDir);
    }

    public Direction[] getPath(){
        return finalPath.toArray(new Direction[0]);
    }
}
