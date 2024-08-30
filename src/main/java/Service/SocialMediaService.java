package Service;

import Model.Account;
import Model.Message;

import java.util.List;

import DAO.SocialMediaDAO;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;    
    
    // Constructor
    public SocialMediaService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    // Constructor with param 
    public SocialMediaService(SocialMediaDAO socialMediaDAO){
        this.socialMediaDAO = socialMediaDAO;
    }

    // 1
    public Account addNewUser(Account user){
        if(socialMediaDAO.getAccountByUsername(user.getUsername())!=null || user.getUsername().length() == 0 || user.getPassword().length() < 4)
            return null;

        return socialMediaDAO.postNewUser(user);
    }

    // 2 true if username + password is equal to acquired credentials 
    public  Account userLogin(Account user){
        return socialMediaDAO.postUserLogins(user);
    }

    // 3
    public Message postMessage(Message message){
        if(message.getMessage_text().length() <= 0 || message.getMessage_text().length() > 255  || socialMediaDAO.getMessageByUserId(message.getPosted_by()) == null)
            return null;

        return socialMediaDAO.postMessage(message);
    }

    // 4
    public List<Message> getAllMessages(){
        return socialMediaDAO.getAllMessages();
    }

    // 5
    public Message getMessageById(int id){
        return socialMediaDAO.getMessageById(id);
    }

    // 6
    public Message deleteMessageById(int id){
        Message msg = socialMediaDAO.getMessageById(id);
        if(msg!=null){
            socialMediaDAO.deleteMessageById(id);
            return msg;
        }
    
        return null;            
    }

    // 7
    public Message updateMessageById(int id, Message message){
        if(socialMediaDAO.getMessageById(id) != null && message.getMessage_text().length() <= 255 && message.getMessage_text().length() > 0){
            socialMediaDAO.updateMessageById(id, message);
            return socialMediaDAO.getMessageById(id);
        }
        else
            return null;
    }

    // 8
    public List<Message> getAllMessagesByUser(int account_id){
        return socialMediaDAO.getAllMessagesByUser(account_id);
    }
}
