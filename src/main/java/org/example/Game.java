package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Game extends JPanel implements ActionListener, KeyListener {

    int boardWidth;
    int boardHeight;
    int tileSize = 5;

    ArrayList<Snake> snakes = new ArrayList<>();
    ArrayList<Integer> leftControls = new ArrayList<>();
    ArrayList<Integer> rightControls = new ArrayList<>();

    Timer gameLoop;
    boolean gameOver = false;

    Game(int boardWidth, int boardHeight, int players) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        Color[] colors = {Color.GREEN, Color.BLUE, Color.RED, Color.ORANGE};
        for (int i = 0; i < players; i++) {
            snakes.add(new Snake(new Tile(50*i+5,50*i+5), colors[i]));
            snakes.get(i).setVelocityX(0);
            snakes.get(i).setVelocityY(0);
            snakes.get(i).setDirection(Direction.DOWN);
        }
        switch (players) {
            case 1:
                leftControls.add(KeyEvent.VK_LEFT);
                rightControls.add(KeyEvent.VK_RIGHT);
                break;
            case 2:
                leftControls.add(KeyEvent.VK_LEFT);
                rightControls.add(KeyEvent.VK_RIGHT);
                leftControls.add(KeyEvent.VK_Z);
                rightControls.add(KeyEvent.VK_X);
                break;
            case 3:
                leftControls.add(KeyEvent.VK_LEFT);
                rightControls.add(KeyEvent.VK_RIGHT);
                leftControls.add(KeyEvent.VK_Z);
                rightControls.add(KeyEvent.VK_X);
                leftControls.add(KeyEvent.VK_N);
                rightControls.add(KeyEvent.VK_M);
                break;
            default:
                System.out.println("Acceptable number of players: 1-3");
        }

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
        //Snakes
        for (Snake snake : snakes) {
            g.setColor(snake.getColor());
            g.fillRect(snake.getSnakeHead().x * tileSize,
                       snake.getSnakeHead().y * tileSize,
                          tileSize,
                          tileSize);
            for (Tile elem : snake.getSnakeBody()) {
                g.fillRect(elem.x * tileSize,
                        elem.y * tileSize,
                        tileSize,
                        tileSize);
            }
        }
    }

    public boolean checkCollisions() {
        for (Snake snake : snakes) {
            //wall collisions
            if (snake.getSnakeHead().x <= 0 || snake.getSnakeHead().y <= 0
                    || snake.getSnakeHead().x >= (boardWidth - 1) / tileSize
                    || snake.getSnakeHead().y >= (boardHeight - 1) / tileSize) {
                return true;
            }
            //snake collisions
            for (Snake otherSnake : snakes) {
                for (Tile elem : otherSnake.getSnakeBody()) {
                    if (elem.x == snake.getSnakeHead().x && elem.y == snake.getSnakeHead().y) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void move() {
        if (checkCollisions()) {
            gameOver = true;
        }
        for (Snake snake : snakes) {
            switch (snake.getDirection()) {
                case UP:
                    snake.setVelocityX(0);
                    snake.setVelocityY(-1);
                    if (snake.isTurnLeft()) {
                        snake.setDirection(Direction.UPLEFT);
                    }
                    else if (snake.isTurnRight()) {
                        snake.setDirection(Direction.UPRIGHT);
                    }
                    break;
                case UPLEFT:
                    snake.setVelocityX(-1);
                    snake.setVelocityY(-1);
                    if (snake.isTurnLeft()) {
                        snake.setDirection(Direction.LEFT);
                    }
                    else if (snake.isTurnRight()) {
                        snake.setDirection(Direction.UP);
                    }
                    break;
                case LEFT:
                    snake.setVelocityX(-1);
                    snake.setVelocityY(0);
                    if (snake.isTurnLeft()) {
                        snake.setDirection(Direction.DOWNLEFT);
                    }
                    else if (snake.isTurnRight()) {
                        snake.setDirection(Direction.UPLEFT);
                    }
                    break;
                case DOWNLEFT:
                    snake.setVelocityX(-1);
                    snake.setVelocityY(1);
                    if (snake.isTurnLeft()) {
                        snake.setDirection(Direction.DOWN);
                    }
                    else if (snake.isTurnRight()) {
                        snake.setDirection(Direction.LEFT);
                    }
                    break;
                case DOWN:
                    snake.setVelocityX(0);
                    snake.setVelocityY(1);
                    if (snake.isTurnLeft()) {
                        snake.setDirection(Direction.DOWNRIGHT);
                    }
                    else if (snake.isTurnRight()) {
                        snake.setDirection(Direction.DOWNLEFT);
                    }
                    break;
                case DOWNRIGHT:
                    snake.setVelocityX(1);
                    snake.setVelocityY(1);
                    if (snake.isTurnLeft()) {
                        snake.setDirection(Direction.RIGHT);
                    }
                    else if (snake.isTurnRight()) {
                        snake.setDirection(Direction.DOWN);
                    }
                    break;
                case RIGHT:
                    snake.setVelocityX(1);
                    snake.setVelocityY(0);
                    if (snake.isTurnLeft()) {
                        snake.setDirection(Direction.UPRIGHT);
                    }
                    else if (snake.isTurnRight()) {
                        snake.setDirection(Direction.DOWNRIGHT);
                    }
                    break;
                case UPRIGHT:
                    snake.setVelocityX(1);
                    snake.setVelocityY(-1);
                    if (snake.isTurnLeft()) {
                        snake.setDirection(Direction.UP);
                    }
                    else if (snake.isTurnRight()) {
                        snake.setDirection(Direction.RIGHT);
                    }
                    break;
            }
            //Snake body
            snake.getSnakeBody().add(new Tile(snake.getSnakeHead().x,snake.getSnakeHead().y));
            //Snake head
            snake.getSnakeHead().x += snake.getVelocityX();
            snake.getSnakeHead().y += snake.getVelocityY();
        }
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
        for (int i = 0; i < snakes.size(); i++) {
            if (e.getKeyCode() == leftControls.get(i)) {
                snakes.get(i).setTurnLeft(true);
            }
            else if (e.getKeyCode() == rightControls.get(i)) {
                snakes.get(i).setTurnRight(true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (int i = 0; i < snakes.size(); i++) {
            if (e.getKeyCode() == leftControls.get(i)) {
                snakes.get(i).setTurnLeft(false);
            }
            else if (e.getKeyCode() == rightControls.get(i)) {
                snakes.get(i).setTurnRight(false);
            }
        }
    }
}
