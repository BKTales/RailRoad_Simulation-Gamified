import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit

# 1. Ler dados
df = pd.read_csv("../java/pt/ipp/isep/dei/USoutOfProgram/us029/results/us14_times.csv")
x = df['InputSize'].values
y = df['Time(ms)'].values

def poly4_model(x, a, b, c, d, e):
    return a * x**4 + b * x**3 + c * x**2 + d * x + e


params, _ = curve_fit(poly4_model, x, y)
a, b, c, d, e = params
fitted_y = poly4_model(x, a, b, c, d, e)

# Calcular R²
ss_res = np.sum((y - fitted_y) ** 2)
ss_tot = np.sum((y - np.mean(y)) ** 2)
r_squared = 1 - (ss_res / ss_tot)

# Plotar
plt.scatter(x, y, label='Tempos medidos', color='blue')
plt.plot(x, fitted_y, label=f'Ajuste polinomial 4ª ordem (R²={r_squared:.4f})', color='red')
plt.xlabel('Input Size')
plt.ylabel('Execution Time (ms)')
plt.title('Ajuste da curva para US14')
plt.legend()
plt.grid(True)
plt.show()