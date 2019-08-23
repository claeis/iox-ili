package ch.interlis.iom_j.itf.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.iom.IomObject;

public class LineobjectIterator implements java.util.Iterator<IomObject> {

    Map<AttributeDef, ? extends  Linetable2Polygon> areaAttrs;
    private Iterator<AttributeDef> currentAreaAttrIt;
    private Linetable2Polygon polygonizer=null;
    private Iterator<String> lineTidIt=null;
    public LineobjectIterator(Map<AttributeDef, ? extends Linetable2Polygon> currentAreaAttrs) {
        areaAttrs=currentAreaAttrs;
        currentAreaAttrIt=areaAttrs.keySet().iterator();
        if(currentAreaAttrIt.hasNext()) {
            AttributeDef areaAttr=currentAreaAttrIt.next();
            polygonizer=areaAttrs.get(areaAttr);
            lineTidIt=polygonizer.lineTableTidIterator();
        }
    }

    @Override
    public boolean hasNext() {
        if(lineTidIt==null) {
            return false;
        }
        if(lineTidIt.hasNext()) {
            return true;
        }
        while(currentAreaAttrIt.hasNext()) {
            AttributeDef areaAttr=currentAreaAttrIt.next();
            polygonizer=areaAttrs.get(areaAttr);
            lineTidIt=polygonizer.lineTableTidIterator();
            if(lineTidIt.hasNext()) {
                return true;
            }
        }
        polygonizer=null;
        lineTidIt=null;
        return false;
    }

    @Override
    public IomObject next() {
        if(lineTidIt==null) {
            throw new NoSuchElementException();
        }
        String lineTid=null;
        if(lineTidIt.hasNext()) {
            lineTid=lineTidIt.next();
        }
        while(lineTid==null && currentAreaAttrIt.hasNext()) {
            AttributeDef areaAttr=currentAreaAttrIt.next();
            polygonizer=areaAttrs.get(areaAttr);
            lineTidIt=polygonizer.lineTableTidIterator();
            if(lineTidIt.hasNext()) {
                lineTid=lineTidIt.next();
            }
        }
        IomObject lineObj=polygonizer.getLineObject(lineTid);
        return lineObj;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
