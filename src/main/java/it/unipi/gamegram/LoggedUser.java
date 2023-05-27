package it.unipi.gamegram;

import it.unipi.gamegram.Entities.User;
public class LoggedUser {

    private static LoggedUser user = null;
    private static User logged = null;
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
            user.logged = new User(User.findByNick(nick));
        }
    }

    public static User getLoggedUser() {
        if(user == null) {
            throw new RuntimeException("No user instance.");
        } else {
            return user.logged;
        }
    }

    public static Boolean getIsAdmin() {
        Boolean isAdmin = false;
        if(user == null) {
            throw new RuntimeException("No user instance.");
        } else {
            if(user.logged.getIsAdmin().equals("Yes"))
                isAdmin = true;
            return isAdmin;
        }
    }

    public static void logOut() {
        if(user == null) {
            throw new RuntimeException("No user instance.");
        } else {
            logged = null;
            user = null;
        }
    }

}


