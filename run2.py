import collections
import io
import os
import re
import statistics
from shutil import copyfile

import nltk
import numpy as np
from nltk.corpus import stopwords
from sklearn.naive_bayes import MultinomialNB
from tabulate import tabulate
from tqdm import tqdm

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

import random
random.seed(123)

scores = []

nltk.download('stopwords')
nltk.download('punkt')

train_path = "data" + os.sep + "train"
test_path = "data" + os.sep + "test"
hockey = 'rec.sport.hockey-full'
baseball = 'rec.sport.baseball-full'

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


otrain_hockey, otrain_hockey_names, otrain_hockey_lens = load_tokens(train_path, hockey, vocab)
otrain_baseball, otrain_baseball_names, otrain_baseball_lens = load_tokens(train_path, baseball, vocab)
test_hockey, test_hockey_names, test_hockey_lens = load_tokens(test_path, hockey, vocab)
test_baseball, test_baseball_names, test_baseball_lens = load_tokens(test_path, baseball, vocab)

vocab = sorted(vocab)

# print(vocab)

id_lookup = {x: i for i, x in enumerate(vocab)}
vocab_lookup = {i: x for i, x in enumerate(vocab)}

vocab_lookup[-1] = '<UNK/>'


def to_id(ls, name="?"):
    res = []
    for l in tqdm(ls, desc="to_id on " + name):
        ct = collections.Counter()
        ct.update([id_lookup[clean_word(x)] if clean_word(x) in id_lookup else -1 for x in l])
        # print(ct)
        bow = []
        for i in range(len(id_lookup)):
            bow.append(ct[i])
            # print(len(bow), len(id_lookup))
        res.append(bow)
    return res

# added for cosine similarity to find matches
otest_hockey = test_hockey
otest_baseball = test_baseball

test_hockey = to_id(test_hockey, name="test_hockey")
test_baseball = to_id(test_baseball, name="test_baseball")

