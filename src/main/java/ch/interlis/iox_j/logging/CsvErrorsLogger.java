package ch.interlis.iox_j.logging;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import org.interlis2.validator.models.ILIVERRORS;
import org.interlis2.validator.models.IliVErrors.ErrorLog.Error;
import org.interlis2.validator.models.IliVErrors.ErrorLog.Error_Type;

import ch.ehi.basics.logging.AbstractStdListener;
import ch.ehi.basics.logging.LogEvent;
import ch.ehi.basics.logging.LogListener;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.impl.ItfScanner;
import ch.interlis.iom_j.xtf.XtfModel;
import ch.interlis.iom_j.xtf.XtfWriterBase;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxLogEvent;
import ch.interlis.iox.IoxWriter;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;

public class CsvErrorsLogger extends AbstractXtfErrorsLogger {
    private static int FIELD_MESSAGE=0;
    private static int FIELD_TYPE=1;
    private static int FIELD_OBJTAG=2;
    private static int FIELD_TID=3;
    private static int FIELD_TECHID=4;
    private static int FIELD_USERID=5;
    private static int FIELD_ILIQNAME=6;
    private static int FIELD_DATASOURCE=7;
    private static int FIELD_LINE=8;
    private static int FIELD_GEOMETRY_X=9;
    private static int FIELD_GEOMETRY_Y=10;
    private static int FIELD_TECHDETAILS=11;

    private PrintStream out=null; 
	public CsvErrorsLogger(File logFile)
	{
		try {
		    out=new PrintStream(new BufferedOutputStream(new FileOutputStream(logFile)),true,"UTF-8");
		    {
		        String values[]=new String[12];
		        values[FIELD_MESSAGE]="Message";
                values[FIELD_TYPE]="Type";
		        values[FIELD_OBJTAG]="ObjTag";
		        values[FIELD_TID]="Tid";
		        values[FIELD_TECHID]="TechId";
		        values[FIELD_USERID]="UserId";
		        values[FIELD_ILIQNAME]="IliQName";
		        values[FIELD_DATASOURCE]="DataSource";
                values[FIELD_LINE]="Line";
                values[FIELD_GEOMETRY_X]="Geometry_x";
                values[FIELD_GEOMETRY_Y]="Geometry_y";
		        values[FIELD_TECHDETAILS]="TechDetails";
		        try {
		            writeRecord(values);
		        } catch (IOException e) {
		            throw new IllegalStateException(e);
		        }
		    }
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
    @Override
    public void writeObject(Error iomObj) {
        String values[]=new String[12];
        values[FIELD_MESSAGE]=iomObj.getMessage();
        Error_Type type = iomObj.getType();
        if(type!=null) {
            values[FIELD_TYPE]=Error_Type.toXmlCode(type);
        }
        values[FIELD_OBJTAG]=iomObj.getObjTag();
        values[FIELD_TID]=iomObj.getTid();
        values[FIELD_TECHID]=iomObj.getTechId();
        values[FIELD_USERID]=iomObj.getUserId();
        values[FIELD_ILIQNAME]=iomObj.getIliQName();
        values[FIELD_DATASOURCE]=iomObj.getDataSource();
        Integer line = iomObj.getLine();
        if(line!=null) {
            values[FIELD_LINE]=line.toString();
        }
        IomObject geom = iomObj.getGeometry();
        if(geom!=null) {
            values[FIELD_GEOMETRY_X]=geom.getattrvalue(Iom_jObject.COORD_C1);
            values[FIELD_GEOMETRY_Y]=geom.getattrvalue(Iom_jObject.COORD_C2);
        }
        values[FIELD_TECHDETAILS]=iomObj.getTechDetails();
        
        try {
            writeRecord(values);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
	@Override
	public void close()
	{
		if(out!=null){
            out.flush();
            out.close();
			out=null;
		}
	}
    private void writeRecord(String values[]) 
            throws java.io.IOException
    {
        String sep="";
        for(int i=0;i<values.length;i++) {
            out.print(sep);
            String value=values[i];
            if(value==null) {
                out.print("");
            }else if(value.contains(",") || value.contains("\n") || value.contains("\r") || value.contains("\"")) {
                out.print("\"");
                out.print(value);
                out.print("\"");
            }else {
                out.print(value);
            }
            sep=",";
        }
    }
	
}
