package com.tema1.Player;

import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsComparator;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.IllegalGoods;
import com.tema1.main.GameInput;
import java.util.ArrayList;

public final class GreedyPlayer extends Player {

    public GreedyPlayer(final String strategy) {
        super(strategy);
    }

    private boolean lessThan8Cards() {
        Constants constants = new Constants();
        return getBag().size() < constants.getMaxCardsInBag();
    }
    private void addIllegal() {
        if (isIllegal()) {
            GoodsComparator goodsComparator = new GoodsComparator();
            getCards().sort(goodsComparator);
            getBag().add(getCards().get(0));
        }
    }
    public boolean isIllegal() {
        Constants constants = new Constants();
        for (Goods goods : getCards()) {
            if (goods.getId() >= constants.getIllegalID()) {
                return true;
            }
        }
        return false;
    }

    public void greedyEvenPlaystyle() {
        if (lessThan8Cards()) {
            addIllegal();
        }
    }

    public void greedySherifPlaystyle(final ArrayList<Player> players, final GameInput gameInput,
                                      final GoodsFactory factory) {
        Constants constants = new Constants();
        for (Player player : players) {
            if (player.getRole() != 0 && player.getBribe() == 0) {
                if (getGold() < constants.getMinMoney()) {
                    setRole(1);
                    return;
                } else {
                    checkPlayer(player, gameInput, factory);
                }
            } else {
                notCheckingPlayer(player, constants, factory);
                player.getCards().removeAll(player.getCards());
                player.setBribe(0);
            }
        }
        setRole(1);
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
}

