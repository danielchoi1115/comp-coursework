from sklearn.neighbors import KNeighborsClassifier
import numpy as np
from numpy import mean, std
import pandas as pd
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import normalize

data = pd.read_csv('./adult.csv', index_col=False)

for k in data.keys():
    if(type(data[k][0]) == str):
        # ffill means forward fill that fill the NaN entry with the last seen items within the same attribute column (contrastively, bfill means backward fill)
        data[k].fillna(method='ffill', inplace=True)
    else:
        # here fill the NaN entry with the mean value of the attribute column
        data[k].fillna(data[k].mean(), inplace=True)


label_dict = {}
for k in data.keys():
    if type(data[k][0]) == str:
        encoder = LabelEncoder()
        data[k] = encoder.fit_transform(data[k])
        label_dict[k] = encoder


vectors = data.to_numpy()
print(vectors.shape)
# vector represntation X in shape (32561, 14)
# groundtruth y in shape (32561,)
# TODO: assign X and y
X, y = np.split(vectors, [-1], axis=1)
X = (X - mean(X, axis=0)) / std(X, axis=0)

X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=3)

# TODO: KNN classification
model = KNeighborsClassifier(n_neighbors=5)
# Train the model using the training sets
model.fit(X_train, y_train)

y_pred = model.predict(X_test)
