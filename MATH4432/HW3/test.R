
lambda <- scan(file = "C:/Users/Daniel Choi/Desktop/comp-coursework/MATH4432/HW3/lambda.txt")
loglam <- log(lambda, base=10)
train <- read.table(file = 'C:/Users/Daniel Choi/Desktop/comp-coursework/MATH4432/HW3/train.txt', sep = '\t', header = TRUE)

X = train[,2:21]
y = train[,1]
typeof(t(X))
calc_p <- function(X, beta) {
  beta = as.vector(beta)
  return(exp(X%*%beta) / (1+ exp(X%*%beta)))
}  

manualReg <- function(X, y, lambda) {
  
  n  <- nrow(X)
  x_col  <- ncol(X)
  
  #Centering, Scaling
  
  ym <- mean(y)
  y  <- y-ym
  Xm <- colMeans(X)
  X <- scale(X,center = T,scale = F)
  Xsd <- sqrt(colMeans(X^2))
  X <- apply(X,MARGIN = 1,function(X,sd) X/sd,sd=Xsd)
  X <- t(X)
  
  ns <- length(lambda)
  beta <- matrix(0,x_col,ns)
  
  for(i in 1:ns) {
    
    if (i == 1){
      beta_old <- beta[,1]
    }
    else {
      beta_old <- beta[, i-1]
    }

    p <- as.vector(calc_p(X, beta_old))
    W  <- diag(p*(1-p)) 
    
    lambda_matrix <- diag(lambda[i], x_col)
    d1 <- t(X) %*% (p - y) + 2 * lambda_matrix %*% beta_old
    d2 <- t(X) %*% W %*% X + 2 * lambda_matrix
    
    beta_change <- solve(d2) %*% (d1)
    beta[, i] <- beta_old - beta_change
    
  }
  beta <- beta/Xsd
  w0 <- ym - colSums(beta*Xm)
  beta <- rbind(w0,beta)

  beta <- list(w=beta,lambda=lambda,eigvalues=D)
  attr(beta, "class") <- "manualReg"
  
  return(beta)
}

predict.manualReg <- function(object, Xtest) {
  Xtest <- cbind(1,Xtest)
  Xtest <- t(t(Xtest))
  y_h <- Xtest %*% object$w
}

fit <- manualReg(X, y, lambda)



cv.manualReg <- function(X, y, lambda, center = F, scale = F, nfolds=10, ...){
  p <- ncol(X)
  n <- nrow(X)
  
  # decide the cv assignments
  idx <- round(sample(1:n)/n*nfolds)
  
  likelyhood  <- matrix(0,nfolds,length(lambda))
  
  # report settings
  message("Info: Number of variables: ", p)
  message("Info: Sample size: ", n)
  message("Info: Number of cv folds: ", nfolds)
  
  cat("start cv process......... total",nfolds,"validation sets \n")
  
  for(i in 1:nfolds) {
    cat(i,"-th validation set... \n")
    
    X_train <- X[idx!=i,]
    y_train <- y[idx!=i]
    
    X_test  <- X[idx==i,]
    y_test  <- y[idx==i]
    
    
    fit <- manualReg(X_train,y_train,lambda=lambda)
    
    predict(object=fit,X=X_test)
    y_hat <- predict(fit,X_test)

    phi <- sigmoid(y_hat)
    
    likelyhood[i,]  <- colMeans(y_test*log(phi) + (1-y_test)*log(1-phi))
  }
  
  cvsd <- sqrt(apply(likelyhood,MARGIN = 2,var)/(nfolds-1))
  cvm  <- colMeans(likelyhood)
  idx.max <- which.max(cvm)
  lambda.max <- fit$lambda[idx.max]
  
  manualReg.fit <- manualReg(X,y,lambda.max)
  
  cv_manual <- list(cvm=cvm,cvsd=cvsd,lambda=fit$lambda,lambda.max=lambda.max,manualReg.fit=manualReg.fit,likelyhood=likelyhood)
  attr(cv_manual,"class") <- "cv.manualReg"
  
  return(cv_manual)
}

sigmoid <- function(x){
  return (1 / (1 + exp(-x)))
}

plot.cv.manualReg <- function(object,width=0.01,...) {
  
  x <- log(object$lambda)
  y <- object$cvm
  
  upper <- y+object$cvsd/2
  lower <- y-object$cvsd/2
  ylim  <- range(upper,lower)
  barw  <- diff(range(x))*width
  
  plot(x,y,ylim = ylim,type = "n",xlab = "Log(Lambda)",ylab = "Predictive Likelihood",...)
  
  segments(x,lower,x,upper,col="darkgrey")
  segments(x-barw,lower,x+barw,lower,col="darkgrey")
  segments(x-barw,upper,x+barw,upper,col="darkgrey")
  points(x,y,col="red",pch=20)
  
  abline(v=log(object$lambda.max),lty=3)
}

set.seed(10) 
nfold_10 <- cv.manualReg(X,y,lambda=lambda,center=T,scale=T,nfolds = 10)
plot(nfold_10,main="10-fold cv")
