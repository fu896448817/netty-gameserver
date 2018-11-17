package com.linkflywind.gameserver.cardlib.poker;

import java.util.Collections;
import java.util.LinkedList;

public class PokerManager {
    private LinkedList<Poker> pokers = new LinkedList();

    public PokerManager() {
        PokerNumber[] pokerNumber = PokerNumber.values();
        PokerType[] pokerTypes = PokerType.values();

        for (int i = 0; i < pokerTypes.length; i++) {
            for (int j = 0; j < pokerTypes.length; j++) {
                pokers.add(new Poker(pokerNumber[i], pokerTypes[j]));
            }
        }
    }

    private void shuffle() {
        Collections.shuffle(pokers);
    }

    protected Poker get() {
        return pokers.pollFirst();
    }

}
