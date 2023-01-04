package org.example.business.users;

import org.example.data.AdminDAO;
import org.example.exceptions.authentication.UsernameAlreadyExistsException;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.authentication.WrongPasswordException;

public class Admin extends User{
    private boolean is_Premium;

    public boolean getPremium() {return is_Premium;}

    public void setPremium(boolean premium) {this.is_Premium = premium;}

    public Admin(String username, String password, boolean premium) {
        super(username, password);
        this.is_Premium=premium;
    }

    public Admin(String username,boolean premium) {
        super(username);
        this.is_Premium=premium;
    }

    public static Admin register(String username,String password,boolean is_premium) {
        AdminDAO adb = AdminDAO.getInstance();
        Admin t = new Admin(username,is_premium);
        t.setPassword(password);
        return adb.put(t);
    }
    public static Admin login(String username,String password) throws WrongPasswordException, UsernameDoesNotExistException {
        AdminDAO adb = AdminDAO.getInstance();
        Admin ad = adb.get(username);
        if (ad==null){
            throw new UsernameDoesNotExistException();
        }
        if (!Admin.checkPassword(password,ad.getHashedPassword())){
            throw new WrongPasswordException();
        }
        return ad;
    }

}
