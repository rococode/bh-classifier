package edu.washington.hcde.leah;

import com.sun.org.glassfish.external.statistics.TimeStatistic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class DBManager {

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

    public static void parseSpecificOpenQ() {
        try (PrintWriter writer = new PrintWriter(new File("openq_perf_why.csv"))) {
//        String query = "select * from exp_demographics as a inner join exp_openq as b on a.id = b.id where a.pilot is not true;";
            String query = "select a.condition, a.id, b.how_decide, c.accuracy_standard_why, c.frustration_why, c.trust_why, c.recommend_why, c.feedback_importance_why, c.overall, d.learn_why, d.perf_why from exp_demographics as a inner join exp_openq1 as b on a.id = b.id inner join exp_openq2 as c on b.id = c.id inner join exp_openq3 as d on c.id = d.id where a.pilot is not true and a.dq is not true and a.created > '2019-08-14 17:00:00';";
            try (Connection conn = DriverManager.getConnection(url, props)) {
                PreparedStatement stmt = conn.prepareStatement(query);
                log.info("Executing " + stmt.toString());
                ResultSet rs = stmt.executeQuery();
                Map<String, Map<String, List<String>>> answers = new HashMap<>(); // condition -> user id -> response
                while (rs.next()) {
                    String condition = rs.getString("condition");
                    if (!answers.containsKey(condition)) {
                        answers.put(condition, new HashMap<String, List<String>>());
                    }
                    Map<String, List<String>> map = answers.get(condition);
//                    safePut(map, rs.getString("id"), rs.getString("how_decide"));
//                    safePut(map, rs.getString("id"), rs.getString("accuracy_standard_why"));
//                    safePut(map, rs.getString("id"), rs.getString("frustration_why"));
//                    safePut(map, rs.getString("id"), rs.getString("trust_why"));
//                    safePut(map, rs.getString("id"), rs.getString("recommend_why"));
//                    safePut(map, rs.getString("id"), rs.getString("feedback_importance_why"));
//                    safePut(map, rs.getString("id"), rs.getString("overall"));
//                    safePut(map, rs.getString("id"), rs.getString("learn_why"));
                    safePut(map, rs.getString("id"), rs.getString("perf_why"));
                }
                for (Map.Entry<String, Map<String, List<String>>> entry : answers.entrySet()) {
                    System.out.println("Condition: \'" + entry.getKey() + "\'");
                    System.out.println("num users: " + entry.getValue().size());
                    for (Map.Entry<String, List<String>> e : entry.getValue().entrySet()) {
                        StringBuilder sb = new StringBuilder();
//                        sb.append(entry.getKey() + ","); // condition
//                        sb.append(e.getKey()); // user id

                        System.out.println("\t" + e.getKey());
                        for (String s : e.getValue()) {
//                            sb.append("," + s);
                            sb.append(s);
                            System.out.println("\t\t" + s);
                        }

                        sb.append("\n");
                        writer.write(sb.toString());
                    }
                }
            } catch (SQLException e) {
                System.out.println(query);
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void parseOpenQ() {
        try (PrintWriter writer = new PrintWriter(new File("openq_study2.csv"))) {
//        String query = "select * from exp_demographics as a inner join exp_openq as b on a.id = b.id where a.pilot is not true;";
            String query = "select a.condition, b.how_decide, c.accuracy_standard_why, c.frustration_why, c.trust_why, c.recommend_why, c.feedback_importance_why, c.overall, d.learn_why, d.perf_why from exp_demographics as a inner join exp_openq1 as b on a.id = b.id inner join exp_openq2 as c on b.id = c.id inner join exp_openq3 as d on c.id = d.id where a.pilot is not true and a.dq is not true and a.created > '2019-08-14 17:00:00';";
            try (Connection conn = DriverManager.getConnection(url, props)) {
                PreparedStatement stmt = conn.prepareStatement(query);
                log.info("Executing " + stmt.toString());
                ResultSet rs = stmt.executeQuery();
                Map<String, Map<String, List<String>>> answers = new HashMap<>();
                while (rs.next()) {
                    String condition = rs.getString("condition");
                    if (!answers.containsKey(condition)) {
                        answers.put(condition, new HashMap<String, List<String>>());
                    }
                    Map<String, List<String>> map = answers.get(condition);
                    safePut(map, "how_decide", rs.getString("how_decide"));
                    safePut(map, "accuracy_standard_why", rs.getString("accuracy_standard_why"));
                    safePut(map, "frustration_why", rs.getString("frustration_why"));
                    safePut(map, "trust_why", rs.getString("trust_why"));
                    safePut(map, "recommend_why", rs.getString("recommend_why"));
                    safePut(map, "feedback_importance_why", rs.getString("feedback_importance_why"));
                    safePut(map, "overall", rs.getString("overall"));
                    safePut(map, "learn_why", rs.getString("learn_why"));
                    safePut(map, "perf_why", rs.getString("perf_why"));
                }
                for (Map.Entry<String, Map<String, List<String>>> entry : answers.entrySet()) {
                    System.out.println("Condition: \'" + entry.getKey() + "\'");
                    for (Map.Entry<String, List<String>> e : entry.getValue().entrySet()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(entry.getKey() + ","); // condition
                        sb.append(e.getKey()); // user id

                        System.out.println("\t" + e.getKey());
                        for (String s : e.getValue()) {
                            sb.append("," + s);
                            System.out.println("\t\t" + s);
                        }

                        sb.append("\n");
                        writer.write(sb.toString());
                    }
                }
            } catch (SQLException e) {
                System.out.println(query);
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void getVals() {
//        String query = "select * from exp_demographics as a inner join exp_openq as b on a.id = b.id where a.pilot is not true;";
        String query = "select a.condition, a.id, b.perceived_accuracy, b.understanding, c.accuracy_standard, c.frustration, c.trust, c.recommend, c.feedback_importance, d.learn, d.perf from exp_demographics as a inner join exp_openq1 as b on a.id = b.id inner join exp_openq2 as c on b.id = c.id inner join exp_openq3 as d on c.id = d.id where a.pilot is not true and a.dq is not true and a.created > '2019-08-14 17:00:00' and a.id != '53cec591-6adf-4630-8dfa-4af235aec433' order by a.condition;";
        try (Connection conn = DriverManager.getConnection(url, props)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            log.info("Executing " + stmt.toString());
            ResultSet rs = stmt.executeQuery();
            Map<String, List<Map<String, String>>> answers = new HashMap<>();
            int participants = 0;
            while (rs.next()) {
                String condition = rs.getString("condition");
                String id = rs.getString("id");
                if (!answers.containsKey(condition + "#" + id)) {
                    answers.put(condition + "#" + id, new ArrayList<Map<String, String>>());
                }
                Map<String, String> map = new HashMap<>();
                safePut2(map, "perceived_accuracy", rs.getString("perceived_accuracy"));
                safePut2(map, "understanding", rs.getString("understanding"));
                safePut2(map, "accuracy_standard", rs.getString("accuracy_standard"));
                safePut2(map, "frustration", rs.getString("frustration"));
                safePut2(map, "trust", rs.getString("trust"));
                safePut2(map, "recommend", rs.getString("recommend"));
                safePut2(map, "feedback_importance", rs.getString("feedback_importance"));
                safePut2(map, "learn", rs.getString("learn"));
                safePut2(map, "perf", rs.getString("perf"));
                answers.get(condition + "#" + id).add(map);
                participants += 1;
            }
            Function<String, String> fn = (x) -> {
                int idx = x.indexOf("-");
                x = x.substring(idx + 1);
                return x;
            };

            System.out.println("condition\tid\tperceived_accuracy\tunderstanding\tfrustration\ttrust\trecommend\tfeedback_importance\tlearn\tperf");
            try (PrintWriter writer = new PrintWriter(new File("likert_study2.csv"))) {
                for (Map.Entry<String, List<Map<String, String>>> entry : answers.entrySet()) {
                    String condition = entry.getKey().substring(0, entry.getKey().indexOf("#"));
                    if (condition.length() == 0) {
                        condition = "empty";
                    }
                    String user = entry.getKey().substring(entry.getKey().indexOf("#") + 1);


                        StringBuilder sb = new StringBuilder();
                        for (Map<String, String> m : entry.getValue()) {
                            sb.append(condition + ",");
                            sb.append(user + ",");
                            sb.append(m.get("perceived_accuracy") + ",");
                            sb.append(m.get("understanding") + ",");
                            sb.append(m.get("accuracy_standard") + ",");
                            sb.append(m.get("frustration") + ",");
                            sb.append(m.get("trust") + ",");
                            sb.append(m.get("recommend") + ",");
                            sb.append(m.get("feedback_importance") + ",");
                            sb.append(m.get("learn") + ",");
                            sb.append(m.get("perf"));
                            sb.append("\n");
                            writer.write(sb.toString());

                            System.out.println(condition + "\t" + user + "\t"
                                    + fn.apply(m.get("perceived_accuracy")) + "\t"
                                    + fn.apply(m.get("understanding")) + "\t"
                                    + fn.apply(m.get("accuracy_standard")) + "\t"
                                    + fn.apply(m.get("frustration")) + "\t"
                                    + fn.apply(m.get("trust")) + "\t"
                                    + fn.apply(m.get("recommend")) + "\t"
                                    + fn.apply(m.get("feedback_importance")) + "\t"
                                    + fn.apply(m.get("learn")) + "\t"
                                    + fn.apply(m.get("perf"))
                            );
                        }
                        System.out.println("Total processed: " + participants + " participants");
    //                  System.exit(2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            for (Map.Entry<String, List<Map<String, String>>> entry : answers.entrySet()) {
                BiConsumer<String, String> fn2 = (field, condition) -> {
//                    System.out.println("For field: " + field);
                    Map<String, Integer> ct = new HashMap<>();
                    if (condition.length() == 0) {
                        condition = "empty";
                    }
                    System.out.println(condition + "\trating\tfreq");
                    for (Map<String, String> vals : entry.getValue()) {
                        String val = fn.apply(vals.get(field));
                        if (!ct.containsKey(val)) {
                            ct.put(val, 0);
                        }
                        ct.put(val, ct.get(val) + 1);
                    }
                    for (int k = 1; k <= 7; k++) {
                        System.out.println("\t" + k + "\t" + ct.getOrDefault(k + "", 0));
                    }
//                    for (Map.Entry<String, Integer> e : ct.entrySet()) {
//                        System.out.println(e.getKey() + "\t" + e.getValue());
//                    }
                };
            }
//                String condition = entry.getKey();
//                fn2.accept("perceived_accuracy", condition);
//                fn2.accept("frustration", condition);
//                fn2.accept("trust", condition);
//                fn2.accept("recommend", condition);
//                fn2.accept("understanding", condition);
//                fn2.accept("teaming", condition);
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

        public static void getOpenQNoCondition() {
//        String query = "select * from exp_demographics as a inner join exp_openq as b on a.id = b.id where a.pilot is not true;";
//        b.how_decide, c.accuracy_standard_why, c.frustration_why, c.trust_why, c.recommend_why, c.feedback_importance_why, c.overall, d.learn_why, d.perf_why from exp_demographics as a inner join exp_openq1 as b on a.id = b.id inner join exp_openq2 as c on b.id = c.id inner join exp_openq3 as d on c.id = d.id where a.pilot is true;
        String query = "select a.id, a.how_decide, b.accuracy_standard_why, b.frustration_why, b.trust_why, b.recommend_why, b.feedback_importance_why, b.overall, c.learn_why, c.perf_why from exp_openq1 as a inner join exp_openq2 as b on a.id = b.id inner join exp_openq3 as c on b.id = c.id inner join exp_demographics as d on c.id = d.id where d.pilot is true;";
        try (Connection conn = DriverManager.getConnection(url, props)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            log.info("Executing " + stmt.toString());
            ResultSet rs = stmt.executeQuery();
            List<Map<String, String>> answers = new ArrayList<>();
            Map<String, List<String>> allIds = new HashMap<>();
//            Map<String, List<String>> answers = new HashMap<>();
            String id;
            List<String> randIds = new ArrayList<>();
            for (int k = 10000; k < 99999; k++) {
                randIds.add(k + " ");
            }
            Collections.shuffle(randIds);

            ResultSetMetaData rsmd = rs.getMetaData();
            for (int k = 0; k < rsmd.getColumnCount(); k++) {
                System.out.println(rsmd.getColumnName(k + 1));
            }
            while (rs.next()) {
                List<String> myids = new ArrayList<>();
                Map<String, String> map = new HashMap<>();

                map.put("how_decide", rs.getString("how_decide"));
                id = randIds.remove(0);
                map.put("how_decide_id", id + "");
                myids.add(id + "");

                map.put("accuracy_standard_why", rs.getString("accuracy_standard_why"));
                id = randIds.remove(0);
                map.put("accuracy_standard_why_id", id + "");
                myids.add(id + "");

                map.put("frustration_why", rs.getString("frustration_why"));
                id = randIds.remove(0);
                map.put("frustration_why_id", id + "");
                myids.add(id + "");

                map.put("trust_why", rs.getString("trust_why"));
                id = randIds.remove(0);
                map.put("trust_why_id", id + "");
                myids.add(id + "");

                map.put("recommend_why", rs.getString("recommend_why"));
                id = randIds.remove(0);
                map.put("recommend_why_id", id + "");
                myids.add(id + "");

                map.put("feedback_importance_why", rs.getString("feedback_importance_why"));
                id = randIds.remove(0);
                map.put("feedback_importance_why_id", id + "");
                myids.add(id + "");

                map.put("overall", rs.getString("overall"));
                id = randIds.remove(0);
                map.put("overall_id", id + "");
                myids.add(id + "");

                map.put("learn_why", rs.getString("learn_why"));
//                System.out.println("lwhy: " + rs.getString("id") + ": " + rs.getString("learn_why"));
                id = randIds.remove(0);
                map.put("learn_why_id", id + "");
                myids.add(id + "");

                map.put("perf_why", rs.getString("perf_why"));
//                System.out.println("pwhy: " + rs.getString("id") + ": " + rs.getString("perf_why"));
                id = randIds.remove(0);
                map.put("perf_why_id", id + "");
                myids.add(id + "");

                answers.add(map);
                allIds.put(rs.getString("id"), myids);
            }
            Collections.shuffle(answers);
            System.out.println("unique ids:");
            for (Map.Entry<String, List<String>> e : allIds.entrySet()) {
                System.out.print(e.getKey());
                System.out.print(": ");
                System.out.println(e.getValue().toString());
            }
            Consumer<String> extractor = (q) -> {
                System.out.println();
                System.out.println();
                System.out.println(q);
                int ct = 0;
                List<String[]> ls = new ArrayList<>();
                for (Map<String, String> user : answers) {
                    // add each user's answers
                    String val = user.get(q);
                    if (val == null) {
                        // one person somehow submitted null on the perf..
                        val = "null";
                    }
                    ls.add(new String[]{val, user.get(q + "_id")});
                }
                Collections.shuffle(ls);
                for (String[] s : ls) {
//                    System.out.println(Arrays.toString(s));
                    String output = s[0].replace("\n", "\\n").replace("\t", "\\t");
                    System.out.println(output + "\t" + s[1]);
                    ct += 1;
                }
                System.out.println("printed " + ct);
            };
            extractor.accept("how_decide");
            extractor.accept("accuracy_standard_why");
            extractor.accept("frustration_why");
            extractor.accept("trust_why");
            extractor.accept("recommend_why");
            extractor.accept("feedback_importance_why");
            extractor.accept("overall");
            extractor.accept("learn_why");
            extractor.accept("perf_why");

        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

}
