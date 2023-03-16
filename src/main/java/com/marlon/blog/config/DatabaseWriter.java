package com.marlon.blog.config;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseWriter {
    private final DataSource dataSource;

    public DatabaseWriter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void writeData() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            // Insert data into authority table
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO public.authority(name) VALUES (?), (?)")) {
                statement.setString(1, "ROLE_GUEST");
                statement.setString(2, "ROLE_ADMIN");
                statement.executeUpdate();
            }

            // Insert data into user table
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO public.user(username, email, password, role) VALUES (?, ?, ?, ?)",
                    new String[]{"id"})) {
                statement.setString(1, "Marlon");
                statement.setString(2, "mamedee001@gmail.com");
                statement.setString(3, "$2a$12$YfNxYtZ4U4fQBze5F2ioJuoHadoYz5vm/diX1MmPP1ggomJPp8XWy");
                statement.setString(4, "ROLE_ADMIN");
                statement.executeUpdate();
            }

            // Insert data into post table
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO public.post(title, content, user_id) VALUES (?, ?, ?)")) {
                statement.setString(1, "Something else Ipsum");
                statement.setString(2,
                        "Lorem ipsum dolor sit amet...");
                // 1 is the actual id of the user you want to associate with this post
                statement.setInt(3, 1);
                statement.executeUpdate();
            }
        }
    }
}