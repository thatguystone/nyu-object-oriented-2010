package translator;

/**
 * The purpose of this class is so that you may perform
 * instanceof(JavaPrimitive)
 *
 * Now it actually holds our primitives
 */
class JavaPrimitive extends JavaType {
	
	public static final JavaPrimitive intPrimitive = new JavaPrimitive("int32_t");
	public static final JavaPrimitive longPrimitive = new JavaPrimitive("int64_t");
	public static final JavaPrimitive shortPrimitive = new JavaPrimitive("int16_t");
	public static final JavaPrimitive charPrimitive = new JavaPrimitive("char");
	public static final JavaPrimitive bytePrimitive = new JavaPrimitive("int8_t");
	public static final JavaPrimitive booleanPrimitive = new JavaPrimitive("bool");

	private String cppType;

	private JavaPrimitive(String cppType) {
		//why is this here? I don't know, ask java
		super();
		this.cppType = cppType;
	}

	public String getCppType() {
		return this.cppType;
	}

	protected void process() {}
}

