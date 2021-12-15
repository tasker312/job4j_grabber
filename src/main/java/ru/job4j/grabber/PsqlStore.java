package ru.job4j.grabber;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private final Connection connection;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            properties.load(in);
        }
        PsqlStore store = new PsqlStore(properties);
        Post post1 = new Post("Java developer", "http://127.0.0.1/",
                "Very good job", LocalDateTime.now());
        Post post2 = new Post("Senior Java developer", "http://127.0.0.2/",
                "Perfect job", LocalDateTime.now().plusDays(10));
        store.save(post1);
        store.save(post2);
        System.out.println("Post with id 1:");
        System.out.println(store.findById(1));
        System.out.println();
        System.out.println("All saved posts:");
        store.getAll().forEach(System.out::println);
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = connection.prepareStatement(
                "insert into post (name, text, link, created) values (?,?,?,?)")) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "select * from post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                posts.addAll(getPostsByResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "select * from post where id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                var posts = getPostsByResultSet(resultSet);
                if (!posts.isEmpty()) {
                    return posts.get(0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Post> getPostsByResultSet(ResultSet resultSet) throws SQLException {
        List<Post> posts = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String title = resultSet.getString(2);
            String description = resultSet.getString(3);
            String link = resultSet.getString(4);
            LocalDateTime created = resultSet.getTimestamp(5).toLocalDateTime();
            posts.add(new Post(id, title, link, description, created));
        }
        return posts;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
