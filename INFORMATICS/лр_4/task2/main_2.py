import json
import yaml
from time import time

start_time = time()

file = open("../timetable.json", "r", encoding="utf-8")
text = json.load(file)  # В переменную загрузятся данные из json файла
file.close()

# Конвертация и запись
rezult = yaml.dump(text, allow_unicode=True)

out = open("../output/timetable_2.yaml", "w", encoding="utf-8")
out.write(rezult)
out.close()

print(f"Стократное время выполнения: {(time() - start_time) * 100}")