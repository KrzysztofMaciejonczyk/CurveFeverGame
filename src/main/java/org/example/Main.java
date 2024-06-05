package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int boardWidth = 800;
        int boardHeight = 800;
        int players = 4;

        JFrame frame = new JFrame("Curve fever");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        Game game = new Game(boardWidth, boardHeight, players);
        frame.add(game);
        frame.pack();
        game.requestFocus();
    }
}