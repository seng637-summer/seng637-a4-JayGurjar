/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2013, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ----------------------
 * DataUtilitiesTest.java
 * ----------------------
 * (C) Copyright 2005-2013, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 03-Mar-2005 : Version 1 (DG);
 * 28-Jan-2009 : Added tests for equal(double[][], double[][]) method (DG);
 * 28-Jan-2009 : Added tests for clone(double[][]) (DG);
 * 04-Feb-2009 : Added tests for new calculateColumnTotal/RowTotal methods (DG);
 *
 */

package org.jfree.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Some tests for the {@link DataUtilities} class.
 */
public class DataUtilitiesTest {

	// Set-up
    Mockery mockingContext = new Mockery();
    Mockery mockingContextKeyed = new Mockery();
    Values2D values;
    KeyedValues valuesKeyed;
    
    int[] testIntOne = { 0 };
    int[] testIntMultiple = { 0, 1 };
    int[] testIntNull = null;
    double[] testDataOne = { 0.0 };
    double[] testMultipleData = { 2.4, -3.5, -1.1 };
    double[] testNull = null;
    double[][] testDataOne2D = new double[1][1];
    double[][] testMultipleData2D = {{1.0, 2.0, 3.0, 4.0}, {1.0, 2.0, 3.0, 4.0}, {1.0, 2.0, 3.0, 4.0}, {1.0, 2.0, 3.0, 4.0}};
    double[][] testNull2D = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("Setting up...");
        values = mockingContext.mock(Values2D.class);
        valuesKeyed = mockingContextKeyed.mock(KeyedValues.class);
        testDataOne2D[0][0] = 1;

    }

    // Test Cases

    @Test
    public void calculateColumnTotalForNoRows() {

        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(0));
                one(values).getValue(0, 0);
                will(returnValue(7.5));
                one(values).getValue(1, 0);
                will(returnValue(2.5));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        // verify
        assertEquals(result, 0.0, .000000001d);
    }

    @Test
    public void calculateColumnTotalForTwoValues() {

        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(2));
                one(values).getValue(0, 0);
                will(returnValue(7.5));
                one(values).getValue(1, 0);
                will(returnValue(2.5));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        // verify
        assertEquals(result, 10.0, .000000001d);
    }
    
    @Test
    public void testCalculateColumnTotalWithOneValidRow() {
		mockingContext.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(1));
				one(values).getValue(0,0);
				will(returnValue(5));
			}
		});
		
		double result = DataUtilities.calculateColumnTotal(values, 0, testIntOne);
		
		assertEquals(result, 5.0, 0.000000001d);
	}
	
    @Test
	public void testCalculateRowTotalForNoValidColumns() {
    	int[] empty = {};
	 mockingContext.checking(new Expectations() {
		 {
		 		one(values).getColumnCount();
		 		will(returnValue(0)); 
		 }
	 });
	 
	 double result = DataUtilities.calculateRowTotal(values, 0, empty);
	 
	 assertEquals(result, 0, .000000001d);
	 mockingContext.assertIsSatisfied();
	} 
	
    @Test
	public void testCalculateComumnTotalsOnEmptyValues() {
    	int[] empty = {};
		//setup
		mockingContext.checking(new Expectations() {
			{
				one(values).getRowCount();
				will(returnValue(0));
				never(values).getValue(0, 0);
			}
		});
		
		//exercise
		double result = DataUtilities.calculateColumnTotal(values,  0, empty);
		
		mockingContext.assertIsSatisfied();
		assertEquals(0.0, result, 0.00000001d);
	}
    
    // Cumulative Percentage
    @Test // bug found!
    public void testCumulativePercentageForThreeKeys() {
    	mockingContextKeyed.checking(new Expectations() {
            {
            	allowing(valuesKeyed).getItemCount();
            	will(returnValue(3));
                allowing(valuesKeyed).getKey(0);
                will(returnValue(1));
                allowing(valuesKeyed).getValue(0);
                will(returnValue(2));
                allowing(valuesKeyed).getKey(1);
                will(returnValue(2));
                allowing(valuesKeyed).getValue(1);
                will(returnValue(2));
                allowing(valuesKeyed).getKey(2);
                will(returnValue(3));
                allowing(valuesKeyed).getValue(2);
                will(returnValue(2));
            }
        });
         KeyedValues result = DataUtilities.getCumulativePercentages(valuesKeyed);
        assertEquals(result.getValue(0).doubleValue(), 0.33333333, .001d);
    }
    
    @Test // bug found!
    public void testCumulativePercentageForTwoNegativeKeys() {
    	mockingContextKeyed.checking(new Expectations() {
            {
            	allowing(valuesKeyed).getItemCount();
            	will(returnValue(2));
            	allowing(valuesKeyed).getKey(0);
                will(returnValue(0));
                allowing(valuesKeyed).getValue(0);
                will(returnValue(-10));
                allowing(valuesKeyed).getKey(1);
                will(returnValue(1));
                allowing(valuesKeyed).getValue(1);
                will(returnValue(-2));
            }
        });
        KeyedValues result = DataUtilities.getCumulativePercentages(valuesKeyed);
        assertEquals(result.getValue(1).doubleValue(), 1.0, .000000001d);
    }
    
    @Test // No bugs found
    public void testCumulativePercentageForNoKeys() {
    	mockingContextKeyed.checking(new Expectations() {
            {
            	allowing(valuesKeyed).getItemCount();
            	will(returnValue(0));
            	allowing(valuesKeyed).getKey(0);
                will(returnValue(0));
                allowing(valuesKeyed).getValue(1);
                will(returnValue(4));
                allowing(valuesKeyed).getKey(1);
                will(returnValue(1));
                allowing(valuesKeyed).getValue(1);
                will(returnValue(5));
            }
        });
        KeyedValues result = DataUtilities.getCumulativePercentages(valuesKeyed);
        assertEquals(result.getItemCount(), 0, .000000001d);
    }
    
    // Number Array
    @Test(expected = IllegalArgumentException.class)
    public void checkForInvalidInput() {
        Number[] result = DataUtilities.createNumberArray(testNull);
        assertNull("Should throw Illegal Arg exception", result);
    }

    @Test
    public void TestSizeOfNumberArrayForOneElement() {
        Number[] result = DataUtilities.createNumberArray(testDataOne);
        boolean res = result.equals(result);
        assertEquals(true, res);
        assertEquals(1, result.length);
    }

    @Test
    public void TestNumberArrCreatedIsEqualToInput() {
        Number[] result = DataUtilities.createNumberArray(testMultipleData);
        boolean res = result.equals(result);
        assertEquals(true, res);
    }
    
    // Number Array 2D
    @Test(expected = IllegalArgumentException.class)
    public void checkForInvalidInput2D() {
        Number[][] result = DataUtilities.createNumberArray2D(testNull2D);
        assertNull("Should throw Illegal Arg exception", result);
    }

    @Test
    public void TestSizeOfNumberArrayForOneElement2D() {
        Number[][] result = DataUtilities.createNumberArray2D(testDataOne2D);
        boolean res = result.equals(result);
        assertEquals(true, res);
        assertEquals(1, result.length);
    }

    @Test
    public void TestNumberArrCreatedIsEqualToInput2D() {
        Number[][] result = DataUtilities.createNumberArray2D(testMultipleData2D);
        boolean res = result.equals(result);
        assertEquals(true, res);
    }
    
    // Equals 
    @Test
    public void TestEqualsValues() {
    	double[][] testDataOneTwo2D = {{1}};
        boolean result = DataUtilities.equal(testDataOne2D, testDataOneTwo2D);
        assertEquals(true, result);
    }

    @Test
    public void TestEqualsNull() {
    	boolean result = DataUtilities.equal(testNull2D, null);
        assertEquals(true, result);
    }
    @Test
    public void TestEqualsMultiple() {
    	double[][] testMultipleDataTwo2D = {{1, 2, 3}, {1, 2, 3}};
    	boolean result = DataUtilities.equal(testMultipleData2D, testMultipleDataTwo2D);
        assertEquals(false, result);
    }
    
    // Clone
    @Test
    public void TestCloneOneValues() {
        double[][] result = DataUtilities.clone(testDataOne2D);
        assertEquals(1, result[0].length);
    }
    
    @Test
    public void TestCloneMultipleValues() {
        double[][] result = DataUtilities.clone(testMultipleData2D);
        assertEquals(4, result[1].length);
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestNullInput() {
    	double[][] result = DataUtilities.clone(testNull2D);
    	assertNull("Should throw Illegal Arg exception", result);
    }
    
    // Teardown
    @After
    public void tearDown() throws Exception {
        System.out.println("Tearing down...");
        mockingContext = null;
        mockingContextKeyed = null;
        values = null;
        valuesKeyed = null;
        testDataOne = null;
        testMultipleData = null;
        testDataOne2D = null;
        testMultipleData2D = null;
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}
