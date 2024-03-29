package com.tema1.goods;

import java.util.Map;

public final class IllegalGoods extends Goods {
    private final Map<Goods, Integer> illegalBonus;

    IllegalGoods(final int id, final int profit,
                 final int penalty, final Map<Goods, Integer> illegalBonus) {
        super(id, profit, penalty);
        this.illegalBonus = illegalBonus;
    }

    public Map<Goods, Integer> getIllegalBonus() {
        return illegalBonus;
    }
}
