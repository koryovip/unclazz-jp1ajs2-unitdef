package usertools.jp1ajs2.unitdef.core;

import java.util.Collections;
import java.util.List;

final class ParamImpl implements Param {

	private final String name;

	private final List<ParamValue> values;

	public ParamImpl(final String name,
			final List<ParamValue> values) {
		this.name = name;
		this.values = Collections.unmodifiableList(values);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<ParamValue> getValues() {
		return values;
	}
	
	@Override
	public String getValue() {
		final StringBuilder sb = new StringBuilder();
		for (final ParamValue v : getValues()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(v.toString());
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return name + "=" + getValue();
	}
}