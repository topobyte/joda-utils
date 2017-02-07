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

import java.util.Iterator;

import org.joda.time.YearMonth;

public class MonthIterator implements Iterator<YearMonth>
{

	private YearMonth end;

	private YearMonth next;

	/**
	 * Create an iterator over the specified months
	 * 
	 * @param start
	 *            the first month, inclusive
	 * @param end
	 *            the last month, exclusive
	 */
	public MonthIterator(YearMonth start, YearMonth end)
	{
		this.end = end;
		next = start;
	}

	@Override
	public boolean hasNext()
	{
		return next.isBefore(end);
	}

	@Override
	public YearMonth next()
	{
		YearMonth current = next;
		next = current.plusMonths(1);
		return current;
	}

}
