package org.example.business;

public class Admin extends User{
    private boolean premium;

    public boolean isPremium() {return premium;}

    public void setPremium(boolean premium) {this.premium = premium;}

    public Admin(String username, String password, boolean premium) {
        super(username, password);
        this.premium=premium;
    }

    public Admin(String username,boolean premium) {
        super(username);
        this.premium=premium;
    }
}
