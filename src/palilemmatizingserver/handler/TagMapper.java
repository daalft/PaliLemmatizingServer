package palilemmatizingserver.handler;

public class TagMapper {

	public static String map (String tag) {
		switch(tag) {
		case "NOUN": return "noun";
		case "VERB": return "verb";
		case "INDECL": return "indeclinable";
		case "PRON": return "pronoun";
		case "NUM": return "numeral";
		case "PUNCT": return "indeclinable";
		case "SENT": return "indeclinable";
			
			default:
		return null;
		}
	}
}
