import pywt
from scipy.io import wavfile
import matplotlib.pyplot as plt
import numpy as np
from numpy import random
import math

# num_rec = 2 # Number of Mixed sources

# n = 50000
# s = 3 #number of sounds
# p = 2 #number of micros

# x = np.zeros([n,3])
# x[:,0] = wavfile.read(f"{BASE_DIR}/bird.wav",n)[1][:n]
# x[:,1] = wavfile.read(f"{BASE_DIR}/male.wav",n)[1][:n]
# x[:,2] = wavfile.read(f"{BASE_DIR}/female.wav",n)[1][:n]

# x = x/np.tile(np.std(x,0),(n,1))
# theta = np.linspace(0, np.pi, s + 1)[:-1]
# theta[0] = .2
# M = np.vstack((np.cos(theta), np.sin(theta)))
# y = np.dot(x,np.transpose(M))

# wavfile.write(f"{BASE_DIR}/mixed1.wav", 16000, y[:,0])
# wavfile.write(f"{BASE_DIR}/mixed2.wav", 16000, y[:,1])

n = 50000

num_rec = 2
num_src = 3

BASE_DIR = "C:/Users/Daniel Choi/Desktop/comp-coursework/MATH4992/sources"
x_origin = np.zeros([n,3])
x_origin[:,0] = wavfile.read(f"{BASE_DIR}/bird.wav",n)[1][:n]
x_origin[:,1] = wavfile.read(f"{BASE_DIR}/male.wav",n)[1][:n]
x_origin[:,2] = wavfile.read(f"{BASE_DIR}/female.wav",n)[1][:n]

x = np.zeros([n, 2])

samplerate1, x[:,0] = wavfile.read(f'{BASE_DIR}/mixed1.wav')
samplerate2, x[:,1] = wavfile.read(f'{BASE_DIR}/mixed2.wav')



x = x/np.tile(np.std(x,0),(n,1))
theta = np.linspace(0, np.pi, num_src + 1)[:-1]
theta[0] = .2
M = np.vstack((np.cos(theta), np.sin(theta)))

wt_level = 6
wavelet = 'haar'
mode = 'symmetric'
coef_a = pywt.wavedec(x[:,0], wavelet=wavelet, level=wt_level, mode=mode)
coef_b = pywt.wavedec(x[:,1], wavelet=wavelet, level=wt_level, mode=mode)

# coef_a = pywt.dwt(x[:,0], wavelet=wavelet)
# coef_b = pywt.dwt(x[:,1], wavelet=wavelet)
P = False
for target_level in range(0,wt_level):
    
    wavelet_n = len(coef_a[target_level])
    y = np.zeros([wavelet_n, 2])
    y[:,0] = coef_a[target_level]
    y[:,1] = coef_b[target_level]

    npts = int(wavelet_n*0.8)

    # sel = np.argsort(np.sqrt(y[:,0]**2 + y[:,1]**2))

    sel = random.permutation(npts)
    sel = sel[:npts]
    
    if type(P) == bool:
        P = y[:, :]
    else:
        P = np.concatenate((P,y),axis=0)
    
    # plt.figure(figsize = (7,5))
    plt.plot(y[sel,0], y[sel,1], ".", ms = 3)
radius = 5
plt.xlim(-radius,radius)
plt.ylim(-radius,radius)
plt.title(f'wt_level at {wt_level-target_level}')

plt.show()


# nrow = np.shape(P)[0]
# Theta = np.zeros(nrow)
# for i in range(nrow):
#     Theta[i] = math.atan2(P[i,1],P[i,0])%np.pi
    
# nbins = 100
# t = np.linspace(np.pi/200,np.pi,nbins)
# hist = np.histogram(Theta,t)
# h = hist[0]/np.sum(hist[0])
# t = t[:-1]

# plt.figure(figsize = (7,5))
# plt.bar(t, h, width = np.pi/nbins, color = "darkblue", edgecolor = "darkblue")
# plt.xlim(0,np.pi)
# plt.ylim(0,np.max(h))
# plt.show()

d = np.sum(P**2, 1)
rho = .1
v = np.sort(d)
I = np.argsort(d)[::-1]

#transformed points
I = I[np.arange(1,round(rho*len(I))+1)]
P1 = P[I,:]  
    
#compute Theta
nrow = np.shape(P1)[0]
Theta = np.zeros(nrow)
for i in range(nrow):
    Theta[i] = math.atan2(P1[i,1],P1[i,0])%np.pi

nbins = 200
hist = np.histogram(Theta,nbins)
h = hist[0]/np.sum(hist[0])
t = hist[1][:-1]

# plt.figure(figsize = (7,5))
# plt.bar(t, h, width = np.pi/nbins, color = "darkblue", edgecolor = "darkblue")
# plt.xlim(0,np.pi)
# plt.ylim(0,np.max(h))
# plt.show()

# xxx = 1

s1 = np.hstack((np.arange(2,nbins+1),np.array([nbins-1]))) - 1
s2 = np.hstack((np.array([2]),(np.arange(1,nbins)))) - 1
I = np.where((h[s1] < h ) & (h[s2] < h))
v = np.sort(h[I])
u = np.argsort(h[I])[::-1]
theta1 = t[I[0][u[:3]]]
M1 = np.vstack((np.cos(theta1), np.sin(theta1)))
print("--- M ---")
print(M)
print("--- M1 ---")
print(M1)

C = abs(np.dot(np.transpose(M1),np.transpose(y)))
tmp = np.max(C,0)
I = np.argmax(C,0) + 1
print(I)
T = .05
D = np.sqrt(np.sum(abs(y)**2, 2))
I = I*(D > T)

Proj = np.dot(np.transpose(M1),np.transpose(A))
Xr = np.zeros([w, 4*w+1, num_src])
for i in range(s):
    Xr[:,:,i] = np.reshape(Proj[i,:], (mf, mt))*(I == i)
    
xr = np.zeros([n,s])
for i in range(s):
    xr[:,i] = perform_stft(Xr[:,:,i], w, q, n)
    
plt.figure(figsize = (10,10))

for i in range(num_src):
    plt.subplot(num_src, 1,i+1)
    plt.plot(xr[:,i])
    plt.xlim(0,n)
    plt.title("Estimated source #%i" %(i+1))