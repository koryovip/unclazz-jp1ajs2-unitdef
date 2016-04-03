package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

import static org.unclazz.jp1ajs2.unitdef.util.ListUtils.*;

/**
 * サブユニットをトラバースするための{@link UnitQuery}を生成するファクトリ.
 */
public final class SubUnitQueryFactory {
	SubUnitQueryFactory() {}
	
	/**
	 * 名前が一致するサブユニットを返すクエリを返す.
	 * @param name ユニット名
	 * @return クエリ
	 */
	public ListUnitQuery<Unit> nameEquals(final String name) {
		return new ListUnitQuery<Unit>() {
			@Override
			public List<Unit> queryFrom(Unit unit) {
				for (final Unit sub : unit.getSubUnits()) {
					if (sub.getName().equals(name)) {
						return Collections.singletonList(sub);
					}
				}
				return Collections.emptyList();
			}
		};
	}
	/**
	 * 名前が前方一致するサブユニットを返すクエリを返す.
	 * @param prefix ユニット名の接頭辞
	 * @return クエリ
	 */
	public ListUnitQuery<Unit> nameStartsWith(final String prefix) {
		return new ListUnitQuery<Unit>() {
			@Override
			public List<Unit> queryFrom(Unit unit) {
				final List<Unit> result = linkedList();
				for (final Unit sub : unit.getSubUnits()) {
					if (sub.getName().startsWith(prefix)) {
						result.add(sub);
					}
				}
				return result;
			}
		};
	}
	/**
	 * 名前が部分一致するサブユニットを返すクエリを返す.
	 * @param part ユニット名の部分文字列
	 * @return クエリ
	 */
	public ListUnitQuery<Unit> nameContains(final String part) {
		return new ListUnitQuery<Unit>() {
			@Override
			public List<Unit> queryFrom(Unit unit) {
				final List<Unit> result = linkedList();
				for (final Unit sub : unit.getSubUnits()) {
					if (sub.getName().contains(part)) {
						result.add(sub);
					}
				}
				return result;
			}
		};
	}
	/**
	 * 名前が正規表現にマッチするサブユニットを返すクエリを返す.
	 * @param pattern 正規表現パターン
	 * @return クエリ
	 */
	public ListUnitQuery<Unit> nameMatchs(final Pattern pattern) {
		return new ListUnitQuery<Unit>() {
			private Pattern p = pattern;
			@Override
			public List<Unit> queryFrom(Unit unit) {
				final List<Unit> result = linkedList();
				for (final Unit sub : unit.getSubUnits()) {
					if (p.matcher(sub.getName()).matches()) {
						result.add(sub);
					}
				}
				return result;
			}
		};
	}
	/**
	 * 名前が正規表現にマッチするサブユニットを返すクエリを返す.
	 * @param pattern 正規表現パターン
	 * @return クエリ
	 */
	public ListUnitQuery<Unit> nameMatchs(final String pattern) {
		return nameMatchs(Pattern.compile(pattern));
	}
	/**
	 * 指定された名前のユニット定義パラメータを持つサブユニットを返すクエリを返す.
	 * @param name パラメータ名
	 * @return クエリ
	 */
	public ListUnitQuery<Unit> hasParameter(final String name) {
		return new ListUnitQuery<Unit>() {
			@Override
			public List<Unit> queryFrom(Unit unit) {
				final List<Unit> result = linkedList();
				for (final Unit sub : unit.getSubUnits()) {
					if (sub.getParameter(name) != null) {
						result.add(sub);
					}
				}
				return result;
			}
		};
	}
	/**
	 * 指定されたユニット種別のサブユニットを返すクエリを返す.
	 * @param type ユニット種別
	 * @return クエリ
	 */
	public ListUnitQuery<Unit> is(final UnitType type) {
		return new ListUnitQuery<Unit>() {
			@Override
			public List<Unit> queryFrom(Unit unit) {
				final List<Unit> result = linkedList();
				for (final Unit sub : unit.getSubUnits()) {
					if (sub.getType() == type) {
						result.add(sub);
					}
				}
				return result;
			}
		};
	}
}