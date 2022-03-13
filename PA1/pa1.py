from sklearn import datasets
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import train_test_split

from sklearn.neighbors import KNeighborsClassifier
from KNN import KNNClassifier, calculate_accuracy_score, calculate_MCC_score, generate_confusion_matrix, standardize_dataset
import numpy as np

if __name__ == "__main__":
    train = np.loadtxt("D:/HKUST/COMP2211/PA1/heart_disease_train_dataset.csv", delimiter=',', skiprows=1)
    test = np.loadtxt("D:/HKUST/COMP2211/PA1/heart_disease_test_dataset.csv", delimiter=',', skiprows=1)

    # All except the last column.
    X_train, y_train = train[:, :-1], train[:, -1]
    X_test, y_test = test[:, :-1], test[:, -1]

    # iris = datasets.load_iris()
    # X, y = iris.data, iris.target
    # X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=1342)

    for k in range(43, 44):
        knn = KNNClassifier(k)
        knn.fit(X_train, y_train)
        y_pred = knn.predict(X_test)
        # print(y_pred)

        a = calculate_accuracy_score(y_pred, y_test)
        b = calculate_MCC_score(y_pred, y_test)

        # print(generate_confusion_matrix(y_pred, y_test))

        knn = KNeighborsClassifier(k)
        knn.fit(X_train, y_train)

        y_pred = knn.predict(X_test)
        dist, ind = knn.kneighbors(X_test)
        # print(dist.shape)
        # for i in ind:
        #     print(i)

        print('sklearn Lib')
        print("index:",ind[91][42:44])
        print("distance",dist[91][42:44])
        print('')
        c = calculate_accuracy_score(y_pred, y_test)
        d = calculate_MCC_score(y_pred, y_test)

        # print(confusion_matrix(y_test, y_pred))

        print("for k = {}, Acc = {}%, MCC = {}%".format(k, round((a-c)*100, 2), round((b-d)*100, 2)))
