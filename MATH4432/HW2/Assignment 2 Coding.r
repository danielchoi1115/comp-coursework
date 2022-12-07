library(ISLR)
library(MASS)
library(class)
library(naivebayes)

summary(Weekly)

pairs(Weekly)

plot(Weekly$Volume)

cor(Weekly$Volume, Weekly$Year)

glm_fit <- glm(Direction ~ Lag1 + Lag2 + Lag3 + Lag4 + Lag5 + Volume, data=Weekly, family="binomial")
summary(glm_fit)

prob <- predict(glm_fit, type="response")

preds <- ifelse(prob>.5, "Up", "Down")

conf <- table(preds, Weekly$Direction)
conf

TP <- conf["Up", "Up"]
TN <- conf["Down", "Down"]
FP <- conf["Down", "Up"]
FN <- conf["Up", "Down"]
print(paste("Accuracy: ", (TP+TN)/(TP+TN+FP+FN)))

train_range <- (Weekly$Year <= 2008)
train <- Weekly[train_range,]
test <- Weekly[!train_range,]

train_y <- train$Direction
test_y <- test$Direction

glm_fit <- glm(train_y ~ Lag2, data=train, family="binomial")
prob <- predict(glm_fit, test, type="response")

preds <- ifelse(prob>.5, "Up", "Down")

conf <- table(preds, test_y)
conf

TP <- conf["Up", "Up"]
TN <- conf["Down", "Down"]
FP <- conf["Down", "Up"]
FN <- conf["Up", "Down"]
print(paste("Accuracy: ", (TP+TN)/(TP+TN+FP+FN)))

lda_fit <- lda(train_y ~ Lag2, data=train)
preds <- predict(lda_fit, test)


conf <- table(preds$class, test$Direction)
conf

TP <- conf["Up", "Up"]
TN <- conf["Down", "Down"]
FP <- conf["Down", "Up"]
FN <- conf["Up", "Down"]
print(paste("Accuracy: ", (TP+TN)/(TP+TN+FP+FN)))

qda_fit <- qda(train_y ~ Lag2, data=train)
preds <- predict(qda_fit, test)

conf <- table(preds$class, test$Direction)
conf

TP <- conf["Up", "Up"]
TN <- conf["Down", "Down"]
FP <- conf["Down", "Up"]
FN <- conf["Up", "Down"]
print(paste("Accuracy: ", (TP+TN)/(TP+TN+FP+FN)))

train_x <- as.matrix(train$Lag2)
test_x <- as.matrix(test$Lag2)
set.seed(1)

knn_pred <- knn(train_x, test_x, train_y, k = 1)
conf <- table(knn_pred, test$Direction)
conf

TP <- conf["Up", "Up"]
TN <- conf["Down", "Down"]
FP <- conf["Down", "Up"]
FN <- conf["Up", "Down"]
print(paste("Accuracy: ", (TP+TN)/(TP+TN+FP+FN)))

bayes_fit <- naive_bayes(train_y ~ Lag2, data = train) 
pred <- predict(bayes_fit, test)
conf <- table(pred, test$Direction)
conf

TP <- conf["Up", "Up"]
TN <- conf["Down", "Down"]
FP <- conf["Down", "Up"]
FN <- conf["Up", "Down"]
print(paste("Accuracy: ", (TP+TN)/(TP+TN+FP+FN)))

train_x <- as.matrix(train$Lag2, sqrt(abs(train$Lag1)))
test_x <- as.matrix(test$Lag2, sqrt(abs(train$Lag1)))

for (k in 2:15) {
    knn_pred <- knn(train_x, test_x, train_y, k = k)
    conf <- table(knn_pred, test_y)
    print(paste("K=",k, mean(knn_pred==test_y)))
}

set.seed(1)
knn_pred <- knn(train_x, test_x, train_y, k = 4)
conf <- table(knn_pred, test_y)
conf

print(paste("Accuracy: ", mean(knn_pred==test_y)))

# qda with different vars
qda_fit <- qda(train_y ~ Lag2 + sqrt(abs(Lag1)), data=train)
preds <- predict(qda_fit, test)

conf <- table(preds$class, test_y)
conf

print(paste("Accuracy: ", mean(preds$class==test_y)))

# logistic regression with different model
glm_fit <- glm(train_y ~ Lag2 + sqrt(abs(Lag1)), data=train, family="binomial")
prob <- predict(glm_fit, test, type="response")

preds <- ifelse(prob>.5, "Up", "Down")

conf <- table(preds, test_y)
conf

print(paste("Accuracy: ",  mean(preds==test_y)))

train_x <- cbind(train$Lag2, sqrt(abs(train$Lag1)))
test_x <- cbind(test$Lag2, sqrt(abs(test$Lag1)))
set.seed(1)
for (k in 2:15) {
    set.seed(1)
    knn_pred <- knn(train_x, test_x, train_y, k = k)
    print(paste("K=",k, mean(knn_pred==test_y)))
}

set.seed(1)
knn_pred <- knn(train_x, test_x, train_y, k = 9)
conf <- table(knn_pred, test_y)
conf

print(paste("Accuracy (K = 9): ",  mean(knn_pred==test_y)))

set.seed(1)
knn_pred <- knn(train_x, test_x, train_y, k = 13)
conf <- table(knn_pred, test_y)
conf

print(paste("Accuracy (K = 13): ",  mean(knn_pred==test_y)))

library(MASS)
library(class)
library(repr)
library(naivebayes)

Auto = read.csv("C:/Users/Daniel Choi/Desktop/comp-coursework/COMP4432/HW2/Auto.csv", na.strings="?")

