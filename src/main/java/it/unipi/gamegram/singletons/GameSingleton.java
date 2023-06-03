package it.unipi.gamegram.singletons;

// Singleton class to manage the transitions
public class GameSingleton {

    private static GameSingleton game = null;
    private static String name;

    private GameSingleton(String name) {
        GameSingleton.name = name;
    }
    public static GameSingleton getInstance(String name) {
        if (game == null) {
            game = new GameSingleton(name);
        }
        return game;
    }

    public static String getName() {
        return name;
    }

    public static void setNull() {
        game = null;
    }
}