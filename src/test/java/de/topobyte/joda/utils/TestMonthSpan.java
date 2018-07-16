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

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.YearMonth;
import org.junit.Assert;
import org.junit.Test;

public class TestMonthSpan
{

	@Test
	public void testManual()
	{
		MonthSpan span0 = new MonthSpan(2016, 1, 0);
		Assert.assertTrue(span0.isEmpty());
		Assert.assertEquals(0, span0.length());
		Assert.assertFalse(span0.contains(new YearMonth(2015, 12)));
		Assert.assertFalse(span0.contains(new YearMonth(2016, 1)));
		Assert.assertFalse(span0.contains(new YearMonth(2016, 2)));

		MonthSpan span1 = new MonthSpan(2016, 1, 1);
		Assert.assertFalse(span1.isEmpty());
		Assert.assertEquals(1, span1.length());
		Assert.assertFalse(span1.contains(new YearMonth(2015, 12)));
		Assert.assertTrue(span1.contains(new YearMonth(2016, 1)));
		Assert.assertFalse(span1.contains(new YearMonth(2016, 2)));

		MonthSpan span2 = new MonthSpan(2016, 1, 2);
		Assert.assertFalse(span2.isEmpty());
		Assert.assertEquals(2, span2.length());
		Assert.assertFalse(span2.contains(new YearMonth(2015, 12)));
		Assert.assertTrue(span2.contains(new YearMonth(2016, 1)));
		Assert.assertTrue(span2.contains(new YearMonth(2016, 2)));
		Assert.assertFalse(span2.contains(new YearMonth(2016, 3)));

		MonthSpan span3 = new MonthSpan(new YearMonth(2016, 1),
				new YearMonth(2016, 1));
		Assert.assertTrue(span3.isEmpty());
		Assert.assertEquals(0, span3.length());

		MonthSpan span4 = new MonthSpan(new YearMonth(2016, 1),
				new YearMonth(2016, 2));
		Assert.assertFalse(span4.isEmpty());
		Assert.assertEquals(1, span4.length());

		MonthSpan span5 = new MonthSpan(new YearMonth(2016, 1),
				new YearMonth(2016, 3));
		Assert.assertFalse(span5.isEmpty());
		Assert.assertEquals(2, span5.length());
	}

	@Test
	public void testValid()
	{
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

	/**
	 * Test the value with both constructors of {@link MonthSpan}
	 */
	private void test(int year, int month, int numMonths)
	{
		test1(year, month, numMonths);
		test2(year, month, numMonths);
	}

	/**
	 * Create a span starting at the specified month and spanning across the
	 * specified number of months using
	 * {@link MonthSpan#MonthSpan(YearMonth, YearMonth)}.
	 */
	private void test1(int year, int month, int numMonths)
	{
		YearMonth firstMonth = new YearMonth(year, month);
		YearMonth lastMonth = firstMonth.plusMonths(numMonths);
		MonthSpan span = new MonthSpan(firstMonth, lastMonth);

		test(span, year, month, numMonths);
	}

	/**
	 * Create a span starting at the specified month and spanning across the
	 * specified number of months using
	 * {@link MonthSpan#MonthSpan(YearMonth, int)}.
	 */
	private void test2(int year, int month, int numMonths)
	{
		YearMonth firstMonth = new YearMonth(year, month);

		MonthSpan span = new MonthSpan(firstMonth, numMonths);
		test(span, year, month, numMonths);
	}

	private void test(MonthSpan span, int year, int month, int numMonths)
	{
		if (numMonths == 0) {
			Assert.assertTrue(span.isEmpty());
		} else {
			Assert.assertFalse(span.isEmpty());
		}

		Assert.assertEquals(numMonths, span.length());

		// Obtain the span's interval
		Interval interval = span.toInterval();

		YearMonth firstMonth = new YearMonth(year, month);
		YearMonth lastMonth = firstMonth.plusMonths(numMonths - 1);

		// Check some predicates for each month of the span
		for (YearMonth m = firstMonth; !m.isAfter(lastMonth); m = m
				.plusMonths(1)) {
			// Days: check that both the first and last day of the month are
			// contained within the interval (at the start of the day)
			LocalDate firstDay = m.toLocalDate(1);
			LocalDate lastDay = firstDay.dayOfMonth().withMaximumValue();
			Assert.assertTrue(
					interval.contains(firstDay.toDateTimeAtStartOfDay()));
			Assert.assertTrue(
					interval.contains(lastDay.toDateTimeAtStartOfDay()));

			// The start and end of the months interval should be contained
			// within the interval (except for the last month, where the end is
			// outside of the interval)
			DateTime start = m.toInterval().getStart();
			DateTime end = m.toInterval().getEnd();

			boolean isLast = m.equals(lastMonth);

			// Start is contained anyways
			Assert.assertTrue(interval.contains(start.toDateTime()));
			// End is contained if it is not the last month or the span
			Assert.assertEquals(!isLast, interval.contains(end.toDateTime()));
			// End minus a millisecond is contained anyways
			Assert.assertEquals(true,
					interval.contains(end.toDateTime().minusMillis(1)));
		}

		// If this test is for a zero length span, make sure the first day is
		// not included in the interval
		if (numMonths == 0) {
			YearMonth m = new YearMonth(year, month);
			Assert.assertFalse(interval
					.contains(m.toLocalDate(1).toDateTimeAtStartOfDay()));
		}

		Assert.assertEquals(numMonths == 0, span.isEmpty());

		Iterable<YearMonth> iterable = span.iterateMonths();
		if (numMonths == 0) {
			Assert.assertFalse(iterable.iterator().hasNext());
		} else {
			TestMonthIterable.test(iterable, span.getStart(), span.getEnd());
		}
	}

}
