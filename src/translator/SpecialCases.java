package translator;

import translator.Printer.CodeBlock;

import SpecialCases.SpecialCase;

import java.util.HashMap;
import java.lang.reflect.Constructor;

/**
 * An interface to special cases in the API.
 */
public class SpecialCases {
	/**
	 * Holds a cache of if classes are special case.
	 */
	private static HashMap<String, Boolean> classes = new HashMap<String, Boolean>();
	
	/**
	 * Holds a cache of the special case instances.
	 */
	private static HashMap<String, SpecialCase> cases = new HashMap<String, SpecialCase>();

	/**
	 * Checks to see if the specified class is a special case.
	 */
	public static boolean isSpecialCase(JavaClass cls) {
		return SpecialCases.isSpecialCase(cls.getName());
	}
	
	/**
	 * Checks to see if the specified class is a special case.
	 *
	 * @param name The name of the class, in java.lang.Object format.
	 */
	public static boolean isSpecialCase(String name) {
		if (SpecialCases.classes.containsKey(name))
			return SpecialCases.classes.get(name);
		
		try {
			SpecialCases.getClass(name);
			SpecialCases.classes.put(name, true);
			return true;
		} catch (Exception e) {
			SpecialCases.classes.put(name, false);
			return false;
		}
	}
	
	private static SpecialCase getClass(String name) throws Exception {
		if (SpecialCases.cases.containsKey(name))
			return SpecialCases.cases.get(name);
	
		try {
			Class<?> cls = Class.forName("SpecialCases." + name.replace(".", "_"));
			Constructor<?> constr = cls.getConstructor(new Class[0]);
			SpecialCase c = (SpecialCase)constr.newInstance();
			SpecialCases.cases.put(name, c);
			return c;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * If our class is a special case, he might need to add some extra C++ stuff: let him add extra fields here.
	 */
	public static void addExtraFields(JavaClass cls, CodeBlock b) {
		if (!SpecialCases.isSpecialCase(cls))
			return;
		
		SpecialCase spc;
		try {
			spc = SpecialCases.getClass(cls.getName());
		} catch (Exception e) {
			return;
		}
		
		b.pln().pln("//Extra fields");
		
		for (String name : spc.getExtraFields()) {
			b.pln(name + ";");
		}
	}
	
	/**
	 * If our class is a special case, he might need to add some extra C++ stuff: let him add extra constructors here.
	 */
	public static void addExtraConstructors(JavaClass cls, CodeBlock b) {
		if (!SpecialCases.isSpecialCase(cls))
			return;
		
		SpecialCase spc;
		try {
			spc = SpecialCases.getClass(cls.getName());
		} catch (Exception e) {
			return;
		}
		
		b.pln().pln("//Extra constructors");
		
		for (String name : spc.getExtraConstructors()) {
			b.pln(name + ";");
		}
	}
}