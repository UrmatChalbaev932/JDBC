package org.example.database.beans;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.dbutils.BeanProcessor;
import org.example.database.utils.DBConnection;

import java.beans.BeanDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActorBean {

    int actor_id;
    String first_name;
    String last_name;
    String last_update;

    /**
     * Получает список всех актеров из базы данных.
     * try (с параметрами) - это блок 'Try-With-Resources' или 'try-finally'.
     * @return: Список всех актеров
     * @throws SQLException: если произошла ошибка доступа к базе данных.
     */
    public static List<ActorBean> getAllActors() throws SQLException {
        String query = "SELECT * FROM actor";
        try (ResultSet resultSet = DBConnection.query(query)) {
            return new BeanProcessor().toBeanList(resultSet, ActorBean.class);
        }
    }
    /**
     * Получает результат параметризованного запроса.
     * @param column: переменная экземпляра
     * @param value: значение/парамметр
     * @return объектов, соответствующих заданному условию
     * @throws SQLException
     */

    public static ActorBean getBy(String column, String value) throws SQLException{
        String query = "SELECT * FROM actor WHERE " + column + " = ?;";
        ResultSet resultSet = DBConnection.query(query, value);
        if (!resultSet.next()) {
            return null;
        } else {
            return new BeanProcessor().toBean(resultSet, ActorBean.class);
        }
    }
    public static boolean createActor(String firstName, String lastName) throws SQLException {
        String query = "INSERT INTO actor (first_name, last_name, last_update) VALUES (?, ?, CURRENT_TIMESTAMP);";
        try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
