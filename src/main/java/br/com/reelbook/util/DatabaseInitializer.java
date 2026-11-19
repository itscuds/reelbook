
package br.com.reelbook.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        context.log(
                "[ReelBook] Iniciando a inicialização do banco de dados..."
        );

        try {
            executarSchema();

            context.log(
                    "[ReelBook] Estrutura do banco conferida. "
                            + "Iniciando sincronização do catálogo..."
            );

            CatalogoInicializador.inicializar();

            context.log(
                    "[ReelBook] Inicialização do banco e sincronização "
                            + "do catálogo concluídas."
            );

        } catch (Exception e) {
            context.log(
                    "[ReelBook] ERRO durante a inicialização "
                            + "do banco ou do catálogo.",
                    e
            );

            System.err.println(
                    "[ReelBook] ERRO durante a inicialização "
                            + "do banco ou do catálogo."
            );

            e.printStackTrace();
        }
    }

    private void executarSchema() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/schema.sql")) {

            if (in == null) {
                throw new IllegalStateException(
                        "schema.sql não encontrado no classpath "
                                + "(src/main/resources/schema.sql)."
                );
            }

            String script = new String(
                    in.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            try (
                    Connection connection = ConnectionFactory.getConnection();
                    Statement statement = connection.createStatement()
            ) {
                for (String sql : script.split(";")) {
                    if (!sql.isBlank()) {
                        statement.execute(sql);
                    }
                }
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        event.getServletContext().log(
                "[ReelBook] Contexto da aplicação encerrado."
        );
    }
}
