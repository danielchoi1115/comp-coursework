library(randomForest)
library(xgboost)
library(ggplot2)

X_test = read.table("X_test.txt", header = TRUE, sep = " ", dec = ".")
X_train = read.table("X_train.txt", header = TRUE, sep = " ", dec = ".")
y_test_high_snr = read.table("y_test_high_snr.txt", header = TRUE, sep = " ", dec = ".")
y_test_low_snr = read.table("y_test_low_snr.txt", header = TRUE, sep = " ", dec = ".")
y_train_high_snr = read.table("y_train_high_snr.txt", header = TRUE, sep = " ", dec = ".")
y_train_low_snr = read.table("y_train_low_snr.txt", header = TRUE, sep = " ", dec = ".")

mse <- function(y1, y2) {
    return (mean((y1 - y2)^2))
}

m = seq(20, 50, 1.25)
m

rf_errors_high <- data.frame()

for (i in 1:length(m)) {
    rf.fit = randomForest(
        y ~ ., 
        data=data.frame(X_train, y_train_high_snr), 
        mtry=m[i])
    preds = predict(rf.fit, newdata=X_test)
    rf_errors_high = rbind(rf_errors_high, data.frame(MSE=mse(preds, y_test_high_snr$y)))
    if (i %% 5 == 0) {
    cat(i, "-th loop done.\n")
    }
}

ggplot(rf_errors_high, aes(x=m, y=MSE)) +
    geom_point() +
    geom_line() +
    theme(
        text = element_text(size = 20)
    )

m = seq(1, 49, (49-1)/32)
m

rf_errors_low <- data.frame()

for (i in 1:length(m)) {
    rf.fit = randomForest(
      y ~ ., 
      data=data.frame(X_train, y_train_low_snr), 
      mtry=m[i]
    )
  
    preds = predict(rf.fit, newdata=X_test)
    rf_errors_low = rbind(rf_errors_low, data.frame(MSE=mse(preds, y_test_low_snr$y)))
    if (i %% 5 == 0) {
    cat(i, "-th loop done.\n")
    }
}

ggplot(rf_errors_low, aes(x=m, y=MSE)) +
    geom_point() +
    geom_line() +
    theme(text = element_text(size = 20))

lr = seq(0.05, 0.9, 0.05)^2
lr

xg_errors_high <- data.frame()

for (i in 1:length(lr)) {
    xg.fit = xgboost(
        data = data.matrix(X_train), 
        label = y_train_high_snr$y,
        nrounds = dim(X_train)[1], 
        eta = lr[i], 
        verbose = FALSE
    )
    
    pred = predict(xg.fit, newdata=data.matrix(X_test))
    xg_errors_high = rbind(xg_errors_high, data.frame(MSE=mse(pred, y_test_high_snr$y)))
    if (i %% 5 == 0) {
        cat(i, "-th loop done.\n")
    }
}

ggplot(xg_errors_high, aes(x=lr, y=MSE)) +
    geom_point() +
    geom_line() +
    theme(text = element_text(size = 20))

xg_errors_low <- data.frame()
for (i in 1:length(lr)) {
    xg.fit = xgboost(
        data = data.matrix(X_train), 
        label = y_train_low_snr$y,
        nrounds = dim(X_train)[1], 
        eta = lr[i], 
        verbose = FALSE
    )
    
    pred = predict(xg.fit, newdata=data.matrix(X_test))
    xg_errors_low = rbind(xg_errors_low, data.frame(MSE=mse(pred, y_test_low_snr$y)))
    if (i %% 5 == 0) {
        cat(i, "-th loop done.\n")
    }
}

ggplot(xg_errors_low, aes(x=lr, y=MSE)) +
    geom_point() +
    geom_line() +
    theme(
        text = element_text(size = 20)
    )
