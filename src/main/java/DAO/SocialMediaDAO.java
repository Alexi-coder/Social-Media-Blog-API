package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocialMediaDAO {

    // returns null if no account found
    // grabs account by username
    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM Account WHERE username=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next())
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    // grabs message_id (posted_by) user
    public Message getMessageByUserId(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Message WHERE posted_by=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next())
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
       
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;

    }



    // 1
    public Account postNewUser(Account user) {
        Connection connection = ConnectionUtil.getConnection();

            try{
                String sql = "INSERT INTO Account (username, password) VALUES (?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());

                preparedStatement.executeUpdate();

                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if(pkeyResultSet.next()){
                    int generated_account_id = (int) pkeyResultSet.getLong(1);
                    return new Account(generated_account_id, user.getUsername(), user.getPassword());
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }

            return null;
    }

    // 2
    public Account postUserLogins(Account user) {
        Connection connection = ConnectionUtil.getConnection();

            try{
                String sql = "SELECT * FROM Account WHERE username=? AND password=?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());

                ResultSet rs = preparedStatement.executeQuery();
                
                if(rs.next()) 
                    return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }

            return null;
    }

    // 3
    public Message postMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try{

        String sql = "INSERT INTO  Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());

        
        preparedStatement.executeUpdate();

        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
        if(pkeyResultSet.next()){
            int generated_message_id = (int) pkeyResultSet.getLong(1);
            return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }


    // 4
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }    

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        
        return messages;
    }

    // 5
    public Message getMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
            try{
                String sql = "SELECT * FROM Message WHERE message_id=?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);

                ResultSet rs = preparedStatement.executeQuery();

                while(rs.next())
                    return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
            return null;
    } 

    // 6
    public void deleteMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
            try{
                String sql = "DELETE FROM Message WHERE message_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
    
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
    }


    // 7
    public void updateMessageById(int id, Message message) {
        Connection connection = ConnectionUtil.getConnection();

            try{
                String sql = "UPDATE Message set message_text=? where message_id=?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, message.getMessage_text());
                preparedStatement.setInt(2, id);
    
                preparedStatement.executeUpdate();                
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
    }

    // 8
    public List<Message> getAllMessagesByUser(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
            try{
                String sql = "SELECT * FROM Message where posted_by=?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, account_id);

                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    Message newMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                    messages.add(newMessage);
                }

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }

            return messages;
    }
}
