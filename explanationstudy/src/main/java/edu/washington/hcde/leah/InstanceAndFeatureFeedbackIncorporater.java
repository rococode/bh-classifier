package edu.washington.hcde.leah;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

//package javaToPy;
//import org.python.core.PyObject;
//import org.python.util.PythonInterpreter;

public class InstanceAndFeatureFeedbackIncorporater {

//    private static void safePut(Map<String, List<String>> map, String key, String value) {
//        if (!map.containsKey(key)) {
//            map.put(key, new ArrayList<String>());
//        }
//        map.get(key).add(value);
//    }

    private static String url;
    private static Properties props;

    private static Map<String, Map<String, Map<String, String>>> instance_feedback_map;
    private static Map<String, Map<String, Map<String, List<String>>>> feature_feedback_map;
    private static Map<String, String> model_emails;

    public static void main(String[] args) {
        initialize();
//        setupInstanceFeedback();
        setupFeatureFeedback();
        applyFeedback();
    }

    public static void initialize() {
//        psql --host=voiceinteraction.czjskx89fj5x.us-west-2.rds.amazonaws.com --port=5432 --username voiceinteraction --password --dbname=voiceinteraction
        url = "jdbc:postgresql://voiceinteractiondb.cjt2uxgbtzyl.us-west-2.rds.amazonaws.com/voiceinteractiondb";
        props = new Properties();
        props.setProperty("user", "voiceuser");
        props.setProperty("password", "voicepassword123");
        try (Connection conn = DriverManager.getConnection(url, props)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM exp_demographics");
            ResultSet rs = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // will have to change the filenames to put in this map
        model_emails = new HashMap<>();
//        model_emails.put("54200-h-m4-success.txt", "hockey");
//        model_emails.put("54295-h-m1-success.txt", "hockey");
//        model_emails.put("54366-h-close-success.txt", "hockey");
//        model_emails.put("54721-h-m2-success.txt", "hockey");
//        model_emails.put("54775-h-failed.txt", "hockey");
//        model_emails.put("104944-b-failed.txt", "baseball");
//        model_emails.put("104979-success.txt", "baseball");
//        model_emails.put("104993-success.txt", "baseball");
//        model_emails.put("105028-b-m3-success.txt", "baseball");
//        model_emails.put("105057-b-close-failed.txt", "baseball");
    }

    private static void safePut3(Map<String, List<String>> map, String key, List<String> value) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<String>());
        }
        map.get(key).addAll(value);
    }

