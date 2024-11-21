code = input()
s1, s2, s3 = 0, 0, 0

# вычисление синдрома последовательности
for i in range(len(code)):
    if i % 2 == 0:
        s1 += int(code[i])
    if i in (1, 2, 5, 6):
        s2 += int(code[i])
    if i > 2:
        s3 += int(code[i])
s1 %= 2
s2 %= 2
s3 %= 2


if s1 + s2 + s3 == 0:
    print("В коде нет ошибок.")
    print(code[2] + code[4:])
else:
    err = int(str(s3) + str(s2) + str(s1), 2)
    print(f"Ошибка в бите №{err}")
    code = code[:err-1] + str((int(code[err-1]) + 1) % 2) + code[err:] # инвертирование бита с ошибкой
    print(code[2] + code[4:])
    
    