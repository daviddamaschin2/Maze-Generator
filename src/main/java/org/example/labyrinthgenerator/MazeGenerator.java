package org.example.labyrinthgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MazeGenerator {
    private PathGenerator.Direction[] solvablePath;
    private boolean[][] horizontal_walls;
    private boolean[][] vertical_walls;
    private boolean[][] visitedBySolution;
    private boolean[][] visited;// for the backtracking algorithm that checks if this field has been visited by the algorithm
    private int fieldHeight;
    private int fieldWidth;
    private int startPos;
    private int endPos;

    public MazeGenerator(int fieldHeight, int fieldWidth, PathGenerator.Direction[] solvablePath, int startPos, int endPos){
        this.fieldHeight = fieldHeight;
        this.fieldWidth = fieldWidth;
        this.solvablePath = solvablePath;
        this.startPos = startPos;
        this.endPos = endPos;
        horizontal_walls = new boolean[fieldHeight-1][fieldWidth];
        vertical_walls = new boolean[fieldHeight][fieldWidth-1];
        visitedBySolution = new boolean[fieldHeight][fieldWidth];
        visited = new boolean[fieldHeight][fieldWidth];
    }

    public void generateMaze(){

        //reset all walls
        for(int i = 0; i < horizontal_walls.length; i++){
            for(int j = 0; j < horizontal_walls[i].length; j++){
                horizontal_walls[i][j] = true;
            }
        }
        for(int i = 0; i < vertical_walls.length; i++){
            for(int j = 0; j < vertical_walls[i].length; j++){
                vertical_walls[i][j] = true;
            }
        }


        //first trace out an actually solvable path
        if(solvablePath == null){
            System.out.println("Null Path");
        }
        else{
            int x = startPos;
            int y = fieldHeight-1;
            for(int i = 0; i < solvablePath.length; i++){
                switch(solvablePath[i]){
                    case NORTH -> {
                        y--;
                        horizontal_walls[y][x] = false;
                        visitedBySolution[y][x] = true;
                    }
                    case SOUTH -> {
                        y++;
                        horizontal_walls[y-1][x] = false;
                        visitedBySolution[y][x] = true;
                    }
                    case WEST -> {
                        x--;
                        vertical_walls[y][x] = false;
                        visitedBySolution[y][x] = true;
                    }
                    case EAST -> {
                        x++;
                        vertical_walls[y][x-1] = false;
                        visitedBySolution[y][x] = true;
                    }
                }
            }
        }


        //clear out some spaces to make the maze more twisted
        for(int y = fieldHeight-1; y >= 0; y--){
            for(int x = 0; x < fieldWidth; x++){
                if(!connectedToRestOfMaze(x, y, null)){
                    createRandomConnection(x, y);
                }
                mergeVisitedBySolution_Visited();
                visited = new boolean[fieldHeight][fieldWidth];
            }
        }

    }

    private boolean connectedToRestOfMaze(int xPos, int yPos, PathGenerator.Direction origin){
        if(xPos < 0 || xPos >= fieldWidth || yPos < 0 || yPos >= fieldHeight){
            return false;
        }

        if(xPos == startPos && yPos == fieldHeight-1 ||visitedBySolution[yPos][xPos]){
            return true;
        }

        if(visited[yPos][xPos]) return false;

        visited[yPos][xPos] = true;

        PathGenerator.Direction[] walkableDirections = new PathGenerator.Direction[4];
        walkableDirections[0] = PathGenerator.Direction.NORTH;
        walkableDirections[1] = PathGenerator.Direction.SOUTH;
        walkableDirections[2] = PathGenerator.Direction.EAST;
        walkableDirections[3] = PathGenerator.Direction.WEST;

        shuffleDirectionArray(walkableDirections);

        for(int i = 0; i < walkableDirections.length; i++){
            switch(walkableDirections[i]){
                case NORTH -> {
                    if(yPos > 0 && origin != PathGenerator.Direction.SOUTH){
                        if(!horizontal_walls[yPos-1][xPos]){
                            if(connectedToRestOfMaze( xPos, yPos-1, PathGenerator.Direction.NORTH)){
                                visitedBySolution[yPos][xPos] = true;
                                return true;
                            }
                        }
                    }
                }
                case SOUTH -> {
                    if(yPos < fieldHeight-1 && origin != PathGenerator.Direction.NORTH){
                        if(!horizontal_walls[yPos][xPos]){
                            if(connectedToRestOfMaze(xPos, yPos+1, PathGenerator.Direction.SOUTH)){
                                visitedBySolution[yPos][xPos] = true;
                                return true;
                            }
                        }
                    }
                }
                case EAST -> {
                    if(xPos < fieldWidth-1 && origin != PathGenerator.Direction.WEST){
                        if(!vertical_walls[yPos][xPos]){
                            if(connectedToRestOfMaze(xPos+1, yPos, PathGenerator.Direction.EAST)){
                                visitedBySolution[yPos][xPos] = true;
                                return true;
                            }
                        }
                    }
                }
                case WEST -> {
                    if(xPos > 0 && origin != PathGenerator.Direction.EAST){
                        if(!vertical_walls[yPos][xPos-1]){
                            if(connectedToRestOfMaze(xPos-1, yPos, PathGenerator.Direction.WEST)){
                                visitedBySolution[yPos][xPos] = true;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void mergeVisitedBySolution_Visited(){
        for(int i = 0; i < visited.length; i++){
            for(int j = 0; j < visited[i].length; j++){
                if(visited[i][j]) visitedBySolution[i][j] = true;
            }
        }
    }

    private void createRandomConnection(int posX, int posY){
        if(posX < 0 || posX >= fieldWidth || posY < 0 || posY >= fieldHeight){
            System.out.println("An Error occurred with randomConnection");
            return;
        }
        List<PathGenerator.Direction> list = new ArrayList<>();
        if (posX > 0 && vertical_walls[posY][posX-1] && !visited[posY][posX-1] && !(posY == 0 && posX-1 == endPos)) list.add(PathGenerator.Direction.WEST);
        if (posX < fieldWidth - 1 && vertical_walls[posY][posX] && !visited[posY][posX+1] && !(posY == 0 && posX+1 == endPos)) list.add(PathGenerator.Direction.EAST);
        if (posY > 0 && horizontal_walls[posY-1][posX] && !visited[posY-1][posX] && !(posY-1 == 0 && posX == endPos)) list.add(PathGenerator.Direction.NORTH);
        if (posY < fieldHeight - 1 && horizontal_walls[posY][posX] && !visited[posY+1][posX]) list.add(PathGenerator.Direction.SOUTH);

        PathGenerator.Direction[] possibleDirections = list.toArray(new PathGenerator.Direction[0]);
        shuffleDirectionArray(possibleDirections);

        visited[posY][posX] = true;

        for(PathGenerator.Direction dir : possibleDirections){
            int newX = posX;
            int newY = posY;

            switch (dir){
                case NORTH -> {
                    newY--;
                    horizontal_walls[newY][newX] = false;
                }
                case SOUTH -> {
                    newY++;
                    horizontal_walls[newY - 1][newX] = false;
                }
                case WEST -> {
                    newX--;
                    vertical_walls[newY][newX] = false;
                }
                case EAST -> {
                    newX++;
                    vertical_walls[newY][newX - 1] = false;
                }
            }

            if(connectedToRestOfMaze(newX, newY, null)){
                return;
            }
            else{
                createRandomConnection(newX, newY);
                return;
            }
        }
    }

    private void shuffleDirectionArray(PathGenerator.Direction[] dirs) {
        // Fisher–Yates in-place shuffle
        for (int i = dirs.length - 1; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));
            PathGenerator.Direction tmp = dirs[i];
            dirs[i] = dirs[j];
            dirs[j] = tmp;
        }
    }

    private boolean wouldCreateLargeSpace(int x, int y) {
        // Check if removing this wall would create a 2x2 or larger open space
        int openNeighbors = 0;

        // Check all four directions
        if(x > 0 && !vertical_walls[y][x-1]) openNeighbors++;
        if(x < fieldWidth-1 && !vertical_walls[y][x]) openNeighbors++;
        if(y > 0 && !horizontal_walls[y-1][x]) openNeighbors++;
        if(y < fieldHeight-1 && !horizontal_walls[y][x]) openNeighbors++;

        // If more than 3 walls are already open this would create a large space
        return openNeighbors >= 3;
    }

    public boolean[][] getVertical_walls() {
        return vertical_walls;
    }

    public boolean[][] getHorizontal_walls() {
        return horizontal_walls;
    }
}
