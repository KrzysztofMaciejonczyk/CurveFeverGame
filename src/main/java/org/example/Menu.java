package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Menu extends JPanel {
    private final JFrame frame;
    private final int boardWidth;
    private final int boardHeight;

    public Menu(JFrame frame, int boardWidth, int boardHeight) {
        this.frame = frame;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBackground(new Color(242, 232, 182));

        JLabel titleLabel = new JLabel("Curve Fever");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 60));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(titleLabel, BorderLayout.PAGE_START);

        this.add(Box.createRigidArea(new Dimension(0,150)));

        JLabel playersLabel = new JLabel("Select number of players:");
        playersLabel.setFont(new Font("Serif", Font.PLAIN, 36));
        playersLabel.setForeground(Color.BLACK);
        playersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(playersLabel, BorderLayout.CENTER);

        String[] playerOptions = {"1", "2", "3", "4"};
        JComboBox<String> playerComboBox = new JComboBox<>(playerOptions);
        playerComboBox.setFont(new Font("Serif", Font.PLAIN, 36));
        playerComboBox.setForeground(Color.BLACK);
        playerComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerComboBox.setMaximumSize(new Dimension(50,50));
        this.add(playerComboBox);

        add(Box.createRigidArea(new Dimension(0, 50)));

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Serif", Font.PLAIN, 50));
        startButton.setBackground(Color.CYAN);
        startButton.setForeground(Color.BLACK);
        startButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(startButton);

        startButton.addActionListener(e -> {
            int players = Integer.parseInt((String) Objects.requireNonNull(playerComboBox.getSelectedItem()));
            startGame(players);
        });
    }

    private void startGame(int players) {
        frame.getContentPane().removeAll();
        Game game = new Game(boardWidth - 250, boardHeight, players);
        frame.add(game);
        frame.add(game.sidePanel, BorderLayout.EAST);
        frame.pack();
        game.requestFocus();
    }
}
