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
y0 <- sin(2 * Xtrain[, 1]) # Only the first predictor is related to y
ytest <- sin(2 * Xtest[, 1])

plot(Xtrain[, 1], y)

out_knn <- data.frame() # Output results
out_lm <- data.frame()

######### DO: fit linear regression using lm funciton, assign predicted value to yhat_lm[,l] #########


for(i in 1:length(puse)){
  yhat_lm <- matrix(0, ntest, nrep)
  yhat_knn <- replicate(length(k), matrix(0, ntest, nrep))
  # i <- 2
  x <- Xtrain[, 1:puse[i]]
  
  for(l in 1:nrep){
    y <- y0 + rnorm(ntrain, 0, sigma)
    
    ######### DO: fit linear regression using lm funciton, assign predicted value to yhat_lm[,l] #########
    data = data.frame(x, y)
    fit_lm <- lm(y ~ x, data)
    call(fit_lm)
    cc <- coef(fit_lm)
    ccc <- coefficients(fit_lm)
    beta <- coefficients(fit_lm)
    if (i == 1) {
      yhat_lm[, l] <- Xtest[,1:i]*coef[2:length(coef)] + coef[1]
    } else {
      yhat_lm[, l] <- rowSums(Xtest[,1:i]*coef[2:length(coef)]) + coef[1] # Predicted value by lm
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
  # ybar_lm <- # E(f^hat)
  # 
  # # Compute bias^2
  # biasSQ_lm <- # E[ (f - E(f^hat))^2 ]
  # 
  # # Compute variance
  # variance_lm <- # E[ (E(f^hat) - f^hat)^2 ]
  # 
  # # Compute total MSE
  # err_lm <-
  # 
  # out_lm <- rbind(out_lm, data.frame(error = biasSQ_lm, component = "squared-bias", p = paste0("p = ", puse[i])))
  # out_lm <- rbind(out_lm, data.frame(error = variance_lm, component = "variance", p = paste0("p = ", puse[i])))
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
