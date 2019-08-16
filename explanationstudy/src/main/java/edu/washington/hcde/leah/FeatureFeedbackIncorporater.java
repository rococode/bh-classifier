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

public class FeatureFeedbackIncorporater {

    private static String url;
    private static Properties props;

    private static Map<String, Map<String, Map<String, List<String>>>> feedback_map;
//    private static Map<String, String> model_emails;

    public static void main(String[] args) {
        initialize();
        setupFeatureFeedback();
        applyFeatureFeedback();
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
//        model_emails = new HashMap<>();
//        model_emails.put("54172-h-failed.txt", "hockey");
//        model_emails.put("54174-h-failed.txt", "hockey");
//        model_emails.put("54198-h-failed.txt", "hockey");
//        model_emails.put("54199-h-success.txt", "hockey");
//        model_emails.put("54220-h-success.txt", "hockey");
//        model_emails.put("54226-h-success.txt", "hockey");
//        model_emails.put("54229-h-success.txt", "hockey");
//        model_emails.put("54244-h-success.txt", "hockey");
//        model_emails.put("54245-h-success.txt", " hockey");
//        model_emails.put("54260-h-success.txt", "hockey");
//        model_emails.put("104910-b-success.txt", "baseball");
//        model_emails.put("104936-b-success.txt", "baseball");
//        model_emails.put("104942-b-success.txt", "baseball");
//        model_emails.put("104944-b-failed.txt", "baseball");
//        model_emails.put("104947-b-failed.txt", "baseball");
//        model_emails.put("105015-b-success.txt", "baseball");
//        model_emails.put("105058-b-success.txt", "baseball");
//        model_emails.put("105086-b-success.txt", "baseball");
//        model_emails.put("105087-b-success.txt", "baseball");
//        model_emails.put("105144-b-success.txt", "baseball");

//        from first study:
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

    public static void setupFeatureFeedback() {
        // get 60 users with feature-level feedback
        String query = "select a.condition, a.id, b.email, b.chosen, b.instance from exp_demographics as a inner join exp_email as b on a.id = b.id where a.pilot is not true and a.created > '2019-08-14 17:00:00' and (a.condition = '_feature' or a.condition = '_feature_explain') and b.mode = 'train';";

        try (Connection conn = DriverManager.getConnection(url, props)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            log.info("Executing " + stmt.toString());
            ResultSet rs = stmt.executeQuery();

            // Condition -> id -> email#instance -> list of 3 words
            Map<String, Map<String, Map<String, List<String>>>> answers = new HashMap<>();

            while (rs.next()) {
                String condition = rs.getString("condition");
                if (!answers.containsKey(condition)) {
                    answers.put(condition, new HashMap<>());
                }
                Map<String, Map<String, List<String>>> map = answers.get(condition);
                String id = rs.getString("id");
                if (!map.containsKey(id)) {
                    map.put(id, new HashMap<>());
                }
                Map<String, List<String>> emails = map.get(id);
                String email_key = rs.getString("email") + "#" + rs.getString("instance");
                Array words = rs.getArray("chosen");
                safePut3(map.get(id), email_key, Arrays.asList((String[]) words.getArray()));

                feedback_map = answers;
            }
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

    public static void applyFeatureFeedback() {
        // for each user:
        // identify their labels for the 20 emails (in resources\\train)
        // if also incorporating instance-level feedback:
        //      copy the 20 emails to the appropriate folder (hockey or baseball in data\\feedback)
        for (Map.Entry<String, Map<String, Map<String, List<String>>>> entry : feedback_map.entrySet()) {
            for (Map.Entry<String, Map<String, List<String>>> user : entry.getValue().entrySet()) {

                List<String> chosen_hockey = new ArrayList<>();
                List<String> chosen_baseball = new ArrayList<>();

                for (Map.Entry<String, List<String>> email : user.getValue().entrySet()) {
                    String email_name = email.getKey().substring(0, email.getKey().indexOf("#"));
                    System.out.println("email: " + email_name);

                    // get the 3 words -- should end up with 3 * 20 = 60 words total
                    // sort them into either hockey or baseball

                    String user_thought = email.getKey().substring(email.getKey().indexOf("#") + 1);

                    if (user_thought.equals("baseball")) {
                        chosen_baseball.addAll(email.getValue());
                    } else if(user_thought.equals("hockey")) {
                        chosen_hockey.addAll(email.getValue());
                    } else System.out.println("oops");

//                    The following was used for study 1 -- not relevant anymore
//                    if (user_thought.equals("correct") && email.getKey().contains("failed")) { // (predicted the wrong one, so user is wrong)
//                        if (model_emails.get(email_name).equals("baseball")) {
//                            chosen_hockey.addAll(email.getValue());
//                        } else {
//                            chosen_baseball.addAll(email.getValue());
//                        }
//                    } else if (user_thought.equals("correct") && email.getKey().contains("success")) {
//                        if (model_emails.get(email_name).equals("baseball")) {
//                            chosen_baseball.addAll(email.getValue());
//                        } else {
//                            chosen_hockey.addAll(email.getValue());
//                        }
//                    } else if (user_thought.equals("incorrect") && email.getKey().contains("failed")) { // user is right
//                        if (model_emails.get(email_name).equals("baseball")) {
//                            chosen_baseball.addAll(email.getValue());
//                        } else {
//                            chosen_hockey.addAll(email.getValue());
//                        }
//                    } else if (user_thought.equals("incorrect") && email.getKey().contains("success")) { // user is wrong
//                        if (model_emails.get(email_name).equals("baseball")) {
//                            chosen_hockey.addAll(email.getValue());
//                        } else {
//                            chosen_baseball.addAll(email.getValue());
//                        }
//                    }
                    // else user was unsure, their feedback is ignored
                }
                System.out.println(chosen_hockey.size() + " hockey words for user " + user.getKey());
                System.out.println(chosen_baseball.size() + " baseball words for user " + user.getKey());

                try {
                    String[] cmd = new String[5 + chosen_hockey.size() + chosen_baseball.size()];
                    cmd[0] = "C:\\Users\\Melissa Birchfield\\AppData\\Local\\Programs\\Python\\Python37\\python.exe";
                    cmd[1] = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\run.py";
                    cmd[2] = entry.getKey(); // condition
                    cmd[3] = user.getKey(); // user id
                    cmd[4] = Integer.toString(chosen_hockey.size() + chosen_baseball.size()); // total number of words (should be 60)
                    cmd[5] = Integer.toString(chosen_hockey.size()); // number of hockey words (should be multiple of 3)

                    // very hacky: send all the words as command line args
                    List<String> chosen = new ArrayList<>();
                    chosen.addAll(chosen_hockey);
                    chosen.addAll(chosen_baseball);

                    for (int i = 6; i < cmd.length; i++) {
                        cmd[i] = chosen.get(i - 6);
                    }

                    // call run.py to incorporate feature-level feedback
                    System.out.println(Arrays.toString(cmd));
                    Process p = Runtime.getRuntime().exec(cmd);
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
                        p.waitFor();
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
