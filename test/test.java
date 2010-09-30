    // The data layout for java.lang.Object.
    class __Object {
      __Object_VT* __vptr;

      __Object()
      : __vptr(&__vtable) {
      };

      // --------------------------------------------------------------------

      // The function returning the class object representing
      // java.lang.Object.
      //
      // We use a function instead of a static field to avoid C++'s
      // "static initialization fiasco".  C++ does not specify the
      // order, in which static fields are initialized.  However,
      // the class object for some type T must point to the class
      // object for its direct superclass S, i.e., the class object
      // for S must be created before the class object for T.
      // The function enforces this ordering by allocating the
      // class object on first invocation and returning the same
      // object on subsequent invocations.
      static Class __class();

      // The methods implemented by java.lang.Object.
      static int32_t hashCode(Object);
      static bool equals(Object, Object);
      static Class getClass(Object);
      static String toString(Object);

    private:
      // The vtable for java.lang.Object.
      static __Object_VT __vtable;
    };
