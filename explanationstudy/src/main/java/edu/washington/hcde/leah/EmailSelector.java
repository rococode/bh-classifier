package edu.washington.hcde.leah;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmailSelector {

    public static void main(String[] args) {

        selectTrainEmails();  // selects 20

//        selectTestEmails();  // selects 2 out of the 20 -- SAME

//        selectMatches();  // selects 2 -- SIMILAR

    }

    public static void selectMatches() {
        File match_set = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\matches3");
        File[] files = match_set.listFiles();

        List<String> m_success = new ArrayList<>();
        List<String> m_failed = new ArrayList<>();

        for (File f : files) {
            if (f.getName().contains("success")) {
                m_success.add(f.getName());
            } else {
                m_failed.add(f.getName());
            }
        }

        System.out.println("Matches success: " + m_success.size());
        System.out.println("Matches failed: " + m_failed.size());

        int index1 = (int) (Math.random() * m_success.size());
        int index2 = (int) (Math.random() * m_failed.size());

        System.out.println("i1: " + index1);
        System.out.println("i2: " + index2);

        System.out.println("Match Train 1: " + m_success.get(index1));
        System.out.println("Match Train 2: " + m_failed.get(index2));

        System.out.println("The Test SIMILAR emails will be the pairs of these emails.");
    }

    public static void selectTrainEmails() {

        File train2 = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\final2\\better-names-final");

        File folder_success = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\success2");
        File folder_failed = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\failed2");

        File[] files_success = folder_success.listFiles();
        File[] files_failed = folder_failed.listFiles();

        System.out.println(files_success.length);
        int idx = (int) (Math.random() * files_success.length);
        int curr_idx = 0;

        // randomly choose 8 correct baseball emails
        int chosen_correct_baseball = 1;
        List<String> copied_names = new ArrayList<String>();
        if (files_success != null) { //some JVMs return null for empty dirs
            while (chosen_correct_baseball > 0) {
                for (File f : files_success) {
                    System.out.println("f: " + f.getName());
                    if (f.getName().contains("baseball") && curr_idx == idx & !copied_names.contains(f.getName())) { // one of the random -- 2 choices, either in or out
                        // copy email over to the train2 folder
                        if (chosen_correct_baseball > 0) {
                            try {
                                FileUtils.copyFileToDirectory(f, train2);
                                System.out.println("copied file " + f.getName());
                                copied_names.add(f.getName());
                                chosen_correct_baseball--;
                                System.out.println("correct baseball left to choose: " + chosen_correct_baseball);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    curr_idx++;
                }
            }
        }

        // randomly choose 6 correct hockey emails
        int chosen_correct_hockey = 0;
        if (files_success != null) { //some JVMs return null for empty dirs
            while (chosen_correct_hockey > 0) {
                for (File f : files_success) {
                    if (f.getName().contains("hockey") && (int) (Math.random() * 2) == 0 & !copied_names.contains(f.getName())) { // one of the random -- 2 choices, either in or out
                        // copy email over to the train2 folder
                        if (chosen_correct_hockey > 0) {
                            try {
                                FileUtils.copyFileToDirectory(f, train2);
                                System.out.println("copied file " + f.getName());
                                copied_names.add(f.getName());
                                chosen_correct_hockey--;
                                System.out.println("correct hockey left to choose: " + chosen_correct_hockey);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        // randomly choose 1 incorrect baseball emails
        int chosen_incorrect_baseball = 0;
        if (files_failed != null) { //some JVMs return null for empty dirs
            while (chosen_incorrect_baseball > 0) {
                for (File f : files_failed) {
                    if (f.getName().contains("baseball") && (int) (Math.random() * 2) == 0 & !copied_names.contains(f.getName())) { // one of the random -- 2 choices, either in or out
                        // copy email over to the train2 folder
                        if (chosen_incorrect_baseball > 0) {
                            try {
                                FileUtils.copyFileToDirectory(f, train2);
                                System.out.println("copied file " + f.getName());
                                copied_names.add(f.getName());
                                chosen_incorrect_baseball--;
                                System.out.println("incorrect baseball left to choose: " + chosen_incorrect_baseball);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }


        // randomly choose 3 incorrect hockey emails
        int chosen_incorrect_hockey = 0;
        if (files_failed != null) { //some JVMs return null for empty dirs
            while (chosen_incorrect_hockey > 0) {
                for (File f : files_failed) {
                    if (f.getName().contains("hockey") && (int) (Math.random() * 2) == 0 & !copied_names.contains(f.getName())) { // one of the random -- 2 choices, either in or out
                        // copy email over to the train2 folder
                        if (chosen_incorrect_hockey > 0) {
                            try {
                                FileUtils.copyFileToDirectory(f, train2);
                                System.out.println("copied file " + f.getName());
                                copied_names.add(f.getName());
                                chosen_incorrect_hockey--;
                                System.out.println("incorrect hockey left to choose: " + chosen_incorrect_hockey);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    // select 2 emails from the 20, 1 correct and 1 incorrect
    public static void selectTestEmails() {

        File test2 = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\final2\\test-final");

        File folder = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\final2\\better-names-final");
        File[] files = folder.listFiles();

        // select one correct
        int index_to_select = (int) (Math.random() * 15);
        int index = 0;
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                System.out.println("index to select: " + index_to_select);
                System.out.println("index: " + index);
                System.out.println(f.getName());

                if (f.getName().contains("success")) {
                    if (index == index_to_select) {
                        // copy email over to the train2 folder
                        try {
                            FileUtils.copyFileToDirectory(f, test2);
                            System.out.println("selected file " + f.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    index++;
                }
            }
        }

        // select one incorrect
        index_to_select = (int) (Math.random() * 5);
        index = 0;
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.getName().contains("failed")) {
                    if (index == index_to_select) {
                        // copy email over to the train2 folder
                        try {
                            FileUtils.copyFileToDirectory(f, test2);
                            System.out.println("selected file " + f.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    index++;
                }
            }
        }
    }

    // chose 4 emails
//    public static void selectTestEmails() {
//
//        File test2 = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\final2\\test");
//
//        File folder = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\final2\\better-names");
//        File[] files = folder.listFiles();
//
//        // select one baseball correct
//        int index_to_select = (int) (Math.random() * 8);
//        int index = 0;
//        if (files != null) { //some JVMs return null for empty dirs
//            for (File f : files) {
//                System.out.println("index to select: " + index_to_select);
//                System.out.println("index: " + index);
//                System.out.println(f.getName());
//
//                if (f.getName().contains("b") && f.getName().contains("success")) {
//                    if (index == index_to_select) {
//                        // copy email over to the train2 folder
//                        try {
//                            FileUtils.copyFileToDirectory(f, test2);
//                            System.out.println("selected file " + f.getName());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    index++;
//                }
//            }
//        }
//
//        System.out.println("index to select: " + index_to_select);
//        System.out.println("index: " + index);
//
//        // select one hockey correct
//        index_to_select = (int) (Math.random() * 7);
//        index = 0;
//        if (files != null) { //some JVMs return null for empty dirs
//            for (File f : files) {
//                System.out.println("index to select: " + index_to_select);
//                System.out.println("index: " + index);
//                System.out.println(f.getName());
//
//                if (f.getName().contains("h") && f.getName().contains("success")) {
//                    if (index == index_to_select) {
//                        // copy email over to the train2 folder
//                        try {
//                            FileUtils.copyFileToDirectory(f, test2);
//                            System.out.println("selected file " + f.getName());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    index++;
//                }
//            }
//        }
//
//        // select one baseball incorrect
//        index_to_select = (int) (Math.random() * 2);
//        index = 0;
//        if (files != null) { //some JVMs return null for empty dirs
//            for (File f : files) {
//                if (f.getName().contains("b") && f.getName().contains("failed")) {
//                    if (index == index_to_select) {
//                        // copy email over to the train2 folder
//                        try {
//                            FileUtils.copyFileToDirectory(f, test2);
//                            System.out.println("selected file " + f.getName());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    index++;
//                }
//            }
//        }
//
//        // select one hockey incorrect
//        index_to_select = (int) (Math.random() * 3);
//        index = 0;
//        if (files != null) { //some JVMs return null for empty dirs
//            for (File f : files) {
//                if (f.getName().contains("h") && f.getName().contains("failed")) {
//                    if (index == index_to_select) {
//                        // copy email over to the train2 folder
//                        try {
//                            FileUtils.copyFileToDirectory(f, test2);
//                            System.out.println("selected file " + f.getName());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    index++;
//                }
//            }
//        }
//    }


//    public static boolean match(String filename, String sport, boolean correct) {
//        if (sport.equals("baseball") && correct) {
//            return filename.contains("baseball") && (filename.contains("close-true") || filename.contains("success"));
//        } else if (sport.equals("baseball") && !correct) {
//            return filename.contains("baseball") && (filename.contains("close-false") || filename.contains("failed"));
//        } else if (sport.equals("hockey") && correct) {
//            return filename.contains("hockey") && (filename.contains("close-true") || filename.contains("success"));
//        } else if (sport.equals("hockey") && !correct) {
//            return filename.contains("hockey") && (filename.contains("close-false") || filename.contains("failed"));
//        }
//        return false;
//    }
}
