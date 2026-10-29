package br.com.reelbook.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try (InputStream in = getClass().getResourceAsStream("/schema.sql");
             Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {

            String script = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            for (String sql : script.split(";")) {
                if (!sql.isBlank()) {
                    statement.execute(sql);
                }
            }
            System.out.println("success in schema creation");

        } catch (Exception e) {
            System.out.println("fail in schema creation");
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
