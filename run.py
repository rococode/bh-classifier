import collections
import io
import os
import re
import statistics
from shutil import copyfile
import sys

import nltk
import numpy as np
from nltk.corpus import stopwords
from sklearn.naive_bayes import MultinomialNB
from tabulate import tabulate
from tqdm import tqdm

import random
random.seed(123)

scores = []

nltk.download('stopwords')
nltk.download('punkt')

train_path = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data" + os.sep + "train"
test_path = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data" + os.sep + "test"
hockey = 'rec.sport.hockey-full'
baseball = 'rec.sport.baseball-full'

# incorporate instance-level feedback
feedback_path = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data" + os.sep + "feedback"
feedback_hockey = 'feedback_hockey'
feedback_baseball = 'feedback_baseball'

vocab = set()

pattern = re.compile('[^a-zA-Z]+')
english_stopwords = stopwords.words('english')


def clean_word(word):
    if word in english_stopwords:
        return None
    word = word.strip()
    if len(word) == 0:
        return None
    word = pattern.sub('', word)
    if len(word) == 0:
        return None
    word = word.lower()
    return word


def load_tokens(path, category, vocab):
    token_lists = []
    file_names = []
    lens = []
    for name in tqdm(os.listdir(os.path.join(path, category))):
        full_path = os.path.join(path, category, name)
        with io.open(full_path, "r", encoding="latin-1") as f:
            lines = f.readlines()
            # skip headers and empty lines
            # lines = [x.strip() for x in lines if len(x.strip()) > 0 and x.find("From:") == -1 and x.find("Subject:") == -1]
            lines = [x.strip() for x in lines if len(x.strip()) > 0 and x.find("From:") == -1]
            lines = '\n'.join(lines)
            # tokens = nltk.word_tokenize(lines)
            tokens = lines.split()
            tokens = [clean_word(x) for x in tokens if clean_word(x) is not None]
            # tokens = [x for x in tokens if x not in stopwords.words('english')]
            # tokens = [x.strip() for x in tokens if len(x.strip()) > 0]
            token_lists.append(tokens)
            file_names.append(full_path)
            lens.append(len(tokens))
            for t in tokens:
                vocab.add(t)
    return token_lists, file_names, lens


# incorporating instance-level feedback
def add_tokens(path, category, vocab, token_lists, file_names, lens):
    # token_lists = []
    # file_names = []
    # lens = []
    for name in tqdm(os.listdir(os.path.join(path, category))):
        full_path = os.path.join(path, category, name)
        with io.open(full_path, "r", encoding="latin-1") as f:
            lines = f.readlines()
            # skip headers and empty lines
            # lines = [x.strip() for x in lines if len(x.strip()) > 0 and x.find("From:") == -1 and x.find("Subject:") == -1]
            lines = [x.strip() for x in lines if len(x.strip()) > 0 and x.find("From:") == -1]
            lines = '\n'.join(lines)
            # tokens = nltk.word_tokenize(lines)
            tokens = lines.split()
            tokens = [clean_word(x) for x in tokens if clean_word(x) is not None]
            # tokens = [x for x in tokens if x not in stopwords.words('english')]
            # tokens = [x.strip() for x in tokens if len(x.strip()) > 0]
            token_lists.append(tokens)
            file_names.append(full_path)
            lens.append(len(tokens))
            # for t in tokens:
            #     vocab.add(t)
    # return token_lists, file_names, lens

# incorporating feature-level feedback
# condition = sys.argv[1]
# userid = sys.argv[2]
# num_total = int(sys.argv[3]) # should be 3 * 20 = 60
# num_hockey = int(sys.argv[4]) # should be multiple of 3
# chosen_hockey = []
# chosen_baseball = []

# print("num total", num_total, file=sys.stderr)
# print("num hockey", num_hockey, file=sys.stderr)
#
# for i in range(num_hockey):
#     chosen_hockey.append(sys.argv[i + 5])
#     print("appended h ", sys.argv[i + 5], file=sys.stderr)
#
# for i in range(num_total - num_hockey):
#     chosen_baseball.append(sys.argv[i + 4 + num_hockey])
#     print("appended b", sys.argv[i + 4 + num_hockey], file=sys.stderr)

