package org.example.business.systems;

import org.example.business.users.Admin;
import org.example.business.users.Player;
import org.example.exceptions.authentication.UsernameAlreadyExistsException;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.authentication.WrongPasswordException;

public interface UserSystemFacade extends SystemFacade {

    Player registerPlayer(String username, String password) throws UsernameAlreadyExistsException;

    Admin registerAdmin(String username, String password, boolean premium) throws UsernameAlreadyExistsException;

    Player loginPlayer(String username, String password) throws UsernameDoesNotExistException, WrongPasswordException;

    Admin loginAdmin(String username, String password) throws UsernameDoesNotExistException, WrongPasswordException;

    boolean doesUsernameExist(String username);
}
