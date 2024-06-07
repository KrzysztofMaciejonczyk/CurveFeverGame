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
import java.util.concurrent.*;

public class Game extends JPanel implements ActionListener, KeyListener {

    int gameBoardWidth;
    int gameBoardHeight;
    int tileSize = 5;

    ArrayList<Snake> snakes = new ArrayList<>();
    ArrayList<Integer> leftControls = new ArrayList<>();
    ArrayList<Integer> rightControls = new ArrayList<>();
    ArrayList<String> stringControls = new ArrayList<>();
    int playersLeft;
    int playersInit;
    int[] wins;

    javax.swing.Timer gameLoop;
    boolean gameOver = false;
    boolean gameStarted = false;
    private final ExecutorService collisionExecutorService = Executors.newFixedThreadPool(4);

    JPanel sidePanel;
    ArrayList<JLabel> snakeStatsLabels = new ArrayList<>();

    Game(int gameBoardWidth, int gameBoardHeight, int players) {
        this.gameBoardWidth = gameBoardWidth;
        this.gameBoardHeight = gameBoardHeight;
        playersLeft = players;
        playersInit = players;
        wins = new int[players];
        setPreferredSize(new Dimension(gameBoardWidth, gameBoardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        Random rand = new Random();
        for (int i = 0; i < players; i++) {
            snakes.add(new Snake(new Tile(rand.nextInt(gameBoardWidth/tileSize - 20) + 10, rand.nextInt(gameBoardHeight/tileSize - 20) + 10), colors[i]));
            snakes.get(i).setVelocityX(0);
            snakes.get(i).setVelocityY(0);
            snakes.get(i).setDirection(Direction.values()[rand.nextInt(8)]);
        }
        if (players >= 1) {
            leftControls.add(KeyEvent.VK_LEFT);
            rightControls.add(KeyEvent.VK_RIGHT);
            stringControls.add(KeyEvent.getKeyText(KeyEvent.VK_LEFT));
            stringControls.add(KeyEvent.getKeyText(KeyEvent.VK_RIGHT));
        }
        if (players >= 2) {
            leftControls.add(KeyEvent.VK_Z);
            rightControls.add(KeyEvent.VK_X);
            stringControls.add(KeyEvent.getKeyText(KeyEvent.VK_Z));
            stringControls.add(KeyEvent.getKeyText(KeyEvent.VK_X));
        }
        if (players >= 3) {
            leftControls.add(KeyEvent.VK_N);
            rightControls.add(KeyEvent.VK_M);
            stringControls.add(KeyEvent.getKeyText(KeyEvent.VK_N));
            stringControls.add(KeyEvent.getKeyText(KeyEvent.VK_M));
        }
        if (players >= 4) {
            leftControls.add(KeyEvent.VK_MULTIPLY);
            rightControls.add(KeyEvent.VK_SUBTRACT);
            stringControls.add(KeyEvent.getKeyText(KeyEvent.VK_MULTIPLY));
            stringControls.add(KeyEvent.getKeyText(KeyEvent.VK_SUBTRACT));
        }
        setupSidePanel();
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
    private void setupSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(250, gameBoardHeight));
        sidePanel.setBackground(new Color(242, 232, 182));
        JLabel title = new JLabel("DASHBOARD");
        title.setFont(new Font("Comic Sans", Font.BOLD, 38));
        title.setForeground(Color.BLACK);
        sidePanel.add(title);
        for (int i = 0; i < playersInit; i++) {
            JLabel label = new JLabel("Snake " + (i + 1) + ": " + wins[i]);
            label.setFont(new Font("Comic Sans", Font.PLAIN, 30));
            label.setBackground(snakes.get(i).getColor());
            label.setForeground(Color.BLACK);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            snakeStatsLabels.add(label);
            sidePanel.add(label);
        }

        sidePanel.add(Box.createRigidArea(new Dimension(100,70)));

        JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
        separator1.setPreferredSize(new Dimension(250, 10));
        separator1.setBackground(Color.BLACK);
        separator1.setForeground(Color.BLACK);
        sidePanel.add(separator1);

        JLabel controls = new JLabel("Controls");
        controls.setFont(new Font("Comic Sans", Font.BOLD, 38));
        controls.setForeground(Color.BLACK);
        controls.setBackground(new Color(242, 232, 182));
        controls.setOpaque(true);
        sidePanel.add(controls);
        JLabel left = new JLabel(" Left    Right");
        left.setFont(new Font("Comic Sans", Font.PLAIN, 35));
        left.setForeground(Color.BLACK);
        left.setBackground(new Color(242, 232, 182));
        left.setOpaque(true);
        sidePanel.add(left);
        for (int i = 0; i < playersInit*2; i++) {
            JLabel leftLabel = new JLabel(stringControls.get(i));
            leftLabel.setFont(new Font("Comic Sans", Font.PLAIN, 20));
            leftLabel.setBackground(snakes.get(i/2).getColor());
            leftLabel.setForeground(Color.BLACK);
            leftLabel.setOpaque(true);
            leftLabel.setPreferredSize(new Dimension(100,50));
            leftLabel.setHorizontalAlignment(SwingConstants.CENTER);
            leftLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            sidePanel.add(leftLabel);
        }

    }
    private void updateStats() {
        for (int i = 0; i < playersInit; i++) {
            snakeStatsLabels.get(i).setText("Snake " + (i + 1) + ": " + wins[i]);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (gameOver) {
            resetGame();
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

    private void resetGame() {
        gameStarted = false;
        playersLeft = playersInit;
        Random rand = new Random();
        ArrayList<Integer> positionsX = new ArrayList<>();
        ArrayList<Integer> positionsY = new ArrayList<>();
        for (int i = 0; i < playersInit; i++) {
            positionsX.add(rand.nextInt(gameBoardWidth/tileSize - 20) + 10);
            positionsY.add(rand.nextInt(gameBoardHeight/tileSize - 20) + 10);
        }
        int j = 0;
        for (Snake snake : snakes) {
            Direction direction = Direction.values()[rand.nextInt(8)];
            snake.reset(positionsX.get(j), positionsY.get(j), direction);
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
        ArrayList<Callable<Boolean>> tasks = new ArrayList<>();
        for (Snake snake : snakes) {
            tasks.add(() -> checkCollisionsPerSnake(snake));
        }
        try {
            ArrayList<Future<Boolean>> results = (ArrayList<Future<Boolean>>) collisionExecutorService.invokeAll(tasks, 100, TimeUnit.MILLISECONDS);
            for (Future<Boolean> result : results) {
                if (result.get()) {
                    return true;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean checkCollisionsPerSnake(Snake snake) {
            if (snake.isAlive()) {
                //wall collisions
                if (snake.getSnakeHead().x <= 0 || snake.getSnakeHead().y <= 0
                        || snake.getSnakeHead().x >= (gameBoardWidth - 1) / tileSize
                        || snake.getSnakeHead().y >= (gameBoardHeight - 1) / tileSize) {
                    snake.setVelocityX(0);
                    snake.setVelocityY(0);
                    snake.setAlive(false);
                    return true;
                }
                //snake collisions
                for (Snake otherSnake : snakes) {
                    //check collision for body
                    for (Tile bodyTile : otherSnake.getSnakeBody()) {
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
        return false;
    }

    public void move() {
        if (checkCollisions()) {
            --playersLeft;
            if (playersLeft <= 1) {
                gameOver = true;
                int winnerIndex = 0;
                for (int i = 0; i < snakes.size(); i++) {
                    if (snakes.get(i).isAlive()) {
                        winnerIndex = i;
                        break;
                    }
                }
                wins[winnerIndex]++;
                updateStats();
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
                //Snake body (sometimes place holes 3 pixel wide)
                if (snake.isCreatingHole()) {
                    snake.setHoleSize(snake.getHoleSize()+1);
                    if (snake.getHoleSize() == 3) {
                        snake.setHoleSize(0);
                        snake.setCreatingHole(false);
                    }
                }
                Random rand = new Random();
                if (rand.nextInt(0, 100) != 0 && !snake.isCreatingHole()) {
                    snake.getSnakeBody().add(new Tile(snake.getSnakeHead().x,snake.getSnakeHead().y));
                }
                else {
                    snake.setCreatingHole(true);
                }
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
