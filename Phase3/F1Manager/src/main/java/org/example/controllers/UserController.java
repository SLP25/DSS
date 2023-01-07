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
     * user <username> register (player|[premium] admin) <password>
     * user <username> login (player|admin) <password>
     * user <username> check
     *
     */

    @Endpoint(regex = "user (\\S+) register player (.+)")
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

    @Endpoint(regex = "user (\\S+) register (premium )?admin (.+)")
    public void registerAdmin(String username,String _premium, String password)
    {
        boolean premium = !_premium.isEmpty();

        try {
            Admin a = getModel().registerAdmin(username, password, premium);
            getView().registerSuccess(a);
        } catch (AuthException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "user (\\S+) login player (.+)")
    public void loginPlayer(String username, String password)
    {
        try {
            Player p = getModel().loginPlayer(username, password);
            getView().loginSuccess(p);
        } catch (AuthException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "user (\\S+) login admin (.+)")
    public void loginAdmin(String username, String password)
    {
        try {
            Admin a = getModel().loginAdmin(username, password);
            getView().loginSuccess(a);
        } catch (AuthException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "user (\\S+) check")
    public void checkUsername(String username)
    {
        boolean exists = getModel().doesUsernameExist(username);
        getView().checkUsername(username, exists);
    }
}
