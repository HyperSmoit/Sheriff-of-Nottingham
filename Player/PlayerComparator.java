package com.tema1.Player;

import java.util.Comparator;

public final class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(final Player p1, final Player p2) {
        return p2.getGold() - p1.getGold();
    }
}
