import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit

import pandas as pd
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit

# 1. Ler dados de ficheiros separados
df13 = pd.read_csv("../java/pt/ipp/isep/dei/USoutOfProgram/us029/results/us13_times.csv")
df14 = pd.read_csv("../java/pt/ipp/isep/dei/USoutOfProgram/us029/results/us14_times.csv")

# 2. Extrair os valores
x13 = df13['InputSize'].values
y13 = df13['Time(ms)'].values

x14 = df14['InputSize'].values
y14 = df14['Time(ms)'].values

# 3. Definir modelo polinomial de 4ª ordem
def poly4_model(x, a, b, c, d, e):
    return a * x**4 + b * x**3 + c * x**2 + d * x + e

# 4. Ajustar curvas
params13, _ = curve_fit(poly4_model, x13, y13)
params14, _ = curve_fit(poly4_model, x14, y14)

# 5. Calcular valores ajustados
fit13 = poly4_model(x13, *params13)
fit14 = poly4_model(x14, *params14)

# 6. Plotar resultados no mesmo gráfico
plt.figure(figsize=(10, 6))

plt.scatter(x13, y13, label='US13 - Tempos medidos', color='blue', marker='o')
plt.plot(x13, fit13, label='US13 - Ajuste polinomial 4ª ordem', color='blue')

plt.scatter(x14, y14, label='US14 - Tempos medidos', color='orange', marker='s')
plt.plot(x14, fit14, label='US14 - Ajuste polinomial 4ª ordem', color='orange')

plt.xlabel('Input Size')
plt.ylabel('Execution Time (ms)')
plt.title('Ajuste da curva para US13 e US14 (O(n⁴))')
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()

