package ch.interlis.iox_j;

import ch.interlis.ili2c.metamodel.TransferDescription;

public interface IoxIliReader {
    public static final String ITF_10="application/interlis+txt;version=1.0";
    public static final String XTF_22="application/interlis+xml;version=2.2";
    public static final String XTF_23="application/interlis+xml;version=2.3";
    public static final String XTF_24="application/interlis+xml;version=2.4";
    public static final String ILIGML_20="application/gml+xml;version=3.2";
    public void setModel(TransferDescription td);
    /** read only given topics
     * @param topicNames qualified ili-names of topics to read. All other topics are skipped.
     */
    public void setTopicFilter(String topicNames[]);
    public String getMimeType();
}
