#include <iostream>
using namespace std;

int main() {
  long long a, b, c, d, k;
  cin >> a;
  cin >> b;
  cin >> c;
  cin >> d;
  cin >> k;

  long long f = a;
  long long previous = 0;

  for (long long i = 1; i < k + 1; i++) {
    f = f * b;
    f = f - c;
    if (f < 0) {
      f = 0;
      break;
    }

    if (f > d) {
      f = d;
      break;
    }

    if (f == previous) {
      break;
    }
    previous = f;
  }
  cout << f;

  return 0;
}