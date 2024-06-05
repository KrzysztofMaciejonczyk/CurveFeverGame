package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Snake {
    private Tile snakeHead;
    private ArrayList<Tile> snakeBody = new ArrayList<>();
    private int velocityX = 0;
    private int velocityY = 0;
    private boolean turnLeft = false;
    private boolean turnRight = false;
    private Direction direction = Direction.UP;
    private final Color color;
    private boolean alive = true;

    Snake(Tile snakeHead, Color color) {
        this.snakeHead = snakeHead;
        this.color = color;
    }

    public ArrayList<Tile> getSurroundingTiles() {
        ArrayList<Tile> surroundingTiles = new ArrayList<>();
        Tile head = getSnakeHead();

        switch (direction) {
            case UP:
            case DOWN:
            case LEFT:
            case RIGHT:
            case UPLEFT:
                surroundingTiles.add(new Tile(head.x - 1, head.y));
                surroundingTiles.add(new Tile(head.x, head.y - 1));
                break;
            case DOWNRIGHT:
                surroundingTiles.add(new Tile(head.x + 1, head.y));
                surroundingTiles.add(new Tile(head.x, head.y + 1));
                break;
            case UPRIGHT:
                surroundingTiles.add(new Tile(head.x + 1, head.y));
                surroundingTiles.add(new Tile(head.x, head.y - 1));
                break;
            case DOWNLEFT:
                surroundingTiles.add(new Tile(head.x - 1, head.y));
                surroundingTiles.add(new Tile(head.x, head.y + 1));
                break;
        }
        return surroundingTiles;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isTurnLeft() {
        return turnLeft;
    }

    public void setTurnLeft(boolean turnLeft) {
        this.turnLeft = turnLeft;
    }

    public boolean isTurnRight() {
        return turnRight;
    }

    public void setTurnRight(boolean turnRight) {
        this.turnRight = turnRight;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Tile getSnakeHead() {
        return snakeHead;
    }

    public void setSnakeHead(Tile snakeHead) {
        this.snakeHead = snakeHead;
    }

    public ArrayList<Tile> getSnakeBody() {
        return snakeBody;
    }

    public void setSnakeBody(ArrayList<Tile> snakeBody) {
        this.snakeBody = snakeBody;
    }

    public Color getColor() {
        return color;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void reset(int positionX, int positionY, Direction direction) {
        Random rand = new Random();
        this.snakeHead = new Tile(positionX, positionY);
        this.snakeBody.clear();
        this.velocityX = 0;
        this.velocityY = 0;
        this.turnLeft = false;
        this.turnRight = false;
        this.direction = direction;
        this.alive = true;
    }
}

