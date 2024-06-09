package org.example;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5433/curvefever";
    private static final String USER = "postgres";
    private static final String PASSWORD = "kolos3421";

    public Database() {
        // Initialize the database connection and create the table if it doesn't exist
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            String createTable = "CREATE TABLE IF NOT EXISTS playerStats (" +
                    "playerid SERIAL PRIMARY KEY," +
                    "wins INTEGER DEFAULT 0)";
            statement.execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void resetDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            String clearTable = "DELETE FROM playerStats";
            statement.execute(clearTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWins(int playerId) {
        String updateQuery = "INSERT INTO playerStats(playerid, wins) VALUES(?, 1) " +
                "ON CONFLICT(playerid) DO UPDATE SET wins = playerStats.wins + 1";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, playerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getWins(int playerId) {
        String selectQuery = "SELECT wins FROM playerStats WHERE playerid = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("wins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}