package edu.washington.hcde.leah;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Email {

    public static final int CHOSEN_WORDS = 3;

    public final String id;
    public final String sender;
    public final String subject;
    public final String content;

    public final String pred;
    public final String[] words;

    Email(String id, String subject, String sender, String content, String pred, List<String> words) {
        this.id = id;
        this.subject = subject;
        this.sender = sender;
        this.content = content.trim();
        this.pred = pred;
        // convert to json representation
//        this.words = "[\"" + String.join("\", \"", words) + "\"]";
        this.words = new String[words.size()];
        for (int k = 0; k < this.words.length; k++) {
            this.words[k] = words.get(k);
        }
    }

    @Override
    public String toString() {
        return this.id
                + ":\n\tFrom: "
                + this.sender
                + "\n\tSubject: "
                + this.subject
                + "\n\tContent: "
                + this.content.substring(0, Math.min(50, this.content.length()))
                + "\n\tPredicted: "
                + this.pred
                + "\n\tBest Words: "
                + this.words;
    }

    public static Email fromFile(File f) throws FileNotFoundException {
        String id = f.getName();
        log.info("Loading email from file " + f.getPath() + " as " + id);
        String sender = "";
        String subject = "";
        StringBuilder content = new StringBuilder();
        Scanner scan = new Scanner(f);

        boolean foundSender = false;
        boolean foundSubject = false;
        scan.nextLine();
        String pred = scan.nextLine();
        pred = pred.split(" ")[1];
        scan.nextLine(); // skip table headers
        scan.nextLine();
        List<String> words = new ArrayList<>();
        boolean firstNonEmptyAfterSubjectFrom = false;
        while (scan.hasNextLine()) {
            String s = scan.nextLine();
//            log.info("here: " + s);
//            log.info(s);
            if (s.trim().length() == 0 && !firstNonEmptyAfterSubjectFrom) {
                continue;
            }
            if (foundSender && foundSubject) {
                firstNonEmptyAfterSubjectFrom = true;
            }
            if (!foundSender && s.startsWith("From: ")) {
                sender = s.substring("From: ".length());
//                log.info("found sender: " + sender);
                foundSender = true;
            } else if (!foundSubject && s.startsWith("Subject: ")) {
                subject = s.substring("Subject: ".length());
                foundSubject = true;
            } else if (!foundSender) {
                String[] row = s.split("\\|");
//                log.info(id + " " + Arrays.toString(row));
                if (words.size() < CHOSEN_WORDS) {
                    words.add(row[1].trim());
                }
            } else {
                content.append(s);
                content.append('\n');
            }
        }
        if (words.size() != CHOSEN_WORDS || content.toString().trim().length() == 0) {
            throw new RuntimeException("Bad email parse " + id);
        }
        return new Email(id, subject, sender, content.toString(), pred, words);
    }
}
