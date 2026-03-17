#include <iostream>
using namespace std;

void push(int array[], int element) {
  array[0] = array[1];
  array[1] = array[2];
  array[2] = element;
}

bool checkEqual(int a[]) { return (a[0] == a[1]) && (a[1] == a[2]); }

void compareToMaxRangeAndSwap(int maxRange[], int start, int end) {
  if ((maxRange[1] - maxRange[0]) < (end - start)) {
    maxRange[0] = start;
    maxRange[1] = end;
  }
}

int main() {
  int n;
  int a[3];
  int tmp;

  int startIdx = 0;
  int endIdx = 0;
  int maxRange[2] = {0, 0};
  cin >> n;

  for (int i = 0; i < n; i++) {
    cin >> tmp;
    push(a, tmp);
    if (checkEqual(a)) {
      compareToMaxRangeAndSwap(maxRange, startIdx, endIdx);
      startIdx = i - 1;
      endIdx = i;
    } else {
      endIdx = i;
    }
  }
  compareToMaxRangeAndSwap(maxRange, startIdx, endIdx);
  cout << maxRange[0] + 1 << " " << maxRange[1] + 1 << endl;
  return 0;
}
