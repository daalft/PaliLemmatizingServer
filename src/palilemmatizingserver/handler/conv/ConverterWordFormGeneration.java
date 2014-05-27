package palilemmatizingserver.handler.conv;


import java.util.*;

import de.cl.dictclient.DictWord;

import de.general.json.*;

import de.unitrier.daalft.pali.lexicon.*;

import de.unitrier.daalft.pali.morphology.element.*;
import de.unitrier.daalft.pali.phonology.element.*;



public class ConverterWordFormGeneration extends AbstractFormatConverter
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

	public ConverterWordFormGeneration()
	{
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	public String fromFormat()
	{
		return "generatedwordforms";
	}

	public String toFormat()
	{
		return "json";
	}

	public Object convert(Object obj)
	{
		List<ConstructedWord> model = (List<ConstructedWord>)obj;

		HashMap<String, ArrayList<ConstructedWord>> grouped = __groupBy("paradigm", model);
		
		JObject ret = new JObject();
		ret.add("lemma", new JValue(model.get(0).getLemma()));	// TODO: pass not a list but a unique model class to this methods (and the other conversion methods) because lists to convert may be empty!

		JObject ret2 = new JObject();
		ret.add("forms", ret2);

		for (String paradigmValue : grouped.keySet()) {
			ArrayList<ConstructedWord> words = grouped.get(paradigmValue);
			JArray formsArray = new JArray();
			ret2.add(paradigmValue, formsArray);

			for (ConstructedWord cw : words) {
				formsArray.add(__toJObject(cw.clone()));
			}
		}

		return ret;
	}

	private HashMap<String, ArrayList<ConstructedWord>> __groupBy(String featureName, List<ConstructedWord> inputList)
	{
		HashMap<String, ArrayList<ConstructedWord>> ret = new HashMap<String, ArrayList<ConstructedWord>>();

		for (int i = 0; i < inputList.size(); i++) {
			ConstructedWord cw = inputList.get(i);
			String value = cw.getFeatureSet().getFeature(featureName);

			ArrayList<ConstructedWord> list = ret.get(value);
			if (list == null) {
				list = new ArrayList<>();
				ret.put(value, list);
			}
			list.add(cw);
		}
		
		return ret;
	}

	private JObject __toJObject(ConstructedWord cw)
	{
		JObject ret = new JObject();

		ret.add("word", new JValue(cw.getWord()));
		for (Feature f : cw.getFeatureSet()) {
			String key = f.getKey();
			if (key.equals("paradigm")) continue;
			String value = f.getValue();
			ret.add(key, new JValue(value));
		}

		return ret;
	}

}

