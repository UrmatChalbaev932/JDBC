package org.example;

import org.example.database.utils.DBConnection;

import java.sql.SQLException;
import java.sql.Timestamp;

public class Main {
    public static void main(String[] args) throws SQLException {

            DBConnection.open("dvdrental");

            String[] columns = {"first_name", "last_name", "last_update"};
            Object[] values = {"John1", "Doe1", new Timestamp(System.currentTimeMillis())};

            boolean isInserted = DBConnection.insertInto("actor", columns, values);
            if (isInserted) {
                System.out.println("Actor inserted successfully!");
            } else {
                System.out.println("Actor insertion failed.");
            }

            DBConnection.close();
        }
}