// Copyright 2017 Sebastian Kuerten
//
// This file is part of joda-utils.
//
// joda-utils is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// joda-utils is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with joda-utils. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.joda.utils;

import org.joda.time.YearMonth;
import org.junit.Assert;
import org.junit.Test;

public class TestMonthIterable
{

	@Test
	public void testManual()
	{
		test(2016, 1, 1, new YearMonth(2016, 1), new YearMonth(2016, 1));
		test(2016, 1, 2, new YearMonth(2016, 1), new YearMonth(2016, 2));
		test(2016, 1, 0, null, null);
		test(2016, 1, 12, new YearMonth(2016, 1), new YearMonth(2016, 12));
		test(2016, 1, 13, new YearMonth(2016, 1), new YearMonth(2017, 1));
	}

	@Test
	public void testValid()
	{
		test(2016, 1, 2);
		test(2016, 1, 12);

		test(2016, 1, 1);
		test(2016, 1, 2);
		test(2016, 1, 12);
		test(2016, 1, 13);
		test(2016, 1, 19);
		test(2016, 1, 25);

		test(2016, 3, 1);
		test(2016, 3, 2);
		test(2016, 3, 12);
		test(2016, 3, 13);
		test(2016, 3, 19);
		test(2016, 3, 25);
	}

	@Test
	public void testEmpty()
	{
		test(2016, 1, 0);
		test(2016, 2, 0);
	}

	private void test(int year, int month, int numMonths, YearMonth first,
			YearMonth last)
	{
		YearMonth start = new YearMonth(year, month);
		YearMonth end = start.plusMonths(numMonths);
		MonthIterable iterable = new MonthIterable(start, end);

		if (numMonths == 0) {
			Assert.assertFalse(iterable.iterator().hasNext());
			return;
		}

		test(iterable, start, last);
	}

	private void test(int year, int month, int numMonths)
	{
		YearMonth start = new YearMonth(year, month);
		YearMonth end = start.plusMonths(numMonths);
		MonthIterable iterable = new MonthIterable(start, end);

		if (numMonths == 0) {
			Assert.assertFalse(iterable.iterator().hasNext());
			return;
		}

		YearMonth last = start.plusMonths(numMonths - 1);
		test(iterable, start, last);
	}

	public static void test(Iterable<YearMonth> iterable, YearMonth fist,
			YearMonth last)
	{
		YearMonth firstReached = null;
		YearMonth lastReached = null;

		YearMonth previous = null;

		int n = 0;
		for (YearMonth m : iterable) {
			if (n++ == 0) {
				firstReached = m;
			} else {
				// Make sure, returned values are continuous
				m.toInterval().abuts(previous.toInterval());
			}
			lastReached = m;
			previous = m;
		}

		// Make sure we got the expected first and last value
		Assert.assertEquals(fist, firstReached);
		Assert.assertEquals(last, lastReached);
	}

}
