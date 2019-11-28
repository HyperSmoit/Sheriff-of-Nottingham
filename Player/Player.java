package com.tema1.Player;
import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsComparator;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.IllegalGoods;
import com.tema1.main.GameInput;
import java.util.ArrayList;
import java.util.Objects;
public class Player {
    private Constants constants = new Constants();
    private String strategy;
    private int gold;
    private int role;
    private ArrayList<Goods> cards;
    private int profit;
    private ArrayList<Goods> bag;
    private int type;
    private boolean checked = false;
    private int[] bonus = new int[constants.getNumberOfCardsinDeck()];
    private int bribe = 0;
    private int playerID;

    public Player(final String strategy) {
        this.strategy = strategy;
        this.gold = constants.getStartingMoney();
        this.cards = null;
        this.role = 1;
        this.cards = new ArrayList<>();
        this.bag = new ArrayList<>();
    }

    public final int getPlayerID() {
        return playerID;
    }

    public final void setPlayerID(final int player2ID) {
        playerID = player2ID;
    }

    final int getBribe() {
        return bribe;
    }

    public final void setChecked() {
        this.checked = true;
    }

    public final int[] getBonus() {
        return bonus;
    }

    private int getType() {
        return type;
    }

    final void setType(final int type) {
        this.type = type;
    }

    public final String getStrategy() {
        return strategy;
    }

    public final int getGold() {
        return gold;
    }

    public final ArrayList<Goods> getBag() {
        return bag;
    }

    public final void setGold(final int gold) {
        this.gold = gold;
    }

    public final int getRole() {
        return role;
    }

    public final void setRole(final int role) {
        this.role = role;
    }

    public final ArrayList<Goods> getCards() {
        return cards;
    }

    public final int getProfit() {
        return profit;
    }

    public final void setProfit(final int profit) {
        this.profit = profit;
    }

    final Goods findID(final int goodID) {
        for (Goods g: cards) {
            if (g.getId() == goodID) {
                return g;
            }
        }
        return null;
    }


    public final void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    //Playstyle for Basic Player
    private void highestFrequency() {
        int maxApp = -1;
        int indexOf = 0;
        boolean singleMax = true;
        int[] freq = new int[constants.getNumberOfCardsinDeck()];
        for (Goods g : getCards()) {
            if (g.getId() <= constants.getLegalID()) {
                freq[g.getId()]++;
            }
        }
        for (int i = 0; i < constants.getNumberOfCardsinDeck(); ++i) {
            if (freq[i] > maxApp) {
                maxApp = freq[i];
                singleMax = true;
                indexOf = i;
            } else if (freq[i] == maxApp) {
                singleMax = false;
            }
        }
        if (singleMax) {
            for (int i = 0; i < maxApp; ++i) {
                if (getBag().size() < constants.getMaxCardsInBag()) {
                    getBag().add(findID(indexOf));
                    getCards().remove(findID(indexOf));
                    setType(indexOf);
                }
            }
        } else {
            for (int i = 0; i < constants.getNumberOfCardsinDeck(); ++i) {
                if (maxApp != freq[i]) {
                    freq[i] = 0;
                }
            }
            int maxProfit = 0;
            int currentProfit;
            singleMax = true;
            indexOf = 0;
            for (int i = 0; i < constants.getNumberOfCardsinDeck(); ++i) {
                currentProfit = 0;
                if (freq[i] != 0) {
                    currentProfit = findID(i).getProfit();
                }
                if (currentProfit > maxProfit) {
                    maxProfit = currentProfit;
                    singleMax = true;
                    indexOf = i;
                } else if (currentProfit == maxProfit) {
                    singleMax = false;
                }
            }
            if (singleMax) {
                for (int i = 0; i < maxApp; ++i) {
                    if (getBag().size() < constants.getMaxCardsInBag()) {
                        getBag().add(findID(indexOf));
                        getCards().remove(findID(indexOf));
                        setType(indexOf);
                    }
                }
            } else {
                int highestID = 0;
                for (int i = 0; i < constants.getNumberOfCardsinDeck(); ++i) {
                    if (freq[i] != 0 && findID(i).getId() > highestID && findID(i).getProfit()
                            == maxProfit) {
                        highestID = findID(i).getId();

                    }
                }
                for (int i = 0; i < maxApp; ++i) {
                    if (getBag().size() < constants.getMaxCardsInBag()) {
                        getBag().add(findID(highestID));
                        getCards().remove(findID(highestID));
                        setType(highestID);
                    }
                }
            }
        }
    }

