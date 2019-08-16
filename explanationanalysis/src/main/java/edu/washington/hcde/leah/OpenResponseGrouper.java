package edu.washington.hcde.leah;

import javax.sound.midi.SysexMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class OpenResponseGrouper {

    private static String filepath = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\misc\\user_ids_and_question_ids.txt";
    private static String filepath2 = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\misc\\OpenQ_ perf_why.csv";

//    private static Map<String, Map<String, String>> ids_to_conditions_OLD = new HashMap<>(); // map user id -> question ids -> conditions
    private static Map<String, String> ids_to_conditions = new HashMap<>(); // qid -> condition
    private static Map<String, String> ids_to_users = new HashMap<>(); // qid -> user id
//    private static Map<String, Map<Integer, String[]>> ids_to_responses = new HashMap<>(); // map question id -> likert scale -> responses [learn, why, reason] (code1#code2)

    private static Map<String, Map<String, Map<String, String[]>>> conditions_to_responses = new HashMap<>(); // map condition -> user id -> question id -> responses [likert, learn, why, reason]
    public static void main(String[] args) {

        parseQuestionIDs();
        parsePerfWhyResponses();

        for (Map.Entry<String, Map<String, Map<String, String[]>>> entry : conditions_to_responses.entrySet()) {
            System.out.println("Condition: " + entry.getKey());

            int will_learn = 0;
            int will_not_learn = 0;
            int cond_learn = 0;

            int high_qual = 0;
            int low_qual = 0;
            int tricky_input = 0;
            int feedback = 0;
            int model_adapts = 0;
            int needs_feedback = 0;
            int marginal_feedback = 0;
            int bad_feedback = 0;

            int x_learn = 0;

            System.out.println("WHY responses from everyone who said x for Learn?");

            for (Map.Entry<String, Map<String, String[]>> user : entry.getValue().entrySet()) {
                for (Map.Entry<String, String[]> question : user.getValue().entrySet()) {
//                    System.out.println("\tuser id: " + user.getKey());
//                    System.out.println("\tqid: " + question.getKey());
//                    for (String s : question.getValue()) {
//                        System.out.println("\t\t" + s);
//                    }

                    if (question.getValue()[1].contains("x")) {
                        x_learn++;
                        if (question.getValue()[2].contains("HIGH_QUAL")) high_qual++;
                        if (question.getValue()[2].contains("LOW_QUAL")) low_qual++;
                        if (question.getValue()[2].contains("TRICKY_INPUT")) tricky_input++;
                        if (question.getValue()[2].contains("FEEDBACK")) feedback++;
                        if (question.getValue()[2].contains("MODEL_ADAPTS")) model_adapts++;
                        if (question.getValue()[2].contains("NEEDS_FEEDBACK")) needs_feedback++;
                        if (question.getValue()[2].contains("MARGINAL_FEEDBACK")) marginal_feedback++;
                        if (question.getValue()[2].contains("BAD_FEEDBACK")) bad_feedback++;
                        System.out.println("\t" + question.getValue()[3]);
                    }
                    if (question.getValue()[1].contains("WILL_LEARN")) {
                        will_learn++;
//                        if (question.getValue()[2].contains("HIGH_QUAL")) high_qual++;
//                        if (question.getValue()[2].contains("LOW_QUAL")) low_qual++;
//                        if (question.getValue()[2].contains("TRICKY_INPUT")) tricky_input++;
//                        if (question.getValue()[2].contains("FEEDBACK")) feedback++;
//                        if (question.getValue()[2].contains("MODEL_ADAPTS")) model_adapts++;
//                        if (question.getValue()[2].contains("NEEDS_FEEDBACK")) needs_feedback++;
//                        if (question.getValue()[2].contains("MARGINAL_FEEDBACK")) marginal_feedback++;
//                        if (question.getValue()[2].contains("BAD_FEEDBACK")) bad_feedback++;
//                        System.out.println("\t" + question.getValue()[3]);
                    }
                    if (question.getValue()[1].contains("WILL_NOT_LEARN")) {
                        will_not_learn++;
//                        if (question.getValue()[2].contains("HIGH_QUAL")) high_qual++;
//                        if (question.getValue()[2].contains("LOW_QUAL")) low_qual++;
//                        if (question.getValue()[2].contains("TRICKY_INPUT")) tricky_input++;
//                        if (question.getValue()[2].contains("FEEDBACK")) feedback++;
//                        if (question.getValue()[2].contains("MODEL_ADAPTS")) model_adapts++;
//                        if (question.getValue()[2].contains("NEEDS_FEEDBACK")) needs_feedback++;
//                        if (question.getValue()[2].contains("MARGINAL_FEEDBACK")) marginal_feedback++;
//                        if (question.getValue()[2].contains("BAD_FEEDBACK")) bad_feedback++;
//                        System.out.println("\t" + question.getValue()[3]);
                    }
                    if (question.getValue()[1].contains("COND_LEARN")) {
                        cond_learn++;
//                        if (question.getValue()[2].contains("HIGH_QUAL")) high_qual++;
//                        if (question.getValue()[2].contains("LOW_QUAL")) low_qual++;
//                        if (question.getValue()[2].contains("TRICKY_INPUT")) tricky_input++;
//                        if (question.getValue()[2].contains("FEEDBACK")) feedback++;
//                        if (question.getValue()[2].contains("MODEL_ADAPTS")) model_adapts++;
//                        if (question.getValue()[2].contains("NEEDS_FEEDBACK")) needs_feedback++;
//                        if (question.getValue()[2].contains("MARGINAL_FEEDBACK")) marginal_feedback++;
//                        if (question.getValue()[2].contains("BAD_FEEDBACK")) bad_feedback++;
//                        System.out.println("\t" + question.getValue()[3]);
                    }

//                    if (question.getValue()[2].contains("HIGH_QUAL")) high_qual++;
//                    if (question.getValue()[2].contains("LOW_QUAL")) low_qual++;
//                    if (question.getValue()[2].contains("TRICKY_INPUT")) tricky_input++;
//                    if (question.getValue()[2].contains("FEEDBACK")) feedback++;
//                    if (question.getValue()[2].contains("MODEL_ADAPTS")) model_adapts++;
//                    if (question.getValue()[2].contains("NEEDS_FEEDBACK")) needs_feedback++;
//                    if (question.getValue()[2].contains("MARGINAL_FEEDBACK")) marginal_feedback++;
//                    if (question.getValue()[2].contains("BAD_FEEDBACK")) bad_feedback++;

                }
            }

            System.out.println("X_LEARN: " + x_learn);
            System.out.println("WILL_LEARN: " + will_learn);
            System.out.println("WILL_NOT_LEARN: " + will_not_learn);
            System.out.println("COND_LEARN: " + cond_learn);

            System.out.println("HIGH_QUAL: " + high_qual);
            System.out.println("LOW_QUAL: " + low_qual);
            System.out.println("TRICKY_INPUT: " + tricky_input);
            System.out.println("FEEDBACK: " + feedback);
            System.out.println("MODEL_ADAPTS: " + model_adapts);
            System.out.println("NEEDS_FEEDBACK: " + needs_feedback);
            System.out.println("MARGINAL_FEEDBACK: " + marginal_feedback);
            System.out.println("BAD_FEEDBACK: " + bad_feedback);

            System.out.println();

        }


        // how many people think the model will / won't learn
        // go through each condition

//        for (Map.Entry<String, Map<String, Map<String, String>>> entry : answers.entrySet()) {
    }

    private static void parseQuestionIDs() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.

                String userID = line.substring(0, line.indexOf(":"));

                String condition = line.substring(line.indexOf("[") + 1, line.indexOf(","));

                String[] ids = (line.substring(line.indexOf(", ") + 2, line.length() - 1)).split("\\s*,\\s*");
                for (String s : ids) {
                    ids_to_conditions.put(s, condition);
                    ids_to_users.put(s, userID);
                }

//                String userID = line.substring(0, line.indexOf(":"));
////                System.out.print(userID + "\t");
//                if (!ids_to_conditions.containsKey(userID)) {
//                    ids_to_conditions.put(userID, new HashMap<>());
//                }
//
//                String condition = line.substring(line.indexOf("[") + 1, line.indexOf(","));
////                System.out.println("condition: " + condition);
//
////                String ids = line.substring(line.indexOf(", ") + 1, line.length() - 1);
//                String[] ids = (line.substring(line.indexOf(", ") + 2, line.length() - 1)).split("\\s*,\\s*");
//                for (String s : ids) {
////                    System.out.println("\t" + s);
//                    ids_to_conditions.get(userID).put(s, condition);
//                }

            }

