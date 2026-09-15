import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit

# 1. Ler dados
df = pd.read_csv('../java/pt/ipp/isep/dei/us029/results/execution_times.csv')
x = df['InputSize'].values
y = df['US13Time(ms)'].values

# 2. Definir modelo teórico (por exemplo linear O(n))
def linear_model(x, a, b):
    return a * x + b

# 3. Ajustar curva
params, _ = curve_fit(linear_model, x, y)
a, b = params
fitted_y = linear_model(x, a, b)

# 4. Plotar
plt.scatter(x, y, label='Tempos medidos', color='blue')
plt.plot(x, fitted_y, label=f'Ajuste linear: {a:.4f}x + {b:.2f}', color='red')
plt.xlabel('Input Size')
plt.ylabel('Execution Time (ms)')
plt.title('Ajuste da curva para US13')
plt.legend()
plt.grid(True)
plt.show()
