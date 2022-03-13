import numpy as np

# delimiter: The string used to separate values. As ',' is used as delimiter in csv, we specify delimiter as ','
# skiprows: Skip the first skiprows lines; default: 0. As we want to skip the header row, we specify skiprows as 1 
train = np.loadtxt("D:/HKUST/COMP2211/LAB2/heart_disease_train_dataset.csv", delimiter=',', skiprows=1)
test = np.loadtxt("D:/HKUST/COMP2211/LAB2/heart_disease_test_dataset.csv", delimiter=',', skiprows=1)

train_features = train[:, :-1] # All except the last column.
train_labels = train[:, -1]    # Only the last column.

num_heart_disease_yes = 0 # Count of heart_disease = yes
num_heart_disease_no = 0  # Count of heart_disease = no

features_count = {
  'heart_disease_yes': {},
  'heart_disease_no': {}
}

# Count Number of occurence of each feature
# 
################### example #####################
# features_count = {
#     'heart_disease_yes' : {
#         0:{ # column index of resting_blood_pressure
#             0: 0, # Count of resting_blood_pressure = Normal and heart disease = yes
#             1: 0, # Count of resting_blood_pressure = Elevated and heart disease = yes
#             2: 0  # Count of resting_blood_pressure = Hypertension and heart disease = yes
#         },
#         1: { # column index of serum_cholesterol
#             0: 0, # Count of serum_cholesterol = normal and heart disease = yes
#             1: 0,

for row in range(train_features.shape[0]):
  if train_labels[row] == 1:
    num_heart_disease_yes += 1
    heart_disease = 'heart_disease_yes'
  else:
    num_heart_disease_no += 1
    heart_disease = 'heart_disease_no'
  
  for column in range(6):
    if not features_count[heart_disease].get(column):
      features_count[heart_disease][column] = {}
    label_value = train_features[row][column]
    if not features_count[heart_disease][column].get(label_value):
      features_count[heart_disease][column][label_value] = 0
    features_count[heart_disease][column][label_value] += 1
print(features_count)
heart_disease_yes = num_heart_disease_yes/train.shape[0] # P(heart_disease = yes)
heart_disease_no = num_heart_disease_no/train.shape[0]   # P(heart_disease = no)

prior_probability = {} 

# Prior Probability dictionary, P(label)
#
################### example #####################
# prior_probability = {
#     0: {
#       0: prob,
#       1: prob2...
#     }
# }

for features, label_dict in features_count['heart_disease_yes'].items():
  if not prior_probability.get(features):
    prior_probability[features] = {}
  for label, count in label_dict.items():
    p = (features_count['heart_disease_yes'][features][label] + features_count['heart_disease_no'][features][label])/train.shape[0]
    prior_probability[features][label] = p

# Relative Frequency dictionary for given heart disease ouccrence, P(feature|label))
#
################### example #####################
# relative_frequency = {
#   'heart_disease_yes': {
#     0: { 
#       0: prob,
#       1: prob2...
#     

relative_frequency = {
  'heart_disease_yes': {},
  'heart_disease_no': {}
}

for heart_disease in relative_frequency:
  for feature, label_dict in features_count[heart_disease].items():
    if not relative_frequency[heart_disease].get(feature):
      relative_frequency[heart_disease][feature] = {}
    for label, count in label_dict.items():
      if heart_disease == 'heart_disease_yes':
        relative_frequency[heart_disease][feature][label] = count/num_heart_disease_yes
      else:
        relative_frequency[heart_disease][feature][label] = count/num_heart_disease_no
print(relative_frequency)



# The given code below sums the log-prior-probability of Heart Disease.
# Modify the code to also sum the log-likelihoods for all the remaining features, according to the specific test case.

test_features = test[:, :-1] # All except the last column.
test_labels = test[:, -1]    # Only the last column.
predict_labels = np.zeros_like(test_labels)       # Create a numpy array of zeros with the same shape as test_labels. 

log_heart_disease_yes = np.log(heart_disease_yes) # log_e of P(heart_disease = yes)
log_heart_disease_no = np.log(heart_disease_no)   # log_e of P(heart_disease = no)

for row in range(test_features.shape[0]):
  # 6 features
  predict_yes = log_heart_disease_yes # log_e of P(heart_disease = yes)
  predict_no = log_heart_disease_no   # log_e of P(heart_disease = no)

  for feature_index in range(6):
    case_index = test_features[row][feature_index]
    predict_yes += np.log(relative_frequency['heart_disease_yes'][feature_index][case_index])
    predict_no  += np.log(relative_frequency['heart_disease_no'][feature_index][case_index])
  
  if predict_yes > predict_no:
    predict_labels[row] = 1
  

num_match = 0
for i in range(predict_labels.shape[0]):
  if predict_labels[i] == test_labels[i]:
    num_match += 1
accuracy_score = num_match/predict_labels.shape[0]

print(accuracy_score)
print(predict_labels)
print(test_labels)