package Controller;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    SocialMediaService socialMediaService;
    public SocialMediaController(){
        socialMediaService = new SocialMediaService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::postNewUserHandler); // 1
        app.post("/login", this::postUserLoginsHandler); // 2
        app.post("/messages", this::postMessagesHandler); // 3
        app.get("/messages", this::getAllMessagesHandler); // 4
        app.get("/messages/{message_id}", this::getMessageByIdHandler); // 5
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler); // 6
        app.patch("/messages/{message_id}", this::putUpdateMessageByIdHandler); // 7
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler); // 8
       
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    // creates new users 1
    private void postNewUserHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(ctx.body(), Account.class);
        Account addedUser = socialMediaService.addNewUser(user);
        if(addedUser == null)
            ctx.status(400);
        else
            ctx.json(addedUser);
    }

    // process user login 2
    private void postUserLoginsHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(ctx.body(), Account.class);
        Account userLogin = socialMediaService.userLogin(user);
        if(userLogin == null)
            ctx.status(401);
        else
            ctx.json(userLogin);
        
    }

    // posts messages 3
    private void postMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = socialMediaService.postMessage(message);
        if(newMessage == null)
            ctx.status(400);
        else
            ctx.json(newMessage);
    }

    // gets all messages 4
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        ctx.json(socialMediaService.getAllMessages()); // status code 200 default even when empty
    }
    
    // 5
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        Message updatedMessage = socialMediaService.getMessageById(Integer.parseInt(ctx.pathParam("message_id"))); 
        
        if(updatedMessage == null)
            ctx.status(200);
        else
            ctx.json(updatedMessage);
    }

    // 6
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException{
        Message deletedMessage = socialMediaService.deleteMessageById(Integer.parseInt(ctx.pathParam("message_id")));

        if(deletedMessage == null)
            ctx.status(200);
        else
            ctx.json(deletedMessage);
    }
    
    // 7
    private void putUpdateMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        
        Message updatedMsg = socialMediaService.updateMessageById(Integer.parseInt(ctx.pathParam("message_id")), message);

        if(updatedMsg != null)
            ctx.json(updatedMsg);
        else
            ctx.status(400);
    }

    // 8
    private void getAllMessagesByUserHandler(Context ctx) throws JsonProcessingException{
        ctx.json(socialMediaService.getAllMessagesByUser(Integer.parseInt(ctx.pathParam("account_id"))));
    }

}