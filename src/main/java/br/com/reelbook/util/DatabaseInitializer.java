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
        try (
                InputStream in = getClass().getResourceAsStream("/schema.sql");
                Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement()
        ) {
            if (in == null) {
                throw new IllegalStateException(
                        "schema.sql não encontrado em src/main/resources"
                );
            }

            String script = new String(
                    in.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            for (String sql : script.split(";")) {
                if (!sql.isBlank()) {
                    statement.execute(sql);
                }
            }

            CatalogoInicializador.inicializar();

            System.out.println(
                    "Banco inicializado e catálogo demonstrativo conferido."
            );

        } catch (Exception e) {
            System.out.println("Falha ao inicializar o banco de dados.");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}