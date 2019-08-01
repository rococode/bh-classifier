package edu.washington.hcde.leah;

import java.util.Random;

public class Launcher {

    public static void main(String[] args) {
        DBManager.initialize();
//        DBManager.parseOpenQ();
//        DBManager.parseOpenQ();
//        DBManager.getVals();
//        DBManager.getOpenQNoCondition();

        WordExtracter.initialize();
//        WordExtracter.extractWords();
//        WordExtracter.matchWords();
//        WordExtracter.compareWords();
//        WordExtracter.compareInstance();
        WordExtracter.compareNoFeedback();

//        SelectionProcess.initialize();
//        SelectionProcess.countWordsSelected();
    }
}