# print("chosen hockey", chosen_hockey, file=sys.stderr)
# print("chosen baseball", chosen_baseball, file=sys.stderr)

# print("num hockey words: ", len(chosen_hockey))
# print("num baseball words: ", len(chosen_baseball))


otrain_hockey, otrain_hockey_names, otrain_hockey_lens = load_tokens(train_path, hockey, vocab)
otrain_baseball, otrain_baseball_names, otrain_baseball_lens = load_tokens(train_path, baseball, vocab)
test_hockey, test_hockey_names, test_hockey_lens = load_tokens(test_path, hockey, vocab)
test_baseball, test_baseball_names, test_baseball_lens = load_tokens(test_path, baseball, vocab)

vocab = sorted(vocab)

# print("vocab: ", vocab, file=sys.stderr)

id_lookup = {x: i for i, x in enumerate(vocab)}
vocab_lookup = {i: x for i, x in enumerate(vocab)}

vocab_lookup[-1] = '<UNK/>'


def to_id(ls, name="?"):
    res = []
    for l in tqdm(ls, desc="to_id on " + name):
        ct = collections.Counter()
        ct.update([id_lookup[clean_word(x)] if clean_word(x) in id_lookup else -1 for x in l])
        # print("ct: ", ct, file=sys.stderr)
        bow = []
        for i in range(len(id_lookup)):
            bow.append(ct[i])
            # print(len(bow), len(id_lookup))
        res.append(bow)
    return res


test_hockey = to_id(test_hockey, name="test_hockey")
test_baseball = to_id(test_baseball, name="test_baseball")

