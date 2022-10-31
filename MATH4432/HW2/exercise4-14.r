library(ISLR)
library(MASS)
library(naivebayes)

Auto = read.csv("C:/Users/Daniel Choi/Desktop/comp-coursework/COMP4432/HW2/Auto.csv", na.strings="?")
Auto = na.omit(Auto)
summary(Auto)

df <- data.frame(Auto)
median <- median(df$mpg)
df["mpg01"] <- ifelse(df["mpg"] > median, 1, 0)
df[10:20, ]

pairs(df[,!grepl("name",names(df))])

set.seed(1)
auto_sample = sample(dim(df)[1], size = 0.5*dim(df)[1])

train = df[auto_sample,]
test = df[-auto_sample,]

lda_fit <- lda(train$mpg01 ~ cylinders+horsepower+weight+acceleration, data=train)

preds <- predict(lda_fit, test)

conf <- table(preds$class, test$mpg01)
conf

print(paste("Test Error: ", mean(preds$class != test$mpg01)))



bayes_fit <- naive_bayes(as.factor(train$mpg01) ~ cylinders+horsepower+weight+acceleration, data = train) 
pred <- predict(bayes_fit, test)
conf <- table(pred, test$mpg01)
conf


glm_fit = glm(default ~ income + balance, data = Default, family = "binomial")
summary(glm_fit)
summary(glm_fit)$coefficients[,2]

library(boot)
boot.fn <- function(d, index) {
  glm_fit = glm(default ~ income + balance, data = d[index,], family = "binomial")
  return (glm_fit$coefficients)
}
result <- boot(Default, boot.fn, 10)
result
sapply(data.frame(Intercept = result$t[, 1], income = result$t[ ,2], balance = result$t[ ,3]), sd)
