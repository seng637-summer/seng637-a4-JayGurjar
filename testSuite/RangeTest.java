/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2014, by Object Refinery Limited and Contributors.
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
 * --------------
 * RangeTest.java
 * --------------
 * (C) Copyright 2003-2014, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Sergei Ivanov;
 *
 * Changes
 * -------
 * 14-Aug-2003 : Version 1 (DG);
 * 18-Dec-2007 : Additional tests from Sergei Ivanov (DG);
 * 08-Jan-2012 : Added test for combine() method (DG);
 * 23-Feb-2014 : Added isNaNRange() test (DG);
 * 
 */

package org.jfree.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.jfree.chart.TestUtilities;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the {@link Range} class.
 */
public class RangeTest {

	private Range exampleRange;
	Range maxUpperRange;
    Range minLowerRange;
    Range zeroRange;
    Range maxLowerRange;
    Range maxInfLowerRange;
    Range minInfLowerRange;
    Range nanLowerRange;
    Range minUpperRange;
    Range maxInfUpperRange;
    Range minInfUpperRange;
    Range nanUpperRange;

	  @BeforeClass
	  public static void setUpBeforeClass() throws Exception { }

	  @Before
	  public void setUp() throws Exception {
	    exampleRange = new Range(20.0, 40.0);
	    maxLowerRange = new Range(Double.MAX_VALUE, Double.MAX_VALUE);
    	minLowerRange = new Range(-Double.MAX_VALUE, 0);
    	maxInfLowerRange = new Range(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    	minInfLowerRange = new Range(Double.NEGATIVE_INFINITY, 0);
    	nanLowerRange = new Range(Double.NaN, 0);
    	maxUpperRange = new Range(Double.MAX_VALUE, Double.MAX_VALUE);
    	minUpperRange = new Range(-Double.MAX_VALUE, -Double.MAX_VALUE);
    	maxInfUpperRange = new Range(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    	minInfUpperRange = new Range(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    	nanUpperRange = new Range(0, Double.NaN);
  
	  }
	  
	  	@Test
	    public void testEquals() {
	        assertTrue("A Range should equal itself", exampleRange.equals(exampleRange));
	        assertFalse("A Range should not equal a null object", exampleRange.equals(null));
	        assertFalse("A Range should not equal an object of a different type", exampleRange.equals("Not a Range object"));
	        assertTrue("Two Range objects with the same bounds should be equal", exampleRange.equals(new Range(20.0, 40.0)));
	        assertFalse("Two Range objects with different lower bounds should not be equal", exampleRange.equals(new Range(30.0, 40.0)));
	        assertFalse("Two Range objects with different upper bounds should not be equal", exampleRange.equals(new Range(20.0, 30.0)));
	    }
	  	
	  	@Test
	  	public void testCombineWhenRangesOverlap() {
	  	    Range range1 = new Range(0.0, 10.0);
	  	    Range range2 = new Range(5.0, 15.0);
	  	    Range combinedRange = Range.combine(range1, range2);
	  	    assertEquals("Combined range upper bound is incorrect", 15.0, combinedRange.getUpperBound(), 0.000000001d);
	  	    assertEquals("Combined range lower bound is incorrect", 0.0, combinedRange.getLowerBound(), 0.000000001d);
	  	}

	  	@Test
	  	public void testCombineWhenRangesDoNotOverlap() {
	  	    Range range1 = new Range(0.0, 5.0);
	  	    Range range2 = new Range(10.0, 15.0);
	  	    Range combinedRange = Range.combine(range1, range2);
	  	    assertEquals("Combined range upper bound is incorrect", 15.0, combinedRange.getUpperBound(), 0.000000001d);
	  	    assertEquals("Combined range lower bound is incorrect", 0.0, combinedRange.getLowerBound(), 0.000000001d);
	  	}

	  	@Test
	  	public void testCombineWhenRangesAreEqual() {
	  	    Range range1 = new Range(0.0, 10.0);
	  	    Range range2 = new Range(0.0, 10.0);
	  	    Range combinedRange = Range.combine(range1, range2);
	  	    assertEquals("Combined range upper bound is incorrect", 10.0, combinedRange.getUpperBound(), 0.000000001d);
	  	    assertEquals("Combined range lower bound is incorrect", 0.0, combinedRange.getLowerBound(), 0.000000001d);
	  	}

	  	@Test
	  	public void testCombineWhenOneRangeIsNull() {
	  	    Range range1 = new Range(0.0, 10.0);
	  	    Range range2 = null;
	  	    Range combinedRange = Range.combine(range1, range2);
	  	    assertEquals("Combined range upper bound should equal the upper bound of the non-null range", 10.0, combinedRange.getUpperBound(), 0.000000001d);
	  	    assertEquals("Combined range lower bound should equal the lower bound of the non-null range", 0.0, combinedRange.getLowerBound(), 0.000000001d);

	  	    combinedRange = Range.combine(range2, range1);
	  	    assertEquals("Combined range upper bound should equal the upper bound of the non-null range", 10.0, combinedRange.getUpperBound(), 0.000000001d);
	  	    assertEquals("Combined range lower bound should equal the lower bound of the non-null range", 0.0, combinedRange.getLowerBound(), 0.000000001d);
	  	}

	  	@Test
	  	public void testCombineWhenBothRangesAreNull() {
	  	    Range range1 = null;
	  	    Range range2 = null;
	  	    Range combinedRange = Range.combine(range1, range2);
	  	    assertNull("Combined range should be null when both ranges are null", combinedRange);
	  	}

	  	
	  	 @Test
	     public void testHashCode() {
	         assertEquals("Equal Range objects should have the same hashCode", exampleRange.hashCode(), new Range(20.0, 40.0).hashCode());
	     }
	  	 

	  	@Test
	    public void testCombine() {
	        Range range1 = new Range(-1.0, 3.0);
	        Range range2 = new Range(5.0, 6.0);
	        Range combinedRange = Range.combine(range1, range2);
	        assertEquals("Combined range upper bound is incorrect", 6.0, combinedRange.getUpperBound(), 0.000000001d);
	        assertEquals("Combined range lower bound is incorrect", -1.0, combinedRange.getLowerBound(), 0.000000001d);
	  	}
	  	
	  	@Test
	  	public void testShift() {
	  	    Range range = new Range(0.0, 10.0);
	  	    Range shiftedRange = Range.shift(range, 5.0);
	  	    assertEquals("Shifted range upper bound is incorrect", 15.0, shiftedRange.getUpperBound(), 0.000000001d);
	  	    assertEquals("Shifted range lower bound is incorrect", 5.0, shiftedRange.getLowerBound(), 0.000000001d);
	  	}
	  	
	  	@Test
	  	public void testScale() {
	  	    Range range = new Range(0.0, 10.0);
	  	    Range scaledRange = Range.scale(range, 2.0);
	  	    assertEquals("Scaled range upper bound is incorrect", 20.0, scaledRange.getUpperBound(), 0.000000001d);
	  	    assertEquals("Scaled range lower bound is incorrect", 0.0, scaledRange.getLowerBound(), 0.000000001d);
	  	}

	  	
	    @Test
	    public void testLowerBoundaryContains() {
	        assertTrue(exampleRange.contains(20.0));
	    }

	    @Test
	    public void testUpperBoundaryForContains() {
	        assertTrue(exampleRange.contains(40.0));
	    }

	    @Test
	    public void testValueWithinRangeForContains() {
	        assertTrue(exampleRange.contains(30.0));
	    }

	    @Test
	    public void testValueBelowLowerBoundaryForContains() {
	        assertFalse(exampleRange.contains(10.0));
	    }

	    @Test
	    public void testValueAboveThanUpperBoundaryForContains() {
	        assertFalse(exampleRange.contains(50.0));
	    }	  

	  		@Test
		public void lengthShouldBeMaxDoubleValue() {
	  		maxUpperRange = new Range(0, Double.MAX_VALUE);
	  	    zeroRange = new Range(0, 0);
	    	assertEquals(Double.MAX_VALUE,
	    			maxUpperRange.getLength(),
	    			.000000001d);
	    }
		
		@Test
		public void lengthShouldBeMinDoubleValue() {
	    	minLowerRange = new Range(-Double.MAX_VALUE, 0);
	    	zeroRange = new Range(0, 0);
	    	assertEquals(Double.MAX_VALUE,
	    			minLowerRange.getLength(),
	    			.000000001d);
	    }
		
		@Test
		public void lengthShouldBeZero() {
	    	zeroRange = new Range(0, 0);
	    	assertEquals(0,
	    			zeroRange.getLength(),
	    			.000000001d);
	    }
	    
		
		@Test
		public void lowerBoundGetterShouldReturnMaxDoubleValue() {
	    	assertEquals("The getter should have returned the maximum value of a double",
	    			Double.MAX_VALUE,
	    			maxLowerRange.getLowerBound(),
	    			.000000001d);
	    }
	    
	    @Test
	    public void lowerBoundGetterShouldReturnMinDoubleValue() {
	    	assertEquals("The getter should have returned the minimum value of a double",
	    			-Double.MAX_VALUE, minLowerRange.getLowerBound(),
	    			.000000001d);
	    }
	    
	    @Test
	    public void lowerBoundGetterShouldReturnPositiveInfty() {
	    	assertEquals("The getter should have returned positive infinity",
	    			Double.POSITIVE_INFINITY, maxInfLowerRange.getLowerBound(),
	    			.000000001d);
	    }
	    
	    @Test
	    public void lowerBoundGetterShouldReturnNegativeInfty() {
	    	assertEquals("The getter should have returned negative infinity",
	    			Double.NEGATIVE_INFINITY,
	    			minInfLowerRange.getLowerBound(),
	    			.000000001d);
	    }
	    
	   /*
	    public void lowerBoundGetterShouldReturnNaN() {
	    	assertEquals("The getter should have returned NaN",
	    			Double.NaN,
	    			maxLowerRange.getLowerBound(),
	    			.000000001d);
	    } */
	    
	    @Test
		public void upperBoundGetterShouldReturnMaxDoubleValue() {
	    	assertEquals("The getter should have returned the maximum value of a double",
	    			Double.MAX_VALUE,
	    			maxUpperRange.getUpperBound(),
	    			.000000001d);
	    }
	    
	    @Test
	    public void upperBoundGetterShouldReturnMinDoubleValue() {
	    	assertEquals("The getter should have returned the minimum value of a double",
	    			-Double.MAX_VALUE, minUpperRange.getUpperBound(),
	    			.000000001d);
	    }
	    
	    @Test
	    public void upperBoundGetterShouldReturnPositiveInfty() {
	    	assertEquals("The getter should have returned positive infinity",
	    			Double.POSITIVE_INFINITY, maxInfUpperRange.getUpperBound(),
	    			.000000001d);
	    }
	    
	    @Test
	    public void upperBoundGetterShouldReturnNegativeInfty() {
	    	assertEquals("The getter should have returned negative infinity",
	    			Double.NEGATIVE_INFINITY,
	    			minInfUpperRange.getUpperBound(),
	    			.000000001d);
	    }
	    
	/*
	    public void upperBoundGetterShouldReturnNaN() {
	    	assertEquals("The getter should have returned NaN",
	    			Double.NaN,
	    			maxUpperRange.getUpperBound(),
	    			.000000001d);
	    	
	    	/*boolean isNan = Double.isNaN(maxUpperRange.getUpperBound());
	    	assertTrue("The getter should have returned NaN", isNan);
	    }*/
	    

		  @Test
		  public void testPositiveCentralValue() {
		    exampleRange = new Range(3, 7);
		    System.out.println(exampleRange.getCentralValue());
		    assertEquals("The central value of 3 and 7 should be 5", 5, exampleRange.getCentralValue(), .000000001d);
		  }

		  @Test
		  public void testNegativeCentralValue() {
		    exampleRange = new Range(-7, -3);
		    System.out.println(exampleRange.getCentralValue());
		    assertEquals("The central value of -3 and -7 should be -5", -5, exampleRange.getCentralValue(), .000000001d);
		  }

		  @Test
		  public void testZerosCentralValue() {
		    exampleRange = new Range(0, 0);
		    System.out.println(exampleRange.getCentralValue());
		    assertEquals("The central value of 0 and 0 should be 0", 0, exampleRange.getCentralValue(), .000000001d);
		  }

		  @Test
		  public void testMixedCentralValue() {
		    exampleRange = new Range(-3, 7);
		    System.out.println(exampleRange.getCentralValue());
		    assertEquals("The central value of -3 and 7 should be 2", 2, exampleRange.getCentralValue(), .000000001d);
		  }

	    
	    @After
	    public void tearDown() throws Exception {
	    	System.out.println("Tearing down...");
	    	maxUpperRange = null;
	    	minLowerRange = null;
	    	zeroRange = null;
	    	maxLowerRange = null;
	    	maxInfLowerRange = null;
	    	minInfLowerRange = null;
	    	nanLowerRange = null;
	    	minUpperRange = null;
	    	maxInfUpperRange = null;
	    	minInfUpperRange = null;
	    	nanUpperRange = null;
	    }

	    @AfterClass
	    public static void tearDownAfterClass() throws Exception {
	    }
	  
	  
}