# for _ in range(0, 50):
for _ in [1]:
    import random

    combined = list(zip(otrain_hockey, otrain_hockey_names, otrain_hockey_lens))
    random.shuffle(combined)
    otrain_hockey[:], otrain_hockey_names[:], otrain_hockey_lens[:] = zip(*combined)

    combined = list(zip(otrain_baseball, otrain_baseball_names, otrain_baseball_lens))
    random.shuffle(combined)
    otrain_baseball[:], otrain_baseball_names[:], otrain_baseball_lens[:] = zip(*combined)

    training_num = 9
    train_hockey = otrain_hockey[:training_num]
    train_hockey_names = otrain_hockey_names[:training_num]
    train_hockey_lens = otrain_hockey_lens[:training_num]

    # add instance-level feedback
    add_tokens(feedback_path, feedback_hockey, vocab, train_hockey, train_hockey_names, train_hockey_lens)

    train_baseball = otrain_baseball[:training_num]
    train_baseball_names = otrain_baseball_names[:training_num]
    train_baseball_lens = otrain_baseball_lens[:training_num]

    # add instance-level feedback
    add_tokens(feedback_path, feedback_baseball, vocab, train_baseball, train_baseball_names, train_baseball_lens)

    print("sample 3", train_baseball[0:3], file=sys.stderr)
    print("sample names 3", train_baseball_names[0:3], file=sys.stderr)
    print("sample lens 3", train_baseball_lens[0:3], file=sys.stderr)
    print("sample test 3", test_baseball_names[0:3], file=sys.stderr)

    print("len of id_lookup: ", len(id_lookup), file=sys.stderr)

    train_hockey = to_id(train_hockey, name="train_hockey")
    train_baseball = to_id(train_baseball, name="train_baseball")
    print("len:", len(train_hockey), file=sys.stderr)
    print("len[0]:", len(train_hockey[0]), file=sys.stderr)
    print("Shape:", np.array(train_hockey).shape, file=sys.stderr)

    x = train_hockey + train_baseball
    y = [0] * len(train_hockey) + [1] * len(train_baseball)

    X = np.array(x)
    print("X shape:", X.shape, file=sys.stderr)
    y = np.array(y)
    clf = MultinomialNB()
    clf.fit(X, y)

    print("shape of clf.feature_log_prob_", clf.feature_log_prob_.shape, file=sys.stderr)

    # incorporate feature-level feedback
    # adjust_weight = 0.2  # adjust by 20%
    # for i in tqdm(range(len(chosen_hockey))):
    #     # print("word_index ", word_index, file=sys.stderr)
    #     # print("word is ", vocab_lookup[word_index], file=sys.stderr)
    #     # print("word should be ", chosen_hockey[i], file=sys.stderr)
    #     if clean_word(chosen_hockey[i]) in id_lookup:
    #         word_index = id_lookup[clean_word(chosen_hockey[i])]
    #         # print("old weight for word: ", clf.feature_log_prob_[0][word_index], clean_word(chosen_hockey[i]), file=sys.stderr)
    #         clf.feature_log_prob_[0][word_index] *= 1 - adjust_weight  # increase
    #         clf.feature_log_prob_[1][word_index] *= 1 + adjust_weight  # decrease
    #         # print("new weight for word: ", clf.feature_log_prob_[0][word_index], clean_word(chosen_hockey[i]), file=sys.stderr)
    # for i in tqdm(range(len(chosen_baseball))):
    #     if clean_word(chosen_baseball[i]) in id_lookup:
    #         word_index = id_lookup[clean_word(chosen_baseball[i])]
    #         clf.feature_log_prob_[1][word_index] *= 1 - adjust_weight  # increase
    #         clf.feature_log_prob_[0][word_index] *= 1 + adjust_weight  # decrease

    print("clf.feature_log_prob_", clf.feature_log_prob_, file=sys.stderr)

    print(X[2], file=sys.stderr)
    print(clf.predict(X[2:3]), file=sys.stderr)
    # print("X: " + str(X))
    # print("y: " + str(y))
    # print(X[900])
    # print(clf.predict(X[900:901]))

    print("clf score: " + str(clf.score(X, y)))
    # print("X after: " + str(X))
    # print("y after: " + str(y))

    tx = test_hockey + test_baseball
    ty = [0] * len(test_hockey) + [1] * len(test_baseball)

    tX = np.array(tx)
    ty = np.array(ty)
    tNames = test_hockey_names + test_baseball_names

    # print(clf.predict(tX))
    print("SCORE:", clf.score(tX, ty), file=sys.stderr)
    scores.append(clf.score(tX, ty))
    # sys.exit(0)

    preds = clf.predict(tX)
    probs = clf.predict_proba(tX)
    probs = [[i, a, b, abs(a - b)] for i, (a, b) in enumerate(probs)]


    def output(i):
        # print("begin output", file=sys.stderr)
        prediction = preds[i]
        output_lines = []
        output_lines.append("prediction: " + ("hockey" if prediction == 0 else "baseball") + " for " + tNames[i] + "\n")
        prints = []
        for word_idx, word_count in enumerate(tX[i]):
            if word_count > 0:
                prints.append((word_idx, clf.feature_log_prob_[prediction][word_idx], clf.feature_log_prob_[abs(prediction - 1)][word_idx], vocab_lookup[word_idx]))
                # print(word_idx, clf.feature_log_prob_[prediction][word_idx], clf.feature_log_prob_[abs(prediction - 1)][word_idx], vocab_lookup[word_idx])
        prints.sort(reverse=True, key=lambda x: abs(x[1] - x[2]))
        p1 = "p(w|h)" if prediction == 0 else "p(w|b)"
        p2 = "p(w|b)" if prediction == 0 else "p(w|h)"
        # print('\t\t'.join(["word", p1, p2, "delta"]))
        res = []
        for tup in prints[:10]:
            delta = round(tup[1] - tup[2], 2)
            sig = ""
            if abs(delta) > 1:
                sig = "**"
            res.append([str(x) for x in (tup[3], round(tup[1], 2), round(tup[2], 2), delta, sig)])
            # print('\t\t'.join([str(x) for x in (tup[3], round(tup[1], 2), round(tup[2], 2), delta)]))
        output_lines.append(tabulate(res, headers=["word", p1, p2, "delta", "**?"], tablefmt='github') + "\n")
        # print("here: ", output_lines, file=sys.stderr)
        return output_lines


    # print("log probs", probs)

    print("prob len original", len(probs))
    probs_tmp = [[i, a, b, abs(a - b), tNames[i], preds[i] == ty[i]] for i, a, b, diff in probs if diff < 0.25]
    probs = [[i, a, b, abs(a - b), tNames[i], preds[i] == ty[i]] for i, a, b, diff in probs]

    for i, a, b, diff, name, correct in tqdm(probs_tmp):
        # print(name, correct, diff, file=sys.stderr)
        failed_dir = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data" + os.sep + "close calls"
        if not os.path.exists(failed_dir):
            os.makedirs(failed_dir)
        if not os.path.exists(failed_dir + os.sep + str(True)):
            os.makedirs(failed_dir + os.sep + str(True))
        if not os.path.exists(failed_dir + os.sep + str(False)):
            os.makedirs(failed_dir + os.sep + str(False))
            # failed_dir + os.sep + str(correct)
        dest = failed_dir + os.sep + str(correct) + os.sep + tNames[i].replace(os.sep, '_') + ".txt"
        with io.open(tNames[i], "r", encoding="latin-1") as f:
            lines = f.read()
            skip = False
            for l in lines:
                if l[0] == '>':
                    # print("skipping reply!")
                    # print(l)
                    skip = True
                    break
            if skip:
                continue
            copyfile(tNames[i], dest)
            outputs = output(i)
            with io.open(dest, "w", encoding="latin-1") as f2:
                # print("outputs: " + str(outputs), file=sys.stderr)
                f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
                f2.writelines(outputs)
                f2.write(lines)

    # print("filtered", '\n'.join([str(x) for x in probs]))
    # print("tnames len", len(tNames))

    incorrect = 0
    correct = 0

    tNames = test_hockey_names + test_baseball_names
    tLens = test_hockey_lens + test_baseball_lens

    print("MEDIAN of TEST: " + str(statistics.median(tLens)), file=sys.stderr)
    print("MEAN of TEST: " + str(statistics.mean(tLens)))
    sys.stderr.flush()

    ct_match_len = 0
    for i in tqdm(tLens):
        if 30 < i < 120:
            ct_match_len += 1
            # print("ct_match_len: " + str(ct_match_len), file=sys.stderr)
        # print("i: " + str(i), file=sys.stderr)
    print("hi", file=sys.stderr)
    print("match count:", ct_match_len, "/", len(tLens), file=sys.stderr)
    sys.stderr.flush()

    assert len(tNames) == len(preds)

    print("lens: ", len(tNames), len(preds), file=sys.stderr)
    sys.stderr.flush()

    failed_dir = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data" + os.sep + "failed"
    success_dir = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\data" + os.sep + "success"
    if not os.path.exists(success_dir):
        os.makedirs(success_dir)

    if not os.path.exists(failed_dir):
        os.makedirs(failed_dir)

    cache = {}


    def get_words(i):
        if i in cache:
            return cache[i]
        prediction = preds[i]
        words = []
        for word_idx, word_count in enumerate(tX[i]):
            if word_count > 0:
                word = vocab_lookup[word_idx]
                prob1 = clf.feature_log_prob_[prediction][word_idx]
                prob2 = clf.feature_log_prob_[abs(prediction - 1)][word_idx]
                words.append((word, abs(prob1 - prob2)))
        words.sort(reverse=True, key=lambda x: x[1])
        cache[i] = words[:3]
        return cache[i]
        # return words[:3]

    # print("hello 1", file=sys.stderr)
    # sys.stderr.flush()

    # Matches -- no longer relevant for study 2
    # outputted = 0
    # match_num = 0;
    # try:
    #     for i in range(len(preds)):
    #         print("hello 2", file=sys.stderr)
    #         sys.stderr.flush()
    #         if 30 < tLens[i] < 120:
    #             with io.open(tNames[i], "r", encoding="latin-1") as f:
    #                 lines = f.read()
    #                 skip = False
    #                 for l in lines:
    #                     if l[0] == '>':
    #                         skip = True
    #                 if skip:
    #                     continue
    #             words = get_words(i)
    #             print("hello 3", file=sys.stderr)
    #             sys.stderr.flush()
    #             for j in range(len(preds)):
    #                 if i == j:
    #                     continue
    #                 if 30 < tLens[j] < 120:
    #                     words2 = get_words(j)
    #                     print("hello 4", file=sys.stderr)
    #                     sys.stderr.flush()
    #                     with io.open(tNames[j], "r", encoding="latin-1") as f:
    #                         lines = f.read()
    #                         skip = False
    #                         for l in lines:
    #                             if l[0] == '>':
    #                                 skip = True
    #                         if skip:
    #                             continue
    #                     match_words = 0
    #                     for w in words:
    #                         if w in words2:
    #                             match_words += 1
    #                     print("hello 5", file=sys.stderr)
    #                     sys.stderr.flush()
    #                     if match_words >= 2:
    #                         print(tNames[i], "and", tNames[j], "match")
    #                         dest_dir = "data" + os.sep + "matches"
    #                         if not os.path.exists(dest_dir):
    #                             os.makedirs(dest_dir)
    #                         status = None
    #                         if preds[i] != ty[i]:
    #                             status = "failed-"
    #                         else:
    #                             status = "success-"
    #                         name_a = dest_dir + os.sep + "match" + str(match_num) + "-a-" + status + tNames[i].replace(os.sep, '_') + ".txt"
    #                         if preds[j] != ty[j]:
    #                             status = "failed-"
    #                         else:
    #                             status = "success-"
    #                         name_b = dest_dir + os.sep + "match" + str(match_num) + "-b-" + status + tNames[j].replace(os.sep, '_') + ".txt"
    #                         match_num += 1
    #                         print("hello 6", file=sys.stderr)
    #                         sys.stderr.flush()
    #                         copyfile(tNames[i], name_a)
    #                         copyfile(tNames[j], name_b)
    #                         print("hello 7", file=sys.stderr)
    #                         sys.stderr.flush()
    #                         outputs_a = output(i)
    #                         outputs_b = output(j)
    #                         # print("hello 8", file=sys.stderr)
    #                         print("hello 8" + tNames[i], file=sys.stderr)
    #                         print("hello 8" + name_a, file=sys.stderr)
    #                         sys.stderr.flush()
    #                         i, a, b, diff, name, correct = probs[i]
    #                         with io.open(tNames[i], "r", encoding="latin-1") as f:
    #                             lines = f.read()
    #                             print("hello 8.1", file=sys.stderr)
    #                             sys.stderr.flush()
    #                         with io.open(name_a, "w", encoding="latin-1") as f2:
    #                             print("outputs: " + str(outputs_a), file=sys.stderr)
    #                             sys.stderr.flush()
    #                             f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
    #                             f2.writelines(outputs_a)
    #                             f2.write(lines)
    #                             print("hello 8.2", file=sys.stderr)
    #                             sys.stderr.flush()
    #                         print("hello 9" + tNames[j], file=sys.stderr)
    #                         print("hello 9" + name_b, file=sys.stderr)
    #                         sys.stderr.flush()
    #                         i, a, b, diff, name, correct = probs[j]
    #                         with io.open(tNames[j], "r", encoding="latin-1") as f:
    #                             lines = f.read()
    #                             print("hello 9.1", file=sys.stderr)
    #                             sys.stderr.flush()
    #                         with io.open(name_b, "w", encoding="latin-1") as f2:
    #                             print("outputs: " + str(outputs_b), file=sys.stderr)
    #                             sys.stderr.flush()
    #                             f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
    #                             f2.writelines(outputs_b)
    #                             f2.write(lines)
    #                         print("hello 10", file=sys.stderr)
    #                         sys.stderr.flush()
    #                         # print('\t' + str(words))
    #                         # print('\t' + str(words2))
    # except Exception as e:
    #     print("exception, " + str(e), file=sys.stderr)
    # import sys
    # sys.exit(0)

    # print("hello", file=sys.stderr)
    # sys.stderr.flush()

    # for i in range(len(preds)):
    #     if preds[i] != ty[i]:
    #         # print(i, "failed!", tNames[i])
    #         dest = failed_dir + os.sep + tNames[i].replace(os.sep, '_') + ".txt"
    #         # print("LENGTH of " + tNames[i] + " is " + str(tLens[i]))
    #         i, a, b, diff, name, correct = probs[i]
    #         if 30 < tLens[i] < 120:
    #             with io.open(tNames[i], "r", encoding="latin-1") as f:
    #                 lines = f.read()
    #                 skip = False
    #                 for l in lines:
    #                     if l[0] == '>':
    #                         # print("skipping reply!")
    #                         # print(l)
    #                         skip = True
    #                         break
    #                 if skip:
    #                     continue
    #                 copyfile(tNames[i], dest)
    #                 outputs = output(i)
    #                 with io.open(dest, "w", encoding="latin-1") as f2:
    #                     # print("outputs: " + str(outputs))
    #                     f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
    #                     f2.writelines(outputs)
    #                     f2.write(lines)
    #         incorrect += 1
    #         prediction = preds[i]
    #     else:
    #         correct += 1
    #         dest = success_dir + os.sep + tNames[i].replace(os.sep, '_') + ".txt"
    #         # print("LENGTH of " + tNames[i] + " is " + str(tLens[i]))
    #         i, a, b, diff, name, correct = probs[i]
    #         if 30 < tLens[i] < 120:
    #             with io.open(tNames[i], "r", encoding="latin-1") as f:
    #                 lines = f.read()
    #                 skip = False
    #                 for l in lines:
    #                     if l[0] == '>':
    #                         # print("skipping reply!")
    #                         # print(l)
    #                         skip = True
    #                         break
    #                 if skip:
    #                     continue
    #                 copyfile(tNames[i], dest)
    #                 outputs = output(i)
    #                 with io.open(dest, "w", encoding="latin-1") as f2:
    #                     # print("outputs: " + str(outputs))
    #                     f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
    #                     f2.writelines(outputs)
    #                     f2.write(lines)
    #         prediction = preds[i]


    # f = open("user_trained.txt", "a+")  # added by me
    # userid = sys.argv[1]
    # print(f"user [{userid}]")
    print("score", correct, incorrect)
    # f.write(f"score{correct}{incorrect}\n")

    # print("params", clf.get_params())
    # feature_log_prob = p(xi | y)
    # print("feature_log_prob", clf.feature_log_prob_)
    # print("feature_log_prob shape", clf.feature_log_prob_.shape)

# f = open("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\instance_trained.txt", "a+")  # instance-level feedback
# f = open("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\feature_trained.txt", "a+")  # feature-level feedback

# command line args are different for instance-level feedback
# # condition = sys.argv[2]
# f.write(f"{condition},")
# # userid = sys.argv[1]
# f.write(f"{userid},")

# record score -- instance-level feedback
print("SCORES:", scores)
# f.write(f"{scores}\n")
# f.close()

# record score -- feature-level feedback
print("SCORES:", scores)
# f.write(f"{scores},")
# f.write(f"{len(chosen_hockey)},")
# f.write(f"{len(chosen_baseball)}\n")
# f.close()

# run.py is called by the feedback incorporater classes (explanationstudy)
# parse results with ModelAccuracyParser (explanationstudy)

# not used:
# # f.write(f"score correct: " + str(correct) + ", incorrect: " + str(incorrect))
# print("MEDIAN of SCORES: " + str(statistics.median(scores)))
# # f.write(f"MEDIAN OF SCORES: {str(statistics.median(scores))}\n")
# print("MEAN of SCORES: " + str(statistics.mean(scores)))
# # f.write(f"MEAN OF SCORES: {str(statistics.mean(scores))}\n\n")

