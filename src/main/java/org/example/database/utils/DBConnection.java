package org.example.database.utils;

import org.example.сonfigReader.ConfigReader;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
    /**
     * Singleton
     */
    private static Connection connection; // Connection – отвечает за соединение с базой данных
    private static Statement statement; // Statement – отвечает за запрос к базе данных

    /**
     * Получает базовый источник данных (все учетные данные) для подключения к базе данных.
     *
     * @param database: имя базы данных
     * @return: базовый источник данных
     */
    public static PGSimpleDataSource getBaseDataSource(String dataBase) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource() {{
            setServerName(ConfigReader.getValues("server"));
            setPortNumber(Integer.parseInt(ConfigReader.getValues("port")));
            setUser(ConfigReader.getValues("user"));
            setPassword(ConfigReader.getValues("sql_password"));
            setDatabaseName(dataBase);
        }};
        return dataSource;
    }

    /**
     * Подключение к базе данных.
     *
     * @param database: имя базы данных
     */
    public static void open(String dataBase) throws SQLException {
        if (connection == null) {
            connection = getBaseDataSource(dataBase).getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //ResultSet.TYPE_SCROLL_INSENSITIVE: Курсор позволяет перемещаться вперед и назад по результатам запроса
            // (прокручиваемый курсор), но не чувствителен к изменениям, сделанным в базе данных другими транзакциями
            // после получения данных. Это означает, что любые изменения данных, сделанные другими пользователями,
            //не будут отображены в текущем наборе результатов.

            //ResultSet.CONCUR_READ_ONLY: Результирующий набор данных доступен только для чтения.
            // Вы не можете изменять данные в базе через этот объект ResultSet.
        }
    }

    /**
     * Создает и выполняет заданный SQL-запрос.
     *
     * @param query:  оператор (без параметров)
     * @param params: необязательные параметры запроса для различных условий
     * @return: объект ResultSet, содержащий данные, полученные в результате выполнения запроса; никогда не null
     */
    public static ResultSet query(String query, Object... params) throws SQLException {
        if (params.length == 0) {
            return statement.executeQuery(query);
        } else {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeQuery();
        }
    }

    public static Connection getConnection() {
        return connection;
    }


    public static boolean insertInto(String tableName, String[] columns, Object[] values) throws SQLException {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Number of columns and values must match.");
        }

        StringBuilder columnNames = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (int i = 0; i < columns.length; i++) {
            columnNames.append(columns[i]);
            placeholders.append("?");
            if (i < columns.length - 1) {
                columnNames.append(", ");
                placeholders.append(", ");
            }
        }

        String query = String.format("INSERT INTO %s (%s) VALUES (%s);", tableName, columnNames, placeholders);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }



    /**
     * Отключается от базы данных.
     */

    public static void close() {
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
