from json_to_dict import json_to_dict
from dict_to_yaml import dict_to_yaml
from time import time

start_time = time()

rezult = dict_to_yaml(json_to_dict("../timetable.json"))
out = open("../output/timetable_1.yaml", "w", encoding="utf-8")
out.write(rezult)
out.close()

print(f"Стократное время выполнения: {(time() - start_time) * 100}")
