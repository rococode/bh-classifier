# Baseball-Hockey Classifier (Explanation Study)

This is the repo for the Explanation Study. In this study, our goal is to find out how explanations for ML models affect user satisfaction with the system. Our primary hypothesis is that people would rather not have explanations at all, than have explanations and not be able to provide feedback to them.

### Quick Summary

Users are given a series of emails (from the 20newsgroups dataset). We choose a subset of 20newsgroups consisting of hockey and baseball emails. The emails are run through a simple Naive Bayes classifier which decides if the email is about baseball or hockey. We also store scores for each word, and track the most "impactful" words as determined by the delta between their probability of being in a baseball vs. hockey email.

We test our hypothesis with a setup with 2x3 conditions. For the 2 conditions, a user can either get a model with explanations or no explanations. Explanations highlight the top three most impactful words in an email. For the 3 conditions, a user can give no feedback, instance-level feedback (correct or incorrect), or feature-level feedback (select actual words).

The survey has two main phases. First, the user looks at 10 "training" emails. In these training emails, they are supposed to learn how the model works. Then, they look at 15 "test" emails. In the test emails, they tell us what they think the email is about, and what they think the model will decide. 

At the end, we ask them a series of Likert questions with some open-ended "Why?" questions.

We found some indications that qualitatively support our hypothesis, but nothing statistically significant thus far.

## Table of Contents

1. [/data and /data-old](#data) - data files
1. [run.py](#runpy) - the naive bayes classifier
1. [Java Maven Projects](#projects)
    1. [/explanationanalysis](#explanationanalysis)
    1. [/explanationstudy](#explanationstudy)
1. [Misc Files](#miscfiles)

### /data and /data-old <a name="data"></a>

You can probably ignore data-old.

`/data` contains data output from our Naive Bayes classifier. It also contains the data it uses to train.

`/data` contains the following folders:

- **close calls**: emails that were nearly predicted incorrectly
- **failed**: emails that were predicted incorrectly
- **matches**: emails that "match" by having at least 2 of the same top 3 words
- **success**: emails that were predicted correctly
- **test**: emails that are run through the model for prediction after training
- **train**: emails that are used to train. Different folders = different training set sizes (we had to lower it a lot b/c this is an easy problem for a model to solve)
- **final-improved**: a final set of good emails to consider using
- **final-split**: the final set of emails we used

### run.py <a name="runpy"></a>

The Naive Bayes classifier for binary email classification. It's kind of messy, but you may not need to run it since we already have the selected emails (in `explanationstudy/src/main/resources/`'s test, train, and practice folders). If you do need to run it again, let me know and I can walk you through it.

### Java Maven Projects<a name="projects"></a>

Two Maven projects. See the `voiceinteraction` repo for more details on setup (if you are reading this way in the future and don't have access, send me a message).

### /explanationanalysis<a name="explanationanalysis"></a>

Pulls out various data from the database. Tables in the database (which is shared with the explanation project) are prefixed with `exp_`.

### /explanationstudy<a name="explanationstudy"></a>

Runs the web server containing the testbed. If you run it locally you can see it at `localhost:80`. Similar setup to the explanation study - keeps track of a `UserSession` for each user and steps them through progressively.

Emails are contained in and loaded from `src/main/resources/`. Loading is done in `Email.java`. There's some stuff with resource packing in JARs vs. running locally that is handled there that you don't need to worry about.

### Misc Files<a name="miscfiles"></a>

`meeting-notes/` contains some notes from meetings (I have more in a private repo, can send them over if you want them). Also contains a couple sketchy submissions on MTurk that we ended up disqualifying.


`misc/` contains a bunch of data, most of which is probably irrelevant to you as we have better formatted results already in the spreadsheet

`generate_mturk.py` is a super simple script for generating task IDs. I had to use it to even out the numbers at some point after accidentally cancelling a batch at 43 submissions... (you can't restart a batch after just clicking Cancel one time!)
