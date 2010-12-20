package translator;

import java.util.LinkedHashMap;

/**
 * The current setup for expressions requires this class.
 */
abstract public class JavaType {
	/**
	 * If this class has been prepared and is ready for use (stops prepare() from being run more than once).
	 */
	private static boolean prepared = false;

	/**
	 * The list of all types we know about, including primitives.
	 */
	private static LinkedHashMap<String, LinkedHashMap<Integer, JavaType>> types = new LinkedHashMap<String, LinkedHashMap<Integer, JavaType>>();

	protected static JavaClass array;

	/**
	 * A class that represents primitives and their "inheritance".
	 */
	private static class Primitive extends JavaType {
		/**
		 * The name of the primitive type being represented.
		 */
		private String typeName;
		
		/**
		 * The name of the cpp type for the primitive.
		 */
		private String cppTypeName;
		
		/**
		 * The parent of this type. Null if there is no parent.
		 */
		private JavaType parent;
		
		/**
		 * The default value for our primitive.
		 */
		private String defaultValue;
		
		/**
		 * The name of the Object that can represent us.
		 */
		private String representingObjectName;
		
		/**
		 * Setup the basic parameters for the primitive type.
		 */
		Primitive(String javaTypeName, String cppTypeName, Primitive parent, String defaultValue, int dimensions, String representingObjectName) {
			super(javaTypeName, 0);
			this.typeName = javaTypeName;
			this.cppTypeName = cppTypeName;
			this.parent = parent;
			this.defaultValue = defaultValue;
			this.representingObjectName = representingObjectName;
		}

		Primitive(JavaType type, int dimensions) {
			super(type, dimensions);
			this.typeName = type.getName();
			this.cppTypeName = type.getCppName();
			this.parent = ((Primitive)type).getParent();
			this.defaultValue = type.getDefaultValue();
			this.representingObjectName = ((Primitive)type).representingObjectName;
		}
		
		/**
		 * Determines if we are a child of some parent primitive type.
		 */
		protected boolean hasParent(JavaType parent) {
			//only ever 1 instance of the type
			if (parent == this)
				return true;
			
			//if we reach the top, we're clearly not
			if (this.parent == null)
				return false;
			
			//maybe our parent is, thus making us one, too?
			return this.parent.hasParent(parent);
		}
		
		/**
		 * Gets the name that is usable as a type in C++.
		 */
		public String getCppName() {
			return this.cppTypeName;
		}
		
		/**
		 * Gets the name that is usable as a type in Java.
		 */
		public String getName() {
			return this.typeName;
		}
		
		/**
		 * Primitives don't have a class.
		 */
		public JavaClass getJavaClass() {
			if (dimensions != 0)
				return JavaType.array;
			return null;
		}
		
		/**
		 * The default value that primitives have.
		 */
		public String getDefaultValue() {
			return this.defaultValue;
		}

		public JavaType getParent() {
			return this.parent;
		}

		public int getDimensions() {
			return this.dimensions;
		}

		public void setDimensions(int dimensions) {
			this.dimensions = dimensions;
		}
		
		public String getRepresentingObjectName() {
			//someone is trying to access our object...activate it!
			JavaStatic.pkgs.getClass(this.representingObjectName).activate();
			return this.representingObjectName;
		}
	}
	
	/**
	 * A class that represents classes.
	 */
	private static class Object extends JavaType {
		/**
		 * The JavaClass we will use to interface with primitives.
		 */
		private JavaClass cls;

		/**
		 * Basic setup for the object type.
		 */
		Object(JavaClass cls, int dimensions) {
			super(cls.getName(), dimensions);
			this.cls = cls;
			
			//activate the class we're a type of, just in case
			this.cls.activate();
		
			if (JavaType.array == null) {
				JavaType.array = cls.getJavaFile().getImport("java.util.Array");
				if (JavaType.array != null)
				JavaType.array.activate();
			}
		}

		Object(JavaType type, int dimensions) {
			super(type, dimensions);
			this.cls = type.getJavaClass();
		}

		/**
		 * Determines if we are a child of some parent class.
		 */
		protected boolean hasParent(JavaType parent) {
			//we count on this only being called if the JavaType we're given
			//is an object
			return this.cls.isSubclassOf(((Object)parent).cls);
		}
		
		/**
		 * Gets the name that is usable as a type in C++.
		 */
		public String getCppName() {
			return this.cls.getCppName();
		}
		
		/**
		 * Gets the name that is usable as a type in Java.
		 */
		public String getName() {
			return this.cls.getName();
		}
		
		/**
		 * Quick accessor for grabbing the class from a type.
		 */
		public JavaClass getJavaClass() {
			if (dimensions != 0)
				return JavaType.array;
			return this.cls;
		}
		
		/**
		 * The default value that objects have -- null.
		 */
		public String getDefaultValue() {
			return "NULL";
		}

		public int getDimensions() {
			return this.dimensions;
		}

		public void setDimensions(int dimensions) {
			this.dimensions = dimensions;
		}
		
		public String getRepresentingObjectName() {
			return this.getCppName();
		}
	}

	/**
	 * Constructor.  Should only be accessible by the internal classes for instantiation.
	 */
	private JavaType(String typeName, int dimensions) {
		this.setDimensions(dimensions);
		if (types.get(typeName) == null)
			types.put(typeName, new LinkedHashMap<Integer, JavaType>());
		types.get(typeName).put((Integer)dimensions, this);
	}
	
	private JavaType(JavaType type, int dimensions) {
		this.setDimensions(dimensions);
		types.get(type.getName()).put((Integer)dimensions, this);
	}
	
	protected int dimensions;

