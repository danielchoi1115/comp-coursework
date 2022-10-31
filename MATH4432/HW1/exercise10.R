Auto = read.csv("C:/Users/Daniel Choi/git/comp_labs/COMP4432/Assignment 1/Auto.csv", header=T, na.strings="?")
Auto = na.omit(Auto)
summary(Auto)


yhat_lm = lm(mpg~.-name, data=Auto)

par(mfrow=c(2,2))
plot(yhat_lm)