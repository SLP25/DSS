package org.example.controllers;

import org.example.annotations.API;
import org.example.business.systems.UserSystemFacade;
import org.example.business.users.Admin;
import org.example.business.users.Player;
import org.example.exceptions.authentication.AuthException;
import org.example.exceptions.authentication.UsernameAlreadyExistsException;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.authentication.WrongPasswordException;
import org.example.views.UserView;
import org.example.annotations.Endpoint;

@API(model = "org.example.business.systems.UserSystem")
public class UserController extends Controller {

    public UserController(UserSystemFacade system) {
        super(system, new UserView());
    }

    @Override
    protected UserSystemFacade getModel() {
        return (UserSystemFacade)super.getModel();
    }

    @Override
    protected UserView getView() {
        return (UserView)super.getView();
    }

    /*
    * COMMAND SUMMARY:
    *
    * register (player|[premium] admin) <username> <password>
    * login (player|admin) <username> <password>
    * checkusername <username>
    *
    */

    @Endpoint(regex = "register player (\\S+) (.+)")
    public void registerPlayer(String username, String password)
    {
        try {
            Player p = getModel().registerPlayer(username, password);
            getView().registerSuccess(p);
        }
        catch (AuthException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "register (premium )?admin (\\S+) (.+)")
    public void registerAdmin(String _premium, String username, String password)
    {
        boolean premium = !_premium.isEmpty();

        try {
            Admin a = getModel().registerAdmin(username, password, premium);
            getView().registerSuccess(a);
        } catch (AuthException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "login player (\\S+) (.+)")
    public void loginPlayer(String username, String password)
    {
        try {
            Player p = getModel().loginPlayer(username, password);
            getView().loginSuccess(p);
        } catch (AuthException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "login admin (\\S+) (.+)")
    public void loginAdmin(String username, String password)
    {
        try {
            Admin a = getModel().loginAdmin(username, password);
            getView().loginSuccess(a);
        } catch (AuthException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "checkusername (\\S+)")
    public void checkUsername(String username)
    {
        boolean exists = getModel().doesUsernameExist(username);
        getView().checkUsername(username, exists);
    }
}
