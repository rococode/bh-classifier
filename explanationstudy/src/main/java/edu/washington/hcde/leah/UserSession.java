package edu.washington.hcde.leah;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserSession {

    private static final String CONDITIONS = "_explain_feature";

    public final String localid;
    public String condition = CONDITIONS;

    public int numAnswered = 0;

    public Email practice = null;
    public List<Email> train = new ArrayList<>();
    public List<Email> test = new ArrayList<>();
    public int totalTrain = 0;
    public int totalTest = 0;

    public long lastNext = 0;

    private UUID uuid = UUID.randomUUID();
    public Timestamp lastScenarioRequest;

    public String mode = "intro1";

    public Timestamp lastServe = Timestamp.from(Instant.now());

    public UserSession(String localid) {
        this.localid = localid;

        this.train.addAll(EmailManager.getTrainEmails());
        this.totalTrain = this.train.size();

        this.test.addAll(EmailManager.getTestEmails());
        this.totalTest = test.size();

        this.practice = EmailManager.getPracticeEmail();

        Collections.shuffle(this.train);
        Collections.shuffle(this.test);
    }

    private boolean setUUID = false;

    public void setUUID(UUID uuid) {
        if (this.setUUID) {
            throw new RuntimeException("already set uuid! " + uuid + " " + this.uuid);
        }
        this.setUUID = true;
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Email currentEmail = null;

    private boolean didPractice = false;
    private boolean finishedTrain = false;
    private boolean finishedTest = false;
    public int trainIdx = 0;
    public int testIdx = 0;

    public void loadNextEmail() {
        this.lastServe = Timestamp.from(Instant.now());
        log.info("Loading next email... " + this.trainIdx + " " + this.testIdx + " " + this.train.size() + " " + this.test.size() + " for " + this.uuid);
        if (!this.didPractice) {
            log.info("Setting to practice.");
            this.currentEmail = this.practice;
            this.didPractice = true;
            return;
        }
        if (trainIdx == this.train.size()) {
            this.finishedTrain = true;
        }
        if (trainIdx < this.train.size()) {
            this.currentEmail = this.train.get(trainIdx++);
            log.info("New train idx is " + trainIdx);
            return;
        }
        if (testIdx < this.test.size()) {
            this.currentEmail = this.test.get(testIdx++);
            return;
        }
        this.finishedTest = true;
    }

    public boolean finishedTrain() {
        return this.finishedTrain;
    }

    public boolean finishedTest() {
        return this.finishedTest;
    }

    @Override
    public String toString() {
        return this.localid + ":" + (this.uuid == null ? "-" : this.uuid.toString());
    }

}
