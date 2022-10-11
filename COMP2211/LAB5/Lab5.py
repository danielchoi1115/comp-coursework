from sklearn.metrics import accuracy_score
import pandas as pd
import numpy as np
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split

dataset = pd.read_csv('D:/HKUST/COMP2211/LAB5/sonar.all-data.csv', index_col=False, header=None)
encoder = LabelEncoder()

for k in dataset.keys():
    if(type(dataset[k][0]) == str):
        dataset[k] = encoder.fit_transform(dataset[k])

np_dataset = dataset.to_numpy()

X = np_dataset[:, :-1]
Y = np_dataset[:, -1]
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.25, random_state=3)

weights_and_bias = np.full(dataset.shape[1], 0.0)


def predict(X, weights_and_bias):
    # TODO:calculate the prediction results
    weights = weights_and_bias[1:]
    bias = weights_and_bias[:1]
    return ((np.dot(X, weights) + bias) > 0) * 1


def training(weights_and_bias, X, Y, learning_rate, epochs):
    for e in range(epochs):
        for i in range(Y.shape[0]):
            error = Y[i] - predict(X[i], weights_and_bias)
            weights_and_bias[1:] += learning_rate * error * X[i]
            weights_and_bias[0] += learning_rate * error

    return weights_and_bias


final_weights_and_bias = training(weights_and_bias, X_train, Y_train, 0.01, 500)

prediction = predict(X_test, final_weights_and_bias)


# print(accuracy_score(Y_test, prediction))


def accuracy_metric(Y_test, prediction):
    check = np.equal(prediction, Y_test)
    count = np.sum(check, axis=0)
    return count / float(Y_test.shape[0]) * 100.0


scores = accuracy_metric(Y_test, prediction)
print(scores)

# print(metrics.accuracy_score(Y_test, prediction))
# print(metrics.f1_score(Y_test, prediction))
