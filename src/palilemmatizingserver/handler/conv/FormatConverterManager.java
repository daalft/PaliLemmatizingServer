package palilemmatizingserver.handler.conv;


import java.util.*;
import java.lang.reflect.*;


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

	public Object convert(String fromFormat, String toFormat, Object dataToConvert)
	{
		AbstractFormatConverter ac = get(fromFormat, toFormat);
		if (ac == null) return null;
		return ac.convert(dataToConvert);
	}

	public Object convertE(String fromFormat, String toFormat, Object dataToConvert) throws Exception
	{
		AbstractFormatConverter ac = get(fromFormat, toFormat);
		if (ac == null) throw new Exception("No converter available that is capable of converting from '" + fromFormat + "' to '" + toFormat + "'!");
		return ac.convert(dataToConvert);
	}

}

