import re
from loader import load_tests

# Загрузка тестов
tests = load_tests("tests/test_1.txt")

# Подсчёт количества смайликов
for i in range(len(tests)):
    rez = re.findall(r"=-\(", tests[i])
    print(f"Тест {i+1}: {len(rez)} смайликов '=-('")