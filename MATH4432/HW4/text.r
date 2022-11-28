library(randomForest)
library(xgboost)
library(ggplot2)

setwd('C:/Users/Daniel Choi/Desktop/comp-coursework/MATH4432/HW4')

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

errors <- data.frame()
data <- data.frame(X_train, y_train_high_snr)

cat("i=")

for (i in 1:length(m)) {
  rf.fit = randomForest(y ~ ., data=data, mtry=m[i])
  preds = predict(rf.fit, newdata=X_test)
  errors = rbind(errors, data.frame(error=mse(preds, y_test_high_snr$y)))
  cat(i, " ")
}
ggplot(errors, aes(x = m, y = error)) +
  geom_point() +
  geom_line() +
  theme(
    text = element_text(size = 24)
  )
