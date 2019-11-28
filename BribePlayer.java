package com.tema1.Player;
import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsComparator;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.IllegalGoods;
import com.tema1.main.GameInput;

import java.util.ArrayList;

public final class BribePlayer extends Player {
    private ArrayList<Player> neighbors = new ArrayList<>();
    public BribePlayer(final String strategy) {
        super(strategy);
    }

    public ArrayList<Player> getNeighbors() {
        return neighbors;
    }

    public void bribePlayStyle() {
        Constants constants = new Constants();
        boolean illegal = false;
        int penalty = 0;
        int requiredBribe = 0;
        int illegalCounter = 0;
        for (Goods goods : getCards()) {
            if (goods.getId() >= constants.getIllegalID()) {
                illegal = true;
                break;
            }
        }
        if (!illegal || getGold() < constants.getSumOfGoldPlayable()) {
            traderPlaystyle();
        } else {
            GoodsComparator goodsComparator = new GoodsComparator();
            getCards().sort(goodsComparator);
            int[] vector = new int[constants.getNumberOfCardsinDeck()];
            for (int i = 0; i < getCards().size(); ++i) {
                vector[i] = getCards().get(i).getId();
            }
            for (int i = 0; i < getCards().size() - 1; ++i) {
                for (int j = i + 1; j < getCards().size(); ++j) {
                    if (findID(vector[i]).getProfit() == findID(vector[j]).getProfit()
                            && vector[i] < vector[j]) {
                        int aux = vector[i];
                        vector[i] = vector[j];
                        vector[j] = aux;
                    }
                }
            }
            for (int i = 0; i < getCards().size(); ++i) {
                if (getBag().size() < constants.getMaxCardsInBag()) {
                    getBag().add(findID(vector[i]));
                    penalty += findID(vector[i]).getPenalty();
                    if (findID(vector[i]).getId() >= constants.getIllegalID()) {
                        illegalCounter++;
                    }
                    if (illegalCounter <= constants.getMinBribeCards()) {
                        requiredBribe = constants.getLowBribe();
                    } else {
                        requiredBribe = constants.getHighBribe();
                    }
                    if (getGold() <= maxOf(requiredBribe, penalty)) {
                        if (getBag().get(getBag().size() - 1).getId() >= constants.getIllegalID()) {
                            illegalCounter--;
                        }
                        getBag().remove(getBag().size() - 1);
                        penalty -= findID(vector[i]).getPenalty();
                        if (illegalCounter <= 2) {
                            requiredBribe = constants.getLowBribe();
                        } else {
                            requiredBribe = constants.getHighBribe();
                        }
                    }
                }
            }
            setBribe(requiredBribe);
            setType(0);
        }
    }

    public void addneighbors(final ArrayList<Player> players) {
        Constants constants = new Constants();
        if (players.size() > constants.getThreePlayers() && neighbors.size() == 0) {
            if (this.getPlayerID() == 0) {
                neighbors.add(0, players.get(players.size() - 1));
                neighbors.add(1, players.get(1));
            } else if (this.getPlayerID() == players.size() - 1) {
                neighbors.add(0, players.get(0));
                neighbors.add(1, players.get(this.getPlayerID() - 1));
            } else {
                neighbors.add(0, players.get(this.getPlayerID() - 1));
                neighbors.add(1, players.get(this.getPlayerID() + 1));
            }
        } else if (players.size() < constants.getThreePlayers() && neighbors.size() == 0) {
            if (this.getPlayerID() == 0) {
                neighbors.add(0, players.get(1));
            } else if (this.getPlayerID() == players.size() - 1) {
                neighbors.add(0, players.get(0));
            }
        }
    }

    private int maxOf(final int firstInteger, final int secondInteger) {
        return Math.max(firstInteger, secondInteger);
    }

    public void bribeSherifPlaystyle(final ArrayList<Player> players, final
    GameInput gameInput, final GoodsFactory factory) {
        Constants constants = new Constants();
        for (Player player : neighbors) {
            checkPlayer(player, gameInput, factory);
            player.getCards().removeAll(player.getCards());
            player.getBag().removeAll(player.getBag());
        }
        for (Player player : players) {
            if (player.getRole() != 0) {
                if (players.size() <= constants.getThreePlayers()) {
                    checkPlayer(player, gameInput, factory);
                } else {
                    if (isNeighbor(player)) {
                        checkPlayer(player, gameInput, factory);
                    } else {
                        if (player.getBribe() != 0) {
                            this.setGold(this.getGold() + player.getBribe());
                            player.setGold(player.getGold() - player.getBribe());
                            player.setBribe(0);
                        }
                        notCheckingPlayer(player, constants, factory);
                    }
                }
            }
        }
    }

    public void notCheckingPlayer(final Player player, final Constants constants, final
    GoodsFactory factory) {
        setGold(getGold() + player.getBribe());
        player.setGold(player.getGold() - player.getBribe());
        if (!player.getBag().isEmpty()) {
            for (Goods goods : player.getBag()) {
                if (goods.getId() <= constants.getLegalID()) {
                    player.getBonus()[goods.getId()]++;
                } else {
                    for (Goods illgoods : ((IllegalGoods) factory.
                            getGoodsById(goods.getId())).
                            getIllegalBonus().keySet()) {
                        player.setProfit(player.getProfit() + illgoods.getProfit()
                                * ((IllegalGoods) factory.
                                getGoodsById(goods.getId())).getIllegalBonus().
                                get(illgoods));
                        player.getBonus()[illgoods.getId()] += ((IllegalGoods) factory.
                                getGoodsById(goods.getId())).getIllegalBonus().
                                get(illgoods);
                    }
                }
                player.setProfit(player.getProfit() + goods.getProfit());
            }
        }
    }

    private boolean isNeighbor(final Player player) {
        for (Player player1 : neighbors) {
            if (player.getPlayerID() == player1.getPlayerID()) {
                return true;
            }
        }
        return false;
    }
}

