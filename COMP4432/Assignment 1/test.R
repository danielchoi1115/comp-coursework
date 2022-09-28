library(FNN)
library(ggplot2)
library(gridExtra)
rm(list=ls())

ntrain <- 50 # Training
ntest <- 500 # Testing
nrep <- 100 # repeat 100 times
p <- 20
puse <- c(1, 2, 3, 4, 10, 20) # number of predictors
k <- c(1:9)

sigma <- 0.9

Xtrain <- matrix(runif(ntrain * p, -1, 1), ntrain, p)
Xtest <- matrix(runif(ntest * p, -1, 1), ntest, p)
y0 <- sin(2 * pi * Xtrain[, 1]) # Only the first predictor is related to y
ytest <- sin(2 * pi * Xtest[, 1])

out_knn <- data.frame() # Output results
out_lm <- data.frame()


for(i in 1:length(puse)){
  yhat_lm <- matrix(0, ntest, nrep)
  yhat_knn <- replicate(length(k), matrix(0, ntest, nrep))

  for(l in 1:nrep){
    df_train <- data.frame(x=Xtrain[,1:puse[i]])
    y <- y0 + rnorm(ntrain, 0, sigma)

    ######### DO: fit linear regression using lm funciton, assign predicted value to yhat_lm[,l] #########
    fit_lm <- lm(y ~ ., data = df_train)
    testdata = data.frame(x=Xtest[, 1:puse[i]])
    yhat_lm[, l] <- predict(fit_lm, newdata=testdata) # Predicted value by lm
    
    for(j in 1:length(k)){
      ######### DO: fit knn using knn.reg funciton, assign predicted value to yhat_knn[, l, j] #########
      fit_knn <- knn.reg(
        train = df_train, 
        y = y, 
        test = testdata, 
        k = j, 
      ) 
      yhat_knn[, l, j] <- fit_knn$pred # Predicted value by knn.reg
    }
    
    cat(i, "-th p, ", l, "-th repitition finished. \n")
  }
  
  ######### DO: compute bias and variance of linear regression #########
  
  # Compute mean of predicted values
  ybar_lm <- rowMeans(yhat_lm) # E(f^hat)
    
  # Compute bias^2
  biasSQ_lm <- mean((ytest - ybar_lm)^2) # E[ (f - E(f^hat))^2 ]
  
  vari <- ybar_lm - yhat_lm
  yb <- data.frame(ybar_lm)
  # Compute variance
  variance_lm <- mean((ybar_lm - yhat_lm)^2)# E[ (E(f^hat) - f^hat)^2 ]
  
  # Compute total MSE
  err_lm <- biasSQ_lm + variance_lm
  # 
  out_lm <- rbind(out_lm, data.frame(error = biasSQ_lm, component = "squared-bias", p = paste0("p = ", puse[i])))
  out_lm <- rbind(out_lm, data.frame(error = variance_lm, component = "variance", p = paste0("p = ", puse[i])))
  out_lm <- rbind(out_lm, data.frame(error = err_lm, component = "MSE", p = paste0("p = ", puse[i])))


  ######### DO: compute bias and variance of knn regression #########

  # Compute mean of predicted values
  ybar_knn <- colMeans(aperm(yhat_knn, c(2,1,3)), dims=1)# E(f^hat)

  # Compute bias^2
  biasSQ_knn <- colMeans((ybar_knn - ytest)^2) # E[ (f - E(f^hat))^2 ]

  # Compute variance
  variance_knn <- c() # E[ (E(f^hat) - f^hat)^2 ]
  for(j in 1:length(k)){
    variance_knn <- append(variance_knn, mean(ybar_knn[,j] - yhat_knn[,,j]))
  }
  
  # Compute total MSE
  err_knn <- variance_knn + biasSQ_knn

  # out_knn <- rbind(out_knn, data.frame(error = biasSQ_knn, component = "squared-bias", K = 1/k,p = paste0("p = ", puse[i])))
  # out_knn<- rbind(out_knn, data.frame(error = variance_knn, component = "variance", K = 1/k, p = paste0("p = ", puse[i])))
  # out_knn<- rbind(out_knn, data.frame(error = err_knn, component = "MSE", K = 1/k, p = paste0("p = ", puse[i])))
  out_knn <- rbind(out_knn, data.frame(error = biasSQ_knn, component = "squared-bias", K = 1/k,p = puse[i]))
  out_knn<- rbind(out_knn, data.frame(error = variance_knn, component = "variance", K = 1/k, p = puse[i]))
  out_knn<- rbind(out_knn, data.frame(error = err_knn, component = "MSE", K = 1/k, p = puse[i]))
}

bias_result <- subset(out_lm, component == "squared-bias")
variance_result <- subset(out_lm, component == "variance")
mse_result <- subset(out_lm, component == "MSE")
ggplot(data = NULL) +
  geom_line(aes(x = puse, y = bias_result$error, color = "biasSQ"), size = 1) +
  geom_line(aes(x = puse, y = variance_result$error, color = "variance"), size = 1) +
  geom_line(aes(x = puse, y = mse_result$error, color = "biasSQ + variance"), size = 1) +
  geom_vline(xintercept = puse[which.min(mse_result$error)], linetype = "longdash", size = 2) + # Minimal error
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

get_knn_plot <- function(pvalue) {
  knn_bias_result <- subset(out_knn, component == "squared-bias" & p == pvalue)
  knn_variance_result <- subset(out_knn, component == "variance" & p == pvalue)
  knn_mse_result <- subset(out_knn, component == "MSE" & p == pvalue) 
  return (ggplot(data = NULL) +
    geom_line(aes(x = knn_bias_result$K, y = knn_bias_result$error, color = "biasSQ"), size = 1) +
    geom_line(aes(x = knn_bias_result$K, y = knn_variance_result$error, color = "variance"), size = 1) +
    geom_line(aes(x = knn_bias_result$K, y = knn_mse_result$error, color = "biasSQ + variance"), size = 1) +
    geom_vline(xintercept = knn_bias_result$K[which.min(knn_mse_result$error)], linetype = "longdash", size = 2) + # Minimal error
    xlab("1/K") +
    ylab("error") +
    scale_color_manual(name = paste0("P = ",pvalue),
                       breaks = c("biasSQ", "variance", "biasSQ + variance"),
                       values = c("biasSQ" = "red", "variance" = "blue", "biasSQ + variance" = "green")) +
    theme(
    #   text = element_text(size = 18),
    #   axis.text.y = element_text(size = 18),
    #   axis.text.x = element_text(size = 18),
    #   legend.title = element_text(size = 15),
    #   legend.text = element_text(size = 15),
      legend.position = "bottom"
    )
  )
}

draw_knn <- function() {
  plist <- list()
  for(i in 1:length(puse)) {
    plist <- append(plist, list(get_knn_plot(puse[i])))
  }
  grid.arrange(grobs=plist, ncol=3, nrow=2)
}

draw_knn()



