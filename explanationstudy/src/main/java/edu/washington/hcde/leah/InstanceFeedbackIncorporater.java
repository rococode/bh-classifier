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

public class InstanceFeedbackIncorporater {

    private static String url;
    private static Properties props;

    private static Map<String, Map<String, Map<String, String>>> feedback_map;

    public static void main(String[] args) {
        initialize();
        setupInstanceFeedback();
        applyInstanceFeedback();
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
    }

    public static void setupInstanceFeedback() {
        // get 60 users with instance-level feedback
        String query = "select a.condition, a.id, b.email, b.instance, b.real_label, b.real from exp_demographics as a inner join exp_email as b on a.id = b.id where a.pilot is not true and a.dq is not true and a.created > '2019-09-05 13:00:00' and (a.condition = '_instance' or a.condition = '_instance_explain') and b.mode = 'train';";

        try (Connection conn = DriverManager.getConnection(url, props)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            log.info("Executing " + stmt.toString());
            ResultSet rs = stmt.executeQuery();

            // Condition -> id -> email -> string: either baseball or hockey for sorting into folder
            Map<String, Map<String, Map<String, String>>> answers = new HashMap<>();

            while (rs.next()) {
                String condition = rs.getString("condition");
                if (!answers.containsKey(condition)) {
                    answers.put(condition, new HashMap<>());
                }
                Map<String, Map<String, String>> map = answers.get(condition);
                String id = rs.getString("id");
                if (!map.containsKey(id)) {
                    map.put(id, new HashMap<String, String>());
                }
                Map<String, String> emails = map.get(id);
                String email = rs.getString("email");

                String instance = rs.getString("instance"); // either instance_yes or instance_no
                String real_label = rs.getString("real_label"); // either baseball or hockey
                String model_accuracy = rs.getString("real"); // either correct or incorrect

                if (instance.equals("instance_yes") && model_accuracy.equals("correct")) {
                    // user feedback aligns with real label
                    // assign email to that folder
                    emails.put(email, real_label);
                } else if (instance.equals("instance_yes") && model_accuracy.equals("incorrect")) {
                    // user feedback is the opposite of real label
                    if (real_label.equals("baseball")) emails.put(email, "hockey");
                    else emails.put(email, "baseball");
                } else if (instance.equals("instance_no") && model_accuracy.equals("correct")) {
                    // user feedback is the opposite of real label
                    if (real_label.equals("baseball")) emails.put(email, "hockey");
                    else emails.put(email, "baseball");
                } else if (instance.equals("instance_no") && model_accuracy.equals("incorrect")) {
                    // user feedback aligns with real label
                    emails.put(email, real_label);
                }

                feedback_map = answers;
            }
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

    public static void applyInstanceFeedback() {
        // for each user:
        // identify their labels for the 20 emails (in resources\\train)
        // copy the 20 emails to the appropriate folder (hockey or baseball in data\\feedback)
        for (Map.Entry<String, Map<String, Map<String, String>>> entry : feedback_map.entrySet()) {
            for (Map.Entry<String, Map<String, String>> user : entry.getValue().entrySet()) {

                System.out.println();
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

                for (Map.Entry<String, String> email : user.getValue().entrySet()) {

                    // copy emails over to the appropriate subfolder
                    File source = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\explanationstudy\\src\\main\\resources\\train\\" + email.getKey());
                    File dest = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data\\feedback\\feedback_" + email.getValue());

                    try {
                        FileUtils.copyFileToDirectory(source, dest);
                        System.out.println("copied file " + email.getKey() + " to " + email.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // call run.py to incorporate the instance-level feedback for this user
                try {
                    String[] cmd = {"C:\\Users\\Melissa Birchfield\\AppData\\Local\\Programs\\Python\\Python37\\python.exe", "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\run_instance.py", entry.getKey(), user.getKey()};
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
