import numpy as np
from sklearn import datasets
secret_number = 42
k = 6
ndim = 4
X = np.arange(k*ndim).reshape(k, ndim)
print('X')
print(X)

centroids = np.arange(k*ndim).reshape(k, ndim)*2
# print("centroids")
# print(centroids)

dist = np.array([np.sum((centroid-X)**2, axis=1)**0.5 for centroid in centroids])
# print([centroid-X for centroid in centroids])
# print("dist")
# print(dist.round(0))

output = np.argmin(dist, axis=1)
print("min")
print(output)

for i in range(k):
    if i in output:
        centroids[i] = np.average(X[output == i], axis=0)

print('new_centroid')
print(centroids)


for i in range(k):
    if i in output:
        print(np.sum(np.square(X[output == i]-centroids[i])))


