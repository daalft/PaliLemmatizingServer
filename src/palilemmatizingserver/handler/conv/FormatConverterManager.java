package palilemmatizingserver.handler.conv;


import java.util.*;
import java.lang.reflect.*;

import de.general.transliteration.ITransliterator;


public class FormatConverterManager
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	HashMap<String, AbstractFormatConverter> converters;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	public FormatConverterManager()
	{
		converters = new HashMap<>();
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	public void register(Class converterClass) throws Exception
	{
		AbstractFormatConverter c = (AbstractFormatConverter)(converterClass.newInstance());
		converters.put(c.fromFormat() + "~" + c.toFormat(), c);
	}

	public AbstractFormatConverter get(String fromFormat, String toFormat)
	{
		return converters.get(fromFormat + "~" + toFormat);
	}

	public Object convert(String fromFormat, String toFormat, Object dataToConvert, ITransliterator t)
	{
		AbstractFormatConverter ac = get(fromFormat, toFormat);
		if (ac == null) return null;
		return ac.convert(dataToConvert, t);
	}

	public Object convertE(String fromFormat, String toFormat, Object dataToConvert, ITransliterator t) throws Exception
	{
		AbstractFormatConverter ac = get(fromFormat, toFormat);
		if (ac == null) throw new Exception("No converter available that is capable of converting from '" + fromFormat + "' to '" + toFormat + "'!");
		return ac.convert(dataToConvert,t);
	}

}