Auto = na.omit(Auto)
summary(Auto)

df <- data.frame(Auto[,-9])
df["mpg01"] <- ifelse(df["mpg"] > median(df$mpg), 1, 0)
df[10:20, ]

pairs(df)

cor(df)

options(repr.plot.width=10, repr.plot.height=10)
par(mfrow = c(2, 3))
plot(factor(df$mpg01), df$cylinders, ylab = "cylinders", xlab = "mpg01")
plot(factor(df$mpg01), df$displacement, ylab = "displacement", xlab = "mpg01")
plot(factor(df$mpg01), df$horsepower, ylab = "horsepower", xlab = "mpg01")
plot(factor(df$mpg01), df$weight, ylab = "weight", xlab = "mpg01")
plot(factor(df$mpg01), df$acceleration, ylab = "acceleration", xlab = "mpg01")
plot(factor(df$mpg01), df$year, ylab = "year", xlab = "mpg01")

set.seed(1)
auto_sample = sample(dim(df)[1], size = 0.5*dim(df)[1])

train = df[auto_sample,]
test = df[-auto_sample,]

head(train)

head(test)

lda_fit <- lda(train$mpg01 ~ cylinders+horsepower+weight+acceleration, data=train)
preds <- predict(lda_fit, test)

conf <- table(preds$class, test$mpg01)
conf

print(paste("Test Error: ", mean(preds$class != test$mpg01)))

qda_fit <- qda(train$mpg01 ~ cylinders+horsepower+weight+acceleration, data=train)
preds <- predict(qda_fit, test)

conf <- table(preds$class, test$mpg01)
conf

print(paste("Test Error: ", mean(preds$class != test$mpg01)))

glm_fit <- glm(train$mpg01 ~ cylinders+horsepower+weight+acceleration, data=train, family="binomial")

prob <- predict(glm_fit, test, type="response")
preds <- ifelse(prob>.5, 1, 0)

conf <- table(preds, test$mpg01)
conf

TP <- conf['1', '1']
TN <- conf['0', '0']
FP <- conf['0', '1']
FN <- conf['1', '0']

print(paste("Test Eror: ", 1 - (TP+TN)/(TP+TN+FP+FN)))

bayes_fit <- naive_bayes(as.factor(train$mpg01) ~ cylinders+horsepower+weight+acceleration, data = train) 
pred <- predict(bayes_fit, test)
conf <- table(pred, test$mpg01)
conf

TP <- conf['1', '1']
TN <- conf['0', '0']
FP <- conf['0', '1']
FN <- conf['1', '0']

print(paste("Test Eror: ", 1 - (TP+TN)/(TP+TN+FP+FN)))

set.seed(1)
for (k in c(1,2,3,5,7,10,20,50)) {
    knn_pred <- knn(train[2:5], test[2:5], train$mpg01, k = k)
    conf <- table(knn_pred, test$mpg01)
    print(paste("Test Error with K =",k, ": ", mean(knn_pred!=test$mpg01)))
}

data = c(1:100000)
for (i in 1:100000) {
    data[i] = 1 - (1-(1/i))**i
}
plot(data)

set.seed(1)
store <- rep (NA, 10000)
for (i in 1:10000){
    store[i] <- sum (sample (1:100, rep=TRUE) == 4) > 0
}
mean (store)

library(ISLR)

glm_fit = glm(default ~ income + balance, data = Default, family = "binomial")

set.seed(1)
auto_sample = sample(dim(Default)[1], size = 0.5*dim(Default)[1])

train = Default[auto_sample,]
test = Default[-auto_sample,]

dim(train)
head(train)

dim(test)
head(test)

glm_fit = glm(default ~ income + balance, data = train, family = "binomial")

prob <- predict(glm_fit, test, type="response")
preds <- ifelse(prob>.5, "Yes", "No")

mean(preds != test$default)

result <- c(0:3)
result[1] = 0.0254
for (s in 2:4) {
    set.seed(s)
    auto_sample = sample(dim(Default)[1], size = 0.5*dim(Default)[1])

    train = Default[auto_sample,]
    test = Default[-auto_sample,]
    glm_fit = glm(default ~ income + balance, data = train, family = "binomial")
    prob <- predict(glm_fit, test, type="response")
    preds <- ifelse(prob>.5, "Yes", "No")
    result[s] = mean(preds != test$default)
}
result

boxplot(result)
summary(result)

dummy_result <- c(0:3)
for (s in 1:4) {
    set.seed(s)
    auto_sample = sample(dim(Default)[1], size = 0.5*dim(Default)[1])

    train = Default[auto_sample,]
    test = Default[-auto_sample,]
    glm_fit = glm(default ~ ., data = train, family = "binomial")
    prob <- predict(glm_fit, test, type="response")
    preds <- ifelse(prob>.5, "Yes", "No")
    dummy_result[s] = mean(preds != test$default)
}
dummy_result

boxplot(dummy_result)
summary(dummy_result)

library(boot)

set.seed(1)

glm_fit = glm(default ~ income + balance, data = Default, family = "binomial")
summary(glm_fit)

summary(glm_fit)$coefficients[,2]

boot.fn <- function(d, index) {
    glm_fit = glm(default ~ income + balance, data = d[index,], family = "binomial")
    return (glm_fit$coefficients)
}



result <- boot(Default, boot.fn, 1000)
result



std_summary = summary(glm_fit)$coefficients[2:3,2]
boot_summary = sapply(data.frame(income = result$t[ ,2], balance = result$t[ ,3]), sd)
print("Standard error estimated by using `standard formula`")
std_summary
print("Standard error estimated by using `bootstrap`")
boot_summary
