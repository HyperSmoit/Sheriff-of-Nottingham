package com.tema1.main;
import com.tema1.Player.Player;
import com.tema1.goods.GoodsFactory;
import java.util.ArrayList;

abstract class Main {

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();

        GoodsFactory factory = GoodsFactory.getInstance();
        ArrayList<Player> players = new ArrayList<Player>();
        GameEngine game = new GameEngine();

        //Creating players and giving their roles for the first round
        game.addCards(gameInput, players);
        game.startGame(gameInput, players, factory);

    }
}
