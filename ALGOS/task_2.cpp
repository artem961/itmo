#include <iostream>
#include <map>
#include <stack>
#include <vector>
using namespace std;

int abs(int n) {
  if (n < 0)
    return -n;
  else
    return n;
}

bool isPair(char a, char b) {
  if (abs(a - b) == 32)
    return true;
  else
    return false;
}

bool isAnimal(char a) {
  return ('a' <= a) && ('z' >= a);
}

void printAns(bool ans, map<int, int> ansMap) {
  if (ans) {
    cout << "Possible\n";
    for (const auto& [key, animal] : ansMap) {
      cout << animal << " ";
    }
  } else {
    cout << "Impossible\n";
  }
}

int main() {
  char c;
  stack<char> stk;
  stack<int> indexes;
  int catcherCnt = 0;
  int animalCnt = 0;
  map<int, int> ans;

  while (cin >> c) {
    int idx;
    if (isAnimal(c)) {
      idx = ++animalCnt;
    } else {
      idx = ++catcherCnt;
    }

    if (!stk.empty() && isPair(c, stk.top())) {
      if (isAnimal(c)) {
        ans[indexes.top()] = idx;
      } else {
        ans[idx] = indexes.top();
      }
      stk.pop();
      indexes.pop();
    } else {
      stk.push(c);
      indexes.push(idx);
    }
  }
  printAns(stk.empty(), ans);
  return 0;
}