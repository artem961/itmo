#include <iostream>
using namespace std;

bool check(string str) {
  int len = str.length();
  if (len % 2 != 0)
    return false;

  for (int i = 0; i < len / 2; i++) {
    if (str[i] != str[i + len / 2])
      return false;
  }

  return true;
}

int main() {k
  
  int t;
  string str;
  cin >> t;

  for (int i = 0; i < t; i++) {
    cin >> str;
    if (check(str))
      cout << "YES\n";
    else
      cout << "NO\n";
  }

  return 0;
}
