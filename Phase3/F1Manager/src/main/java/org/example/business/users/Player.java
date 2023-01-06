package org.example.business.users;

import org.example.data.PlayerDAO;
import org.example.exceptions.authentication.UsernameAlreadyExistsException;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.authentication.WrongPasswordException;

public class Player extends User{
    public Player(String username, String password) {
        super(username, password);
    }
    public Player(String username) {
        super(username);
    }


    public static Player register(String username, String password) {
        PlayerDAO pdb = PlayerDAO.getInstance();
        Player t = new Player(username);
        t.setPassword(password);
        return pdb.put(t);
    }

    /**
     * Logins a player
     * @param username
     * @param password
     * @return
     * @throws WrongPasswordException
     * @throws UsernameDoesNotExistException
     */
    public static Player login(String username, String password) throws WrongPasswordException, UsernameDoesNotExistException {
        PlayerDAO pdb = PlayerDAO.getInstance();
        Player pl = pdb.get(username);
        if (pl==null){
            throw new UsernameDoesNotExistException(username);
        }
        if (!Player.checkPassword(password,pl.getHashedPassword())){
            throw new WrongPasswordException();
        }
        return pl;
    }

    public Player clone(){
        return new Player(this.getUsername(),this.getHashedPassword());
    }
}
