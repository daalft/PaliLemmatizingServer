package palilemmatizingserver.handler.conv;


public abstract class AbstractFormatConverter
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

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	public abstract String fromFormat();
	public abstract String toFormat();

	/**
	 * Perform conversion
	 */
	public abstract Object convert(Object obj);

}
