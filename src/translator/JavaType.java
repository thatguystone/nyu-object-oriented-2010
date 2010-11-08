package translator;

import java.util.LinkedHashMap;

/**
 * The current setup for expressions requires this class.
 */
public class JavaType {
	/**
	 * If this class has been prepared and is ready for use (stops prepare() from being run more than once).
	 */
	private static boolean prepared = false;
	
	/**
	 * The list of all types we know about, including primitives.
	 */
	private static LinkedHashMap<String, JavaType> types = new LinkedHashMap<String, JavaType>(); 

	/**
	 * The name of the type.
	 */
	private String javaType;
	
	/**
	 * If we're not primitive, then the class.
	 */
	private JavaClass cls;
	
	/**
	 * The cpp name of the type.
	 */
	private String cppType;

	private JavaType(JavaClass cls) {
		this.javaType = cls.getName();
		this.cls = cls;
		
		types.put(javaType, this);
	}
	
	/**
	 * Handles our primitives.
	 */
	private JavaType(String javaType, String cppType) {
		this.javaType = javaType;
		this.cppType = cppType;
		
		types.put(javaType, this);
	}
	
	/**
	 * Setup our primitives.
	 */
	public static void prepare() {
		if (prepared)
			return;
		
		prepared = true;
		
		//and add all our primitives
		new JavaType("char", "char");
		new JavaType("byte", "int8_t");
		new JavaType("short", "int16_t");
		new JavaType("int", "int32_t");
		new JavaType("long", "int64_t");
		new JavaType("float", "float");
		new JavaType("double", "double");
		new JavaType("boolean", "bool");
		new JavaType("void", "void");
	}
	
	/**
	 * Gets the name of the type (as a string) in Java.
	 */
	public String getTypeName() {
		return this.javaType;
	}
	
	/**
	 * If we do not know the fully-qualified class name, or even if the type is a primitive,
	 * this will figure it out for you.
	 */
	public static JavaType getType(JavaScope scope, String type) {
		//if we have the type cached, throw it back to him
		if (types.containsKey(type))
			return getType(type);
		
		//we don't know what type it is, so ask the file for the import
		//at this point, it won't be primitive
		return getType(scope.getJavaFile().getImport(type).getName());
	}
	
	/**
	 * Used mainly for primitives / fully-qualified class names.
	 */
	public static JavaType getType(String type) {
		//if we have the type cached, throw it back to him
		if (!types.containsKey(type)) {
			//the class isnt here yet, so add it on-demand
			new JavaType(JavaStatic.pkgs.getClass(type));
		}
		
		return types.get(type);
	}
}
