package src;

import xtc.tree.Visitor;

public class Translator {
	public static void main(String args []) {
		System.out.println(args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
		
		System.out.println("Happy happy, joy joy!!");
	}
}
