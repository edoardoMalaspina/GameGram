package it.unipi.gamegram;

import it.unipi.gamegram.Entities.User;

public class UserSingleton {

    private static UserSingleton user = null;
    private static String nick;

    private UserSingleton(String nick){
        this.nick = nick;
    }
    public static UserSingleton getInstance(String nick){
        if(user == null) {
            user = new UserSingleton(nick);
        }
        return user;
    }

    public static String getNick() {
        return nick;
    }

    public static void setNull(){
        user = null;
    }
}
