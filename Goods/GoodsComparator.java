package com.tema1.goods;

import java.util.Comparator;

public class GoodsComparator implements Comparator<Goods> {
    @Override
    public final int compare(final Goods goods, final Goods t1) {
        return t1.getProfit() - goods.getProfit();
    }
}
