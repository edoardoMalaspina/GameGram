package it.unipi.gamegram.entities;

import it.unipi.gamegram.managersMongoDB.UserManagerMongoDB;

public class LoggedUser {

    private static LoggedUser user = null;
    private static User logged = null;
    private LoggedUser() {}

    public static LoggedUser getInstance() {
        if (user == null) {
            user = new LoggedUser();
        }
        return user;
    }

    public void setLoggedUser(String nick) {
        if (user == null) {
            throw new RuntimeException("No user instance.");
        } else {
            logged = new User(UserManagerMongoDB.findUserByNick(nick));
        }
    }

    public static User getLoggedUser() {
        if (user == null) {
            throw new RuntimeException("No user instance.");
        } else {
            return logged;
        }
    }

    public static Boolean getIsAdmin() {
        boolean isAdmin = false;
        if (user == null) {
            throw new RuntimeException("No user instance.");
        } else {
            if (logged.getIsAdmin().equals("Yes"))
                isAdmin = true;
            return isAdmin;
        }
    }

    public static void logOut() {
        if (user == null) {
            throw new RuntimeException("No user instance.");
        } else {
            logged = null;
            user = null;
        }
    }

}