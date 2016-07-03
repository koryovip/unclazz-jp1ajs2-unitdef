package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import static org.unclazz.jp1ajs2.unitdef.query2.QueryUtils.*;

public final class IntegerListQuery implements Query<Unit, Iterable<Integer>> {
	private final AbstractParameterValueListQuery<?> baseQuery;
	private final List<Predicate<Integer>> preds;
	
	IntegerListQuery(final AbstractParameterValueListQuery<?> baseQuery, final List<Predicate<Integer>> preds) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
	}
	IntegerListQuery(final AbstractParameterValueListQuery<?> baseQuery) {
		this(baseQuery, Collections.<Predicate<Integer>>emptyList());
	}
	
	@Override
	public Iterable<Integer> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		return LazyIterable.forEach(baseQuery.queryFrom(t), new YieldCallable<ParameterValue,Integer>(){
			@Override
			public Yield<Integer> yield(final ParameterValue item, final int index) {
				final Integer i = tryParse(item.serialize());
				if (i == null) {
					return Yield.yieldVoid();
				}
				for (final Predicate<Integer> pred : preds) {
					if (!pred.test(i)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(i);
			}
		});
	}
	
	public IntegerListQuery and(final Predicate<Integer> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<Integer>> newPreds = new LinkedList<Predicate<Integer>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new IntegerListQuery(this.baseQuery, newPreds);
	}
	
	public Query<Unit,Integer> one(final boolean nullable) {
		return new OneQuery<Unit, Integer>(this, nullable);
	}
	
	public Query<Unit,Integer> one() {
		return new OneQuery<Unit, Integer>(this, false);
	}
	
	private Integer tryParse(final CharSequence cs) {
		try {
			return Integer.parseInt(cs.toString());
		} catch (final RuntimeException e) {
			return null;
		}
	}
}