package org.example.business.systems;

import org.example.business.users.Admin;
import org.example.business.users.Player;
import org.example.business.users.User;
import org.example.data.AdminDAO;
import org.example.data.PlayerDAO;
import org.example.exceptions.authentication.UsernameAlreadyExistsException;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.authentication.WrongPasswordException;

import static org.example.business.users.User.doesUserExists;

public class UserSystem implements UserSystemFacade {

    @Override
    public Player registerPlayer(String username, String password) throws UsernameAlreadyExistsException {
        if (User.doesUserExists(username)){
            throw new UsernameAlreadyExistsException();
        }
        else {
            return Player.register(username, password);
        }
    }

    @Override
    public Admin registerAdmin(String username, String password, boolean premium) throws UsernameAlreadyExistsException {
        if (User.doesUserExists(username)){
            throw new UsernameAlreadyExistsException();
        }
        else {
            return Admin.register(username, password, premium);
        }
    }

    @Override
    public Player loginPlayer(String username, String password) throws UsernameDoesNotExistException, WrongPasswordException {
        return Player.login(username, password);
    }

    @Override
    public Admin loginAdmin(String username, String password) throws UsernameDoesNotExistException, WrongPasswordException {
        return Admin.login(username, password);
    }

    @Override
    public boolean doesUsernameExist(String username) {
        return doesUserExists(username);
    }
}
