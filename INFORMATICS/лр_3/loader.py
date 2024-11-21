def load_tests(ref):
    file = (open(ref, "r", encoding="UTF-8").readlines())
    file.append("\n")
    tests = []
    test = ""
    for str in file:
        if str == "\n" and test != "":
            tests.append(test)
            test = ""
        elif str != "\n":
            test += str
    return tests