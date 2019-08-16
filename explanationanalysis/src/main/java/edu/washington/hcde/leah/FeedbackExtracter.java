package edu.washington.hcde.leah;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeedbackExtracter {

    //         voiceinteraction voicepw123 voiceinteraction (db name)
    // voiceinteractiondb voicepw123 voiceuser
    // psql --host=voiceinteractiondb.cjt2uxgbtzyl.us-west-2.rds.amazonaws.com  --port=5432 --username voiceuser --password --dbname=voiceinteractiondb

    private static String url;
    private static Properties props;

    private static Map<String, List<String>> modelHL;
    private static Map<String, Map<String, Map<String, List<String>>>> userHL; // userHL maps condition -> userID -> email -> words

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

        modelHL = new HashMap<>();
        safePut3(modelHL, "104917-b-practice-success.txt", Arrays.asList("pitch", "montreal", "hard"));
        safePut3(modelHL, "54172-h-failed.txt", Arrays.asList("final", "given", "goals"));
        safePut3(modelHL, "54174-h-failed.txt", Arrays.asList("greg", "really", "goal"));
        safePut3(modelHL, "54198-h-failed.txt", Arrays.asList("nhl", "need", "much"));
        safePut3(modelHL, "54199-h-success.txt", Arrays.asList("playing", "another", "gerald"));
        safePut3(modelHL, "54220-h-success.txt", Arrays.asList("nhl", "problem", "come"));
        safePut3(modelHL, "54226-h-success.txt", Arrays.asList("leafs", "series", "detroit"));
        safePut3(modelHL, "54229-h-success.txt", Arrays.asList("ny", "leads", "cup"));
        safePut3(modelHL, "54244-h-success.txt", Arrays.asList("ny", "cup", "leafs"));
        safePut3(modelHL, "54245-h-success.txt", Arrays.asList("results", "goal", "player"));
        safePut3(modelHL, "54260-h-success.txt", Arrays.asList("gilmour", "slightly", "think"));
        safePut3(modelHL, "104910-b-success.txt", Arrays.asList("sox", "white", "played"));
        safePut3(modelHL, "105144-b-success.txt", Arrays.asList("phillies", "phils", "anyone"));
        safePut3(modelHL, "104936-b-success.txt", Arrays.asList("1st", "baseball", "league"));  // changed "st" to "1st" for processing
        safePut3(modelHL, "104942-b-success.txt", Arrays.asList("b9", "winner", "baseball"));  //changed "b" to "b9" for processing
        safePut3(modelHL, "104944-b-failed.txt", Arrays.asList("anyone", "appreciated", "e-mail"));  // changed "email" to "e-mail" for processing
        safePut3(modelHL, "104947-b-failed.txt", Arrays.asList("cup", "really", "vs"));
        safePut3(modelHL, "105015-b-success.txt", Arrays.asList("three", "much", "anyone"));
        safePut3(modelHL, "105058-b-success.txt", Arrays.asList("baseball", "bb", "single"));
        safePut3(modelHL, "105086-b-success.txt", Arrays.asList("pitch", "help", "much"));
        safePut3(modelHL, "105087-b-success.txt", Arrays.asList("pitch", "got", "much"));

//        from first study:
//        safePut3(modelHL, "104917-b-practice-success.txt", Arrays.asList("pitch", "montreal", "hard"));
//        safePut3(modelHL, "54200-h-m4-success.txt", Arrays.asList("ny", "nhl", "rangers"));
//        safePut3(modelHL, "54295-h-m1-success.txt", Arrays.asList("islanders", "montreal", "series"));
//        safePut3(modelHL, "54366-h-close-success.txt", Arrays.asList("cup", "stanley", "frank"));
//        safePut3(modelHL, "54721-h-m2-success.txt", Arrays.asList("leafs", "wings", "jets"));
//        safePut3(modelHL, "54775-h-failed.txt", Arrays.asList("lot", "caps", "know"));
//        safePut3(modelHL, "104944-b-failed.txt", Arrays.asList("know", "get", "anyway"));
//        safePut3(modelHL, "104979-success.txt", Arrays.asList("players", "baseball", "hit"));
//        safePut3(modelHL, "104993-success.txt", Arrays.asList("rangers", "career", "playing"));
//        safePut3(modelHL, "105028-b-m3-success.txt", Arrays.asList("sox", "well", "boston"));
//        safePut3(modelHL, "105057-b-close-failed.txt", Arrays.asList("another", "come", "la"));

        userHL = new HashMap<>();
    }


    private static void safePut(Map<String, List<String>> map, String key, String value) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<String>());
        }
        map.get(key).add(value);
    }

    private static void safePut2(Map<String, String> map, String key, String value) {
        map.put(key, value);
    }

    private static void safePut3(Map<String, List<String>> map, String key, List<String> value) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<String>());
        }
        map.get(key).addAll(value);
    }

    private static void safePut4(Map<String, Map<String, List<String>>> map, String userID, String email, List<String> words) {
        if (!map.containsKey(userID)) {
            map.put(userID, new HashMap<String, List<String>>());
        }
        safePut3(map.get(userID), email, words);
    }

    public static void compareNoFeedback() {

        String query = "select a.condition as condition, b.id as id, b.email as email, b.feedbackus as confidence, b.real from exp_demographics as a inner join exp_email as b on a.id = b.id where a.pilot is true and a.created > '2019-08-12 00:00:00' and (a.condition = '_explain' or a.condition = '') and b.mode = 'train';";

        try (PrintWriter writer = new PrintWriter(new File("compare_no_feedback.csv"))) {

            try (Connection conn = DriverManager.getConnection(url, props)) {
                PreparedStatement stmt = conn.prepareStatement(query);
                log.info("Executing " + stmt.toString());
                ResultSet rs = stmt.executeQuery();

                // Condition -> id -> email#confidence -> real
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
                    String email_key = rs.getString("email") + "#" + rs.getString("confidence");
                    emails.put(email_key, rs.getString("real"));
                }

                for (Map.Entry<String, Map<String, Map<String, String>>> entry : answers.entrySet()) {
                    System.out.println("Condition: " + entry.getKey());
                    System.out.println("number of users: " + entry.getValue().size());

                    // per condition
                    int count_unsure = 0;
                    int count_correct = 0;
                    int count_incorrect = 0;

                    // per condition
                    int thought_correct_model_correct = 0;
                    int thought_correct_model_incorrect = 0;
                    int thought_incorrect_model_correct = 0;
                    int thought_incorrect_model_incorrect = 0;
                    int thought_unsure_model_correct = 0;
                    int thought_unsure_model_incorrect = 0;

                    for (Map.Entry<String, Map<String, String>> user : entry.getValue().entrySet()) {
                        for (Map.Entry<String, String> email : user.getValue().entrySet()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(entry.getKey() + ","); // Condition
                            sb.append(user.getKey() + ","); // UserID

                            String email_name = email.getKey().substring(0, email.getKey().indexOf("#"));
                            sb.append(email_name + ","); // Email

                            String feedbackus = email.getKey().substring(email.getKey().indexOf("#") + 1); // baseball or hockey
                            String displayed_label;

                            if (email_name.contains("h") && email_name.contains("success")) displayed_label = "hockey";
                            else if (email_name.contains("h") && email_name.contains("failed")) displayed_label = "baseball";
                            else if (email_name.contains("b") && email_name.contains("success")) displayed_label = "baseball";
                            else displayed_label = "hockey"; // (email_name.contains("b") && email_name.contains("failed"))

                            String user_thought;

                            if (feedbackus.equals("unsure")) user_thought = feedbackus;
                            else if (feedbackus.equals(displayed_label)) user_thought = "correct";
                            else user_thought = "incorrect"; // (!feedbackus.equals(displayed_label))

                            sb.append("thought_" + user_thought + ","); // User_Thought c/i/u

                            if (user_thought.equals("unsure")) {
                                count_unsure++;
                                if (email.getValue().equals("correct")) thought_unsure_model_correct++;
                                else thought_unsure_model_incorrect++;
                            }
                            if (user_thought.equals("correct")) {
                                count_correct++;
                                if (email.getValue().equals("correct")) thought_correct_model_correct++;
                                else thought_correct_model_incorrect++;
                            }
                            if (user_thought.equals("incorrect")) {
                                count_incorrect++;
                                if (email.getValue().equals("correct")) thought_incorrect_model_correct++;
                                else thought_incorrect_model_incorrect++;
                            }

                            sb.append("model_" + email.getValue());
                            sb.append("\n");
                            writer.write(sb.toString());
                        }
                    }

                    System.out.println(count_unsure + " unsure");
                    System.out.println(count_correct + " correct");
                    System.out.println(count_incorrect + " incorrect");
                    System.out.println("understood model accuracy: " + thought_correct_model_correct + " [model correct], " + thought_incorrect_model_incorrect + " [model incorrect]");
                    System.out.println("misunderstood model accuracy: " + thought_incorrect_model_correct + " [model correct], " + thought_correct_model_incorrect + " [model incorrect]");
                    System.out.println("unsure: " + thought_unsure_model_correct + " [model correct], " + thought_unsure_model_incorrect + " [model incorrect]");
                    System.out.println();
                }
            } catch (SQLException e) {
                System.out.println(query);
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void compareInstance() {

        String query = "select a.condition as condition, b.id as id, b.email as email, b.feedbackus as confidence, b.instance as feedback, b.real as real from exp_demographics as a inner join exp_email as b on a.id = b.id where a.pilot is true and a.created > '2019-08-12 00:00:00' and (a.condition = '_instance' or a.condition = '_instance_explain') and b.mode = 'train';";

        try (PrintWriter writer = new PrintWriter(new File("compare_instance.csv"))) {

            try (Connection conn = DriverManager.getConnection(url, props)) {
                PreparedStatement stmt = conn.prepareStatement(query);
                log.info("Executing " + stmt.toString());
                ResultSet rs = stmt.executeQuery();

                // Condition -> id -> email#confidence -> [feedback yes/no, real yes/no] (bool[2])
                Map<String, Map<String, Map<String, boolean[]>>> answers = new HashMap<>();

                while (rs.next()) {
                    String condition = rs.getString("condition");
                    if (!answers.containsKey(condition)) {
                        answers.put(condition, new HashMap<>());
                    }
                    Map<String, Map<String, boolean[]>> map = answers.get(condition);
                    String id = rs.getString("id");
                    if (!map.containsKey(id)) {
                        map.put(id, new HashMap<String, boolean[]>());
                    }
                    Map<String, boolean[]> emails = map.get(id);
                    String email_key = rs.getString("email") + "#" + rs.getString("confidence");
                    emails.put(email_key, new boolean[2]);

                    String feedback = rs.getString("feedback");
                    if (feedback.equals("instance_yes")) {
                        emails.get(email_key)[0] = true;
                    } else emails.get(email_key)[0] = false;

                    String real = rs.getString("real");
                    if (real.equals("correct")) {
                        emails.get(email_key)[1] = true;
                    } else emails.get(email_key)[1] = false;
                }

                // go through and append each row to the csv file
                for (Map.Entry<String, Map<String, Map<String, boolean[]>>> entry : answers.entrySet()) {
                    System.out.println("Condition: " + entry.getKey());
                    System.out.println("number of users: " + entry.getValue().size());

                    int count_unsure = 0; // per condition
                    int count_correct = 0;
                    int count_incorrect = 0;

                    // per condition
                    int thought_correct_model_correct = 0;
                    int thought_correct_model_incorrect = 0;
                    int thought_incorrect_model_correct = 0;
                    int thought_incorrect_model_incorrect = 0;
                    int thought_unsure_model_correct = 0;
                    int thought_unsure_model_incorrect = 0;

                    int thought_unsure_said_correct = 0;
                    int thought_unsure_said_incorrect = 0;

                    for (Map.Entry<String, Map<String, boolean[]>> user : entry.getValue().entrySet()) {
                        for (Map.Entry<String, boolean[]> email : user.getValue().entrySet()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(entry.getKey() + ","); // Condition
                            sb.append(user.getKey() + ","); // UserID

                            String email_name = email.getKey().substring(0, email.getKey().indexOf("#"));
                            sb.append(email_name + ","); // Email

                            if (email.getValue()[1]) sb.append("model_correct,");
                            else sb.append("model_incorrect,");

//                            if (email.getValue()[0]) sb.append("fdbk_correct,");
//                            else sb.append("fdbk_incorrect,");

                            String feedbackus = email.getKey().substring(email.getKey().indexOf("#") + 1);
                            String displayed_label;

                            if (email_name.contains("h") && email_name.contains("success")) displayed_label = "hockey";
                            else if (email_name.contains("h") && email_name.contains("failed")) displayed_label = "baseball";
                            else if (email_name.contains("b") && email_name.contains("success")) displayed_label = "baseball";
                            else displayed_label = "hockey"; // (email_name.contains("b") && email_name.contains("failed"))

                            String user_thought;

                            if (feedbackus.equals("unsure")) user_thought = feedbackus;
                            else if (feedbackus.equals(displayed_label)) user_thought = "correct";
                            else user_thought = "incorrect"; // (!feedbackus.equals(displayed_label))

                            sb.append("thought_" + user_thought + ","); // User_Thought r/w/u

                            if (user_thought.equals("unsure")) count_unsure++;
                            if (user_thought.equals("correct")) count_correct++;
                            if (user_thought.equals("incorrect")) count_incorrect++;

                            if (user_thought.equals("correct") && !email.getValue()[0]) {
                                System.out.println(user.getKey() + ": " + email_name + " [thought correct, feedback incorrect]");
                            } else if (user_thought.equals("incorrect") && email.getValue()[0]) {
                                System.out.println(user.getKey() + ": " + email_name + " [thought incorrect, feedback correct]");
                            }

                            if (email.getValue()[0]) sb.append("fdbk_correct");
                            else sb.append("fdbk_incorrect");

                            sb.append("\n");
                            writer.write(sb.toString());

                            if (user_thought.equals("correct")) {
                                if (email.getValue()[1]) thought_correct_model_correct++;
                                else thought_correct_model_incorrect++;
                            } else if (user_thought.equals("incorrect")) {
                                if (email.getValue()[1]) thought_incorrect_model_correct++;
                                else thought_incorrect_model_incorrect++;
                            } else if (user_thought.equals("unsure")) {
                                if (email.getValue()[1]) thought_unsure_model_correct++;
                                else thought_unsure_model_incorrect++;

                                if (email.getValue()[0]) thought_unsure_said_correct++;
                                else thought_unsure_said_incorrect++;
                            }
                        }
                    }
                    System.out.println(count_unsure + " unsure");
                    System.out.println(count_correct + " correct");
                    System.out.println(count_incorrect + " incorrect");
                    System.out.println("understood model accuracy: " + thought_correct_model_correct + " [model correct], " + thought_incorrect_model_incorrect + " [model incorrect]");
                    System.out.println("misunderstood model accuracy: " + thought_incorrect_model_correct + " [model correct], " + thought_correct_model_incorrect + " [model incorrect]");
                    System.out.println("unsure: " + thought_unsure_model_correct + " [model correct], " + thought_unsure_model_incorrect + " [model incorrect]");
                    System.out.println("feedback when unsure: " + thought_unsure_said_correct + " [said correct], " + thought_unsure_said_incorrect + " [said incorrect]");
                    System.out.println();
                }
            } catch (SQLException e) {
                System.out.println(query);
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void compareFeature() {

        String query = "select a.condition as condition, b.id as id, b.email as email, b.chosen as chosen, b.feedbackus as confidence, b.instance as instance from exp_demographics as a inner join exp_email as b on a.id = b.id where a.pilot is true and a.created > '2019-08-12 00:00:00' and (a.condition = '_feature' or a.condition = '_feature_explain') and b.mode = 'train' and (a.id = '7a7e0083-4f9c-40a9-9eb7-f34a1726de7a' or a.id = '478dbbbc-a154-4014-a154-6492f0e40fa2' or a.id = '0ce18d0a-6c6b-415b-bcc2-884b0235f7c1');";

        try (PrintWriter writer = new PrintWriter(new File("compare_feature.csv"))) {

            try (Connection conn = DriverManager.getConnection(url, props)) {
                PreparedStatement stmt = conn.prepareStatement(query);
                log.info("Executing " + stmt.toString());
                ResultSet rs = stmt.executeQuery();

                // condition -> userID -> email#confidence@instance -> list of chosen words (3)
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
                    String email_key = rs.getString("email") + "#" + rs.getString("confidence") + "@" + rs.getString("instance");
                    Array words = rs.getArray("chosen");
                    safePut3(map.get(id), email_key, Arrays.asList((String[]) words.getArray()));
                }

                for (Map.Entry<String, Map<String, Map<String, List<String>>>> entry : answers.entrySet()) {
                    System.out.println("Condition: " + entry.getKey());
//                    System.out.println("Users: " + entry.getValue().size());

                    // per condition
                    int count_unsure = 0;
                    int count_correct = 0;
                    int count_incorrect = 0;

                    int thought_correct_model_correct = 0;
                    int thought_correct_model_incorrect = 0;
                    int thought_incorrect_model_correct = 0;
                    int thought_incorrect_model_incorrect = 0;
                    int thought_unsure_model_correct = 0;
                    int thought_unsure_model_incorrect = 0;

                    int overall_words_matching = 0;
                    int overall_words_different = 0;

                    Map<String, Integer[]> user_matches = new HashMap<>(); // map userid#email -> [number of matches (0-3), confidence code]
                    // confidence code: 1 = unsure, 2 = correct, 3 = incorrect

                    for (Map.Entry<String, Map<String, List<String>>> user : entry.getValue().entrySet()) {
//                        System.out.println("Emails: " + user.getValue().size());

                        for (Map.Entry<String, List<String>> email : user.getValue().entrySet()) {
                            // columns: condition, user id, email, model r/w, user_thought r/w/u, words_match, words_diff, words_leftover
                            StringBuilder sb = new StringBuilder();
                            sb.append(entry.getKey() + ","); // Condition
                            sb.append(user.getKey() + ","); // UserID

                            String email_name = email.getKey().substring(0, email.getKey().indexOf("#"));
                            sb.append(email_name + ","); // Email

                            if (!user_matches.containsKey(user.getKey())) {
                                user_matches.put(user.getKey() + "#" + email_name, new Integer[2]);
                            }

                            // Model r/w
                            boolean model_correct = true;
                            if (email.getKey().substring(0, email.getKey().indexOf("#")).indexOf("success") >= 0) {
                                sb.append("model_correct,");
                            } else {
                                sb.append("model_incorrect,");
                                model_correct = false;
                            }

                            String feedbackus = email.getKey().substring(email.getKey().indexOf("#") + 1, email.getKey().indexOf("@"));
                            String displayed_label;

                            if (email_name.contains("h") && email_name.contains("success")) displayed_label = "hockey";
                            else if (email_name.contains("h") && email_name.contains("failed")) displayed_label = "baseball";
                            else if (email_name.contains("b") && email_name.contains("success")) displayed_label = "baseball";
                            else displayed_label = "hockey"; // (email_name.contains("b") && email_name.contains("failed"))

                            String user_thought;

                            if (feedbackus.equals("unsure")) user_thought = feedbackus;
                            else if (feedbackus.equals(displayed_label)) user_thought = "correct";
                            else user_thought = "incorrect"; // (!feedbackus.equals(displayed_label))

//                            System.out.println("user thought: " + user_thought);
                            sb.append("thought_" + user_thought + ","); // User_Thought r/w/u

                            String instance = "";
                            if (email.getKey().substring(email.getKey().indexOf("@") + 1).equals("instance_no")) { // right button
                                instance = "baseball";
                            } else if (email.getKey().substring(email.getKey().indexOf("@") + 1).equals("instance_yes")) { // left button
                                instance = "hockey";
                            }
                            sb.append(instance + ","); // Instance b/h

                            if (user_thought.equals("unsure")) {
                                user_matches.get(user.getKey() + "#" + email_name)[1] = 1;
                                count_unsure++;
                                if (model_correct) thought_unsure_model_correct++;
                                else thought_unsure_model_incorrect++;
                            }
                            if (user_thought.equals("correct")) {
                                user_matches.get(user.getKey() + "#" + email_name)[1] = 2;
                                count_correct++;
                                if (model_correct) thought_correct_model_correct++;
                                else thought_correct_model_incorrect++;
                            }
                            if (user_thought.equals("incorrect")) {
                                user_matches.get(user.getKey() + "#" + email_name)[1] = 3;
                                count_incorrect++;
                                if (model_correct) thought_incorrect_model_correct++;
                                else thought_incorrect_model_incorrect++;
                            }

                            // Compare user-selected words with the words that the model (would have) highlighted
                            List<String> model_words = modelHL.get(email_name);
                            List<String> matching = new ArrayList<String>();
                            List<String> different = new ArrayList<String>();
                            List<String> leftover = new ArrayList<String>();
                            leftover.addAll(model_words);

                            int matches = 0;
                            for (String s : email.getValue()) {
                                if (model_words.contains(s)) {
                                    matching.add(s);
                                    leftover.remove(s);
                                    overall_words_matching++;
                                    matches++;
                                } else {
                                    different.add(s);
                                    overall_words_different++;
                                }
                            }
                            user_matches.get(user.getKey() + "#" + email_name)[0] = matches;

                            sb.append(formatWordList(matching));
                            sb.append(",");
                            sb.append(formatWordList(different));
                            sb.append(",");
                            sb.append(formatWordList(leftover));

                            sb.append("\n");
                            writer.write(sb.toString());
                        }
                    }
                    System.out.println(count_unsure + " unsure");
                    System.out.println(count_correct + " correct");
                    System.out.println(count_incorrect + " incorrect");
                    System.out.println("understood model accuracy: " + thought_correct_model_correct + " [model correct], " + thought_incorrect_model_incorrect + " [model incorrect]");
                    System.out.println("misunderstood model accuracy: " + thought_incorrect_model_correct + " [model correct], " + thought_correct_model_incorrect + " [model incorrect]");
                    System.out.println("unsure: " + thought_unsure_model_correct + " [model correct], " + thought_unsure_model_incorrect + " [model incorrect]");
                    System.out.println("overall matching words selected: " + overall_words_matching);
                    System.out.println("overall different words selected: " + overall_words_different);

                    int[] no_m = new int[4];
                    int[] one_m = new int[4];
                    int[] two_m = new int[4];
                    int[] three_m = new int[4];
                    for (String user : user_matches.keySet()) {
                        int m = user_matches.get(user)[0];
                        int conf_code = user_matches.get(user)[1];
                        if (m == 0) {
                            no_m[0]++;
                            no_m[conf_code]++;
//                            System.out.println("no matches in: " + user);
                        } else if (m == 1) {
                            one_m[0]++;
                            one_m[conf_code]++;
                        } else if (m == 2) {
                            two_m[0]++;
                            two_m[conf_code]++;
                        } else if (m == 3) {
                            three_m[0]++;
                            three_m[conf_code]++;
//                            System.out.println("all matches in: " + user);
                        }
                    }

                    int total = no_m[0] + one_m[0] + two_m[0] + three_m[0];
                    System.out.println("\ntotal user/email pairings: " + total);
                    System.out.println("no matches: " + no_m[0] + " (" + ((double) no_m[0] / total * 100) + "%) -- user thought: " + no_m[1] + " unsure, " + no_m[2] + " correct, " + no_m[3] + " incorrect");
                    System.out.println("one match: " + one_m[0] + " (" + ((double) one_m[0] / total * 100) + "%) -- user thought: " + one_m[1] + " unsure, " + one_m[2] + " correct, " + one_m[3] + " incorrect");
                    System.out.println("two matches: " + two_m[0] + " (" + ((double) two_m[0] / total * 100) + "%) -- user thought: " + two_m[1] + " unsure, " + two_m[2] + " correct, " + two_m[3] + " incorrect");
                    System.out.println("all matches: " + three_m[0] + " (" + ((double) three_m[0] / total * 100) + "%) -- user thought: " + three_m[1] + " unsure, " + three_m[2] + " correct, " + three_m[3] + " incorrect");
                    System.out.println();
                }
            } catch (SQLException e) {
                System.out.println(query);
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String formatWordList(List<String> list) {
        String result = "";
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size() - 1; i++) {
                result += list.get(i) + " ";
            }
            result += list.get(list.size() - 1);
        }
        return result;
    }
}
