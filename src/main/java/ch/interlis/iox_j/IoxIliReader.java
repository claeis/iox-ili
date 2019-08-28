package ch.interlis.iox_j;

import ch.interlis.ili2c.metamodel.TransferDescription;

public interface IoxIliReader {
    public void setModel(TransferDescription td);
    /** read only given topics
     * @param topicNames qualified ili-names of topics to read. All other topics are skipped.
     */
    public void setTopicFilter(String topicNames[]);
}
