package org.unclazz.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.ElapsedTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.EndStatusJudgementType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.ByYearMonth;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate.DesignationMethod;
import org.unclazz.jp1ajs2.unitdef.query.Queries;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitConnectionType;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.DelayTime.TimingMethod;
import org.unclazz.jp1ajs2.unitdef.parameter.DeleteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle.CycleUnit;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionTimedOutStatus;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchConditionFlag;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchCondition;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.util.StringUtils;
import org.unclazz.jp1ajs2.unitdef.util.UnsignedIntegral;

public class UnitQueriesTest {
	
	private static Unit sampleJobnetUnit(String... params) {
		final StringBuilder buff = StringUtils
				.builder()
				.append("unit=FOO,,,;{ty=n;");
		
		for (final String param : params) {
			buff.append(param);
			if (!param.endsWith(";")) {
				buff.append(';');
			}
		}
		
		return Units.fromCharSequence(buff.append("}").toString()).get(0);
	}
	
	@Test
	public void ar_always_returnsUnitQueryForParameterAR() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ar=(f=BAR0,t=BAR1)",
				"ar=(f=BAR0,t=BAR2,seq)", "ar=(f=BAR1,t=BAR3,con)");
		
		// Act
		final AnteroposteriorRelationship r = unit.query(Queries.ar().list()).get(2);
		
		// Assert
		assertThat(r.getToUnitName(), equalTo("BAR3"));
		assertThat(r.getConnectionType(), equalTo(UnitConnectionType.CONDITIONAL));
	}
	
	@Test
	public void cm_always_returnsUnitQueryForParameterCM() {
		// Arrange
		final Unit unit = sampleJobnetUnit("cm=\"Sample Unit Comment\"");
		
		// Act
		final CharSequence r = unit.query(Queries.cm().list()).get(0);
		
		// Assert
		assertThat(r.toString(), equalTo("Sample Unit Comment"));
	}
	
	@Test
	public void cy_always_returnsUnitQueryForParameterCY() {
		// Arrange
		final Unit unit = sampleJobnetUnit("cy=(4,y)",
				"cy=2,(3,m)", "cy=3,(2,d)", "cy=4,(1,w)");
		
		// Act
		final ExecutionCycle r = unit.query(Queries.cy().list()).get(0);
		
		// Assert
		assertThat(r.getRuleNumber().intValue(), equalTo(1));
		assertThat(r.getInterval(), equalTo(4));
		assertThat(r.getCycleUnit(), equalTo(CycleUnit.YEAR));
	}
	
	@Test
	public void ej_always_returnsUnitQueryForParameterEJ() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ej=gt");
		
		// Act
		final EndStatusJudgementType r = unit.query(Queries.ej().list()).get(0);
		
		// Assert
		assertThat(r, equalTo(EndStatusJudgementType.EXIT_CODE_GT));
	}
	
	@Test
	public void ejc_always_returnsUnitQueryForParameterEJC() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ejc=4294967295");
		
		// Act
		final UnsignedIntegral r = unit.query(Queries.ejc().list()).get(0);
		
		// Assert
		assertThat(r.longValue(), equalTo(4294967295L));
	}
	
	@Test
	public void el_always_returnsUnitQueryForParameterEL() {
		// Arrange
		final Unit unit = sampleJobnetUnit("el=BAR0,n,+80 +48", 
				"el=BAR1,n,+240 +48", "el=BAR2,n,+80 +144");
		
		// Act
		final Element r = unit.query(Queries.el().list()).get(2);
		
		// Assert
		assertThat(r.getUnitName(), equalTo("BAR2"));
		assertThat(r.getUnitType(), equalTo(UnitType.JOBNET));
		assertThat(r.getVPixel(), equalTo(144));
		assertThat(r.getYCoord(), equalTo(1));
	}
	
	@Test
	public void etm_always_returnsUnitQueryForParameterETM() {
		// Arrange
		final Unit unit = sampleJobnetUnit("etm=1440");
		
		// Act
		final ElapsedTime r = unit.query(Queries.etm().list()).get(0);
		
		// Assert
		assertThat(r.intValue(), equalTo(1440));
		assertThat(r.getHours(), equalTo(24));
		assertThat(r.getMinutes(), equalTo(0));
	}
	
	@Test
	public void ets_always_returnsUnitQueryForParameterETS() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ets=nr");
		
		// Act
		final ExecutionTimedOutStatus r = unit.query(Queries.ets().list()).get(0);
		
		// Assert
		assertThat(r, equalTo(ExecutionTimedOutStatus.NORMAL_ENDED));
	}
	
	@Test
	public void eu_always_returnsUnitQueryForParameterEU() {
		// Arrange
		final Unit unit = sampleJobnetUnit("eu=def");
		
		// Act
		final ExecutionUserType r = unit.query(Queries.eu().list()).get(0);
		
		// Assert
		assertThat(r, equalTo(ExecutionUserType.DEFINITION_USER));
	}
	
	@Test
	public void ey_always_returnsUnitQueryForParameterEY() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ey=01:23", "ey=2,M2879", "ey=3,U2878");
		
		// Act
		final EndDelayTime r = unit.query(Queries.ey().list()).get(1);
		
		// Assert
		assertThat(r.getRuleNumber().intValue(), equalTo(2));
		assertThat(r.getTimingMethod(), equalTo(TimingMethod.RELATIVE_WITH_ROOT_START_TIME));
		assertThat(r.getTime().getHours(), equalTo(47));
	}
	
	@Test
	public void fd_always_returnsUnitQueryForParameterFD() {
		// Arrange
		final Unit unit = sampleJobnetUnit("fd=1440");
		
		// Act
		final FixedDuration r = unit.query(Queries.fd().list()).get(0);
		
		// Assert
		assertThat(r.intValue(), equalTo(1440));
	}
	
	@Test
	public void flwc_always_returnsUnitQueryForParameterFLWC() {
		// Arrange
		final Unit unit = sampleJobnetUnit("flwc=d:s");
		
		// Act
		final FileWatchCondition r = unit.query(Queries.flwc().list()).get(0);
		
		// Assert
		assertThat(r.contains(FileWatchConditionFlag.CREATE), equalTo(true));
		assertThat(r.contains(FileWatchConditionFlag.DELETE), equalTo(true));
		assertThat(r.contains(FileWatchConditionFlag.SIZE), equalTo(true));
		assertThat(r.contains(FileWatchConditionFlag.MODIFY), equalTo(false));
	}
	
	@Test
	public void jd_always_returnsUnitQueryForParameterJD() {
		// Arrange
		final Unit unit = sampleJobnetUnit("jd=ab");
		
		// Act
		final ResultJudgmentType r = unit.query(Queries.jd().list()).get(0);
		
		// Assert
		assertThat(r, equalTo(ResultJudgmentType.FORCE_ABNORMAL_END));
	}
	
	@Test
	public void ln_always_returnsUnitQueryForParameterLN() {
		// Arrange
		final Unit unit = sampleJobnetUnit("ln=2","ln=2,1");
		
		// Act
		final LinkedRuleNumber r = unit.query(Queries.ln().list()).get(0);
		
		// Assert
		assertThat(r.getRuleNumber().intValue(), equalTo(1));
		assertThat(r.getLinkedRuleNumber().intValue(), equalTo(2));
	}
	
	@Test
	public void mladr_always_returnsUnitQueryForParameterMLADR() {
		// Arrange
		final Unit unit = sampleJobnetUnit("mladr=to:\"foo@example.com\"",
				"mladr=bcc:\"bar@example.com\"", "mladr=cc:\"baz@example.com\"");
		
		// Act
		final MailAddress r = unit.query(Queries.mladr().list()).get(1);
		
		// Assert
		assertThat(r.getType(), equalTo(MailAddress.MailAddressType.BCC));
		assertThat(r.getAddress(), equalTo("bar@example.com"));
	}
	
	@Test
	public void sc_always_returnsUnitQueryForParameterSC() {
		// Arrange
		final Unit unit = sampleJobnetUnit("sc=\"foo.exe bar baz\"");
		
		// Act
		final CommandLine r = unit.query(Queries.sc().list()).get(0);
		
		// Assert
		assertThat(r.getCommand(), equalTo("foo.exe"));
		assertThat(r.getArguments().get(0), equalTo("bar"));
		assertThat(r.getArguments().get(1), equalTo("baz"));
	}
	
	@Test
	public void sd_always_returnsUnitQueryForParameterSD() {
		// Arrange
		final Unit unit = sampleJobnetUnit("sd=0, ud",
				"sd=en", "sd=2, 2016/02/20");
		
		// Act
		final StartDate r = unit.query(Queries.sd().list()).get(2);
		
		// Assert
		assertThat(r.getRuleNumber().intValue(), equalTo(2));
		assertThat(r.getDesignationMethod(), equalTo(DesignationMethod.SCHEDULED_DATE));
		assertThat(((ByYearMonth.WithDayOfMonth)r).getYearMonth().getYear(), equalTo(2016));
		assertThat(((ByYearMonth.WithDayOfMonth)r).getYearMonth().getMonth(), equalTo(2));
		assertThat(((ByYearMonth.WithDayOfMonth)r).getDay(), equalTo(20));
	}
	
	@Test
	public void sea_always_returnsUnitQueryForParameterSEA() {
		// Arrange
		final Unit unit = sampleJobnetUnit("sea=new");
		
		// Act
		final WriteOption r = unit.query(Queries.sea().list()).get(0);
		
		// Assert
		assertThat(r, equalTo(WriteOption.NEW));
	}
	
	@Test
	public void soa_always_returnsUnitQueryForParameterSOA() {
		// Arrange
		final Unit unit = sampleJobnetUnit("soa=add");
		
		// Act
		final WriteOption r = unit.query(Queries.soa().list()).get(0);
		
		// Assert
		assertThat(r, equalTo(WriteOption.ADD));
	}
	
	@Test
	public void st_always_returnsUnitQueryForParameterST() {
		// Arrange
		final Unit unit = sampleJobnetUnit("st=+12:30", "st=1,6:45");
		
		// Act
		final StartTime r = unit.query(Queries.st().list()).get(0);
		
		// Assert
		assertThat(r.isRelative(), equalTo(true));
		assertThat(r.getTime().getHours(), equalTo(12));
		assertThat(r.getTime().getMinutes(), equalTo(30));
	}
	
	@Test
	public void sy_always_returnsUnitQueryForParameterSY() {
		// Arrange
		final Unit unit = sampleJobnetUnit("sy=01:23", "sy=2,M2879", "sy=3,U2878");
		
		// Act
		final StartDelayTime r = unit.query(Queries.sy().list()).get(1);
		
		// Assert
		assertThat(r.getRuleNumber().intValue(), equalTo(2));
		assertThat(r.getTimingMethod(), equalTo(TimingMethod.RELATIVE_WITH_ROOT_START_TIME));
		assertThat(r.getTime().getHours(), equalTo(47));
	}
	
	@Test
	public void sz_always_returnsUnitQueryForParameterSZ() {
		// Arrange
		final Unit unit = sampleJobnetUnit("sz=1×2");
		
		// Act
		final MapSize r = unit.query(Queries.sz().list()).get(0);
		
		// Assert
		assertThat(r.getHeight(), equalTo(2));
		assertThat(r.getWidth(), equalTo(1));
	}
	
	@Test
	public void ty_always_returnsUnitQueryForParameterTY() {
		// Arrange
		final Unit unit = sampleJobnetUnit("sz=1×2");
		
		// Act
		final UnitType r = unit.query(Queries.ty().list()).get(0);
		
		// Assert
		assertThat(r, equalTo(UnitType.JOBNET));
	}
	
	@Test
	public void te_always_returnsUnitQueryForParameterTE() {
		// Arrange
		final Unit unit = sampleJobnetUnit("te=\"foo.sh bar baz\"");
		
		// Act
		final CommandLine r = unit.query(Queries.te().list()).get(0);
		
		// Assert
		assertThat(r.getCommand(), equalTo("foo.sh"));
		assertThat(r.getArguments().get(0), equalTo("bar"));
		assertThat(r.getArguments().get(1), equalTo("baz"));
	}
	
	@Test
	public void tho_always_returnsUnitQueryForParameterTHO() {
		// Arrange
		final Unit unit = sampleJobnetUnit("tho=123");
		
		// Act
		final ExitCodeThreshold r = unit.query(Queries.tho().list()).get(0);
		
		// Assert
		assertThat(r.intValue(), equalTo(123));
	}
	
	@Test
	public void tmitv_always_returnsUnitQueryForParameterTMITV() {
		// Arrange
		final Unit unit = sampleJobnetUnit("tmitv=1440");
		
		// Act
		final ElapsedTime r = unit.query(Queries.tmitv().list()).get(0);
		
		// Assert
		assertThat(r.intValue(), equalTo(1440));
	}
	
	@Test
	public void tmitv_always_returnsUnitQueryForParameterTOP1() {
		// Arrange
		final Unit unit = sampleJobnetUnit("top1=sav");
		
		// Act
		final DeleteOption r = unit.query(Queries.top1().list()).get(0);
		
		// Assert
		assertThat(r, equalTo(DeleteOption.SAVE));
	}
}
