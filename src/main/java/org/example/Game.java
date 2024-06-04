package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Game extends JPanel implements ActionListener, KeyListener {
    private static class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    private enum Direction {
        UP, UPLEFT, UPRIGHT, DOWN, DOWNLEFT, DOWNRIGHT, LEFT, RIGHT
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 5;

    Tile snakeHead;
    ArrayList<Tile> snakeBody = new ArrayList<>();

    Timer gameLoop;
    boolean gameOver = false;

    int velocityX;
    int velocityY;
    boolean turnLeft;
    boolean turnRight;
    Direction direction;

    Game(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        velocityX = 0;
        velocityY = 0;
        direction = Direction.DOWN;
        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over", boardWidth / 2 - 40, boardHeight / 2);
            return;
        }
        //Grid
        for (int x = 0; x < boardWidth/tileSize; x++) {
            g.drawLine(x*tileSize, 0, x*tileSize, boardHeight);
            g.drawLine(0, x*tileSize, boardWidth, x*tileSize);
        }
        //Snake
        g.setColor(Color.GREEN);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        for (Tile elem : snakeBody) {
            System.out.println(elem.x + " " + elem.y);
            g.fillRect(elem.x * tileSize, elem.y * tileSize, tileSize, tileSize);
        }
    }

    public boolean checkCollision(Tile headTile, ArrayList<Tile> bodyTiles) {
        //wall collisions
        if (headTile.x == 0 || headTile.y == 0 || headTile.x == boardWidth - 1 || headTile.y == boardHeight - 1) {
            return true;
        }
        //snake collisions
        for (Tile elem : bodyTiles) {
            if (elem.x == headTile.x && elem.y == headTile.y) {
                return true;
            }
        }
        return false;
    }

    public void move() {
        if (checkCollision(snakeHead, snakeBody)) {
            gameOver = true;
        }
        switch (direction) {
            case UP:
                velocityX = 0;
                velocityY = -1;
                if (turnLeft) {
                    direction = Direction.UPLEFT;
                }
                else if (turnRight) {
                    direction = Direction.UPRIGHT;
                }
                break;
            case UPLEFT:
                velocityX = -1;
                velocityY = -1;
                if (turnLeft) {
                    direction = Direction.LEFT;
                }
                else if (turnRight) {
                    direction = Direction.UP;
                }
                break;
            case LEFT:
                velocityX = -1;
                velocityY = 0;
                if (turnLeft) {
                    direction = Direction.DOWNLEFT;
                }
                else if (turnRight) {
                    direction = Direction.UPLEFT;
                }
                break;
            case DOWNLEFT:
                velocityX = -1;
                velocityY = 1;
                if (turnLeft) {
                    direction = Direction.DOWN;
                }
                else if (turnRight) {
                    direction = Direction.LEFT;
                }
                break;
            case DOWN:
                velocityX = 0;
                velocityY = 1;
                if (turnLeft) {
                    direction = Direction.DOWNRIGHT;
                }
                else if (turnRight) {
                    direction = Direction.DOWNLEFT;
                }
                break;
            case DOWNRIGHT:
                velocityX = 1;
                velocityY = 1;
                if (turnLeft) {
                    direction = Direction.RIGHT;
                }
                else if (turnRight) {
                    direction = Direction.DOWN;
                }
                break;
            case RIGHT:
                velocityX = 1;
                velocityY = 0;
                if (turnLeft) {
                    direction = Direction.UPRIGHT;
                }
                else if (turnRight) {
                    direction = Direction.DOWNRIGHT;
                }
                break;
            case UPRIGHT:
                velocityX = 1;
                velocityY = -1;
                if (turnLeft) {
                    direction = Direction.UP;
                }
                else if (turnRight) {
                    direction = Direction.RIGHT;
                }
                break;
        }
        //Snake body
        snakeBody.add(new Tile(snakeHead.x, snakeHead.y));
        //Snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            turnLeft = true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            turnRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            turnLeft = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            turnRight = false;
        }
    }
}
