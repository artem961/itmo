from json_to_yaml import json_to_yaml
from time import time

start_time = time()

file = open("../timetable.json", 'r', encoding='utf-8')
rezult = json_to_yaml(file.read())
file.close()

out = open("../output/timetable_4.yaml", 'w', encoding='utf-8')
out.write(rezult)
out.close()

print(f"Стократное время выполнения: {(time() - start_time) * 100}")