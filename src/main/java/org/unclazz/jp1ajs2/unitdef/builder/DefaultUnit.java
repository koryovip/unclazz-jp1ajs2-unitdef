package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Attributes;
import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.query.UnitQueries;
import org.unclazz.jp1ajs2.unitdef.query.UnitQuery;
import org.unclazz.jp1ajs2.unitdef.Unit;

import static org.unclazz.jp1ajs2.unitdef.util.ListUtils.*;

final class DefaultUnit implements Unit {

	private final FullQualifiedName fqn;
	private final Attributes attributes;
	private final List<Parameter> parameterList;
	private final List<Unit> subUnitList;
	
	DefaultUnit(FullQualifiedName fqn, Attributes attributes,
			List<Parameter> parameterList, List<Unit> subUnitList) {
		this.fqn = fqn;
		this.attributes = attributes;
		this.parameterList = parameterList;
		this.subUnitList = subUnitList;
	}
	
	@Override
	public FullQualifiedName getFullQualifiedName() {
		return fqn;
	}
	@Override
	public Attributes getAttributes() {
		return attributes;
	}
	@Override
	public String getName() {
		return attributes.getUnitName();
	}
	@Override
	public UnitType getType() {
		return query(UnitQueries.ty()).get(0);
	}
	@Override
	public List<Parameter> getParameters() {
		return Collections.unmodifiableList(parameterList);
	}

	@Override
	public List<Parameter> getParameters(final String name) {
		return query(UnitQueries.parameter(name));
	}

	@Override
	public Parameter getParameter(String name) {
		for (final Parameter p : parameterList) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public <R> R query(UnitQuery<R> r) {
		return r.queryFrom(this);
	}

	@Override
	public List<Unit> getSubUnits() {
		return Collections.unmodifiableList(subUnitList);
	}

	@Override
	public Unit getSubUnit(String name) {
		for (final Unit s : subUnitList) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}

	@Override
	public Iterator<Unit> iterator() {
		final List<Unit> targets = linkedList();
		collect(this, targets);
		return new UnremovableIterator<Unit>(targets.iterator());
	}
	
	private void collect(Unit u, List<Unit> l) {
		l.add(u);
		for (final Unit s : u.getSubUnits()) {
			collect(s, l);
		}
	}
}
