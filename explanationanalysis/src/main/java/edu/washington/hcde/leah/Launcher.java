package edu.washington.hcde.leah;

import java.util.Random;

public class Launcher {

    public static void main(String[] args) {
        DBManager.initialize();
//        DBManager.parseOpenQ();
//        DBManager.parseOpenQ();
//        DBManager.getVals();
        DBManager.getOpenQNoCondition();
    }
}
