package org.example;

import java.awt.*;
import java.util.ArrayList;

public class Snake {
    private Tile snakeHead;
    private ArrayList<Tile> snakeBody = new ArrayList<>();
    private int velocityX = 0;
    private int velocityY = 0;
    private boolean turnLeft = false;
    private boolean turnRight = false;
    private Direction direction = Direction.UP;
    private final Color color;

    Snake(Tile snakeHead, Color color) {
        this.snakeHead = snakeHead;
        this.color = color;
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
}

