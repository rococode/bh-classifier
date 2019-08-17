package edu.washington.hcde.leah;

import java.util.Random;

public class Launcher {

    public static void main(String[] args) {
//        Pulls out various data from the database.
        DBManager.initialize();
//        DBManager.parseSpecificOpenQ(); <-- used for study 2
        DBManager.parseOpenQ();
        DBManager.getVals();
//        DBManager.getOpenQNoCondition();

//        Extracts the feedback from different conditions and stores it in csv files.
//        Also prints out some relevant counts and comparisons.
//        FeedbackExtracter.initialize();
//        FeedbackExtracter.compareFeature(); // results: compare_feature.csv
//        FeedbackExtracter.compareInstance(); // results: compare_instance.csv
//        FeedbackExtracter.compareNoFeedback(); // results: compare_no_feedback.csv

//        This was to examine the feature-feedback users' process of selecting words.
//        We didn't see anything very interesting here.
//        SelectionProcess.initialize();
//        SelectionProcess.countWordsSelected();
    }
}
