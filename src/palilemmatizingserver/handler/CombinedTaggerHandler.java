package palilemmatizingserver.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import palilemmatizingserver.AppRuntime;
import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import de.general.json.JArray;
import de.general.json.JObject;
import de.general.json.JValue;
import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.lexicon.LexiconAdapter;
import de.unitrier.daalft.pali.morphology.Lemmatizer;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.tools.WordConverter;

public class CombinedTaggerHandler extends AbstractHandler {

	@Override
	public ResponseContainer processRequest(AppRuntime ar,
			ClientRequest request, ILogInterface log) throws Exception {
		// retrieve sentence object
		String sentenceString = request.getRequestParameter("s");
		// convert to JObject
		JObject sentenceObject = WordConverter.toJObject(sentenceString);
		// retrieve tokens
		JObject[] tokens = sentenceObject.getPropertyObjectList("tk");

		List<String> list = new ArrayList<String>();
		// collect tokens
		for (JObject token : tokens) {
			list.add(token.getPropertyStringValueNormalized("t").toString());
		}
		
		// tag sentence
		String[] tags = ar.getTagger().tag(list.toArray(new String[0]));
		LexiconAdapter lexiconAdapter = ar.getLexiconAdapter();
		Lemmatizer lemmatizer = ar.getLemmatizer();
		for (int i = 0; i < tags.length; i++) {
			JObject token = tokens[i];
			String tag = tags[i];
			token.add("pos", new JValue(tag));
			String nlptag = TagMapper.map(tag);
			String tokenText = token.getPropertyStringValueNormalized("t").toLowerCase();
			List<ConstructedWord> lemmata = lemmatizer.lemmatize(null, tokenText, nlptag);
			JArray lemmas = new JArray();
			token.add("lemma", lemmas);
			Set<String> lemmaSet = new HashSet<String>();
			for (ConstructedWord lemma : lemmata) {

				JObject[] lemmaObjects = lexiconAdapter.getLemmaEntriesAsJObjectArray(lemma.getLemma());
				if (lemmaObjects == null) 
					continue;// TODO try something else?

				for (JObject lemmaObject : lemmaObjects) {
					//System.out.println(lemmaObject.toJSON());
					if (lemmaObject.getPropertyStringValueNormalized(new String[]{"gramGrp","PoS","value"}) != null) {
						if (lemmaObject.getPropertyStringValueNormalized(new String[]{"gramGrp","PoS","value"}).equals(nlptag)) {
							// full certainty
							lemmaSet.add(lemma.getLemma());
							//token.add("certainty", new JValue("certain"));
						}else {
							// log.warn("Could not find lemma " + lemma + " in dictionary");
							// calculated lemma
							//lemmaSet.add(lemma.getLemma());
						}
					} else {
						// no certainty
						lemmaSet.add(lemma.getLemma());
					}
				}
			}
			for (String slemma : lemmaSet) {
				lemmas.add(new JValue(slemma));
			}
			if (lemmas.size()==0 && (nlptag.equals("indeclinable"))) {
				lemmas.add(new JValue(tokenText));
			}
		}
		return createSuccessResponse(sentenceObject);
	}

}
