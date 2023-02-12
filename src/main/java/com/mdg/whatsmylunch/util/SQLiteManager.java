package com.mdg.whatsmylunch.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mdg.whatsmylunch.domain.Restaurant;

public class SQLiteManager {
    public static Connection getConnection() {
        Connection connection;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:whatsmylunchdb.db");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    public static void createTableIfNotExists() {
        Connection connection = getConnection();
        try (connection) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "create table if not exists restaurant_list("
                    + "id INTEGER primary key autoincrement, "
                    + "name varchar(400))"
            );
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Restaurant> selectRestaurants() {
        Connection connection = getConnection();

        try (connection) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from restaurant_list");
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Restaurant> restaurants = new ArrayList<>();
            while (resultSet.next()) {
                restaurants.add(Restaurant.of(resultSet.getInt("id"), resultSet.getString("name")));
            }
            return restaurants;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertRestaurant(String name) {
        Connection connection = getConnection();

        try (connection) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into restaurant_list(name) values (?)");
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteRestaurant(int id) {
        Connection connection = getConnection();

        try (connection) {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from restaurant_list where id = ? ");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
