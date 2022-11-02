import pandas as pd
import numpy as np
import math
from sklearn import preprocessing
q1_data = pd.read_csv('C:/Users/Daniel Choi/Desktop/comp-coursework/COMP4331/Assignment 2/Q1.csv')

data = q1_data.iloc[4:, :]

df = pd.DataFrame()
target_name = 'proper_question'
for c in data:
    le = preprocessing.LabelEncoder()
    le.fit(data[c])
    df[c] = le.transform(data[c])

def get_entropy(total: int, counter: dict):
    ent = 0
    for y, cnt in counter.items():
        ent += -(cnt/total) * math.log2(cnt/total)
    return ent

def get_split_info(cnt, total):
    return -(cnt/total) * math.log2(cnt/total)

stat = {}
entropy_D = get_entropy(10, {0:7, 1:3})

for c in df.iloc[:,:-1]:
    stat[c] = {}
    hashmap = {}
    for index, row in df.iterrows():
        hashmap.setdefault(row[c], {})
        if row[target_name] in hashmap[row[c]]:
            hashmap[row[c]][row[target_name]] += 1
        else:
            hashmap[row[c]][row[target_name]] = 1
    
    
    entropy = 0
    splitinfo = 0
    for att, counter in hashmap.items():
        total = sum([val for val in counter.values()])
        entropy += get_entropy(total, counter) * (total/df.shape[0])
        splitinfo += get_split_info(total, df.shape[0])
        
    stat[c]['entropy'] = entropy
    stat[c]['Gain'] = entropy_D - entropy
    stat[c]['Split Info'] = splitinfo
    stat[c]['Gain Ratio'] = (entropy_D - entropy)/splitinfo
    
print (stat)