package com.tema1.main;

import java.util.ArrayList;

import com.tema1.Player.Player;
import com.tema1.Player.BribePlayer;
import com.tema1.Player.BasePlayer;
import com.tema1.Player.GreedyPlayer;
import com.tema1.goods.GoodsFactory;
import com.tema1.Player.PlayerComparator;
import com.tema1.goods.LegalGoods;
import com.tema1.common.Constants;


class GameEngine {
    void addCards(final GameInput gameInput, final ArrayList<Player> players) {
        for (int i = 0; i < gameInput.getPlayerNames().size(); ++i) {
            //checking whether it's Base, Bribe or Greedy Player
            if (gameInput.getPlayerNames().get(i).equals("basic")) {
                BasePlayer p = new BasePlayer(gameInput.getPlayerNames().get(i));
                p.setRole(1);
                p.setPlayerID(i);
                players.add(p);

            }
            if (gameInput.getPlayerNames().get(i).equals("bribed")) {
                BribePlayer p = new BribePlayer(gameInput.getPlayerNames().get(i));
                p.setRole(1);
                p.setPlayerID(i);
                players.add(p);
            }
            if (gameInput.getPlayerNames().get(i).equals("greedy")) {
                GreedyPlayer p = new GreedyPlayer(gameInput.getPlayerNames().get(i));
                p.setRole(1);
                p.setPlayerID(i);
                players.add(p);
            }
        }

    }

    private void kingQuennBonus(final ArrayList<Player> players,
                                final GoodsFactory factory) {
        Constants constants = new Constants();
        for (int k = 0; k < constants.getNumberOfCardsinDeck(); ++k) {
            int kingBonus = 0;
            int queenBonus = 0;
            int indexOfKing = -1;
            int indexOfQueen = -1;
            int kingID = 0;
            int queenID = 0;
            for (Player player : players) {
                if (player.getBonus()[k] != 0) {
                    if (player.getBonus()[k] > kingBonus) {
                        queenBonus = kingBonus;
                        kingBonus = player.getBonus()[k];
                        indexOfQueen = indexOfKing;
                        indexOfKing = players.indexOf(player);
                        queenID = kingID;
                        kingID = k;
                    } else if (player.getBonus()[k] <= kingBonus && player.getBonus()[k]
                            > queenBonus) {
                        queenBonus = player.getBonus()[k];
                        indexOfQueen = players.indexOf(player);
                        queenID = k;
                    }
                }
            }
            if (indexOfKing >= 0) {
                LegalGoods king = (LegalGoods) factory.getGoodsById(kingID);
                players.get(indexOfKing).setGold(players.get(indexOfKing).getGold()
                        + king.getKingBonus());
            }
            if (indexOfQueen >= 0) {
                LegalGoods queen = (LegalGoods) factory.getGoodsById(queenID);
                players.get(indexOfQueen).setGold(players.get(indexOfQueen).getGold()
                        + queen.getQueenBonus());
            }
        }
    }

    void splitCardsEachSubround(final GameInput gameInput, final  ArrayList<Player> players, final
                                GoodsFactory factory) {
        Constants constants = new Constants();
        for (Player player : players) {
            if (player.getRole() != 0) {
                for (int i = 0; i < constants.getNumberOfCardsinDeck(); ++i) {
                    player.getCards().add(factory.getGoodsById(gameInput.getAssetIds().get(0)));
                    gameInput.getAssetIds().remove(0);
                }
            }
        }
    }

    void startGame(final GameInput gameInput, final ArrayList<Player> players,
                   final GoodsFactory factory) {
        //trader role - 1
        //sherif role - 0
        for (int i = 0; i < gameInput.getRounds(); ++i) {
            for (int j = 0; j < players.size(); ++j) {
                players.get(j).setRole(0);
                splitCardsEachSubround(gameInput, players, factory);
                ArrayList<Player> sherif = new ArrayList<>();
                for (Player p : players) {
                    if (p instanceof BasePlayer) {
                        if (p.getRole() == 1) {
                            p.traderPlaystyle();
                        } else {
                            sherif.add(0, p);
                        }
                    } else if (p instanceof GreedyPlayer) {
                        if (p.getRole() == 1) {
                            p.traderPlaystyle();
                            if ((i + 1) % 2 == 0 && ((GreedyPlayer) p).isIllegal()) {
                                ((GreedyPlayer) p).greedyEvenPlaystyle();
                            }
                        } else {
                            sherif.add(0, p);
                        }
                    } else if (p instanceof BribePlayer) {
                        if (p.getRole() == 1) {
                            ((BribePlayer) p).bribePlayStyle();
                        } else {
                            sherif.add(0, p);
                        }
                    }
                }
                if (sherif.get(0) instanceof BasePlayer) {
                    sherif.get(0).sherifPlaystyle(players, gameInput, factory);
                }
                if (sherif.get(0) instanceof GreedyPlayer) {
                    ((GreedyPlayer) sherif.get(0)).greedySherifPlaystyle(players, gameInput,
                            factory);
                }
                if (sherif.get(0) instanceof BribePlayer) {
                    ((BribePlayer) sherif.get(0)).addneighbors(players);
                    ((BribePlayer) sherif.get(0)).bribeSherifPlaystyle(players, gameInput, factory);
                }
                for (Player player : players) {
                    player.getCards().removeAll(player.getCards());
                    player.getBag().removeAll(player.getBag());
                    player.setBribe(0);
                }
                players.get(j).setRole(1);
            }
        }

        for (Player player : players) {
            player.setGold(player.getGold() + player.getProfit());
            player.setProfit(0);
        }
        kingQuennBonus(players, factory);
        display(players);
    }

    private void display(final ArrayList<Player> players) {
        PlayerComparator playersComparator = new PlayerComparator();
        players.sort(playersComparator);
        for (Player player : players) {
            System.out.println(player.getPlayerID() + " " + player.getStrategy().toUpperCase()
                    + " " + player.getGold());
        }
    }
}
