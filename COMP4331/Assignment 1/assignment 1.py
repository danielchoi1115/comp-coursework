#!/usr/bin/env python
# coding: utf-8

# # Comp4331 Assignment 1

# In[2]:


from google.colab import drive
drive.mount('/content/drive/')


# In[3]:


get_ipython().run_line_magic('cd', '"/content/drive/MyDrive/HKUST/COMP 4331"')


# ## Preparing the dataset

# In[9]:



import seaborn as sns
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import scipy
import sklearn
from sklearn import preprocessing


# In[5]:


# Please ensure the file "forestfires.csv" is under current directory
data_file = "forestfires.csv"


# In[6]:


data_df = pd.read_csv(data_file, sep=',')


# In[7]:


data_df.head(10)


# ## Task 1. Basic summary

# In[8]:


data_df[["RH",	"wind"]].describe()


# ## Task 2. Box plot

# In[10]:


sns.boxplot(y=data_df["RH"])


# In[11]:


sns.boxplot(y=data_df["RH"], x=data_df["month"])


# In[12]:


sns.boxplot(y=data_df["RH"], x=data_df["day"])


# ## Task 3. Hist plot

# In[ ]:


sns.histplot(data_df["wind"], bins=8)


# In[ ]:


sns.histplot(data_df["RH"], bins=8)


# # Task 4. Scatter plot

# In[ ]:


sns.scatterplot(x="area", y="wind", data=data_df)
scipy.stats.pearsonr(data_df["area"],data_df["wind"])


# Correlation Coefficient between `wind` and `area` 0.0123

# ## Task 5. Correlation

# In[ ]:


hashmap = {}
for att in ["FFMC", "DMC", "DC", "ISI", "RH", "wind"]:
  hashmap[att] = scipy.stats.pearsonr(data_df["area"],data_df[att])[0]
print("Top 3  attributes that are most correlated with `area`")
sorted(hashmap.items(), key=lambda item: abs(item[1]), reverse=True)[:3]


# # Task 6. $\chi^2$-test
# 
# 

# In[ ]:


temp_mean = data_df["temp"].mean()
wind_mean = data_df["wind"].mean()

temp_ge_mean = data_df["temp"] >= temp_mean
wind_ge_mean = data_df["wind"] >= wind_mean
crosstab = pd.crosstab(temp_ge_mean, wind_ge_mean)

print(crosstab)
chi2, p, dof, _ = scipy.stats.chi2_contingency(crosstab)
print()
print(f"chi^2: {chi2}, p: {p}, dof: {dof}")


# Since p-value > 0.01, `temp` and `wind` are independent of each other.

# In[ ]:


x_mean = data_df["X"].mean()
y_mean = data_df["Y"].mean()

x_ge_mean = data_df["X"] >= x_mean
y_ge_mean = data_df["Y"] >= y_mean
crosstab = pd.crosstab(x_ge_mean, y_ge_mean)

print(crosstab)
chi2, p, dof, _ = scipy.stats.chi2_contingency(crosstab)
print()
print(f"chi^2: {chi2}, p: {p}, dof: {dof}")


# Since p-value <<< 0.01, `X` and `Y` are not independent of each other.

# ## Task 7. Normalization

# #### a)

# In[13]:


scaler = preprocessing.MinMaxScaler(feature_range=(0,1))
data_df["normalized_FFMC"] = scaler.fit_transform(data_df[["FFMC"]])

data_df


# #### b)

# In[17]:


scaler = preprocessing.StandardScaler()
for att in ["DC", "ISI", "temp", "RH", "wind"]:
  data_df[f"normalized_{att}"] = scaler.fit_transform(data_df[[att]])

data_df.iloc[:, -5:]


# In[15]:


d = data_df.iloc[:, -6:]
d


# In[16]:


d.to_csv("data_normalized.csv", sep=',')


# ## Task 8. PCA

# In[ ]:


from sklearn.decomposition import PCA


# In[ ]:


df = data_df.iloc[:, -5:]
df


# In[ ]:


pca = PCA()


# In[ ]:


PrincipalComponents = pca.fit_transform(df)


# In[ ]:


explain_ratio = pca.explained_variance_ratio_.cumsum().round(2)
print('explain_ratio', explain_ratio)

fig, axes = plt.subplots(1,1, figsize=(16,7), dpi=100)
plt.plot(range(1,len(explain_ratio)+1), explain_ratio, color='firebrick')
plt.title('Variance Explained Ratio %', fontsize=22)
plt.xlabel('Number of Principal Components', fontsize=16)


# Minimum number of coefficient > 0.9 = 4

# In[ ]:


pca = PCA(n_components=4)
PrincipalComponents = pca.fit_transform(df)
PrincipalComponents


# In[ ]:


reduced_df = pd.DataFrame(
    PrincipalComponents,
    columns = ["PC1", "PC2", "PC3", "PC4"]
)
reduced_df


# In[ ]:


reduced_df.to_csv("data_reduced.csv", sep=',', header=False)


# In[ ]:


reduced_df.describe().applymap(lambda x: f"{x:0.3f}")


# ## Task 9. Missing values

# reloading the dataset

# In[ ]:


data_df = pd.read_csv(data_file, sep=',')


# In[ ]:


nonzero_mean = data_df["area"][~(data_df["area"]==0)].mean()
nonzero_mean


# In[ ]:


data_df["area"][(data_df["area"]==0)] = nonzero_mean


# In[ ]:


data_df["area"]


# In[ ]:


sns.boxplot(y=data_df["area"])


# In[ ]:


sns.scatterplot(x="area", y="wind", data=data_df)
scipy.stats.pearsonr(data_df["area"],data_df["wind"])


# Their corrleation went down from 0.0123 to 0.0016.
# Which weakened the significance of null hypothesis.

# In[ ]:


get_ipython().system('apt-get install texlive texlive-xetex texlive-latex-extra pandoc')
get_ipython().system('pip install pypandoc')


# In[19]:


get_ipython().system("jupyter nbconvert --to PDF '/content/drive/MyDrive/HKUST/COMP 4331/assignment1.ipynb'")

