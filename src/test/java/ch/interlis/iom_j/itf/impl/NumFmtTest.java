package ch.interlis.iom_j.itf.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.junit.Test;

import ch.interlis.iom_j.itf.ItfWriter;

import org.junit.Assert;

public class NumFmtTest {

    @Test
    public void test05() throws Exception
    {
        DecimalFormat fmt=null;
        fmt=ItfWriter.buildDecimalFormat(3);
        fmt.setRoundingMode(RoundingMode.HALF_UP);
        BigDecimal num=new BigDecimal("3.0005");
        String numTxt=fmt.format(num);
        Assert.assertEquals("3.001", numTxt);
    }
    @Test
    public void test04() throws Exception
    {
        DecimalFormat fmt=null;
        fmt=ItfWriter.buildDecimalFormat(3);
        fmt.setRoundingMode(RoundingMode.HALF_UP);
        BigDecimal num=new BigDecimal("3.0004");
        String numTxt=fmt.format(num);
        Assert.assertEquals("3.000", numTxt);
    }
    @Test
    public void test06() throws Exception
    {
        DecimalFormat fmt=null;
        fmt=ItfWriter.buildDecimalFormat(3);
        fmt.setRoundingMode(RoundingMode.HALF_UP);
        BigDecimal num=new BigDecimal("3.0006");
        String numTxt=fmt.format(num);
        Assert.assertEquals("3.001", numTxt);
    }
}
