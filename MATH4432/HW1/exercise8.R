

# 8. (b)
rownames(college) <- college[,1]
college <- college[,-1]

# 8. (c)

# i.
summary(college)

# ii.
college$Private <- as.factor(college$Private)
pairs(college[,1:10])

# iii.
plot(college$Private, college$Outstate)

# iv.
Elite <- rep ("No", nrow (college))
Elite[college$Top10perc > 50] <- " Yes "
Elite <- as.factor(Elite)
college <- data.frame(college , Elite)

summary(college)
plot(college$Elite, college$Outstate)

# v.
par(mfrow = c(2, 2))
hist(college$Apps)
hist(college$perc.alumni, col=2)
hist(college$Top25perc, col=3, breaks=30)
hist(college$PhD, breaks=10)

# vi.
plot(college$ perc.alumni, college$Grad.Rate)
# Graduation rate greater than 100% is an error data
plot(college$Outstate, college$Grad.Rate)
# High tution correlates to high graduation rate
plot(college$Personal, college$Top25perc)
# Students from school ranking correlates with