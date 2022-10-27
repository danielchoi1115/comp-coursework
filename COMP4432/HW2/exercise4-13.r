library(ISLR)
library(MASS)
library(class)
library(naivebayes)

train_range <- (Weekly$Year <= 2008)
train <- Weekly[train_range,]
test <- Weekly[!train_range,]

train_y <- train$Direction
test_y <- test$Direction

train_x <- as.matrix(train$Lag2)
train_x <- cbind(train$Lag2, sqrt(abs(train$Lag1)))
test_x <- as.matrix(test$Lag2, sqrt(abs(train$Lag1)))

bayes_fit <- naive_bayes(train_y ~ Lag2, data = train) 
pred <- predict(bayes_fit, test)
conf <- table(pred, test_y)
conf
