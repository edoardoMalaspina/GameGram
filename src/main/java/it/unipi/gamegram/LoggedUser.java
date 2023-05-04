package it.unipi.gamegram;

import it.unipi.gamegram.Entities.User;
public class LoggedUser {

    private static LoggedUser user = null;
    private User logged = null;
    private LoggedUser(){};

    public static LoggedUser getInstance(){
        if(user == null) {
            user = new LoggedUser();
        }
        return user;
    }

    public void setLoggedUser(String nick) {
        if(user == null) {
            throw new RuntimeException("No user instance.");
        } else {
            user.logged = new User(nick);
        }
    }

    public User getLoggedUser() {
        if(user == null) {
            throw new RuntimeException("No user instance.");
        } else {
            return user.logged;
        }
    }

}


