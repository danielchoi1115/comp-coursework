import numpy as np
from sklearn import datasets
import matplotlib.pyplot as plt


def plot_diagram(X, y=None, k=None):
    from itertools import cycle
    cycol = cycle('rgbcmk')  # Create a color cycle iteratable object
    if y is not None:        # y is the label of each individual points
        if k is None:        # k is number of cluster there is
            k = y.max() + 1  # The total number of clusters is the max label + 1, assuming the class labels are consecutive and start from 0
        for i, c in zip(range(k), cycol):
            plt.scatter(*X[y == i].T, c=c)  # Plot points of class with color c


def plot_points(points, marker='o'):
    plt.scatter(*points, c='black', marker=marker)  # Plot points with color white and specified shape


class KCluster:
    def __init__(self, k, X, ndim=2):
        self.k = k
        self.ndim = ndim
        # Choose k data points from X as the initial centroids.
        self.centroid = X[np.random.randint(0, len(X), size=(k, ))]

    def run(self, X):
        # TODO: 1. Find the difference between each data point and centroid, assign the result to diff
        # (Hints: the shape of diff should be (self.k, X.shape[0], self.ndim))
        # diff =
        # TODO: 2. Calculate the Euclidean distance between each data point and centroid, assign the result to dist.
        # (Hints: Euclidean distance = ((x2 - x1)**2 + (y_2 - y_1)**2) ** 0.5. You can also check the documentation of numpy.linalg.norm)
        # TODO (optional): You can also calculate the distance between each data point and centroid directly without following the instruction above:
        dist = np.array([np.sum((centroid-X)**2, axis=1)**0.5 for centroid in self.centroid])
        # TODO: 3. Assign the index of the closest centroid to each data point.
        # (Hints: use numpy.argmin to find the index of the closest centroid for each data point)
        output = np.argmin(dist, axis=0)
        # TODO: 4. Update each centroid using the mean of each cluster.
        for i in range(self.k):
            if i in output:
                self.centroid[i] = np.average(X[output == i], axis=0)  # the average of the position of all points are the new coordinate of the centroid

        return output


def SSE(X, y, k, centroids):
    # TODO: For each cluster, calculate distance (square of difference, i.e. Euclidean/L2-distance) of samples to the datapoints and accumulate the sum to `sse`. (Hints: use numpy.sum and for loop)
    return sum(np.sum(np.square(X[y == i]-centroids[i])) for i in range(k) if i in y)  # get the sum of sse for each cluster


secret_number = 42

X, y = datasets.make_blobs(10000, 2, centers=3, random_state=secret_number)  # Create a dataset with 3 blobs of cluster

np.random.seed(secret_number)  # Set seeds to expect the same result everytime
kmean = KCluster(3, X)
initial_points = kmean.centroid.copy()
for n in range(500):
    output = kmean.run(X)
sse = SSE(X, output, 3, kmean.centroid)
print('SSE: ', round(sse, 5))
plt.ion()
plot_diagram(X, output, 3)
plot_points(kmean.centroid.T, marker='+')
plot_points(initial_points.T, marker='o')
print('done')
