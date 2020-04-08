package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Connection conn = null;
        Scanner scanner = new Scanner(System.in);
        try {
            String url = "jdbc:mysql://localhost:3306/chat_program?user=root";
            conn = DriverManager.getConnection(url);
            System.out.println("connected");
            Statement stmt = null;
            String command = "";

            //todo: create new user in DB
            int successful = 0;
            while (successful != 1) {
                String name = scanner.nextLine();
                command = "INSERT INTO `user`(`user_name`) VALUES ('" + name + "')";
                try {
                    stmt = conn.createStatement();
                    successful = stmt.executeUpdate(command);
                } catch (SQLException ex) {
                    System.out.println("The chosen name ist not valid or already in use. Please try again with another name");;
                }
            }


            //todo: login as user
            //todo: create directory
            //todo: send message to other user from your directory
            //todo: receive messages
        } catch (SQLException ex){
            throw new Error("Problem", ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }


    }
}
