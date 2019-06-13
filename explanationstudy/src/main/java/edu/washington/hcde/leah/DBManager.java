package edu.washington.hcde.leah;

import java.sql.*;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.locks.Condition;

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
            if (rs.next()) {
                log.info("demographics entries #: " + rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveEmail(UserSession session, String mode, String modeNum, String feedbackUs, String[] chosen, String instance, String testSelf, String testModel) {
        String query = "INSERT INTO exp_email (id, started, mode, modenum, email, feedbackUs, chosen, instance, test_self, test_model) VALUES (?, ?, ?, ?, ? ,?, ?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(url, props)) {
            // TODO: do prep statement properly lol
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setObject(1, session.getUUID());
            stmt.setTimestamp(2, session.lastServe);
            stmt.setString(3, mode);
            stmt.setString(4, modeNum);
            stmt.setString(5, session.currentEmail.id);
            stmt.setString(6, feedbackUs);
            stmt.setArray(7, conn.createArrayOf("varchar", chosen));
            stmt.setString(8, instance);
            stmt.setString(9, testSelf);
            stmt.setString(10, testModel);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

    public static void logEvent(UserSession session, String event) {
        String query = "INSERT INTO exp_log (id, entry, email) VALUES (?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(url, props)) {
            // TODO: do prep statement properly lol
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setObject(1, session.getUUID());
            stmt.setString(2, event);
            stmt.setString(3, session.currentEmail.id);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

    public static void saveDemographics(UserSession session, String age, String education, String gender, String pc, String ml, String hockey, String baseball) {
        String query = "INSERT INTO exp_demographics (age, education, gender, pc, ml, hockey, baseball, condition) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        try (Connection conn = DriverManager.getConnection(url, props)) {
            // TODO: do prep statement properly lol
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, age);
            stmt.setString(2, education);
            stmt.setString(3, gender);
            stmt.setString(4, pc);
            stmt.setString(5, ml);
            stmt.setString(6, hockey);
            stmt.setString(7, baseball);
            stmt.setString(8, session.condition);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                session.setUUID(UUID.fromString(rs.getString("id")));
                log.info("Set uuid for session " + session.localid + " to " + session.getUUID());
            }
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

    public static void savePerf(UserSession session, String perf, String perf_why) {
        String query = "INSERT INTO exp_perf (id, perf, perf_why) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, props)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setObject(1, session.getUUID());
            stmt.setString(2, perf);
            stmt.setString(3, perf_why);
            log.info("Executing " + stmt.toString());
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

    public static void saveFinal(UserSession session, String how_decide, String perceived_accuracy, String understanding, String teaming, String frustration, String frustration_why, String trust, String trust_why, String recommend, String recommend_why, String overall) {
        String query = "INSERT INTO exp_openq (id, how_decide, perceived_accuracy, understanding, teaming, frustration, frustration_why, trust, trust_why, recommend, recommend_why, overall) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, props)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setObject(1, session.getUUID());
            stmt.setString(2, how_decide);
            stmt.setString(3, perceived_accuracy);
            stmt.setString(4, understanding);
            stmt.setString(5, teaming);
            stmt.setString(6, frustration);
            stmt.setString(7, frustration_why);
            stmt.setString(8, trust);
            stmt.setString(9, trust_why);
            stmt.setString(10, recommend);
            stmt.setString(11, recommend_why);
            stmt.setString(12, overall);
            log.info("Executing " + stmt.toString());
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }
}
