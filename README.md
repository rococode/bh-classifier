# Baseball-Hockey Classifier (Explanation Study)

This is the repo for the Explanation Study. In this study, our goal is to find out how explanations for ML models affect user satisfaction with the system. Our primary hypothesis is that people would rather not have explanations at all, than have explanations and not be able to provide feedback to them.

### Quick Summary

Users are given a series of emails (from the 20newsgroups dataset). We choose a subset of 20newsgroups consisting of hockey and baseball emails. The emails are run through a simple Naive Bayes classifier which decides if the email is about baseball or hockey. We also store scores for each word, and track the most "impactful" words as determined by the delta between their probability of being in a baseball vs. hockey email.

We test our hypothesis with a setup with 2x3 conditions. For the 2 conditions, a user can either get a model with explanations or no explanations. Explanations highlight the top three most impactful words in an email. For the 3 conditions, a user can give no feedback, instance-level feedback (correct or incorrect), or feature-level feedback (select actual words).

The first survey had two main phases. First, the users looked at 10 "training" emails. In these training emails, they were supposed to learn how the model works. Then, they looked at 15 "test" emails. In the test emails, they told us what they thought the email is about, and what they thought the model will decide. At the end, we asked them a series of Likert questions with some open-ended "Why?" questions. This experiment yielded some indications that qualitatively supported our hypothesis, but nothing statistically significant.

For the second survey, users look at 20 "training" emails with the specified purpose of understanding how the model works so that they can then evaluate its performance and usefulness. Next, we ask them a series of Likert questions with some open-ended "Why?" questions. At the end, they look at 4 "test" emails and tell us what they think the email is about, and what they think the (updated) model will decide. We ran this survey twice, once with a 76.5% accurate model and following up with a 94.4% accurate model.

## Table of Contents

1. [/data and /data-old](#data) - data files
1. [run.py](#runpy) - the naive bayes classifier (study 1)
1. [run2.py](#run2py) - the naive bayes classifier (study 2)
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
- **close calls2**: emails that were nearly predicted incorrectly (study 2)
- **failed2**: emails that were predicted incorrectly (study 2)
- **matches2**: emails that "match" by having at least 2 of the same top 3 words (study 2)
- **success2**: emails that were predicted correctly (study 2)
- **test**: emails that are run through the model for prediction after training
- **train**: emails that are used to train. Different folders = different training set sizes (we had to lower it a lot b/c this is an easy problem for a model to solve)
- **final-improved**: a final set of good emails to consider using
- **final-split**: the final set of emails we used
- **final2**: the final set of emails we used for study 2
- **feedback**: used to categorize instance-level feedback to incorporate into the model

### run.py <a name="runpy"></a>

The Naive Bayes classifier for binary email classification. It's kind of messy, but you may not need to run it since we already have the selected emails (in `explanationstudy/src/main/resources/`'s test, train, and practice folders). If you do need to run it again, let me know and I can walk you through it. For study 2, we used **run2.py**.

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

### To run the study on MTurk:

Use `generate_mturk.py` to generate a random order of task conditions. Just specify the number of batches you need, as well as an output file name.

Sign into Amazon MTurk as a Requester and select "Publish Batch" for Explanation Study 2.

Choose the .csv file with the randomized conditions, then follow the next few prompts to publish the batch. It's a pretty straightforward process, but let me know if you run into any issues.

After workers have submitted the task, you'll want to approve their task, assign them the DoneExplanationStudy qualification, and pay them their bonus.

### To update the testbed:

If you're just changing the interface, everything you need should be in explanationstudy/src/main/resources/web/static/js/main.2bcbbf7a.chunk.js and css/main.0092d80c.chunk.css in the same static folder. In the .js file, there are some variables and functions defined at the top, followed by the JavaScript for each phase of the study ("intro1", "practice", "train", etc.) which correspond to the cases in the Launcher class.

If you want to update more of the backend, the Launcher class in explanationstudy main is a good place to start. When running locally, you can set the default condition in UserSession. To update the emails included in the study, you can find those files in explanationstudy/src/main/resources/ (practice, test, and train).

To upload the new version of the testbed to the server, first you'll want to build the JAR locally via maven install.

You can use the upload-survey script in the voiceinteraction repo to upload the JAR file to the AWS server. Just edit the JAR to be the explanationstudy JAR, and make sure the voice-kp.pem (in the voiceinteraction repo) is in your ssh folder. That will upload the JAR to EC2.

Then, you'll want to connect to EC2, stop the currently running server, and restart it with rexp.sh.

### To run the analysis scripts:

You can run the Launcher in the explanationanalysis main class. There are calls for extracting the open-ended responses as well as the feedback and research question responses. The FeedbackExtracter class also performs some simple comparisons of the data and writes it to .csv files.

To apply the feedback to see the resulting model accuracy, you can run InstanceFeedbackIncorporator main and FeatureFeedbackIncorporator main, in explanationstudy. These call run_instance.py and run_feature.py, respectively. For the feature-level feedback, you can also incorporate the instance-level feedback by uncommenting the relevant lines in both the java and python files.

The feature-level feedback is currently implemented by increasing each word's weight by 20% for the indicated class and decreasing the weight by 20% for the other class. You can change this in run_feature.py.
