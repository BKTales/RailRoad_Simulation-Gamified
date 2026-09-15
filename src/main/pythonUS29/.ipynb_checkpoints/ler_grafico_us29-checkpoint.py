import pandas as pd
import matplotlib.pyplot as plt

# Lê o arquivo gerado
df = pd.read_csv('../java/pt/ipp/isep/dei/us029/results/execution_times.csv')

plt.figure(figsize=(10, 6))
plt.plot(df["InputSize"], df["US13Time(ms)"], label="US13", marker='o')
plt.plot(df["InputSize"], df["US14Time(ms)"], label="US14", marker='s')

plt.title("Tempo de execução vs Tamanho do input")
plt.xlabel("Número de estações")
plt.ylabel("Tempo (ms)")
plt.legend()
plt.grid(True)
plt.tight_layout()

plt.savefig("us29_execucao.png")
plt.show()
