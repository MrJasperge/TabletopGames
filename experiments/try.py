import math
import matplotlib.pyplot as plt
import numpy as np

data1 = [20, 18, 12, 11, 8, 4, 2, 0]
data2 = [20, 19, 18, 15, 14, 12, 11, 8, 4, 2, 0]
data3 = [20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
data4 = [20, 3, 15, 19, 0, 3]

resolution = 101

newDatas = []

for data in [data1, data2, data3, data4]:
    if len(data) < 2:
        continue
    stepSize = (len(data) - 1) / (resolution - 1)

    newData = []

    for i in range(resolution - 1):
        index = i * stepSize
        low = math.floor(index)
        high = low + 1
        fraction = max(0, index - low)

        print(f"Index: {i}, Low: {low}, High: {high}, Fraction: {fraction}")

        newData.append(data[low] * (1 - fraction) + data[high] * fraction)

    newData.append(data[-1])  # Append the last value
    newDatas.append(newData)

plt.figure(figsize=(10, 5))
for newData in newDatas:
    plt.plot(newData, label='Interpolated Data' + str(newDatas.index(newData) + 1))

# plot mean of the interpolated data
meanData = np.mean(newDatas, axis=0)
plt.plot(meanData, label='Mean Interpolated Data', linestyle='--', color='black')

plt.legend()
plt.title('Data Interpolation')
plt.xlabel('Index')
plt.ylabel('Value')
plt.grid()
plt.show()

