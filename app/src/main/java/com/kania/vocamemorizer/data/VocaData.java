package com.kania.vocamemorizer.data;

import java.util.ArrayList;

/**
 * Created by Seonghan Kim on 2016-10-31.
 * not using setter/getter for performance
 */

public class VocaData {
    public static final String MEANING_DELIMITER = "_";
    public static final int ID_NOT_NEEDED = -2;
    public static final int ID_INPUT_POINT = -1;

    public int id;
    public String word;
    public ArrayList<String> meanings;
    public long lastUpdated;
    public int correctCount;
    public boolean isIncorrectPrev;

    public VocaData (int id, String word, String meanings, long lastUpdated, int correctCount,
                     boolean isIncorrectPrev) {
        this.id = id;
        this.word = word;
        this.meanings = getMeaningList(meanings);
        this.lastUpdated = lastUpdated;
        this.correctCount = correctCount;
        this.isIncorrectPrev = isIncorrectPrev;
    }

    private ArrayList<String> getMeaningList(String meaningString) {
        ArrayList<String> ret = new ArrayList<>();
        String[] arrMeanings = meaningString.split(MEANING_DELIMITER);
        for (String meaning : arrMeanings) {
            ret.add(meaning);
        }
        return ret;
    }

    public String getMeaningString() {
        StringBuilder sb = new StringBuilder();
        if (meanings != null) {
            for (int i = 0; i < meanings.size(); ++i) {
                sb.append(meanings.get(i));
                if (i < meanings.size() - 1) {
                    sb.append(MEANING_DELIMITER);
                }
            }
        }
        return sb.toString();
    }
}
