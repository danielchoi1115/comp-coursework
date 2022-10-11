from sklearn.metrics import accuracy_score, matthews_corrcoef
from sklearn.model_selection import KFold, train_test_split
import numpy as np
from sklearn.neighbors import KNeighborsClassifier
from KNN import standardize_dataset
from pa1 import KNNClassifier, calculate_MCC_score


class DFoldCV:
    def __init__(self, X, y, k_list, d):
        self.X = X
        self.y = y
        self.k_list = k_list
        self.d = d

    def generate_folds(self):
        train_d_folds = []
        test_d_folds = []

        feature_and_labels = np.c_[self.X, self.y]

        d_folds = np.array_split(feature_and_labels, self.d)
        for index, fold in enumerate(d_folds):
            fold_copy = d_folds[:]
            test_d_folds.append(fold_copy.pop(index))
            train_d_folds.append(np.concatenate(fold_copy))
        return train_d_folds, test_d_folds

    def cross_validate(self):
        train_d_folds, test_d_folds = self.generate_folds()
        return np.array([self._score(k, train_d_folds, test_d_folds) for k in self.k_list])

    def _score(self, k, train_d_folds, test_d_folds):
        knn_model = KNNClassifier(k)
        fold_scores = []
        for train, test in zip(train_d_folds, test_d_folds):
            X_train, y_train = train[:, :-1], train[:, -1]
            X_test, y_test = test[:, :-1], test[:, -1]

            knn_model.fit(X_train, y_train)
            y_predict = knn_model.predict(X_test)
            fold_scores.append(calculate_MCC_score(y_predict, y_test))

        return fold_scores

    def validate_best_k(self):
        average = [np.average(score) for score in self.cross_validate()]
        return self.k_list[np.argsort(average)[-1]]


X = np.loadtxt("D:/HKUST/COMP2211/PA1/heart_disease_train_dataset.csv", delimiter=',', skiprows=1)
test = np.loadtxt("D:/HKUST/COMP2211/PA1/heart_disease_test_dataset.csv", delimiter=',', skiprows=1)

D = 200
k_list = [3, 5, 7, 9, 11]

dfold_model = DFoldCV(X=standardize_dataset(X[:, :-1]), y=X[:, -1], k_list=k_list, d=D)
print('Average Score of my model')
print([np.average(score) for score in dfold_model.cross_validate()])

score_list = []
kf = KFold(n_splits=D, shuffle=False)

_X = standardize_dataset(X[:, :-1])
_y = X[:, -1]

for k in k_list:
    model = KNeighborsClassifier(k)
    scores_for_k = []
    for train_index, test_index in kf.split(X):
        X_train, y_train = _X[train_index], _y[train_index]
        X_test, y_test = _X[test_index], _y[test_index]

        model.fit(X_train, y_train)
        y_pred = model.predict(X_test)
        matth = matthews_corrcoef(y_test, y_pred)

        scores_for_k.append(matth)

    score_list.append(scores_for_k)
print('')

# for i in range(len(np.array(val)[0])):
#     if np.array(val)[0][i] != np.array(score_list)[0][i]:
#         print(i)
# print(np.array(val)[0])
# print(np.array(score_list)[0])

average = [np.average(score) for score in score_list]
print('Average Score of sklearn model')
print(average)
print(k_list[np.argsort(average)[-1]])

# X = np.loadtxt("D:/HKUST/COMP2211/PA1/heart_disease_train_dataset.csv", delimiter=',', skiprows=1)
# test = np.loadtxt("D:/HKUST/COMP2211/PA1/heart_disease_test_dataset.csv", delimiter=',', skiprows=1)

# for k in k_list:
#     model = KNeighborsClassifier(k)
#     X_train, X_test, y_train, y_test = train_test_split(X[:, :-1], X[:, -1], test_size=0.2, random_state=1342)
#     # X_train, y_train = standardize_dataset(X[:, :-1]), X[:, -1]
#     # X_test, y_test = standardize_dataset(test[:, :-1]), test[:, -1]

#     model.fit(X_train, y_train)
#     y_pred = model.predict(X_test)
#     print(k, round(accuracy_score(y_test, y_pred), 3), round(matthews_corrcoef(y_test, y_pred), 3))
