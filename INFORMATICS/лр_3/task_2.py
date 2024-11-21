import re
from loader import load_tests

# Загрузка тестов
tests = load_tests("tests/test_2.txt")

# Замена всех вхождений времени на строку (TBD)
for i in range(len(tests)):
    rez = re.sub(r"(([01][0-9])|(2[0-3])):[0-5][0-9](:[0-5][0-9])?", "(TBD)", tests[i])
    print(f"ТЕСТ {i+1}:\n{rez}")