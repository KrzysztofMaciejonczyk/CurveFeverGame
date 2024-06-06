package org.example;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        int boardWidth = 1000;
        int boardHeight = 750;

        JFrame frame = new JFrame("Curve fever");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        Menu mainMenu = new Menu(frame, boardWidth, boardHeight);
        frame.add(mainMenu);
        frame.pack();
//        if (gameStarted) {
//            Game game = new Game(boardWidth-250, boardHeight, players);
//            frame.add(game, BorderLayout.CENTER);
//            frame.add(game.sidePanel, BorderLayout.EAST);
//            game.requestFocus();
//        }
//        frame.pack();
    }
}