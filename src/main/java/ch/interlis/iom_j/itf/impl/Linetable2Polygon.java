package ch.interlis.iom_j.itf.impl;

import java.util.ArrayList;
import java.util.Iterator;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.IoxInvalidDataException;

public interface Linetable2Polygon {
    public Iterator<String> lineTableTidIterator();
    public IomObject getLineObject(String lineTid);
    boolean isKeepLinetables();
    void setKeepLinetables(boolean keepLinetables, String ref1, String ref2);
    ArrayList<IoxInvalidDataException> getDataerrs();
    void addItfLinetableObject(IomObject iomObj);
    void buildSurfaces() throws IoxException;
    void close();
    IomObject getSurfaceObject(String mainObjectTid) throws IoxException;
    Iterator<String> mainTableTidIterator();

}
