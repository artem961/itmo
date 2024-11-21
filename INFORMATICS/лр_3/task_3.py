import re
from loader import load_tests

# Загрузка тестов
tests = load_tests("tests/test_3.txt")

# Проверка правильности адреса и вывод результата
for i in range(len(tests)):
    if re.fullmatch(r"^[\w.]+@[a-zA-Z.]+(\.[a-zA-Z]+)$", tests[i].strip()):
        print(f"Тест {i + 1}: {re.search(r"@[\w.]+", tests[i])[0][1:]}")
    else:
        print(f"Тест {i+1}: Fail!")