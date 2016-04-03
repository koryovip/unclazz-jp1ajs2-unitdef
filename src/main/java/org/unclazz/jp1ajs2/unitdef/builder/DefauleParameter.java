package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Iterator;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.CharSequential;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.query.ParameterQuery;

class DefauleParameter implements Parameter {
	private final int size;
	private final String name;
	private final List<ParameterValue> values;
	
	DefauleParameter(final CharSequence name, final List<ParameterValue> values) {
		this.size = values.size();
		this.values = values;
		this.name = name.toString();
	}
	
	@Override
	public Iterator<ParameterValue> iterator() {
		return new Iterator<ParameterValue>() {
			private final Iterator<ParameterValue> inner = values.iterator();
			@Override
			public boolean hasNext() {
				return inner.hasNext();
			}

			@Override
			public ParameterValue next() {
				return inner.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ParameterValue getValue(int i) {
		return values.get(i);
	}

	@Override
	public int getValueCount() {
		return size;
	}

	@Override
	public String toString() {
		return toCharSequence().toString();
	}

	@Override
	public CharSequence toCharSequence() {
		final StringBuilder buff = CharSequenceUtils.builder();
		buff.append(name).append('=');
		final int initLen = buff.length();
		for (final ParameterValue value : values) {
			if (buff.length() > initLen) {
				buff.append(',');
			}
			buff.append(value.toCharSequence());
		}
		return buff;
	}

	@Override
	public boolean contentEquals(CharSequence other) {
		return CharSequenceUtils.contentsAreEqual(toCharSequence(), other);
	}

	@Override
	public boolean contentEquals(CharSequential other) {
		return contentEquals(other.toCharSequence());
	}

	@Override
	public <R> R query(ParameterQuery<R> q) {
		return q.queryFrom(this);
	}
}
