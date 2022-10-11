import numpy as np
import h5py

f = h5py.File('D:/HKUST/COMP2211/PA2/draw_model.h5', 'r')
from keras.utils import plot_model

plot_model(model, to_file='model.png')
