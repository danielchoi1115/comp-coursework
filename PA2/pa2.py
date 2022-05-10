"""This is where you write graded codes for your PA2

"""

import numpy as np
from keras.layers import (
    Rescaling,
    Conv2D,
    MaxPooling2D,
    Flatten,
    Dense,
    Dropout,
)
from keras.layers import RandomFlip, RandomRotation
from keras import Sequential
from keras.initializers.initializers_v2 import HeNormal
import tensorflow as tf
from keras.optimizer_v2.gradient_descent import SGD
from tensorflow import keras

############################
##         Task 1         ##
############################


def reshape_x(x_train_raw, x_val_raw, x_test_raw):
    """Reshape images to proper shapes

    Parameters
    ----------
    x_train_raw : np.ndarray
    x_val_raw : np.ndarray
    x_test_raw : np.ndarray

    Returns
    -------
    A 3-tuple of reshaped x_train_raw, x_val_raw, x_test_raw, respectively
    """
    # START YOUR CODE HERE
    return (
        x_train_raw.reshape(x_train_raw.shape[0], 28, 28, 1),
        x_val_raw.reshape(x_val_raw.shape[0], 28, 28, 1),
        x_test_raw.reshape(x_test_raw.shape[0], 28, 28, 1),
    )
    # END YOUR CODE HERE

############################
##         Task 2         ##
############################


def encode_y(y_train_raw, y_val_raw, y_test_raw, N_labels):
    """One-hot encode the labels

    Parameters
    ----------
    y_train_raw : np.ndarray
    y_val_raw : np.ndarray
    y_test_raw : np.ndarray

    Returns
    -------
    A 3-tuple of encoded y_train_raw, y_val_raw, y_test_raw, respectively
    """
    # START YOUR CODE HERE
    return (
        tf.one_hot(y_train_raw, N_labels),
        tf.one_hot(y_val_raw, N_labels),
        tf.one_hot(y_test_raw, N_labels),
    )
    # END YOUR CODE HERE

############################
##         Task 3         ##
############################


# These imports makes life easier
# For you don't need to write keras.Sequential, layers.RandomFlip, etc.
# But Sequential, RandomFlip, etc. directly.
# You can ignore them if not needed, or import additional stuff if you like


def AugmentationLayer():
    """Creates a keras model for data augmentation

    Returns
    -------
    keras.Model
        A keras model created by Sequential(),
        containing the data augmentation pipeline.
    """
    # START YOUR CODE HERE
    return Sequential([
        RandomFlip("horizontal"),
        RandomRotation([-0.1, 0.1], fill_value=0, fill_mode="constant"),
    ])
    # END YOUR CODE HERE

############################
##         Task 4         ##
############################


# These imports makes life easier
# You can ignore them if not needed, or import additional stuff if you like


def build_model(N_labels):
    """Creates and returns a model described above

    Parameters
    ----------
    N_labels : int
        The number of possible labels.
        Used when creating the last Dense layer.

    Returns
    -------
    keras.Model
        The Sequential model created
    """
    initializer = HeNormal()
    # START YOUR CODE HERE
    model = Sequential([
        AugmentationLayer(),
        Rescaling(scale=1./255),
        Conv2D(filters=16, kernel_size=(3, 3), activation='relu', kernel_initializer=initializer),
        MaxPooling2D(pool_size=(2, 2)),
        Conv2D(filters=32, kernel_size=(3, 3), activation='relu', kernel_initializer=initializer),
        MaxPooling2D(pool_size=(2, 2)),
        Conv2D(filters=64, kernel_size=(3, 3), activation='relu', kernel_initializer=initializer),
        MaxPooling2D(pool_size=(2, 2)),
        Flatten(),
        Dropout(0.2),
        Dense(units=N_labels, activation='softmax')

    ])
    # END YOUR CODE HERE
    model.build((None, 28, 28, 1))
    return model


############################
##         Task 5         ##
############################


def compile_model(model: Sequential, lr):
    """Compile a model according to the description above

    Parameters
    ----------
    model : keras.Model
        The model to compile
    lr : float
        The learning rate
    """
    # START YOUR CODE HERE
    opt = SGD(learning_rate=lr)
    model.compile(loss="categorical_crossentropy", optimizer=opt, metrics=["accuracy"])
    # END YOUR CODE HERE


############################
##         Task 6         ##
############################


def train_model(model: Sequential, epochs, x_train, y_train, x_val, y_val):
    """Train the model according to the description above.
    NOTE: Please return your `model.fit(...)` call
    for us to grade >.<

    Parameters
    ----------
    model : keras.Model
        The model to train
    epochs : int
        The number of epochs to train
    x_train : np.ndarray
    y_train : np.ndarray
    x_val : np.ndarray
    y_val : np.ndarray

    Returns
    -------
    keras.callback.History
        You don't need to care about the return value.
        But just make sure you do return the return value
        of `model.fit`.
    """
    # START YOUR CODE HERE
    return model.fit(x_train, y_train, batch_size=32, epochs=epochs, validation_data=(x_val, y_val), validation_batch_size=32)
    # END YOUR CODE HERE


############################
##         Task 7         ##
############################


def evaluate_model(model: Sequential, x_test, y_test):
    """Evaluate the model according to the description above

    Parameters
    ----------
    model : keras.Model
        The model to evaluate
    x_test : np.ndarray
    y_test : np.ndarray
    """
    # START YOUR CODE HERE
    score = model.evaluate(x_test, y_test, batch_size=32)
    # END YOUR CODE HERE
    print("Test loss:", score[0])
    print("Test accuracy:", score[1])


############################
##         Task 8         ##
############################


def predict_images(model, x):
    """Predict the model given images in shape (k, 28, 28, 1)

    Parameters
    ----------
    model : keras.Model
        The model for prediction
    x : np.ndarray
        The array of input images

    Returns
    -------
    np.ndarray
        The predicted models in shape (k,)
    """
    # START YOUR CODE HERE
    return np.argmax(model.predict(x), axis=1)
    # END YOUR CODE HERE


# if __name__ == "__main__":
