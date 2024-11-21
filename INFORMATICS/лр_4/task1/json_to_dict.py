# Основная функция
def json_to_dict(source):
    file = open(source, "r", encoding="utf-8")
    string = ""
    IN_QUOTES = False
    tmp = file.read()
    for i in tmp:
        if i in '"':
            IN_QUOTES = not IN_QUOTES
        if not (not IN_QUOTES and i in "\n ") or i == '"':
            string += i
    file.close()
    return parse_json(string)


# Функция для распределения
def parse_json(string):
    try:
        string = string.strip()
    except:
        return string

    # определяем тип того, что передали в функцию и передаём эту грязную работу другой функции, либо возвращаем значение
    if string.startswith("{") and string.endswith("}"):
        return parse_object(string[1:-1]) # Парсинг объекта
    elif string.startswith("[") and string.endswith("]"):
        return parse_array(string[1:-1]) # Парсинг массива
    elif (":" in string and string.count('"') >= 2) and not (":" in string and string.endswith('"') and string.count('"') == 2): # так много, чтобы значения с двоеточием не попали сюда
        return parse_item(string) # Парсинг пары ключ-значение
    else: # Парсинг строки, числа
        if string.startswith('"') and string.endswith('"'):
            string = string[1:-1]
            return string
        try:
            string = float(string)
            if int(string) == string:
                string = int(string)
        except:
            pass
        if string == "Null":
            string = None
        elif string == "true":
            string = True
        elif string == "false":
            string = False
        elif not (isinstance(string, int) or isinstance(string, float) or isinstance(string, str)):
            string = parse_json(string)
        return string


# Парсит объект
def parse_object(string):
    dictionary = {}
    IN_QUOTES = False
    IN_OBJ = False
    IN_ARR = False
    item = ""
    i = 0
    while i < len(string):
        if string[i] in '"':
            IN_QUOTES = not IN_QUOTES
        if string[i] in '[]':
            IN_ARR = not IN_ARR
        if string[i] in '{}':
            IN_OBJ = not IN_OBJ
        if string[i] == ',' and not (IN_QUOTES or IN_OBJ or IN_ARR):
            rez = parse_json(item)
            dictionary[rez[0]] = parse_json(rez[1])
            item = ""
        else:
            item += string[i]
        i += 1
    if item != "":
        rez = parse_json(item)
        dictionary[rez[0]] = rez[1]
    return dictionary


# Парсит массив
def parse_array(string):
    arr = []
    IN_QUOTES = False
    IN_OBJ = False
    IN_ARR = False
    item = ""
    i = 0
    while i < len(string):
        if string[i] in '"':
            IN_QUOTES = not IN_QUOTES
        if string[i] in '[]':
            IN_ARR = not IN_ARR
        if string[i] in '{}':
            IN_OBJ = not IN_OBJ
        if string[i] == ',' and not (IN_QUOTES or IN_OBJ or IN_ARR):
            arr.append(parse_json(item))
            item = ""
        else:
            item += string[i]
        i += 1
    if item != "":
        arr.append(parse_json(item))
    return arr


# Парсит пару ключ-значение
def parse_item(string):
    string = string.strip()
    IN_QUOTES = False
    IN_OBJ = False
    IN_ARR = False
    KEY_PARSED = False
    key = ""
    value = ""
    i = 0
    while i < len(string):
        if string[i] in '[]':
            IN_ARR = not IN_ARR
        if string[i] in '{}':
            IN_OBJ = not IN_OBJ
        if string[i] == '"' and not KEY_PARSED:
            if IN_QUOTES and not KEY_PARSED:
                KEY_PARSED = True
            IN_QUOTES = not IN_QUOTES
        elif IN_QUOTES and not KEY_PARSED:
            key += string[i]
        elif KEY_PARSED and not IN_QUOTES and string[i] == ":": # Считываем значение после записи ключа
            i += 1
            while i < len(string):
                value += string[i]
                i += 1
        i += 1
    value = parse_json(value)
    return (key, value)


if __name__ == "__main__":
    # out = json_to_dict("../timetable.json")  # print(out)
    #print(parse_json('{"ПП": "12:2"}'))
    print(json_to_dict("../timetable.json"))
