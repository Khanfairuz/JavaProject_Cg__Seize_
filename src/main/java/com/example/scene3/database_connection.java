package com.example.scene3;

import java.sql.*;


public class database_connection {



  public void connection(String tableName , HelloController scene) {
    //String sql = "select question from objectorientedprogramming where index=2";
    String sql = "SELECT question, optiona, optionb, optionc, optiond, correctans FROM " + tableName + " WHERE index = ?";
    String url = "jdbc:postgresql://localhost:5432/question_and_answer_2";
    String username = "postgres";
    String password = "1234";

    try (Connection connection = DriverManager.getConnection(url, username, password);
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      int randomIndex = (int) (Math.random() * 5) + 1; // Generate random index from 1 to 5
      preparedStatement.setInt(1, randomIndex);


      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          String question = rs.getString("question");
          String optionA = rs.getString("optiona");
          String optionB = rs.getString("optionb");
          String optionC = rs.getString("optionc");
          String optionD = rs.getString("optiond");
          String correctAns = rs.getString("correctans");

         /* System.out.println("Question: " + question);
          System.out.println("Option A: " + optionA);
          System.out.println("Option B: " + optionB);
          System.out.println("Option C: " + optionC);
          System.out.println("Option D: " + optionD);
          System.out.println("Correct Answer: " + correctAns);*/

          //setting values
          scene.setQuestion(question);
          scene.setOptionA(optionA);
          scene.setOptionB(optionB);
          scene.setOptionC(optionC);
          scene.setOptionD(optionD);
          scene.setCorrectAns(correctAns);
        } else {
          System.out.println("No question found.");
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void connection2(String tableName , HelloController2 scene) {
    //String sql = "select question from objectorientedprogramming where index=2";
    String sql = "SELECT question, optiona, optionb, optionc, optiond, correctans FROM " + tableName + " WHERE index = ?";
    String url = "jdbc:postgresql://localhost:5432/question_and_answer_2";
    String username = "postgres";
    String password = "1234";

    try (Connection connection = DriverManager.getConnection(url, username, password);
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      int randomIndex = (int) (Math.random() * 5) + 1; // Generate random index from 1 to 5
      preparedStatement.setInt(1, randomIndex);


      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          String question = rs.getString("question");
          String optionA = rs.getString("optiona");
          String optionB = rs.getString("optionb");
          String optionC = rs.getString("optionc");
          String optionD = rs.getString("optiond");
          String correctAns = rs.getString("correctans");

         /* System.out.println("Question: " + question);
          System.out.println("Option A: " + optionA);
          System.out.println("Option B: " + optionB);
          System.out.println("Option C: " + optionC);
          System.out.println("Option D: " + optionD);
          System.out.println("Correct Answer: " + correctAns);*/

          //setting values
          scene.setQuestion(question);
          scene.setOptionA(optionA);
          scene.setOptionB(optionB);
          scene.setOptionC(optionC);
          scene.setOptionD(optionD);
          scene.setCorrectAns(correctAns);
        } else {
          System.out.println("No question found.");
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