//    public static void setupInstanceFeedback() {
//        // get 60 users with feature-level feedback
////        String query = "select a.condition, a.id, b.email, b.instance, b.real_label, b.real from exp_demographics as a inner join exp_email as b on a.id = b.id where a.pilot is not true and a.created < '2019-06-01 00:00:00' and (a.condition = '_instance' or a.condition = '_instance_explain') and b.mode = 'train';";
//
////        try (Connection conn = DriverManager.getConnection(url, props)) {
////            PreparedStatement stmt = conn.prepareStatement(query);
////            log.info("Executing " + stmt.toString());
////            ResultSet rs = stmt.executeQuery();
//
//            // Condition -> id -> email -> string: either baseball or hockey for sorting into folder
//            // (list of 3 strings: <instance (to sort into folder), real_label (bball or hockey), real (incorrect or correct)>)
//            Map<String, Map<String, Map<String, String>>> answers = new HashMap<>();
//
//            while (rs.next()) {
//                String condition = rs.getString("condition");
//                if (!answers.containsKey(condition)) {
//                    answers.put(condition, new HashMap<>());
//                }
//                Map<String, Map<String, String>> map = answers.get(condition);
//                String id = rs.getString("id");
//                if (!map.containsKey(id)) {
//                    map.put(id, new HashMap<String, String>());
//                }
//                Map<String, String> emails = map.get(id);
//                String email = rs.getString("email");
//
//                String instance = rs.getString("instance"); // either instance_yes or instance_no
//                String real_label = rs.getString("real_label"); // either baseball or hockey
//                String model_accuracy = rs.getString("real"); // either correct or incorrect
//
//                if (instance.equals("instance_yes") && model_accuracy.equals("correct")) {
//                    // user feedback aligns with real label
//                    // assign email to that folder
//                    emails.put(email, real_label);
//                } else if (instance.equals("instance_yes") && model_accuracy.equals("incorrect")) {
//                    // user feedback is the opposite of real label
//                    if (real_label.equals("baseball")) emails.put(email, "hockey");
//                    else emails.put(email, "baseball");
//                } else if (instance.equals("instance_no") && model_accuracy.equals("correct")) {
//                    // user feedback is the opposite of real label
//                    if (real_label.equals("baseball")) emails.put(email, "hockey");
//                    else emails.put(email, "baseball");
//                } else if (instance.equals("instance_no") && model_accuracy.equals("incorrect")) {
//                    // user feedback aligns with real label
//                    emails.put(email, real_label);
//                }
//
//                instance_feedback_map = answers;
//            }
//        } catch (SQLException e) {
//            System.out.println(query);
//            e.printStackTrace();
//        }
//    }

    public static void setupFeatureFeedback() {
        // get 60 users with feature-level feedback
        String query = "select a.condition, a.id, b.email, b.chosen, b.feedbackus from exp_demographics as a inner join exp_email as b on a.id = b.id where a.pilot is not true and a.created < '2019-06-01 00:00:00' and (a.condition = '_feature' or a.condition = '_feature_explain') and b.mode = 'train';";

        try (Connection conn = DriverManager.getConnection(url, props)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            log.info("Executing " + stmt.toString());
            ResultSet rs = stmt.executeQuery();

            // Condition -> id -> email#confidence@reallabel -> list of 3 words
            Map<String, Map<String, Map<String, List<String>>>> answers = new HashMap<>();

            while (rs.next()) {
                String condition = rs.getString("condition");
                if (!answers.containsKey(condition)) {
                    answers.put(condition, new HashMap<>());
                }
                Map<String, Map<String, List<String>>> map = answers.get(condition);
                String id = rs.getString("id");
                if (!map.containsKey(id)) {
                    map.put(id, new HashMap<String, List<String>>());
                }
                Map<String, List<String>> emails = map.get(id);
                String email_key = rs.getString("email") + "#" + rs.getString("feedbackus");
                Array words = rs.getArray("chosen");
                safePut3(map.get(id), email_key, Arrays.asList((String[]) words.getArray()));
            }

            feature_feedback_map = answers;

        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

    public static void applyFeedback() {
        // for each user:
        // identify their labels for the 10 emails (in resources\\train)
        // copy the 10 emails to the appropriate folder (hockey or baseball in data\\feedback)
        for (Map.Entry<String, Map<String, Map<String, List<String>>>> entry : feature_feedback_map.entrySet()) {
            for (Map.Entry<String, Map<String, List<String>>> user : entry.getValue().entrySet()) {

                // clear data\\feedback folder
                File folder = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\feedback\\feedback_baseball");
                File[] files = folder.listFiles();
                if (files != null) { //some JVMs return null for empty dirs
                    for (File f : files) {
                        f.delete();
                        System.out.println("deleted file " + f.getName());
                    }
                }
                folder = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\feedback\\feedback_hockey");
                files = folder.listFiles();
                if (files != null) { //some JVMs return null for empty dirs
                    for (File f : files) {
                        f.delete();
                        System.out.println("deleted file " + f.getName());
                    }
                }

                List<String> chosen_hockey = new ArrayList<>();
                List<String> chosen_baseball = new ArrayList<>();

                for (Map.Entry<String, List<String>> email : user.getValue().entrySet()) {
                    String email_name = email.getKey().substring(0, email.getKey().indexOf("#"));
                    String user_thought = email.getKey().substring(email.getKey().indexOf("#") + 1);
                    System.out.println("email: " + email_name);

                    // get the 3 words -- should end up with 30 words total

                    String sport = "";

                    if (user_thought.equals("correct") && email.getKey().contains("failed")) { // (predicted the wrong one, so user is wrong)
                        if (model_emails.get(email_name).equals("baseball")) {
                            chosen_hockey.addAll(email.getValue());
                            sport = "hockey";
                        } else {
                            chosen_baseball.addAll(email.getValue());
                            sport = "baseball";
                        }
                    } else if (user_thought.equals("correct") && email.getKey().contains("success")) {
                        if (model_emails.get(email_name).equals("baseball")) {
                            chosen_baseball.addAll(email.getValue());
                            sport = "baseball";
                        } else {
                            chosen_hockey.addAll(email.getValue());
                            sport = "hockey";
                        }
                    } else if (user_thought.equals("incorrect") && email.getKey().contains("failed")) { // user is right
                        if (model_emails.get(email_name).equals("baseball")) {
                            chosen_baseball.addAll(email.getValue());
                            sport = "baseball";
                        } else {
                            chosen_hockey.addAll(email.getValue());
                            sport = "hockey";
                        }
                    } else if (user_thought.equals("incorrect") && email.getKey().contains("success")) { // user is wrong
                        if (model_emails.get(email_name).equals("baseball")) {
                            chosen_hockey.addAll(email.getValue());
                            sport = "hockey";
                        } else {
                            chosen_baseball.addAll(email.getValue());
                            sport = "baseball";
                        }
                    } else {
                        continue;
                    }
                    // else user was unsure, their feedback is ignored

                    System.out.println("need to copy file " + email_name + " to " + sport); // should be either baseball or hockey;

                    // copy emails over to the appropriate subfolder
                    File source = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\explanationstudy\\src\\main\\resources\\train\\" + email_name);
                    File dest = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\feedback\\feedback_" + sport);

                    try {
                        FileUtils.copyFileToDirectory(source, dest);
                        System.out.println("copied file " + email_name + " to " + sport);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(chosen_hockey.size() + " hockey words for user " + user.getKey());
                System.out.println(chosen_baseball.size() + " baseball words for user " + user.getKey());

                try {
                    String[] cmd = new String[5 + chosen_hockey.size() + chosen_baseball.size()];
                    cmd[0] = "C:\\Users\\Melissa Birchfield\\AppData\\Local\\Programs\\Python\\Python37\\python.exe";
                    cmd[1] = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\run.py";
                    cmd[2] = entry.getKey(); // condition
                    cmd[3] = user.getKey(); // user id
                    cmd[4] = Integer.toString(chosen_hockey.size() + chosen_baseball.size()); // total number of words
                    cmd[5] = Integer.toString(chosen_hockey.size()); // number of hockey words

                    List<String> chosen = new ArrayList<>();
                    chosen.addAll(chosen_hockey);
                    chosen.addAll(chosen_baseball);

                    for (int i = 6; i < cmd.length; i++) {
                        cmd[i] = chosen.get(i - 6);
                    }

                    System.out.println(Arrays.toString(cmd));
                    Process p = Runtime.getRuntime().exec(cmd);
                    System.out.println("hey");
                    try {
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        BufferedReader bfr2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String line = "";
                        while ((line = bfr.readLine()) != null) {
                            System.out.println("Python Output [Error]: " + line);
                        }
                        while ((line = bfr2.readLine()) != null) {
                            System.out.println("Python Output [Input]: " + line);
                        }
                        System.out.println("hey 2");
                        p.waitFor();
                        System.out.println("hey 3");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // should end up with 60 entries
    }
}
