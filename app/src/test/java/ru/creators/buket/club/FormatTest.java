package ru.creators.buket.club;

import junit.framework.Assert;

import org.junit.Test;

import ru.creators.buket.club.tools.Helper;

/**
 * Created by user on 09.07.2016.
 */
public class FormatTest {

    @Test
    public void testShouldFormatPrices() {
        Assert.assertEquals("-1 234 567", Helper.intToPriceString(-1234567, ""));
        Assert.assertEquals("0", Helper.intToPriceString(0, ""));
        Assert.assertEquals("1", Helper.intToPriceString(1, ""));
        Assert.assertEquals("12", Helper.intToPriceString(12, ""));
        Assert.assertEquals("123", Helper.intToPriceString(123, ""));
        Assert.assertEquals("1 234", Helper.intToPriceString(1234, ""));
        Assert.assertEquals("12 345 678", Helper.intToPriceString(12345678, ""));
        Assert.assertEquals("1 234 567 891", Helper.intToPriceString(1234567891, ""));
    }
}
