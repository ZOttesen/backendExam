package rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.UserDTO;
import entities.User;
import errorhandling.API_Exception;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;


@Path("user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);
    private final Gson GSON = new Gson();




    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonString) throws API_Exception {
        String username, password,email,status;
        int phone;
        List<String> roles = new ArrayList<>();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();
            phone = jsonObject.get("phone").getAsInt();
            email = jsonObject.get("email").getAsString();
            status = jsonObject.get("status").getAsString();
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        UserDTO user = new UserDTO(USER_FACADE.createUser(username,password,phone,email,status));
        String userJSON = GSON.toJson(user);
        System.out.println(userJSON);
        return Response.ok(userJSON).build();
    }

}
