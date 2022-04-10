import numpy as np
Train = np.array([[0, 1], [1, 2], [2, 3], [3, 4]])
Test = np.array([[5, 6], [7, 8]])


def compute(X_train, X_test):
    num_test = X_test.shape[0]
    num_train = X_train.shape[0]
    distances = np.zeros((num_test, num_train))

    # for i in range(num_test):
    #     for j in range(num_train):
    #         distances[i, j] = np.sqrt(np.sum(np.square(X_test[0]-X_train)))
    TrainSumSquare = np.sum(np.square(Train), axis=1)
    TestSumSquare = np.sum(np.square(Test), axis=1)
    print(np.expand_dims(TestSumSquare, 1)+TrainSumSquare)
    distances = np.sqrt(np.expand_dims(TestSumSquare, 1)+TrainSumSquare-2*np.dot(X_test, X_train.T))

    # distances = np.sum(X_test) + np.sum(X_train) -2*(np.dot,X_test,X_train
#   distances = np.sqrt(np.sum(np.square([X_test]-X_train)))
    return distances


print(compute(Train, Test))
