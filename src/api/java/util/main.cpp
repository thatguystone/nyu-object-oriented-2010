/**
 * testing generic arrays
 */

#include <iostream>

#include "JavaArray.h"

using namespace java::lang;
using namespace java::util;

int main(void) {

  // int[][][] a = new int[5][4][3];
  __JavaArray<int32_t>* a = new __JavaArray<int32_t>(3, 5, 4, 3);
  
  // System.out.println("a[1][1][1] = " + a[1][1][1]);
  std::cout << "a[1][1][1] = " << get(a, 1, 1, 1) << std::endl;
  
  // a[1][1][1] = 4;
  // System.out.println("a[1][1][1] = " + a[1][1][1]);
  set(a, 4, 1, 1, 1);
  std::cout << "a[1][1][1] = " << get(a, 1, 1, 1) << std::endl;
  
  
  // char[][] b = new char[10][10];
  __JavaArray<char>* b = new __JavaArray<char>(2, 10, 10);
  
  // b[1][0] = 'a';
  // b[2][1] = 'b';
  // b[3][2] = 'c';
  // System.out.println("b[1][0] = " + b[1][0]);
  // System.out.println("b[2][1] = " + b[2][1]);
  // System.out.println("b[3][2] = " + b[3][2]);
  set(b, 'a', 1, 0);
  set(b, 'b', 2, 1);
  set(b, 'c', 3, 2);
  std::cout << "b[1][0] = " << get(b, 1, 0) << std::endl;
  std::cout << "b[2][1] = " << get(b, 2, 1) << std::endl;
  std::cout << "b[3][2] = " << get(b, 3, 2) << std::endl;
  
  // Object[] c = new Object[2];
  __JavaArray<__Object>* c = new __JavaArray<__Object>(1, 2);

  // ArrayOutOfBounds Exception
  get(b, 20, 5);
 }

