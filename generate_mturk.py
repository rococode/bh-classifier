#! /usr/bin/python3
import csv

# already = []
# with open('43.csv') as csv_file:
#     csv_reader = csv.reader(csv_file, delimiter=',')
#     idx = 0
#     for row in csv_reader:
#         idx += 1
#         if idx == 1:
#             continue
        # print(row[27])
        # already.append(row[27] + '\n')

num_batches = 27  # * 6 conditions
out = "exp_study2_large_sample.csv"
print("Generating random order for", num_batches * 6, "runs, total", num_batches, "batches.")
with open(out, "w+") as f:
    f.write("zval\n")
    nums = ["1\n", "2\n", "3\n", "4\n", "5\n", "6\n"]
    nums *= num_batches 
    # for x in already:
    #     print("here", x)
    #     nums.remove(x)
    import random; random.shuffle(nums)
    print(nums)
    print("len", len(nums))
    f.writelines(nums)

