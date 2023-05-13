package it.unipi.gamegram;

public class GameSingleton {

    private static GameSingleton game = null;
    private static String name;

    private GameSingleton(String name){
        this.name  = name;
    }
    public static GameSingleton getInstance(String nick){
        if(game == null) {
            game = new GameSingleton(name);
        }
        return game;
    }

    public static String getName() {
        return name;
    }

    public static void setNull(){
        game = null;
    }
}
