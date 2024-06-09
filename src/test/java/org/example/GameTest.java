package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test //At least 10 tiles from edges
    void testSnakesSpawnNotNearEdges() {
        int boardWidth = 1000;
        int boardHeight = 750;
        int tileSize = 5;
        boolean inBounds;
        Game game = new Game(boardWidth, boardHeight, 4, tileSize);
        for (Snake snake : game.snakes) {
            inBounds = snake.getSnakeHead().x > 10 && snake.getSnakeHead().x < boardWidth / tileSize - 10
                    && snake.getSnakeHead().y > 10 && snake.getSnakeHead().y < boardHeight / tileSize - 10;
            if (!inBounds) {
                fail();
            }
        }
    }
    @Test
    void testIfOnlyOneSnakeCollisions() {
        int boardWidth = 150;
        int boardHeight = 150;
        int tileSize = 5;
        Game game = new Game(boardWidth, boardHeight, 1, tileSize);
        ArrayList<Tile> bodyTiles = new ArrayList<>();
        bodyTiles.add(new Tile(20, 20));
        game.snakes.get(0).setSnakeBody(bodyTiles);
        game.snakes.get(0).setSnakeHead(new Tile(20,20));
        Assertions.assertTrue(game.checkCollisions());
    }
    @Test
    void testCollisionsWithWalls() {
        int boardWidth = 150;
        int boardHeight = 150;
        int tileSize = 5;
        Game game = new Game(boardWidth, boardHeight, 1, tileSize);
        game.snakes.get(0).setSnakeHead(new Tile(boardWidth/tileSize,boardHeight/tileSize));
        Assertions.assertTrue(game.checkCollisions());
    }
    @Test
    void testDiagonalCollision() {
        int boardWidth = 400;
        int boardHeight = 450;
        int tileSize = 10;
        Game game = new Game(boardWidth, boardHeight, 2, tileSize);
        ArrayList<Tile> bodyTiles = new ArrayList<>();
        bodyTiles.add(new Tile(20, 20));
        bodyTiles.add(new Tile(21, 21));
        game.snakes.get(1).setSnakeBody(bodyTiles);
        game.snakes.get(0).setDirection(Direction.UPRIGHT);
        game.snakes.get(0).setSnakeHead(new Tile(20,21));
        Assertions.assertTrue(game.checkCollisions());
    }
}