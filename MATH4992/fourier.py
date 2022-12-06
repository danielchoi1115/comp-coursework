#!/usr/bin/env python
# coding: utf-8

# In[80]:


import numpy as np
import matplotlib.pyplot as plt
from scipy.fft import fftfreq
from scipy.fft import fft, ifft, fft2, ifft2


# In[227]:


T = 20 #seconds
N = 150 #measurements
t = np.linspace(0, T, N)
dt = np.diff(t)[0]


# ## Basic assumptions

# We know that for some `frequency` $f$ and `magnitude` $m$, a sine wave $y_i$ along `time` $t$ can be given as
# 
# $$y_i = m_i \sin(2{\pi}f_it), \ \ \ i = 1...n $$

# Now, let
# 
# $$
# f_1 = 0.25, \ \ m_1 = 4 \\
# f_2 = 0.5, \ \ m_2 = 2 \\
# f_3 = 0.75, \ \ m_3 = 3 \\
# $$
# 
# Now our ground truth models are
# 
# $$y_1 = 4 \sin(0.5{\pi}t)$$
# $$y_2 = 2 \sin(1{\pi}t)$$
# $$y_2 = 3 \sin(1.5{\pi}t)$$
# 

# And suppose we have a `source` $y_{measure}$
# 
# $$y_{measure} = y_1 + y_2 + y_3 + e$$
# where $e$ is some measurement noise

# In[322]:


f1 = 0.25
f2 = 0.5
f3 = 0.75

f1, f2, f3


# In[323]:


y1 = 4 * np.sin(2*np.pi*f1*t) + 0.1 * np.random.randn(len(t))
y2 = 2 * np.sin(2*np.pi*f2*t) + 0.1 * np.random.randn(len(t))
y3 = 3 * np.sin(2*np.pi*f3*t) + 0.1 * np.random.randn(len(t))

y_measure = y1 + y2 + y3


# ## Plots and illustrations

# In[324]:


plt.figure(figsize=(10,4))
plt.xlim(0,10)

plt.plot(t, y1, label="y1")
plt.plot(t, y2, label="y2")
plt.plot(t, y3, label="y3")
plt.legend(loc="upper left")
plt.ylabel('magnitude', fontsize=12)
plt.xlabel('time (s)', fontsize=12)
plt.show()


# In[325]:


plt.figure(figsize=(10,4))
plt.xlim(0,10)

plt.plot(t, y_measure, label="y_measure")
plt.legend(loc="upper left")
plt.ylabel('magnitude', fontsize=12)
plt.xlabel('time (s)', fontsize=12)
plt.show()


# ## Perform Fourier transformation

# In[326]:


f = fftfreq(len(t), dt)
y_fft = np.fft.fft(y_measure)


# In[327]:


f_half = f[:N//2]
mag = np.abs(y_fft)[:N//2] * 2 / N


# In[328]:


plt.plot(f_half, mag)
plt.ylabel('$magnitude$', fontsize=12)
plt.xlabel('$frequency$ $(s^{-1})$', fontsize=12)
plt.xlim([0, 2])
plt.show()


# In[329]:


hashmap = {x:y for x, y in zip(mag, f_half)}

j = 0
for i in sorted(hashmap, reverse=True):
  print( "frequency: ", hashmap[i], "magnitude: ", i)
  j += 1
  if j == 3:
    break


# ## Predict original sources

# We found distict frequencies which are
# 
# $$
# \hat f_1 = 0.2483, \ \ \hat m_1 = 4.025 \\
# \hat f_2 = 0.4967, \ \ \hat m_2 = 1.998 \\
# \hat f_3 = 0.7450, \ \ \hat m_3 = 2.894 \\
# $$
# 
# With this example, we can optain 
# 
# $$\hat y_1 = 4.025 \sin(0.2483 * 2{\pi}t)$$
# $$\hat y_2 = 1.998 \sin(0.4967 * 2{\pi}t)$$
# $$\hat y_3 = 2.894 \sin(0.7450 * 2{\pi}t)$$
# 
#   

# In[331]:


freq1, mag1 = 0.24833333333333335, 4.025303669073482
freq2, mag2 = 0.4966666666666667, 1.9984437580222585
freq3, mag3 = 0.7450000000000001, 2.8940682915904685

y = t[:N//2]

yhat_1 = mag1 * np.sin(2*np.pi*freq1*t)
yhat_2 = mag2 * np.sin(2*np.pi*freq2*t)
yhat_3 = mag3 * np.sin(2*np.pi*freq3*t)


# ## Prediction results

# In[343]:


plt.figure(figsize=(10,4))
# measurement 1
plt.plot(t, y1, label="$y_1$")

# prediction 1
plt.plot(t, yhat_1, label="$\hat{y}_1$")


plt.legend(loc="upper left", fontsize=12)
plt.ylabel('magnitude', fontsize=12)
plt.xlabel('time (s)', fontsize=12)
plt.show()


# In[342]:


plt.figure(figsize=(10,4))
# measurement 2
plt.plot(t, y2, label="$y_2$")

# prediction 2
plt.plot(t, yhat_2, label="$\hat{y}_2$")


plt.legend(loc="upper left", fontsize=12)
plt.ylabel('magnitude', fontsize=12)
plt.xlabel('time (s)', fontsize=12)
plt.show()


# In[344]:


plt.figure(figsize=(10,4))
# measurement 3
plt.plot(t, y3, label="$y_3$")

# prediction 3
plt.plot(t, yhat_3, label="$\hat{y}_3$")


plt.legend(loc="upper left", fontsize=12)
plt.ylabel('magnitude', fontsize=12)
plt.xlabel('time (s)', fontsize=12)
plt.show()

