package edu.washington.hcde.leah;

import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Spark;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Launcher {

    private static final Map<String, UserSession> sessions = new HashMap<>();

    private static String getSessionIdentifier(spark.Request req) {
        return req.session().id();
    }

    private static UserSession getOrCreateSession(spark.Request req) {
        String sessionId = getSessionIdentifier(req);
        if (sessions.containsKey(sessionId)) {
            return sessions.get(sessionId);
        }
        UserSession session = new UserSession(sessionId);
        String mode = req.queryParams("z");
        if (mode != null) {
            switch (mode) {
                case "1":
                    session.condition = "";
                    break;
                case "2":
                    session.condition = "_explain";
                    break;
                case "3":
                    session.condition = "_instance";
                    break;
                case "4":
                    session.condition = "_instance_explain";
                    break;
                case "5":
                    session.condition = "_feature";
                    break;
                case "6":
                    session.condition = "_feature_explain";
                    break;
            }
            log.info("gave new session condition: " + session.condition);
        } else {
            log.info("no query condition detected, giving default: " + session.condition);
        }
        sessions.put(sessionId, session);
        return session;
    }

    private static UserSession getSession(spark.Request req) {
        String sessionId = getSessionIdentifier(req);
        if (sessions.containsKey(sessionId)) {
            return sessions.get(sessionId);
        }
        return null;
    }

    private static boolean hasSession(spark.Request req) {
        String sessionId = getSessionIdentifier(req);
        return sessions.containsKey(sessionId);
    }

    public static boolean handleEmail(spark.Request req, UserSession session, String mode, String modeNum) {
        log.info("handle-queryparams: " + req.queryParams().toString());
        for (String s : req.queryParams()) {
            log.info(s + ": " + req.queryParams(s));
        }
        log.info("params: " + req.params().toString());

        String chosenParam = req.queryParams("chosen");

        String[] chosen = null;
        if (chosenParam != null) {
            JSONArray chosen_words = new JSONArray(chosenParam);
            chosen = chosen_words.toList().toArray(new String[chosen_words.length()]);
            log.info("Chosen: " + Arrays.toString(chosen));
            if (chosen.length != 3) {
                log.info("ERROR: chosen length was not 3 for user " + session.getUUID() + " (length was " + chosen.length + ")");
            }
        }

        DBManager.saveEmail(
                session,
                mode,
                modeNum,
                req.queryParams("feedback"),
                chosen,
                req.queryParams("instance"),
                req.queryParams("test_self"),
                req.queryParams("test_model")
        );
        return true;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        DBManager.initialize();
        EmailManager.initialize();

        Spark.port(80);
        Spark.staticFileLocation("/web");

        Spark.get("/", (req, res) -> {
            UserSession session = getOrCreateSession(req);
            log.info("mode at /: " + session.mode);
            return loadMode(session.mode, req, session);
        });
        Spark.post("/log", (req, res) -> {
            UserSession session = getSession(req);
            if (session == null) {
                log.info("WARNING: " + session + " called log while not existing.");
                res.redirect("/");
                return "";
            }
            String event = req.queryParams("event");
            log.info("Got event: " + event);
            DBManager.logEvent(session, event);
            return "";
        });
        Spark.get("/back", (req, res) -> {
            UserSession session = getSession(req);
            if (session == null) {
                log.info("WARNING: " + session + " called back while not existing.");
                res.redirect("/");
                return "";
            }
            log.info("mode at /back: " + session.mode);
            switch (session.mode) {
                case "intro2":
                    session.mode = "intro1";
                    break;
                case "intro3":
                    session.mode = "intro2";
                    break;
                default:
                    log.info("WARNING: unrecognized mode " + session.mode + " for " + session);
            }
            res.redirect("/");
            return "";
        });
        Spark.get("/next", (req, res) -> {
            UserSession session = getSession(req);
            if (session == null) {
                log.info("WARNING: " + session + " called next while not existing.");
                res.redirect("/");
                return "";
            }
            log.info("mode at /next: " + session.mode);
            if (System.currentTimeMillis() - session.lastNext < 1000) {
                log.info("Preventing double click at " + session.mode + " for " + session.toString());
                res.redirect("/");
                return "";
            }
            session.lastNext = System.currentTimeMillis();
            switch (session.mode) {
                case "demographics":
                    log.info("queryparams: " + req.queryParams().toString());
                    for (String s : req.queryParams()) {
                        log.info(s + ": " + req.queryParams(s));
                    }
                    log.info("params: " + req.params().toString());
                    DBManager.saveDemographics(
                            session,
                            req.queryParams("age"),
                            req.queryParams("education"),
                            req.queryParams("gender"),
                            req.queryParams("pc"),
                            req.queryParams("ml"),
                            req.queryParams("hockey"),
                            req.queryParams("baseball")
                    );
                    session.mode = "pintro";
                    break;
                case "intro1":
                    session.mode = "intro2";
                    // instant skip for quick debugging
//                    session.mode = "train";
                    break;
                case "intro2":
                    session.mode = "intro3";
                    break;
                case "intro3":
                    session.mode = "demographics";
//                    session.mode = "openq";
                    break;
                case "pintro":
                    session.mode = "practice";
                    break;
                case "practice":
                    handleEmail(req, session, "practice", "practice-0");
                    session.mode = "instructions";
                    session.loadNextEmail();
                    break;
                case "instructions":
                    session.mode = "train";
                    break;
                case "train":
                    handleEmail(req, session, "train", "train-" + session.trainIdx);
                    session.loadNextEmail();
                    if (session.finishedTrain()) {
                        session.mode = "tinstructions";
                    } else {
                        session.mode = "train";
                    }
                    break;
                case "tinstructions":
                    session.mode = "test";
                    DBManager.savePerf(session,
                            req.queryParams("perf"),
                            req.queryParams("perf_why"));
                    break;
                case "test":
                    handleEmail(req, session, "test", "test-" + session.testIdx);
                    session.loadNextEmail();
                    if (session.finishedTest()) {
                        session.mode = "openq";
                    } else {
                        session.mode = "test";
                    }
                    break;
                case "openq":
                    log.info("openq for " + session.getUUID());
//                    log.info("openq-queryparams: " + req.queryParams().toString());
//                    for (String s : req.queryParams()) {
//                        log.info(s + ": " + req.queryParams(s));
//                    }
//                    log.info("params: " + req.params().toString());
                    DBManager.saveFinal(
                            session,
                            req.queryParams("how_decide"),
                            req.queryParams("perceived_accuracy"),
                            req.queryParams("understanding"),
                            req.queryParams("teaming"),
                            req.queryParams("frustration"),
                            req.queryParams("frustration_why"),
                            req.queryParams("trust"),
                            req.queryParams("trust_why"),
                            req.queryParams("recommend"),
                            req.queryParams("recommend_why"),
                            req.queryParams("overall")
                    );
                    session.mode = "complete";
                    break;
                default:
                    log.info("WARNING: unrecognized mode " + session.mode + " for " + session);
            }
            res.redirect("/");
            return "";
        });

        Spark.post("/submit", (req, res) -> {
            if (!hasSession(req)) {
                res.redirect("/");
            }
            UserSession session = getSession(req);
            int gist = Integer.parseInt(req.queryParams("q-gist"));
            int multitask = Integer.parseInt(req.queryParams("q-multitask"));
            int each = Integer.parseInt(req.queryParams("q-each"));
//            int focus = Integer.parseInt(req.queryParams("q-focus"));
            int spd = Integer.parseInt(req.queryParams("q-spd"));
            Timestamp now = Timestamp.from(Instant.now());
            log.info(session.lastScenarioRequest);
            long diff = now.getTime() - session.lastScenarioRequest.getTime();
            int seconds = (int) (diff / 1000);
            log.info("time elapsed: " + diff + " = " + seconds + " seconds");
//            DBManager.saveScenario(session, gist, multitask, each, spd, seconds, now);
            session.numAnswered += 1;
            log.info("Session " + session.localid + " submitted on #" + session.numAnswered);
//            if (session.numAnswered >= Email.values().length) {
//                res.redirect("/final-questions");
//            } else {
//                res.redirect("/module");
//            }
            return "";
        });

        log.info("Initialized Spark");
        while (true) {

        }
    }

    private static String loadMode(String mode, Request req, UserSession session) {
        log.info("Loading index with mode " + mode + " for " + req.ip());
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("web/module.html");
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scan = new Scanner(stream);
//            Scanner scan = new Scanner(f);
            while (scan.hasNextLine()) {
                sb.append(scan.nextLine());
                sb.append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (session.currentEmail == null) {
            session.loadNextEmail();
        }
        log.info("Serving " + session.currentEmail.id);
        String sender = session.currentEmail.sender;
        String subject = session.currentEmail.subject;
        String email = session.currentEmail.content;
        JSONObject o = new JSONObject();
        o.put("sender", sender);
        o.put("subject", subject);
        o.put("id", session.mode.equals("complete") ? session.getUUID().toString() : "");
        o.put("email", email);
        o.put("pred", session.currentEmail.pred);
        o.put("curr_idx", (session.finishedTrain() ? session.testIdx : session.trainIdx));
        o.put("total_idx", session.finishedTrain() ? session.totalTest : session.totalTrain);
        o.put("explain_words", session.currentEmail.words);
        o.put("mode", mode + session.condition);
        return sb.toString().replace("\"@DATA_INJECTION@\"", o.toString());
    }

}