//            System.out.println(ids_to_conditions.size());
//            for (Map.Entry<String, String> entry : ids_to_conditions.entrySet()) {
//                System.out.println("qid: " + entry.getKey() + "\t" + "[" + entry.getValue() + "]");
//            }
//            System.out.println();
//            for (Map.Entry<String, String> entry : ids_to_users.entrySet()) {
//                System.out.println("qid: " + entry.getKey() + "\t" + "[" + entry.getValue() + "]");
//            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void parsePerfWhyResponses() {
//        private static Map<String, Map<String, Map<String, String[]>>> conditions_to_responses = new HashMap<>();
        // map condition -> user id -> question id -> responses [likert, learn, why, reason]

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filepath2)));
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                String[] components = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // should be 5

                String qid = components[4];

                String condition = ids_to_conditions.get(qid);
                String id = ids_to_users.get(qid);

                if (!conditions_to_responses.containsKey(condition)) {
                    conditions_to_responses.put(condition, new HashMap<>());
                }

                Map<String, Map<String, String[]>> users = conditions_to_responses.get(condition);
                if (!users.containsKey(id)) {
                    users.put(id, new HashMap<String, String[]>());
                }

                Map<String, String[]> user = users.get(id);
                user.put(qid, new String[4]);
                String[] thoughts = user.get(qid);

                thoughts[0] = components[3];  // likert scale rating
                thoughts[1] = components[0];  // will learn?
                thoughts[2] = components[1];  // why?
                thoughts[3] = components[2];  // open ended text
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
