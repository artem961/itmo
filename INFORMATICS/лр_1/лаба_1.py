from idlelib.configdialog import is_int


def fib_n(n): #Получение числа ряда Фибоначчи с номером n
    if n == 0 or n == 1:
        return 1
    return fib_n(n - 1) + fib_n(n - 2)


def to_fib(n): #Перевод в СС Фибоначчи
    if n <= 0:
        return 0
    elif n == 1:
        return 1
    else:
        lst= [0, 1]
        while lst[-1] < n:
            lst.append(lst[-1] + lst[-2])
        lst.pop()
        ans = ""
        for i in range(len(lst) - 1, 0, -1):
            if n >= lst[i]:
                ans += "1"
                n -= lst[i]
            else:
                ans += "0"
        return ans[:-1]


a = input()
b = input()
c = input()
alf = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
ans = 0

if b == "Фиб": #Перевод из Фибоначчиевой в СС-10
    for i in range(len(a)):
        ans += fib_n(len(a) - i ) * alf.index(a[i])

elif is_int(b): #Перевод из любой СС-N в СС-10
    b = int(b)
    for i in range(len(a)):
        ans += b**(len(a)-i-1) * alf.index(a[i])

elif b[-1] in "CС": #Перевод из симметричной СС в СС-10
    b = int(b[:-1])
    tmp = []
    i = 0
    while i < len(a):
        if a[i] in alf:
            tmp.append(int(a[i]))
        else:
            tmp.append(-int(a[i+2]))
            i = i + 3
        i += 1
    a = tmp
    for i in range(len(a)):
        ans += b**(len(a)-i-1) * a[i]

if c == "10":
    print(ans)

elif c == "Фиб":
    print(to_fib(ans))

