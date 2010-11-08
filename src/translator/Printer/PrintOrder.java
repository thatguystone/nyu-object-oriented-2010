package translator.Printer;

/**
 * The locations that can be given to a code block for printing.
 */
public enum PrintOrder {
	PROTOTYPE,
	HEADER,
	IMPLEMENTATION,
	ORDINAL //never used -- the MAXIMUM value that this enum has (useful for printing)
	;
}
