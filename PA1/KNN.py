# Task 1

from sklearn.model_selection import train_test_split
from sklearn import datasets
import numpy as np
from numpy import mean, sort, std
from collections import Counter


def standardize_dataset(input_array):
    return (input_array - mean(input_array, axis=0)/std(input_array, axis=0, ddof=1))


class KNNClassifier:
    def __init__(self, k):
        self.k = k

    def fit(self, X_train, y_train):
        self.X_train = X_train
        self.y_train = y_train

    def calculate_euclidean_distance(self, X_test):
        return np.array([np.sqrt(np.sum(np.square(row - self.X_train), axis=1, keepdims=False)) for row in X_test])

    def find_k_nearest_neighbor_labels(self, X_test):
        euclidean_distances = self.calculate_euclidean_distance(X_test)
        sorted_index_array = np.argsort(euclidean_distances)[:, :self.k]

        return np.take(self.y_train, sorted_index_array)

    def predict(self, X_test):
        k_nearest_labels = self.find_k_nearest_neighbor_labels(X_test)
        a = [Counter(label).most_common(1)[0][0] for label in k_nearest_labels]
        return np.array(a)


def generate_confusion_matrix(y_predict, y_actual):
    if len(y_predict) != len(y_actual):
        return 'Failed to generate confusion matrix. Wrong match'
    tp = tn = fp = fn = 0  # True Positive, True Negative, False Positive, False Negative
    for predict, actual in zip(y_predict, y_actual):
        if actual and predict:  # True Positive: Actual: Positive, Predicted Positivex
            tp += 1
        elif not actual and not predict:  # True Negative: Actual: Negative, Predicted Negative
            tn += 1
        elif predict:  # False Positive: Actual Negative, Predicted Positive
            fp += 1
        else:  # False Negative: Actual Negative, Predicted Negative
            fn += 1
    return tp, tn, fp, fn


def calculate_accuracy_score(y_predict, y_actual):
    tp, tn, fp, fn = generate_confusion_matrix(y_predict, y_actual)
    return (tp + tn)/(tp + tn + fp + fn)


def calculate_MCC_score(y_predict, y_actual):
    tp, tn, fp, fn = generate_confusion_matrix(y_predict, y_actual)

    numerator = (tp*tn - fp*fn)
    denominator = (tp+fp)*(tp+fn)*(tn+fp)*(tn+fn)
    return 0.0 if not denominator else numerator/np.sqrt(denominator)


if __name__ == "__main__":
    train = np.loadtxt("D:/HKUST/COMP2211/PA1/heart_disease_train_dataset.csv", delimiter=',', skiprows=1)
    test = np.loadtxt("D:/HKUST/COMP2211/PA1/heart_disease_test_dataset.csv", delimiter=',', skiprows=1)

    # All except the last column.
    X_train, y_train = train[:, :-1], train[:, -1]
    X_test, y_test = test[:, :-1], test[:, -1]

    a = KNNClassifier(5)
    a.fit(X_train, y_train)
    # print(a.calculate_euclidean_distance(X_test).shape)
    # print(a.find_k_nearest_neighbor_labels(X_test).shape)
    # print(a.predict(X_test).shape)
