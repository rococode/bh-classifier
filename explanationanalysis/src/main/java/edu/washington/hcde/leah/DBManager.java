package edu.washington.hcde.leah;

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

    public static void parseOpenQ() {
//        String query = "select * from exp_demographics as a inner join exp_openq as b on a.id = b.id where a.pilot is not true;";
        String query = "select a.condition, b.how_decide, b.overall, b.frustration_why, b.trust_why, b.recommend_why, c.perf_why from exp_demographics as a inner join exp_openq as b on a.id = b.id inner join exp_perf as c on b.id = c.id where a.pilot is not true;";
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
                safePut(map, "perf_why", rs.getString("perf_why"));
                safePut(map, "how_decide", rs.getString("how_decide"));
                safePut(map, "overall", rs.getString("overall"));
                safePut(map, "frustration_why", rs.getString("frustration_why"));
                safePut(map, "trust_why", rs.getString("trust_why"));
                safePut(map, "recommend_why", rs.getString("recommend_why"));
            }
            for (Map.Entry<String, Map<String, List<String>>> entry : answers.entrySet()) {
                System.out.println("Condition: \'" + entry.getKey() + "\'");
                for (Map.Entry<String, List<String>> e : entry.getValue().entrySet()) {
                    System.out.println("\t" + e.getKey());
                    for (String s : e.getValue()) {
                        System.out.println("\t\t" + s);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

    public static void getVals() {
//        String query = "select * from exp_demographics as a inner join exp_openq as b on a.id = b.id where a.pilot is not true;";
        String query = "select b.condition, frustration, perceived_accuracy, understanding, trust, teaming, recommend from exp_openq as a inner join exp_demographics as b on a.id = b.id where b.pilot is not true and b.dq is not true order by b.condition;";
        try (Connection conn = DriverManager.getConnection(url, props)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            log.info("Executing " + stmt.toString());
            ResultSet rs = stmt.executeQuery();
            Map<String, List<Map<String, String>>> answers = new HashMap<>();
            int participants = 0;
            while (rs.next()) {
                String condition = rs.getString("condition");
                if (!answers.containsKey(condition)) {
                    answers.put(condition, new ArrayList<Map<String, String>>());
                }
                Map<String, String> map = new HashMap<>();
                safePut2(map, "perceived_accuracy", rs.getString("perceived_accuracy"));
                safePut2(map, "understanding", rs.getString("understanding"));
                safePut2(map, "teaming", rs.getString("teaming"));
                safePut2(map, "frustration", rs.getString("frustration"));
                safePut2(map, "trust", rs.getString("trust"));
                safePut2(map, "recommend", rs.getString("recommend"));
                answers.get(condition).add(map);
                participants += 1;
            }
            Function<String, String> fn = (x) -> {
                int idx = x.indexOf("-");
                x = x.substring(idx + 1);
                return x;
            };
            /*
            System.out.println("condition\tperceived_accuracy\tunderstanding\tteaming\tfrustration\ttrust\trecommend");
            for (Map.Entry<String, List<Map<String, String>>> entry : answers.entrySet()) {
                String condition = entry.getKey();
                if(condition.length() == 0) {
                    condition = "empty";
                }
                for(Map<String, String> m : entry.getValue())  {
                    System.out.println(condition + "\t"
                            + fn.apply(m.get("perceived_accuracy")) + "\t"
                            + fn.apply(m.get("understanding")) + "\t"
                            + fn.apply(m.get("teaming")) + "\t"
                            + fn.apply(m.get("frustration")) + "\t"
                            + fn.apply(m.get("trust")) + "\t"
                            + fn.apply(m.get("recommend"))
                    );
                }
            }
            System.out.println("Total processed: " + participants + " participants");
//            System.exit(2);
*/

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
                String condition = entry.getKey();
//                fn2.accept("perceived_accuracy", condition);
//                fn2.accept("frustration", condition);
//                fn2.accept("trust", condition);
//                fn2.accept("recommend", condition);
//                fn2.accept("understanding", condition);
                fn2.accept("teaming", condition);
            }
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

    public static void getOpenQNoCondition() {
//        String query = "select * from exp_demographics as a inner join exp_openq as b on a.id = b.id where a.pilot is not true;";
        String query = "select a.id, a.how_decide, a.overall, a.frustration_why, a.trust_why, a.recommend_why, c.perf_why from exp_openq as a inner join exp_demographics as b on a.id = b.id inner join exp_perf as c on c.id = b.id where b.pilot is not true and b.dq is not true;";
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

                map.put("perf_why", rs.getString("perf_why"));
//                System.out.println("pwhy: " + rs.getString("id") + ": " + rs.getString("perf_why"));
                id = randIds.remove(0);
                map.put("perf_why_id", id + "");
                myids.add(id + "");

                map.put("how_decide", rs.getString("how_decide"));
                id = randIds.remove(0);
                map.put("how_decide_id", id + "");
                myids.add(id + "");

                map.put("overall", rs.getString("overall"));
                id = randIds.remove(0);
                map.put("overall_id", id + "");
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
            extractor.accept("perf_why");
            extractor.accept("how_decide");
            extractor.accept("overall");
            extractor.accept("frustration_why");
            extractor.accept("trust_why");
            extractor.accept("recommend_why");

        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

}
