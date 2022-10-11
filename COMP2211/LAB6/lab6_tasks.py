# Remember to include the import line(s) if you have use valid module(s) other than the one listed here
import pandas as pd
import numpy as np
import re
import keras

from numpy import array
from keras.preprocessing.text import one_hot
from keras.preprocessing.sequence import pad_sequences
from keras.models import Sequential
from keras.layers.core import Activation, Dropout, Dense
from tensorflow.keras import regularizers
from keras.layers import Flatten
from keras.layers.embeddings import Embedding
from sklearn.model_selection import train_test_split
from keras.preprocessing.text import Tokenizer
from keras.initializers import HeNormal


# Here, we use regular expression, which is out of the scope of this course.
# Input: single sentence string
# Return: preprocessed sentence string
def preprocessing(sentence):
    # Remove html tags
    tag = re.compile(r'<[^>]+>')
    sentence = tag.sub('', sentence)
    # Remove punctuations and numbers
    sentence = re.sub('[^a-zA-Z]', ' ', sentence)
    # Remove single char
    sentence = re.sub(r"\s+[a-zA-Z]\s+", ' ', sentence)
    # Remove multiple spaces
    sentence = re.sub(r'\s+', ' ', sentence)
    return sentence


def get_X_y(df):
    # TODO: preprocess all the instances in the dataset and store the processed list of sentence into variable X
    # X = a list of processed review strings

    # TODO: map the sentiment label into a binary value of 0 and 1 and store the binary numpy array into variable y
    # y = 1d binary numpy array

    # END of TODO
    numpy_array = df.to_numpy()
    X = numpy_array[:, 0]
    y = np.where(numpy_array[:, 1] == 'positive', 1, 0)

    for i, review in enumerate(X):
        X[i] = preprocessing(review)

    return X, y


def readglovefile(filepath):
    import gzip
    with gzip.open(filepath, 'r') as f:
        content = f.readlines()
    return [i.decode('utf8') for i in content]


def formatEmbDict(filepath):
    wordDict = {}
    content = readglovefile(filepath)
    # TODO: return a dict variable wordDict with [word as key] and [embedding as value]
    # i.e. calling wordDict['the'] will return a numpy array of [-0.038194, -0.24487, 0.72812, ..., 0.27062]

    for line in content:
        items = line.split()
        wordDict[items[0]] = np.array(items[1:], dtype=np.float32)
    # END of TODO
    return wordDict


def myModel(vocab_size, embedding_matrix, maxlen):
    model = Sequential()
    # In embedding_layer, the word index array for each instance is transformed to the GloVe embedding according to the embeddings matrix
    embedding_layer = Embedding(vocab_size, 100, weights=[embedding_matrix], input_length=maxlen, trainable=False)
    model.add(embedding_layer)
    # TODO: please construct a MLP, you can try different setting your own
    # You will get the point of this part if
    #       - your model gets a accuracy higher than 70% in our private test set
    #       - your model uses at least 2 and most 4 dense layers (including the output layer)
    #       - your model only uses the below listed keras modules (the model summary will be checked)
    # Here only expect the use of these keras modules:   Dense, Flatten, Activation, Dropout
    # For your information, dropout is randomly setting input units to 0 during training to prevent overfitting, details can be found in https://keras.io/api/layers/regularization_layers/dropout/.
    # Remark: your model will be trained with 6 epoches under the same training setting as here (e.g. the same training set, training epoches, optimizer etc.) for evaluation,
    #         you may (depends on how your model design) need to set some argument in the dense layer to prevent overfitting causing poorer result
    model.add(Flatten())
    model.add(Dense(units=50, activation='sigmoid', kernel_regularizer=regularizers.l2(0.003)))
    model.add(Dense(units=1, activation='sigmoid', kernel_regularizer=regularizers.l2(0.003)))
    # END of TODO
    return model


if __name__ == "__main__":
    pd.set_option('display.max_colwidth', None)
    df = pd.read_csv("D:/HKUST/COMP2211/LAB6/imdb.csv")
    X, y = get_X_y(df)
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.15, random_state=30)
    tokenizer = Tokenizer()
    tokenizer.fit_on_texts(X_train)

    X_train = tokenizer.texts_to_sequences(X_train)
    X_test = tokenizer.texts_to_sequences(X_test)
    # To reduce the complexity, we only look at the last 150 tokens of the review
    maxlen = 150

    # Pad to make sure each instance are having the same sequence len
    # padding='post': pad zero after the sequence until the sequence len of the sample reaching maxlen
    # truncating='pre'(default setting): truncate the tokens in the sequence that exceeding maxlen in descending order
    X_train = pad_sequences(X_train, padding='post', maxlen=maxlen)
    X_test = pad_sequences(X_test, padding='post', maxlen=maxlen)

    wordDict = formatEmbDict('D:/HKUST/COMP2211/LAB6/glove.6B.100d.txt.gz')

    vocab_size = len(tokenizer.word_index) + 1

    # Store the embedding corresponding to each vocab (i.e. each specific word) into embedding matrix
    embedding_matrix = np.zeros((vocab_size, 100))
    for word, index in tokenizer.word_index.items():
        # If there is no such word in wordDict, the get function will return None
        embedding_vector = wordDict.get(word)
        if(embedding_vector is not None):
            embedding_matrix[index] = embedding_vector

    model = myModel(vocab_size, embedding_matrix, maxlen)

    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['acc'])

    print(model.summary())

    history = model.fit(X_train, y_train, batch_size=128, epochs=6, verbose=1, validation_split=0.2)

    score = model.evaluate(X_test, y_test, verbose=1)
    print("Loss Score:", score[0])
    print("Test Accuracy:", score[1])
