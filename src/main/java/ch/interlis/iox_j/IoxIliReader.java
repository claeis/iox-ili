package ch.interlis.iox_j;

import ch.interlis.ili2c.metamodel.TransferDescription;

public interface IoxIliReader {
    public void setModel(TransferDescription td);
    public void setTopicFilter(String topicNames[]);
}
