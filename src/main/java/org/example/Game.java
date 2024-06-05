package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends JPanel implements ActionListener, KeyListener {

    int boardWidth;
    int boardHeight;
    int tileSize = 5;

    ArrayList<Snake> snakes = new ArrayList<>();
    ArrayList<Integer> leftControls = new ArrayList<>();
    ArrayList<Integer> rightControls = new ArrayList<>();
    int playersLeft;
    int playersInit;

    javax.swing.Timer gameLoop;
    boolean gameOver = false;
    boolean gameStarted = false;

    Game(int boardWidth, int boardHeight, int players) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        playersLeft = players;
        playersInit = players;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        Color[] colors = {Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW};
        Random rand = new Random();
        for (int i = 0; i < players; i++) {
            snakes.add(new Snake(new Tile(rand.nextInt(boardWidth/tileSize), rand.nextInt(boardHeight/tileSize)), colors[i]));
            snakes.get(i).setVelocityX(0);
            snakes.get(i).setVelocityY(0);
            snakes.get(i).setDirection(Direction.values()[rand.nextInt(8)]);
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

        Timer startTimer = new Timer();
        startTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameStarted = true;
                gameLoop = new javax.swing.Timer(100, Game.this);
                gameLoop.start();
                startTimer.cancel();
            }
        }, 2000);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (gameOver) {
            resetGame();
        }
        //Grid
//        for (int x = 0; x < boardWidth/tileSize; x++) {
//            g.drawLine(x*tileSize, 0, x*tileSize, boardHeight);
//            g.drawLine(0, x*tileSize, boardWidth, x*tileSize);
//        }
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

    private void resetGame() {
        gameStarted = false;
        playersLeft = playersInit;
        Random rand = new Random();
        ArrayList<Integer> positionsX = new ArrayList<>();
        ArrayList<Integer> positionsY = new ArrayList<>();
        for (int i = 0; i < playersInit; i++) {
            positionsX.add(rand.nextInt(boardWidth/tileSize));
            positionsY.add(rand.nextInt(boardHeight/tileSize));
        }
        int j = 0;
        for (Snake snake : snakes) {
            snake.reset(positionsX.get(j), positionsY.get(j));
            j++;
        }
        gameOver = false;
        repaint();
        Timer startTimer = new Timer();
        startTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameLoop.stop();
                gameOver = false;
                gameStarted = true;
                gameLoop.start();
                startTimer.cancel();
            }
        }, 2000);
    }

    public boolean checkCollisions() {
        for (Snake snake : snakes) {
            if (snake.isAlive()) {
                //wall collisions
                if (snake.getSnakeHead().x <= 0 || snake.getSnakeHead().y <= 0
                        || snake.getSnakeHead().x >= (boardWidth - 1) / tileSize
                        || snake.getSnakeHead().y >= (boardHeight - 1) / tileSize) {
                    snake.setVelocityX(0);
                    snake.setVelocityY(0);
                    snake.setAlive(false);
                    return true;
                }
                //snake collisions
                for (Snake otherSnake : snakes) {
                    for (Tile bodyTile : otherSnake.getSnakeBody()) {
                        //check if collision
                        if (bodyTile.x == snake.getSnakeHead().x && bodyTile.y == snake.getSnakeHead().y) {
                            snake.setVelocityX(0);
                            snake.setVelocityY(0);
                            snake.setAlive(false);
                            return true;
                        }
                        //if diagonal direction then check also adequate surrounding
                        if (snake.getDirection() == Direction.UPRIGHT || snake.getDirection() == Direction.UPLEFT ||
                                snake.getDirection() == Direction.DOWNRIGHT || snake.getDirection() == Direction.DOWNLEFT) {
                            ArrayList<Tile> surrounding = snake.getSurroundingTiles();
                            if (otherSnake.getSnakeBody().contains(surrounding.get(0)) && (otherSnake.getSnakeBody().contains(surrounding.get(1)))) {
                                snake.setVelocityX(0);
                                snake.setVelocityY(0);
                                snake.setAlive(false);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void move() {
        if (checkCollisions()) {
            --playersLeft;
            if (playersLeft == 1) {
                gameOver = true;
            }
        }
        for (Snake snake : snakes) {
            if (snake.isAlive()) {
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted) {
            move();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameStarted) {
            for (int i = 0; i < snakes.size(); i++) {
                if (e.getKeyCode() == leftControls.get(i)) {
                    snakes.get(i).setTurnLeft(true);
                }
                else if (e.getKeyCode() == rightControls.get(i)) {
                    snakes.get(i).setTurnRight(true);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameStarted) {
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
}