# for _ in range(0, 50):
for _ in [1]:

    # matches by cosine similarity

    # hockey_docs = []  # figure out which list this should be (list of lists: tokens for each doc)
    # for s in otest_hockey:
    #     if 30 < len(s) < 120:
    #         hockey_docs.append(' '.join(s))
    #
    # baseball_docs = []
    # for s in otest_baseball:
    #     if 30 < len(s) < 120:
    #         baseball_docs.append(' '.join(s))
    #
    # h_tfidf_vectorizer = TfidfVectorizer(english_stopwords)
    # h_tfidf_vectorizer = TfidfVectorizer()
    # h_sparse_matrix = h_tfidf_vectorizer.fit_transform(hockey_docs)
    # print("cosine similarity for hockey")
    # cos_h = cosine_similarity(h_sparse_matrix)
    # print(cos_h)
    # print("end cosine similarity for hockey")
    #
    # # iterate through cos_h and find the most similar pairs...
    # for row in range(len(cos_h)):
    #     for c in range(len(cos_h[row])):
    #         if cos_h[row][c] > 0.5 and row != c:
    #             print("similar hockey h_docs:", test_hockey_names[row], "&", test_hockey_names[c], cos_h[row][c])
    #
    # b_tfidf_vectorizer = TfidfVectorizer(english_stopwords)
    # b_tfidf_vectorizer = TfidfVectorizer()
    # b_sparse_matrix = b_tfidf_vectorizer.fit_transform(baseball_docs)
    # print("cosine similarity for baseball")
    # cos_b = cosine_similarity(b_sparse_matrix)
    # print(cos_b)
    # print("end cosine similarity for baseball")
    #
    # # iterate through cos_b and find the most similar pairs...
    # for row in range(len(cos_b)):
    #     for c in range(len(cos_b[row])):
    #         if cos_b[row][c] > 0.5 and row != c:
    #             print("similar baseball h_docs:", test_baseball_names[row], "&", test_baseball_names[c], cos_b[row][c])

    # --- end matches ---

    import random

    combined = list(zip(otrain_hockey, otrain_hockey_names, otrain_hockey_lens))
    random.shuffle(combined)
    otrain_hockey[:], otrain_hockey_names[:], otrain_hockey_lens[:] = zip(*combined)

    combined = list(zip(otrain_baseball, otrain_baseball_names, otrain_baseball_lens))
    random.shuffle(combined)
    otrain_baseball[:], otrain_baseball_names[:], otrain_baseball_lens[:] = zip(*combined)

    TRAINING_SIZE = 100
    if (TRAINING_SIZE > 0):
        train_hockey = otrain_hockey[:TRAINING_SIZE]
        train_hockey_names = otrain_hockey_names[:TRAINING_SIZE]
        train_hockey_lens = otrain_hockey_lens[:TRAINING_SIZE]

        train_baseball = otrain_baseball[:TRAINING_SIZE]
        train_baseball_names = otrain_baseball_names[:TRAINING_SIZE]
        train_baseball_lens = otrain_baseball_lens[:TRAINING_SIZE]
    else:
        train_hockey = otrain_hockey
        train_hockey_names = otrain_hockey_names
        train_hockey_lens = otrain_hockey_lens

        train_baseball = otrain_baseball
        train_baseball_names = otrain_baseball_names
        train_baseball_lens = otrain_baseball_lens

    print("sample 3", train_baseball_names[0:3])
    print("sample test 3", test_baseball_names[0:3])

    # print(len(id_lookup))

    train_hockey = to_id(train_hockey, name="train_hockey")
    train_baseball = to_id(train_baseball, name="train_baseball")
    # print(train_hockey)
    # print([[vocab_lookup[i] for i in x] for x in train_hockey])
    print("len:", len(train_hockey))
    print("len[0]:", len(train_hockey[0]))
    print("Shape:", np.array(train_hockey).shape)

    x = train_hockey + train_baseball
    y = [0] * len(train_hockey) + [1] * len(train_baseball)

    X = np.array(x)
    print("X shape:", X.shape)
    y = np.array(y)
    clf = MultinomialNB()
    clf.fit(X, y)
    print(X[2])
    print(clf.predict(X[2:3]))
    # print(X[900])
    # print(clf.predict(X[900:901]))

    print(clf.score(X, y))

    tx = test_hockey + test_baseball
    ty = [0] * len(test_hockey) + [1] * len(test_baseball)

    tX = np.array(tx)
    ty = np.array(ty)
    tNames = test_hockey_names + test_baseball_names

    # print(clf.predict(tX))
    print("SCORE:", clf.score(tX, ty))
    scores.append(clf.score(tX, ty))
    # sys.exit(0)

    preds = clf.predict(tX)
    probs = clf.predict_proba(tX)
    probs = [[i, a, b, abs(a - b)] for i, (a, b) in enumerate(probs)]


    def output(i):
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
        # print("here: ", output_lines)
        return output_lines


    # print("log probs", probs)

    print("prob len original", len(probs))
    probs_tmp = [[i, a, b, abs(a - b), tNames[i], preds[i] == ty[i]] for i, a, b, diff in probs if diff < 0.25]
    probs = [[i, a, b, abs(a - b), tNames[i], preds[i] == ty[i]] for i, a, b, diff in probs]

    for i, a, b, diff, name, correct in probs_tmp:
        # print(name, correct, diff)
        failed_dir = "data" + os.sep + "close calls2"
        if not os.path.exists(failed_dir):
            os.makedirs(failed_dir)
        if not os.path.exists(failed_dir + os.sep + str(True)):
            os.makedirs(failed_dir + os.sep + str(True))
        if not os.path.exists(failed_dir + os.sep + str(False)):
            os.makedirs(failed_dir + os.sep + str(False))
            failed_dir + os.sep + str(correct)
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
                # print("outputs: " + str(outputs))
                f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
                f2.writelines(outputs)
                f2.write(lines)

    # print("filtered", '\n'.join([str(x) for x in probs]))
    # print("tnames len", len(tNames))

    numincorrect = 0
    numcorrect = 0

    tNames = test_hockey_names + test_baseball_names
    tLens = test_hockey_lens + test_baseball_lens

    print("MEDIAN of TEST: " + str(statistics.median(tLens)))
    print("MEAN of TEST: " + str(statistics.mean(tLens)))

    ct_match_len = 0
    for i in tLens:
        if 30 < i < 120:
            ct_match_len += 1
    print("match count:", ct_match_len, "/", len(tLens))

    assert len(tNames) == len(preds)

    print("lens: ", len(tNames), len(preds))

    failed_dir = "data" + os.sep + "failed2"
    success_dir = "data" + os.sep + "success2"
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


    # outputted = 0
    # match_num = 0;
    # for i in range(len(preds)):
    #     if 30 < tLens[i] < 120:
    #         with io.open(tNames[i], "r", encoding="latin-1") as f:
    #             lines = f.read()
    #             skip = False
    #             for l in lines:
    #                 if l[0] == '>':
    #                     skip = True
    #             if skip:
    #                 continue
    #         words = get_words(i)
    #         for j in range(len(preds)):
    #             if i == j:
    #                 continue
    #             if 30 < tLens[j] < 120:
    #                 words2 = get_words(j)
    #                 with io.open(tNames[j], "r", encoding="latin-1") as f:
    #                     lines = f.read()
    #                     skip = False
    #                     for l in lines:
    #                         if l[0] == '>':
    #                             skip = True
    #                     if skip:
    #                         continue
    #                 match_words = 0
    #                 for w in words:
    #                     if w in words2:
    #                         match_words += 1
    #                 if match_words >= 2:
    #                     # print(tNames[i], "and", tNames[j], "match")
    #                     dest_dir = "data" + os.sep + "matches2"
    #                     if not os.path.exists(dest_dir):
    #                         os.makedirs(dest_dir)
    #                     status = None
    #                     if preds[i] != ty[i]:
    #                         status = "failed-"
    #                     else:
    #                         status = "success-"
    #                     name_a = dest_dir + os.sep + "match" + str(match_num) + "-a-" + status + tNames[i].replace(os.sep, '_') + ".txt"
    #                     if preds[j] != ty[j]:
    #                         status = "failed-"
    #                     else:
    #                         status = "success-"
    #                     name_b = dest_dir + os.sep + "match" + str(match_num) + "-b-" + status + tNames[j].replace(os.sep, '_') + ".txt"
    #                     match_num += 1
    #                     copyfile(tNames[i], name_a)
    #                     copyfile(tNames[j], name_b)
    #                     outputs_a = output(i)
    #                     outputs_b = output(j)
    #                     i, a, b, diff, name, correct = probs[i]
    #                     with io.open(tNames[i], "r", encoding="latin-1") as f:
    #                         lines = f.read()
    #                     with io.open(name_a, "w", encoding="latin-1") as f2:
    #                         # print("outputs: " + str(outputs_a))
    #                         f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
    #                         f2.writelines(outputs_a)
    #                         f2.write(lines)
    #                     i, a, b, diff, name, correct = probs[j]
    #                     with io.open(tNames[j], "r", encoding="latin-1") as f:
    #                         lines = f.read()
    #                     with io.open(name_b, "w", encoding="latin-1") as f2:
    #                         # print("outputs: " + str(outputs_b))
    #                         f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
    #                         f2.writelines(outputs_b)
    #                         f2.write(lines)
    #                     # print('\t' + str(words))
    #                     # print('\t' + str(words2))
    # import sys
    # sys.exit(0)

    # cosine similarity
    match_names1 = []
    match_names2 = []
    match_scores = []
    h_docnames = []
    h_docs = []
    b_docnames = []
    b_docs = []
    for i in range(len(otest_hockey)):
        if 30 < test_hockey_lens[i] < 120:
            # print(tNames[i])
            with io.open(tNames[i], "r", encoding="latin-1") as f:
                lines = f.read()
                skip = False
                for l in lines:
                    # print("line", l)
                    if l[0] == '>':
                        skip = True
                if skip:
                    continue
                h_docs.append(' '.join(otest_hockey[i]))
                h_docnames.append(test_hockey_names[i])

    print("len(h_docs)", len(h_docs))
    print("docs[0]", h_docs[0])
    tfidf_vectorizer = TfidfVectorizer(english_stopwords)
    tfidf_vectorizer = TfidfVectorizer()
    sparse_matrix = tfidf_vectorizer.fit_transform(h_docs)
    print("cosine similarity for hockey")
    cos_sim = cosine_similarity(sparse_matrix)
    print(cos_sim)
    print("end cosine similarity for hockey")

    # iterate through cos_sim and find the most similar pairs...
    for row in range(len(cos_sim)):
        for c in range(len(cos_sim[row])):
            if cos_sim[row][c] > 0.1 and row != c:
                print("similar hockey h_docs:", h_docnames[row], h_docnames[c], cos_sim[row][c])
                match_names1.append(h_docnames[row])
                match_names2.append(h_docnames[c])
                match_scores.append(cos_sim[row][c])
                print(h_docs[row])
                print(h_docs[c])

    for i in range(len(otest_baseball)):
        if 30 < test_baseball_lens[i] < 120:
            # print(tNames[i])
            with io.open(tNames[i], "r", encoding="latin-1") as f:
                lines = f.read()
                skip = False
                for l in lines:
                    # print("line", l)
                    if l[0] == '>':
                        skip = True
                if skip:
                    continue
                b_docs.append(' '.join(otest_baseball[i]))
                b_docnames.append(test_baseball_names[i])

    print("len(b_docs)", len(b_docs))
    print("b_docs[0]", b_docs[0])
    b_tfidf_vectorizer = TfidfVectorizer(english_stopwords)
    b_tfidf_vectorizer = TfidfVectorizer()
    b_sparse_matrix = b_tfidf_vectorizer.fit_transform(b_docs)
    print("cosine similarity for baseball")
    cos_simb = cosine_similarity(b_sparse_matrix)
    print(cos_simb)
    print("end cosine similarity for baseball")

    # iterate through cos_sim and find the most similar pairs...
    for row in range(len(cos_simb)):
        for c in range(len(cos_simb[row])):
            if cos_simb[row][c] > 0.001 and row != c:
                print("similar baseball docs:", b_docnames[row], b_docnames[c], cos_simb[row][c])
                match_names1.append(b_docnames[row])
                match_names2.append(b_docnames[c])
                match_scores.append(cos_simb[row][c])
                print(b_docs[row])
                print(b_docs[c])

    print("len(match1)", len(match_names1))
    print("len(match2)", len(match_names2))
    print("len(match_scores))", len(match_scores))

    match_num = 0
    idx = -1
    j = -1
    print("len(preds)", len(preds))
    print("len(tNames)", len(tNames))
    for i in range(len(preds)):
        if tNames[i] in match_names1:
            idx = match_names1.index(tNames[i])
            # print("idx", idx, match_names1[idx], match_names2[idx], match_scores[idx])
            idx += 1
            while idx < len(match_names1) and match_names1[idx] == tNames[i]:
                # print("idx", idx, match_names1[idx], match_names2[idx], match_scores[idx])
                j = tNames.index(match_names2[idx])
                idx += 1
                dest_dir = "data" + os.sep + "matches-exp2"
                if not os.path.exists(dest_dir):
                    os.makedirs(dest_dir)
                status = None
                if preds[i] != ty[i]: # change this?
                    status = "failed-"
                else:
                    status = "success-"
                name_a = dest_dir + os.sep + "match" + str(match_num) + "-a-" + status + tNames[i].replace(os.sep, '_') + "-" + str(match_scores[idx]) + ".txt" # change tNames, match_num
                if preds[j] != ty[j]: # change this
                    status = "failed-"
                else:
                    status = "success-"
                name_b = dest_dir + os.sep + "match" + str(match_num) + "-b-" + status + tNames[j].replace(os.sep, '_') + "-" + str(match_scores[idx]) + ".txt" # change tNames, match_num
                match_num += 1
                copyfile(tNames[i], name_a)
                copyfile(tNames[j], name_b)
                outputs_a = output(i)
                outputs_b = output(j)
                i, a, b, diff, name, correct = probs[i]
                with io.open(tNames[i], "r", encoding="latin-1") as f:
                    lines = f.read()
                with io.open(name_a, "w", encoding="latin-1") as f2:
                    # print("outputs: " + str(outputs_a))
                    f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
                    f2.writelines(outputs_a)
                    f2.write(lines)
                i, a, b, diff, name, correct = probs[j]
                with io.open(tNames[j], "r", encoding="latin-1") as f:
                    lines = f.read()
                with io.open(name_b, "w", encoding="latin-1") as f2:
                    # print("outputs: " + str(outputs_b))
                    f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
                    f2.writelines(outputs_b)
                    f2.write(lines)

    # loop over the predicted emails, and put them in success or fail directories
    for i in range(len(preds)):
        if preds[i] != ty[i]:
            # print(i, "failed!", tNames[i])
            dest = failed_dir + os.sep + tNames[i].replace(os.sep, '_') + ".txt"
            # print("LENGTH of " + tNames[i] + " is " + str(tLens[i]))
            i, a, b, diff, name, correct = probs[i]
            if 30 < tLens[i] < 120:
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
                        # print("outputs: " + str(outputs))
                        f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
                        f2.writelines(outputs)
                        f2.write(lines)
            numincorrect += 1
            prediction = preds[i]
        else:
            dest = success_dir + os.sep + tNames[i].replace(os.sep, '_') + ".txt"
            # print("LENGTH of " + tNames[i] + " is " + str(tLens[i]))
            i, a, b, diff, name, correct = probs[i]
            if 30 < tLens[i] < 120:
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
                        # print("outputs: " + str(outputs))
                        f2.writelines([' '.join([str(x) for x in [a, b, diff, correct]]) + '\n'])
                        f2.writelines(outputs)
                        f2.write(lines)
            numcorrect += 1
            prediction = preds[i]

    print("score", numcorrect, numincorrect)

    # print("params", clf.get_params())
    # feature_log_prob = p(xi | y)
    print("feature_log_prob", clf.feature_log_prob_)
    print("feature_log_prob shape", clf.feature_log_prob_.shape)

print("SCORES:", scores)
print("MEDIAN of SCORES: " + str(statistics.median(scores)))
print("MEAN of SCORES: " + str(statistics.mean(scores)))