	/**
	 * Setup our primitives.
	 */
	public static void prepare() {
		if (prepared)
			return;
		
		prepared = true;
		
		//and add all our primitives in the order they can be cast to
		Primitive dbl = new Primitive("double", "double", null, "0.0", 0, "");
		Primitive flt = new Primitive("float", "float", dbl, "0.0", 0, "");
		Primitive lng = new Primitive("long", "int64_t", flt, "0", 0, "");
		Primitive it = new Primitive("int", "int32_t", lng, "0", 0, "java.lang.Integer");
		Primitive chr = new Primitive("char", "char_t", it, "0", 0, "");
		Primitive shrt = new Primitive("short", "int16_t", it, "0", 0, "");
		Primitive byt = new Primitive("byte", "int8_t", shrt, "0", 0, "");
		
		//and those tricky primitives that can't be cast to anything
		new Primitive("void", "void", null, "NULL", 0, "");
		new Primitive("boolean", "bool", null, "false", 0, "");
		
		//and get the C++ types that we need
		JavaStatic.h.pln("#include <stdint.h>");
		JavaStatic.h.pln("typedef unsigned char char_t;");
		JavaStatic.h.pln();
	}
	
	/**
	 * If we do not know the fully-qualified class name, or even if the type is a primitive,
	 * this will figure it out for you.
	 */
	public static JavaType getType(JavaScope scope, String type, int dimensions) {
		//if we have the type cached, throw it back to him
		if (types.containsKey(type))
			return getType(type, (Integer)dimensions);
		
		//we don't know what type it is, so ask the file for the import
		//at this point, it won't be primitive
		return getType(scope.getJavaFile().getImport(type).getName(), (Integer)dimensions);
	}
	
	public static JavaType getType(JavaScope scope, String type) {
		return getType(scope, type, 0);
	}

	/**
	 * Used mainly for primitives / fully-qualified class names.
	 */
	public static JavaType getType(String type, int dimensions) {
		//if we have the type cached, throw it back to him
		if (!types.containsKey(type)) {
			//the class isnt here yet, so add it on-demand
			JavaClass cls = JavaStatic.pkgs.getClass(type);
			
			//make sure we have the type cached locally (added in JavaType.constructor)
			if (cls != null)
				new JavaType.Object(cls, dimensions);
		}
		else if (!types.get(type).containsKey(dimensions)) {
			if (types.get(type).get(0).isPrimitive())
				new JavaType.Primitive(types.get(type).get((Integer)0), dimensions);
			else
				new JavaType.Object(types.get(type).get((Integer)0), dimensions);
		}
		if(types.get(type) != null)
			return types.get(type).get((Integer)dimensions);
		return null;
	}

	public static JavaType getType(String type) {
		return getType(type, 0);
	}

	/**
	 * Figure out the resulting type of an ArithmeticExpression.
	 * Perhaps this should be named getArithmeticType?
	 */
	public static JavaType getType(JavaType type1, JavaType type2) {
		if (type1.getName().equals("java.lang.String"))
			return type1;
		if (type2.getName().equals("java.lang.String"))
			return type2;
		//is type2 a parent of type1?
		if (type1.hasParent(type2)) {
			//is int a parent of type2?
			if (type2.hasParent(JavaType.getType("int")))
				return JavaType.getType("int");
			return type2;
		}
		//is int a parent of type1?
		if (type1.hasParent(JavaType.getType("int")))
			return JavaType.getType("int");
		return type1;
	}
	
	/**
	 * Gets the name that is usable as a type in C++.
	 */
	public abstract String getCppName();
	
	public String getCppName(boolean array) {
		return this.getCppName(array, true);
	}
	
	public String getCppName(boolean array, boolean asPointer) {
		if (array) {
			if (asPointer) {
				return "ARRAY(" + this.getCppName() + ")";
			}
			return "java::util::__Array<" + this.getCppName() + ">";
		}
		if (asPointer || this.isPrimitive())
			return this.getCppName();
		return this.getJavaClass().getCppName(true, false);
	}

	public abstract int getDimensions();

	public abstract void setDimensions(int dimensions);

	/**
	 * Gets the java name of the type.
	 */
	public abstract String getName();
	
	/**
	 * Get the class of a type.
	 */
	public abstract JavaClass getJavaClass();
	
	/**
	 * The default value that the type has.
	 */
	public abstract String getDefaultValue();
	
	public abstract String getRepresentingObjectName();
	
	/**
	 * ==================================================================================================
	 * Methods that determine java type information
	 */
	 
	/**
	 * Are we just a wee primitive?  No need to push this off to the inner classes as there will only ever
	 * be two, and putting them there is just adding more code...but we could. Eh. This works.
	 */
	public boolean isPrimitive() {
		return (this instanceof Primitive);
	}
	
	/**
	 * Determines if we are a child of some parent class.
	 * Will only ever be called when the object is the same class as the parent; that is, it will only
	 * be called when (this.getClass() == parent.getClass()), so you do not need to worry about checks like
	 * that.
	 */
	protected abstract boolean hasParent(JavaType parent);
	
	/**
	 * Determines if we are a subclass / sub-primitive of the given type.  In other words, given another
	 * JavaType, it determines if that JavaType is one of our parents.
	 * Note: a class is a child of itself.
	 */
	public boolean isChildOf(JavaType parent) {
		if ((this.getDimensions()) > 0 && (parent.getName().equals("java.lang.Object"))) {
			return true;
		}
		
		//are we comparing a primitive with a class (or vis-versa)?
		if (!this.getClass().isInstance(parent))
			return false;
		
		//we can do a direct memory compare on this and parent because we only ever have 1 instance
		//of the type floating around
		return ((this == parent || this.hasParent(parent)) && (this.getDimensions() == parent.getDimensions()));
	}
}
