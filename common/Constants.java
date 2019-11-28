package com.tema1.common;

public final class Constants {
    // add/delete any constants you think you may use
    private final int minMoney = 16;
    private final int legalID = 9;
    private final int illegalID = 20;
    private final int startingMoney = 80;
    private final int numberOfCardsinDeck = 10;
    private final int sumOfGoldPlayable = 6;
    private final int maxCardsInBag = 8;
    private final int minBribeCards = 2;
    private final int lowBribe = 5;
    private final int highBribe = 10;
    private final int threePlayers = 3;
    private final int allIDs = 25;

    public int getMinMoney() {
        return minMoney;
    }

    public int getAllIDs() {
        return allIDs;
    }

    public int getLegalID() {
        return legalID;
    }

    public int getIllegalID() {
        return illegalID;
    }

    public int getStartingMoney() {
        return startingMoney;
    }

    public int getNumberOfCardsinDeck() {
        return numberOfCardsinDeck;
    }

    public int getSumOfGoldPlayable() {
        return sumOfGoldPlayable;
    }

    public int getMaxCardsInBag() {
        return maxCardsInBag;
    }

    public int getMinBribeCards() {
        return minBribeCards;
    }

    public int getLowBribe() {
        return lowBribe;
    }

    public int getHighBribe() {
        return highBribe;
    }

    public int getThreePlayers() {
        return threePlayers;
    }
}
