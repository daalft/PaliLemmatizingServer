package palilemmatizingserver.handler.conv;


import java.util.*;

import org.eclipse.jetty.util.log.Log;

import de.cl.dictclient.DictWord;
import de.general.json.*;
import de.general.transliteration.ITransliterator;
import de.pali.PaliTransliterationMgr;
import de.unitrier.daalft.pali.lexicon.*;
import de.unitrier.daalft.pali.morphology.element.*;
import de.unitrier.daalft.pali.phonology.element.*;



public class ConverterSandhiSolver extends AbstractFormatConverter
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	public ConverterSandhiSolver()
	{
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	public String fromFormat()
	{
		return "sandhisolverforms";
	}

	public String toFormat()
	{
		return "json";
	}

	public Object convert(Object obj, ITransliterator t)
	{
		List<String> list = new ArrayList<String>();
		String[] array = (String[]) obj;
		
		for (String s : array)
			list.add(s);
		
		JObject ret = new JObject();
		if (t != null) {
			ret.add("original", new JValue(t.transliterate(list.remove(list.size()-1))));
		} else {
			ret.add("original", new JValue(list.remove(list.size()-1)));
		}
		JArray ret2 = new JArray();
		ret.add("resolved", ret2);

		for (String s : list) {
			if (t != null)
				ret2.add(new JValue(t.transliterate(s)));
			else
				ret2.add(new JValue(s));
		}
		return ret;
	}
}

