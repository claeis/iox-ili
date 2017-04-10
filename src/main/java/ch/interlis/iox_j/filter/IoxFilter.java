package ch.interlis.iox_j.filter;

import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox.IoxValidationDataPool;

/** filter interface.
 * 
 * @author ceis
 *
 */
public interface IoxFilter {
	/** filters a data event.
	 * @param event
	 */
	public IoxEvent filter(IoxEvent event) throws IoxException;
	/** frees/closes any resources.
	 */
	public void close();
	/** get the error collector.
	 * 
	 * @return the error collector
	 */
	public IoxLogging getLoggingHandler();
	public void setLoggingHandler(IoxLogging errs);
	/** gets the data pool.
	 * 
	 * @return the data pool
	 */
	public IoxValidationDataPool getDataPool();
}
