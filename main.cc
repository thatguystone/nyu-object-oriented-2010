/*
 * Object-Oriented Programming
 * Copyright (C) 2010 Robert Grimm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */

#include <iostream>

#include "JavaArray.h"

using namespace java::lang;
using namespace java::util;

int main(void) {

  // int[] a = new int[5];
  __JavaArray<int32_t>* a = new __JavaArray<int32_t>(3, 5, 6, 7);
  std::cout << get(a, 1, 1, 1) << std::endl;
  set(a,3,1,1,1);
  std::cout << get(a, 1, 1, 1) << std::endl;
  checkIndex(a, 1, 1, 1);
  
  //std::cout << "a[2]  : " << a->__data[2] << std::endl;

  /*// a[2] = 4;
  std::cout << "a[2] <- 4;" << std::endl;

  __checkNotNull(a);
  __checkArrayIndex(a, 2);
  a->__data[2] = 4;

  std::cout << "a[2]  : " << a->__data[2] << std::endl;

  std::cout << "-------------------------------------------------------------"
            << "-----------------"
            << std::endl;

  // Class[] b = new Class[2];
  ArrayOfClass b = new __Array<Class>(2);

  std::cout << "b[1]  : " << b->__data[1] << std::endl;

  // b[1] = b.getClass().getSuperClass();
  std::cout << "b[1] <- b.getClass().getSuperclass();" << std::endl;

  __checkNotNull(b);
  Class t1 = b->__vptr->getClass(b);

  __checkNotNull(t1);
  Class t2 = t1->__vptr->getSuperclass(t1);

  __checkNotNull(b);
  __checkArrayIndex(b, 1);
  __checkArrayStore(b, t2);
  b->__data[1] = t2;

  std::cout << "b[1]  : " << b->__data[1] << std::endl;

  std::cout << std::endl << "Happy, happy, joy, joy!" << std::endl;*/
  return 0;
}

