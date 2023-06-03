package it.unipi.gamegram.singletons;

// Singleton class to manage the transitions
public class UserSingleton {

    private static UserSingleton user = null;
    private static String nick;
    private static Boolean flag;

    private UserSingleton(String nick) {
        UserSingleton.nick = nick;
    }
    public static UserSingleton getInstance(String nick) {
        if (user == null) {
            user = new UserSingleton(nick);
        }
        return user;
    }

    public static String getNick() {
        return nick;
    }

    public static Boolean getFlag() {
        return flag;
    }
    public static void setFlag(Boolean b) {
        flag = b;
    }

    public static void setNull() {
        user = null;
    }
}
