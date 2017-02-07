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

import org.joda.time.Interval;
import org.joda.time.Months;
import org.joda.time.YearMonth;

public class MonthSpan
{

	private boolean empty;

	private YearMonth limitLower;
	private YearMonth limitUpper;

	/**
	 * Create a new span of months with the specified bounds.
	 * 
	 * @param limitLower
	 *            the lower limit, inclusive
	 * @param limitUpper
	 *            the upper limit, exclusive
	 */
	public MonthSpan(YearMonth limitLower, YearMonth limitUpper)
	{
		this.limitLower = limitLower;
		this.limitUpper = limitUpper;

		empty = !limitLower.isBefore(limitUpper);
	}

	/**
	 * Create a new span of months with the specified bounds.
	 * 
	 * @param limitLower
	 *            the lower limit, inclusive
	 * @param numMonths
	 *            the number of months to span
	 */
	public MonthSpan(YearMonth limitLower, int numMonths)
	{
		this.limitLower = limitLower;
		empty = numMonths <= 0;

		if (empty) {
			limitUpper = limitLower;
		} else {
			limitUpper = limitLower.plusMonths(numMonths);
		}
	}

	/**
	 * Create a new span of months with the specified bounds.
	 * 
	 * @param year
	 *            the year of the first month
	 * @param month
	 *            the month of year of the first month
	 * @param numMonths
	 *            the number of months to span
	 */
	public MonthSpan(int year, int month, int numMonths)
	{
		this(new YearMonth(year, month), numMonths);
	}

	/**
	 * @return whether the specified month is within this span.
	 */
	public boolean contains(YearMonth month)
	{
		if (empty) {
			return false;
		}

		return !month.isBefore(limitLower) && month.isBefore(limitUpper);
	}

	public Interval toInterval()
	{
		Interval m1 = limitLower.toInterval();
		Interval m2 = limitUpper.toInterval();
		return new Interval(m1.getStart(), m2.getStart());
	}

	public boolean isEmpty()
	{
		return empty;
	}

	public int length()
	{
		return Months.monthsBetween(limitLower, limitUpper).getMonths();
	}

	public YearMonth getStart()
	{
		return limitLower;
	}

	public YearMonth getEnd()
	{
		return limitUpper;
	}

	public Iterable<YearMonth> iterateMonths()
	{
		if (isEmpty()) {
			return new MonthIterable(limitLower, limitLower);
		}
		return new MonthIterable(limitLower, limitUpper.plusMonths(1));
	}

}
