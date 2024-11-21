

def dict_to_yaml(inp, indent=0):
    rezult = ""
    spacement = "  " * indent
    if isinstance(inp, dict):
        for key, value in inp.items():
            if isinstance(value, (dict, list)): # Если значение ключа это вложенная конструкция, то рекурсивный вызов
                rezult += f"{spacement}{key}:\n" + dict_to_yaml(value, indent+1)
            else:
                rezult += f"{spacement}{key}: \"{value}\"\n"
    elif isinstance(inp, list):
        for i in inp:
            if isinstance(i, (dict, list)): # Если значение ключа это вложенная конструкция, то рекурсивный вызов
                rezult += "-" + dict_to_yaml(i, indent)[1:] + "\n"
            else:
                rezult += spacement[:-2] + "- " + str(i) + "\n"
    return rezult