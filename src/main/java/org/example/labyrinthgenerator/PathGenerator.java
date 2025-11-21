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
        int maxDepth = fieldHeight*fieldWidth;
        boolean solutionFound = false;

        //finding a possible path
        for(int i = 0; i < attempts && !solutionFound; i++){
            List<Direction> path = new ArrayList<Direction>();
            boolean[][] visited = new boolean[fieldHeight][fieldWidth];

            System.out.println("Attempt " + (i+1));

            visited[fieldHeight-1][startX] = true;

            if(createRecursivePath(startX, fieldHeight - 1, path, visited, 0, maxDepth)){
                solutionFound = true;
                finalPath = path;
                System.out.println("Solution found!"); // Add this
            }
        }

        //returning the path if it has been found
        if(!solutionFound){
            System.out.println("No solution found after " + attempts + " attempts");
            finalPath = null;
        }
    }

    private boolean createRecursivePath(int currentX, int currentY, List<Direction> path, boolean visited[][], int depth, int maxDepth){
        if(currentY == 0 && currentX == endX){
            return true;
        }
        else if(depth >= maxDepth){
            return false;
        }
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        shuffleArray(directions);

        int backtrackCount = 0;
        int maxBacktracks = 3;

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
            if(nextX >= 0 && nextX < fieldWidth && nextY >= 0 && nextY < fieldHeight && !visited[nextY][nextX]){

                path.add(dir);
                visited[nextY][nextX] = true;

                if(createRecursivePath(nextX, nextY, path, visited, depth+1, maxDepth)){
                    return true;
                }

                // Backtrack
                path.removeLast();
                visited[nextY][nextX] = false;
                backtrackCount++;

                if(backtrackCount >= maxBacktracks){
                    return false;
                }
            }
        }
        return false;
    }

    private void shuffleArray(PathGenerator.Direction[] dirs) {
        for (int i = dirs.length - 1; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));
            PathGenerator.Direction tmp = dirs[i];
            dirs[i] = dirs[j];
            dirs[j] = tmp;
        }
    }

    public Direction[] getPath(){
        return finalPath.toArray(new Direction[0]);
    }
}
