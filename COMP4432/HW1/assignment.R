library(FNN)
library(ggplot2)
rm(list=ls())

ntrain <- 50 # Training
ntest <- 500 # Testing
nrep <- 100 # repeat 100 times
p <- 20
puse <- c(1, 2, 3, 4, 10, 20) # number of predictors
k <- c(1:9)

sigma <- 0.025

Xtrain <- matrix(runif(ntrain * p, -1, 1), ntrain, p)
Xtest <- matrix(runif(ntest * p, -1, 1), ntest, p)
y0 <- sin(2 * pi * Xtrain[, 1]) # Only the first predictor is related to y
ytest <- sin(2 * pi * Xtest[, 1])

plot(Xtrain[, 1], y)

out_knn <- data.frame() # Output results
out_lm <- data.frame()

######### DO: fit linear regression using lm funciton, assign predicted value to yhat_lm[,l] #########


for(i in 1:length(puse)){
  yhat_lm <- matrix(0, ntest, nrep)
  yhat_knn <- replicate(length(k), matrix(0, ntest, nrep))
  i <- 2
  x <- Xtrain[, 1:puse[i]]
  
  for(l in 1:nrep){
    y <- y0 + rnorm(ntrain, 0, sigma)
    df <- data.frame(x)
    ######### DO: fit linear regression using lm funciton, assign predicted value to yhat_lm[,l] #########
    fit_lm <- lm(y ~ x, data=df)
    # summary(fit_lm)
    plot(x=predict(fit_lm), y=y)
    line()
    l <- 2
    beta <- coefficients(fit_lm)
    beta
    newdata <- data.frame(Xtest[,1:2])
    newdata
    pred <- predict(fit_lm, newdata)
    pred <- predict(fit_lm)
    predict(fit_lm)
    Xtest[,1:2]
    Xtest[,1:i]* beta[2:length(beta)]
    
    Xtest[1,1]*beta[2] + Xtest[1,2]*beta[3] + beta[1]
    
    if (i == 1) {
      yhat_lm[, l] <- Xtest[,1]*beta[2] + beta[1]
    } else {
      yhat_lm[, l] <- rowSums(Xtest[,1:i]*beta[2:length(beta)]) + beta[1] # Predicted value by lm
    }
    
    for(j in 1:length(k)){
      ######### DO: fit knn using knn.reg funciton, assign predicted value to yhat_knn[, l, j] #########
      knn_test = Xtest[,1:puse[i]]
      fit_knn <- knn.reg(
        train = as.data.frame(x), 
        y = y, 
        test = as.data.frame(knn_test), 
        k = j, 
      )
      yhat_knn[, l, j] <- fit_knn$pred# Predicted value by knn.reg
    }
    
    cat(i, "-th p, ", l, "-th repitition finished. \n")
  }
  
  ######### DO: compute bias and variance of linear regression #########
  
  # Compute mean of predicted values
  ybar_lm <- rowMeans(yhat_lm)# E(f^hat)

  # Compute bias^2
  biasSQ_lm <- mean((ytest - ybar_lm)^2)# E[ (f - E(f^hat))^2 ]

  # Compute variance
  tt <- ybar_lm - yhat_lm
  variance_lm <- mean((ybar_lm - yhat_lm)^2) # E[ (E(f^hat) - f^hat)^2 ]

  # Compute total MSE
  # err_lm <-

  out_lm <- rbind(out_lm, data.frame(error = biasSQ_lm, component = "squared-bias", p = paste0("p = ", puse[i])))
  out_lm <- rbind(out_lm, data.frame(error = variance_lm, component = "variance", p = paste0("p = ", puse[i])))
  # t_lm <- rbind(out_lm, data.frame(error = err_lm, component = "MSE", p = paste0("p = ", puse[i])))
  # 
  # 
  # ######### DO: compute bias and variance of knn regression #########
  # 
  # # Compute mean of predicted values
  # ybar_knn <- # E(f^hat)
  # 
  # # Compute bias^2
  # biasSQ_knn <- # E[ (f - E(f^hat))^2 ]
  # 
  # # Compute variance
  # variance_knn <- # E[ (E(f^hat) - f^hat)^2 ]
  # 
  # # Compute total MSE
  # err_knn <-
  # 
  # out_knn <- rbind(out_knn, data.frame(error = biasSQ_knn, component = "squared-bias", K = 1/k,p = paste0("p = ", puse[i])))
  # out_knn<- rbind(out_knn, data.frame(error = variance_knn, component = "variance", K = 1/k, p = paste0("p = ", puse[i])))
  # out_knn<- rbind(out_knn, data.frame(error = err_knn, component = "MSE", K = 1/k, p = paste0(" p = ", puse[i])))
}
bias_result <- subset(out_lm, component == "squared-bias")
variance_result <- subset(out_lm, component == "variance")
sum_result <- bias_result$error + variance_result$error
ggplot(data = NULL) +
  geom_line(aes(x = puse, y = bias_result$error, color = "biasSQ"), size = 1) +
  geom_line(aes(x = puse, y = variance_result$error, color = "variance"), size = 1) +
  geom_line(aes(x = puse, y = sum_result, color = "biasSQ + variance"), size = 1) +
  geom_vline(xintercept = log(puse)[which.min(sum_result)], linetype = "longdash", size = 2) + # Minimal error
  xlab("log(lambda)") +
  ylab("Error") +
  scale_color_manual(name = "Sources of error",
                     breaks = c("biasSQ", "variance", "biasSQ + variance"),
                     values = c("biasSQ" = "red", "variance" = "blue", "biasSQ + variance" = "green")) +
  theme(
    text = element_text(size = 18),
    axis.text.y = element_text(size = 18),
    axis.text.x = element_text(size = 18),
    legend.title = element_text(size = 15),
    legend.text = element_text(size = 15),
    legend.position = "bottom"
  )


linReg <- function (X, y) {
  n <- nrow(X) # Sample size
  p <- ncol(X) # Number of features
  
  X <- cbind(1, X) # Design matrix
  invK <- solve(t(X) %*% X) # Inverse of XTX
  beta <- invK %*% (t(X) %*% y) # Estimate beta
  
  residual <- y - X %*% beta
  Rsq <- 1 - sum(residual^2)/sum((y - mean(y))^2) # R square: ratio of "explained" variance to the "total" variance of y
  sig2 <- sum(residual^2)/(n - p - 1) # Why (n - p - 1)?
  
  Sig_beta <- sig2 * invK # Variance of estimation of beta
  se <- sqrt(diag(Sig_beta)) # Standard error
  t <- beta/se # t-statistics
  pval <- pt(abs(t), n - p - 1, lower.tail = F) * 2 # p-value
  return(list(beta = beta, sig2 = sig2, se = se, t = t, pval = pval, Rsq = Rsq))
}
