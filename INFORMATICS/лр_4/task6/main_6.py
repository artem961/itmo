from py_json_to_proto.convert import convert, Options

file = open('../timetable.json', 'r', encoding="utf-8")
data = convert(file.read(), Options(False, False))
file.close()

out = open("../output/timetable_6.proto", 'w', encoding="utf-8")
out.write(str(data))
out.close()