package edu.washington.hcde.leah;

import java.util.Random;

public class Launcher {

    public static void main(String[] args) {
//        DBManager.initialize();
//        DBManager.parseOpenQ();
//        DBManager.getVals();
//        DBManager.getOpenQNoCondition();

        FeedbackExtracter.initialize();
        FeedbackExtracter.compareFeature();
        FeedbackExtracter.compareInstance();
        FeedbackExtracter.compareNoFeedback();

//        SelectionProcess.initialize();
//        SelectionProcess.countWordsSelected();
    }
}
