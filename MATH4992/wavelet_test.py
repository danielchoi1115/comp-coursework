import pywt

import matplotlib.pyplot as plt
x = [3, 7,1,1,-2,5,4,6,6,4,5,-2,1,1,7,3]

plt.plot(x)
plt.ylabel('some numbers')

plt.show()

cA2, cD2, cD1 = pywt.wavedec(x, 'db1', level=2, mode='periodic')

plt.plot(cD1)
plt.ylabel('some numbers')

plt.show()

print("cA2=", cA2)
print("cD2=",cD2)
print("cD1=",cD1)