    public final void traderPlaystyle() {
        //In case he has only Illegal Cards
        if (!legalCards()) {
            illegalCardsPlaystyle();
            setType(0);
        } else {
            highestFrequency();
        }
    }

    private boolean legalCards() {
        for (Goods goods : this.getCards()) {
            if (goods.getId() < constants.getLegalID()) {
                return true;
            }
        }
        return false;
    }

    private void illegalCardsPlaystyle() {
        GoodsComparator goodsComparator = new GoodsComparator();
        getCards().sort(goodsComparator);
        getBag().add(getCards().get(0));
        getCards().remove(getCards().get(0));
    }

    public final void sherifPlaystyle(final ArrayList<Player> players, final GameInput gameInput,
                                final GoodsFactory factory) {
        for (Player player : players) {
            if (player.getRole() != 0) {
                checkPlayer(player, gameInput, factory);
            }
        }
    }

    private void removeIllegal(final Player player, final int[] vectorOfIllegal) {
        for (int i = 0; i < constants.getAllIDs(); ++i) {
            if (vectorOfIllegal[i] != 0) {
                for (int j = 0; j < vectorOfIllegal[i]; ++j) {
                    player.getBag().remove(GoodsFactory.getInstance().getGoodsById(i));
                }
            }
        }
    }

    public final void checkPlayer(final Player player, final GameInput gameInput,
                            final GoodsFactory factory) {
        int[] indexOfIllegal = new int[constants.getAllIDs()];
        boolean illegalFound = false;
        GoodsComparator goodsComparator = new GoodsComparator();
        player.getBag().sort(goodsComparator);
        if (getGold() >= constants.getMinMoney()) {
            for (Goods goods : player.getBag()) {
                if (goods.getId() >= constants.getIllegalID() || player.getType()
                        != goods.getId()) {
                    player.setGold(player.getGold() - goods.getPenalty());
                    setGold(getGold() + goods.getPenalty());
                    gameInput.getAssetIds().add(goods.getId());
                    indexOfIllegal[goods.getId()]++;
                    illegalFound = true;
                } else if (illegalFound) {
                    player.setProfit(player.getProfit() + goods.getProfit());
                } else {
                    setGold(getGold() - goods.getPenalty());
                    player.setGold(player.getGold() + goods.getPenalty());
                    player.setProfit(player.getProfit() + goods.getProfit());
                }
            }
        } else {
            for (Goods goods : player.getBag()) {
                profitUncheckedGoods(player, goods);
            }
        }
        player.setChecked();
        removeIllegal(player, indexOfIllegal);
        if (!player.getBag().isEmpty()) {
            for (Goods goods : player.getBag()) {
                if (goods.getId() <= constants.getLegalID()) {
                    player.getBonus()[goods.getId()]++;
                } else {
                    for (Goods illgoods : ((IllegalGoods) factory.getGoodsById(goods.getId()))
                            .getIllegalBonus().keySet()) {
                        player.setProfit(player.getProfit() + illgoods.getProfit()
                                * ((IllegalGoods) factory.getGoodsById(goods.getId())).
                                getIllegalBonus().get(illgoods));
                        player.getBonus()[illgoods.getId()] += ((IllegalGoods) factory.
                                getGoodsById(goods.getId())).
                                getIllegalBonus().get(illgoods);
                    }
                    player.setProfit(player.getProfit() + goods.getProfit());
                }
            }
        }
        player.getBag().removeAll(player.getBag());
        player.getCards().removeAll(player.getCards());
    }
    public final void profitUncheckedGoods(final Player player, final Goods goods) {
        if (goods.getId() <= constants.getLegalID()) {
            player.setProfit(player.getProfit() + goods.getProfit());
        }
    }
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return gold == player.gold
                && role == player.role
                && profit == player.profit
                && type == player.type
                && checked == player.checked
                && Objects.equals(strategy, player.strategy)
                && Objects.equals(cards, player.cards)
                && Objects.equals(bag, player.bag);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(strategy, gold, role, cards, profit, bag, type, checked);
    }
}
