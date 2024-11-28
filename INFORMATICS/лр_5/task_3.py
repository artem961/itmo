import matplotlib.pyplot as plt
import pandas
import seaborn

df = pandas.read_csv("task_2.csv", delimiter=";")

df_melted = df.melt(
    id_vars="Date",
    value_vars=["Open", "Max", "Min", "Close"],
    var_name="Type",
    value_name="Value"
)

plt.figure(figsize=(10, 6))
seaborn.boxplot(
    data=df_melted,
    x="Date",
    y="Value",
    hue="Type",
    palette=["blue", "orange", "gray", "yellow"]
)

plt.title("Статистика")
plt.ylabel("Значение")
plt.xlabel("Дата")
plt.legend(loc="upper right")

plt.savefig("task_3.png")
plt.show()
