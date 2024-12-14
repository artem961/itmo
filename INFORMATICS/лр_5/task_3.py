import matplotlib.pyplot as plt
import pandas
import seaborn

dataf = pandas.read_csv("task_2.csv", delimiter=";")

dataf_melted = dataf.melt(
    id_vars="Date",
    value_vars=["Open", "Max", "Min", "Close"],
    var_name="Type",
    value_name="Value"
)

plt.figure(figsize=(10, 6))
seaborn.boxplot(
    data=dataf_melted,
    x="Date",
    y="Value",
    hue="Type",
    palette=["blue", "red", "green", "yellow"]
)

plt.title("Статистика")
plt.ylabel("Значение")
plt.xlabel("Дата")
plt.legend(loc="upper left")

plt.savefig("task_3.png")