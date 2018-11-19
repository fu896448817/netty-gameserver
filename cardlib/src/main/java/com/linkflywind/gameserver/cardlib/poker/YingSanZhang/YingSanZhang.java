package com.linkflywind.gameserver.cardlib.poker.YingSanZhang;

import com.linkflywind.gameserver.cardlib.poker.Poker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class YingSanZhang implements Comparable<YingSanZhang> {
    private List<Poker> cards = new ArrayList<>();

    public YingSanZhang(Poker card1, Poker card2, Poker card3) {
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.sort(Comparator.comparing(Poker::getNumber));
    }

    private boolean isTonghua() {
        if (cards.get(0).getCardType().equals(cards.get(1).getCardType()) &&
                cards.get(1).getCardType().equals(cards.get(2).getCardType()))
            return true;
        return false;
    }

    private boolean isShun() {
        if ((cards.get(2).getNumber().ordinal() - cards.get(1).getCardType().ordinal() == 1 ||
                cards.get(2).getNumber().ordinal() - cards.get(1).getCardType().ordinal() == 10)
                &&
                (cards.get(1).getCardType().ordinal() - cards.get(0).getCardType().ordinal() == 1))
            return true;
        return false;
    }

    private boolean isSanZhang() {
        if (cards.get(0).getNumber().equals(cards.get(1).getNumber()) &&
                cards.get(1).getNumber().equals(cards.get(2).getNumber()))
            return true;
        return false;
    }

    private boolean isDui() {
        if (cards.get(0).getNumber().ordinal() == cards.get(1).getNumber().ordinal() ||
                cards.get(1).getNumber().ordinal() == cards.get(2).getNumber().ordinal())
            return true;
        return false;
    }

    private YingSanZhangType getYingSanZhangType() {
        if (isSanZhang())
            return YingSanZhangType.Sanzhang;
        if (isTonghua() && isShun())
            return YingSanZhangType.tonghuaShun;
        if (isShun())
            return YingSanZhangType.shun;
        if (isTonghua())
            return YingSanZhangType.tonghua;
        if (isDui())
            return YingSanZhangType.dui;
        return YingSanZhangType.None;
    }

    private Poker getDuiNumber() {
        if (this.cards.get(0).getNumber().equals(this.cards.get(1).getCardType()) ||
                this.cards.get(0).getNumber().equals(this.cards.get(2).getCardType()))
            return this.cards.get(0);
        else
            return this.cards.get(1);
    }


    private int compareCard(Poker card1, Poker card2) {
        if (card1.getNumber().ordinal() > card2.getNumber().ordinal())
            return 1;
        else if (card1.getNumber().ordinal() == card2.getNumber().ordinal())
            return 0;
        else
            return -1;
    }


    private int compareDui(YingSanZhang o) {
        if (this.getDuiNumber().getNumber().ordinal() > o.getDuiNumber().getNumber().ordinal()) {
            return 1;
        } else if (this.getDuiNumber().getNumber().ordinal() == o.getDuiNumber().getNumber().ordinal())
            return 0;
        else
            return -1;
    }

    private int compareNone(YingSanZhang o) {
        int result = compareCard(this.cards.get(0), o.cards.get(0));
        if (result == 0) {
            result = compareCard(this.cards.get(1), o.cards.get(1));
            if (result == 0) {
                result = compareCard(this.cards.get(2), o.cards.get(2));
                if (result == 0)
                    return result;
                return result;
            }
            return result;
        }
        return result;
    }

    private int compareShun(YingSanZhang o) {
        if (this.cards.get(0).getNumber().ordinal() == o.cards.get(0).getNumber().ordinal())
            return 0;
        else if (this.cards.get(0).getNumber().ordinal() > o.cards.get(0).getNumber().ordinal())
            return 1;
        else
            return -1;
    }

    private int compareSanZhang(YingSanZhang o) {
        if (this.cards.get(0).getNumber().ordinal() > o.cards.get(0).getNumber().ordinal())
            return 1;
        else
            return -1;
    }

    @Override
    public int compareTo(YingSanZhang o) {
        if (this.getYingSanZhangType().ordinal() > o.getYingSanZhangType().ordinal())
            return 1;
        else if (this.getYingSanZhangType().ordinal() == o.getYingSanZhangType().ordinal()) {
            if (this.getYingSanZhangType() == YingSanZhangType.Sanzhang) {
                return compareSanZhang(o);
            } else if (this.getYingSanZhangType() == YingSanZhangType.tonghuaShun) {
                return compareShun(o);
            } else if (this.getYingSanZhangType() == YingSanZhangType.tonghua) {
                return compareNone(o);
            } else if (this.getYingSanZhangType() == YingSanZhangType.shun) {
                return compareShun(o);
            } else if (this.getYingSanZhangType() == YingSanZhangType.dui &&
                    o.getYingSanZhangType() == YingSanZhangType.dui) {
                return compareDui(o);
            } else {
                return compareNone(o);
            }
        } else
            return -1;
    }
}
