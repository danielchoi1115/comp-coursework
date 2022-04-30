import numpy as np
import cv2
import os


# Input: data_dir(str) -- the path of data.
#        cate2Idx(dict) -- mapping the category name to class index.
# Return: x(array) -- the images data, the shape in this task should be (15178, 28, 28, 3).
#         y(array) -- the label of images, the shape in this task should be (15178,).

def data_preprocessing(data_dir, cate2Idx):
    join = os.path.join
    x = []
    y = []
    for label, idx in cate2Idx.items():
        folderpath = join(data_dir, label)
        for img_name in os.listdir(folderpath):
            img = cv2.imread(join(folderpath, img_name))
            img = cv2.resize(img, (28, 28))
            img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
            x.append(img)
            y.append(idx)
    #### END TODO
    return np.array(x), np.array(y)


