package shared.model.commandmanager.game;

import org.json.JSONObject;
import server.IServerFacade;
import server.ServerTranslator;
import shared.model.commandmanager.BaseCommand;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alise on 9/18/2016.
 */
public class RegisterCommand extends BaseCommand {
    /**
     * name user chose
     */
    private String username;

    /**
     * password of new user
     */
    private String password;

    /**
     * Creates empty Command
     */
    public RegisterCommand() {
    }

    /**
     * Creates RegisterCommand to send to client.ClientFacade. Sets data members.
     * @param username
     * @param password
     */
    public RegisterCommand(String username, String password){
        this.username = username;
        this.password = password;
    }

    /**
     * makes it possible that the superclass can follow the correct cookie format
     * @return
     */
    @Override
    public JSONObject getCookieJSON() {
        return null;
    }


    /**
     * Tells server to add new user with given name and password
     */
    @Override
    public String serverExec(){

        String request = getRequest();
        JSONObject requestJSON = new JSONObject(request);
        username = (String) requestJSON.get("username");
        password = (String) requestJSON.get("password");


        int response = IServerFacade.getInstance().register(this);
        if(response >= 0) {
            String loginCookieJSON = "{\"name\":\""+username+"\",\"password\":\""+password+"\",\"playerID\":"+response+"}";//todo: figure out a way to get playerID
            String loginCookieStr = URLEncoder.encode(loginCookieJSON);
            String fullResponseLoginCookieStr = "catan.user="+loginCookieStr+";Path=/;";
            List<String> cookieList = new ArrayList<>(1);
            cookieList.add(fullResponseLoginCookieStr);
            getHttpExchange().getResponseHeaders().put("Set-cookie", cookieList);
            return ServerTranslator.getInstance().booleanToString(true);
        }
        return ServerTranslator.getInstance().booleanToString(false);
        //return ServerTranslator.getInstance().booleanToString(response);
    }

    @Override
    public void reExecute(){}

    //Getters

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
