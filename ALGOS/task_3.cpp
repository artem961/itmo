#include <algorithm>
#include <iostream>
#include <map>
#include <stack>
#include <vector>
using namespace std;

bool isNumber(string s) {
  for (size_t i = 0; i < s.length(); i++) {
    if (!((s[i] >= '0' && s[i] <= '9') || s[i] == '-')) {
      return false;
    }
  }
  return true;
}

vector<string> parseData(string s) {
  vector<string> data = {"", ""};
  for (size_t i = 0; i < s.length(); i++) {
    if (s[i] == '=') {
      data[0] = s.substr(0, i);
      data[1] = s.substr(i + 1, s.length());
      break;
    }
  }
  return data;
}

int main() {
  map<string, stack<int>> variables;
  stack<vector<string>> changes;
  string line;

  vector<string> newVector;
  changes.push(newVector);

  while (getline(cin, line)) {
    if (line == "{") {
      vector<string> newVector;
      changes.push(newVector);
    } else if (line == "}") {
      vector<string> ch = changes.top();
      for (size_t i = 0; i < ch.size(); i++) {
        variables[ch[i]].pop();
      }
      changes.pop();
    } else {
      int value;
      vector<string> data = parseData(line);

      if (isNumber(data[1])) {
        value = stoi(data[1]);
      } else {
        if (variables.find(data[1]) == variables.end() || variables[data[1]].empty()) {
          value = 0;
        } else {
          value = variables[data[1]].top();
        }
        cout << value << endl;
      }

      variables[data[0]].push(value);
      changes.top().push_back(data[0]);
    }
  }
  return 0;
}
