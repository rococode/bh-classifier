package edu.washington.hcde.leah;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class SelectionProcess {

    //         voiceinteraction voicepw123 voiceinteraction (db name)
    // voiceinteractiondb voicepw123 voiceuser
    // psql --host=voiceinteractiondb.cjt2uxgbtzyl.us-west-2.rds.amazonaws.com  --port=5432 --username voiceuser --password --dbname=voiceinteractiondb

    private static String url;
    private static Properties props;

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

    private static void safePut4(Map<String, Map<String, List<String>>> map, String id, String email, String entry) {
        if (!map.containsKey(id)) {
            map.put(id, new HashMap<String, List<String>>());
        }
        Map<String, List<String>> emails = map.get(id);
//        if (!emails.containsKey(email)) {
            safePut(emails, email, entry);
//        }
    }

    public static void countWordsSelected() {
//        String query = "select * from exp_demographics as a inner join exp_openq as b on a.id = b.id where a.pilot is not true;";
//        String query = "select a.condition, b.how_decide, b.overall, b.frustration_why, b.trust_why, b.recommend_why, c.perf_why from exp_demographics as a inner join exp_openq as b on a.id = b.id inner join exp_perf as c on b.id = c.id where a.pilot is not true;";

//        String query = "select a.condition as condition, b.email as email, b.chosen as chosen from exp_demographics as a inner join exp_email as b on a.id = b.id where a.pilot is not true;";

        String query = "select a.condition as condition, a.id as id, b.email as email, b.entry as entry from exp_demographics as a inner join exp_log as b on a.id = b.id where a.pilot is not true and a.dq is not true;";
        Map<String, Map<String, Map<String, List<String>>>> answers = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(url, props)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            log.info("Executing " + stmt.toString());
            ResultSet rs = stmt.executeQuery();
//            Map<String, Map<String, Map<String, List<String>>>> answers = new HashMap<>();
            // map condition -> userID -> email -> entries (toggled on/off)
            while (rs.next()) {
                String condition = rs.getString("condition");
                if (!answers.containsKey(condition)) {
                    answers.put(condition, new HashMap<>());
                }
                Map<String, Map<String, List<String>>> map = answers.get(condition);
                safePut4(map, rs.getString("id"), rs.getString("email"), rs.getString("entry"));
            }
            for (Map.Entry<String, Map<String, Map<String, List<String>>>> entry : answers.entrySet()) {
                System.out.println("Condition: \'" + entry.getKey() + "\'");
                System.out.println("number of users: " + entry.getValue().size());
                for (Map.Entry<String, Map<String, List<String>>> e : entry.getValue().entrySet()) {
                    System.out.println("\t" + e.getKey()); // user
//                    System.out.println("\tnumber of emails: " + e.getValue().size());
                    for (Map.Entry<String, List<String>> toggled : e.getValue().entrySet()) {
                        System.out.print("\t\t" + toggled.getKey()); // email
                        int count_selected = 0;
//                        System.out.println("number of entries for this email: " + toggled.getValue().size());
                        for (String s : toggled.getValue()) {
                            if (s.contains("toggled_on")) {
                                count_selected++;
                            }
//                            System.out.println("\t\t" + count_selected);
                        }
                        System.out.println("(" + count_selected + ")");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }

        try (PrintWriter writer = new PrintWriter(new File("selection_process.csv"))) {
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<String, Map<String, Map<String, List<String>>>> entry : answers.entrySet()) {
//                sb.append(entry.getKey() + ",");
//                System.out.println("number of users: " + entry.getValue().size());
                for (Map.Entry<String, Map<String, List<String>>> e : entry.getValue().entrySet()) {
//                    sb.append(e.getKey() + ","); // user
//                    System.out.println("\tnumber of emails: " + e.getValue().size());
                    for (Map.Entry<String, List<String>> toggled : e.getValue().entrySet()) {
//                        sb.append(toggled.getKey() + ","); // email
                        int count_selected = 0;
//                        System.out.println("number of entries for this email: " + toggled.getValue().size());
                        for (String s : toggled.getValue()) {
                            if (s.contains("toggled_on")) {
                                count_selected++;
                            }
//                            System.out.println("\t\t" + count_selected);
                        }
                        sb.append(entry.getKey() + ",");
                        sb.append(e.getKey() + ",");
                        sb.append(toggled.getKey() + ",");
                        sb.append(count_selected + "");
                        sb.append("\n");
                    }
                }
            }

            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
