/*
============================================================================
文件名称	:	LiturgicYear.cpp
公司		:	CathAssist
作者		:	李亚科
创建时间	:	2014-06-12 12:27
修改时间	:	2014-06-12 12:30
说明		:	用于计算礼仪年
============================================================================
*/

#include "LiturgicYear.h"
#include <sstream>

using namespace CathAssist::Calendar;

std::multimap<int,CellInfo> CathAssist::Calendar::LiturgicYear::mapPropers;

void CathAssist::Calendar::LiturgicYear::initPropers()
{
	//初始化常用节日（日期固定）

	//1月
	mapPropers.insert(std::make_pair(101,CellInfo(SOLEMNITY,WHITE,"天主之母节")));
	mapPropers.insert(std::make_pair(102,CellInfo(MEMORIAL,WHITE,"圣巴西略及圣额我略·纳齐安（主教、圣师)")));
	mapPropers.insert(std::make_pair(103,CellInfo(OPTIONAL,NOCOLOR,"耶稣圣名节")));
	mapPropers.insert(std::make_pair(106,CellInfo(SOLEMNITY,WHITE,"主显节")));
	mapPropers.insert(std::make_pair(107,CellInfo(OPTIONAL,NOCOLOR,"圣雷孟（司铎）")));
	mapPropers.insert(std::make_pair(113,CellInfo(OPTIONAL,NOCOLOR,"圣依拉略（主教、圣师）")));
	mapPropers.insert(std::make_pair(117,CellInfo(MEMORIAL,WHITE,"圣安东尼（院长）")));
	mapPropers.insert(std::make_pair(120,CellInfo(OPTIONAL,NOCOLOR,"圣法比盎（教宗、殉道)")));
	mapPropers.insert(std::make_pair(120,CellInfo(OPTIONAL,NOCOLOR,"圣巴斯弟盎（殉道）")));
	mapPropers.insert(std::make_pair(121,CellInfo(MEMORIAL,RED,"圣依搦斯（童贞、殉道)")));
	mapPropers.insert(std::make_pair(122,CellInfo(OPTIONAL,NOCOLOR,"圣味增爵（执事、殉道）")));
	mapPropers.insert(std::make_pair(124,CellInfo(MEMORIAL,WHITE,"圣方济各·沙雷氏（主教、圣师）")));
	mapPropers.insert(std::make_pair(125,CellInfo(FEAST,WHITE,"圣保禄归化（宗徒）")));
	mapPropers.insert(std::make_pair(126,CellInfo(MEMORIAL,WHITE,"圣弟茂德与圣弟铎（主教）")));
	mapPropers.insert(std::make_pair(127,CellInfo(OPTIONAL,NOCOLOR,"圣安琪拉·美利西（童贞）")));
	mapPropers.insert(std::make_pair(128,CellInfo(MEMORIAL,WHITE,"圣多玛斯·阿奎纳（司铎、圣师）")));
	mapPropers.insert(std::make_pair(131,CellInfo(MEMORIAL,WHITE,"圣若望·鲍思高（司铎")));


	//12月份
	mapPropers.insert(std::make_pair(1225,CellInfo(SOLEMNITY,WHITE,"圣诞节")));
}


LiturgicYear::LiturgicYear(const int& y)
	: year(y)
{
	init();
}

LiturgicYear::~LiturgicYear(void)
{
}

LiturgicDay LiturgicYear::getLiturgicDay(const Date& d)
{
	if(d.year() != year)
		return LiturgicDay();

    //填充数据
    LiturgicDay ld = d;
	if(ld<bl)
	{
		testChristmas1(ld);
	}
	else if(ld<aw)
	{
		testOrdinary1(ld);
	}
	else if(ld<easter)
	{
		testLent(ld);
	}
	else if(ld<=ps)
	{
		testEaster(ld);
	}
	else if(ld<av)
	{
		testOrdinary2(ld);
	}
	else if(ld<cm)
	{
		testAdvent(ld);
	}
	else
	{
		testChristmas2(ld);
	}

	testProper(ld);

    return ld;
}

std::string LiturgicYear::toString() const
{
    std::ostringstream ostr;
    ostr<<"年份:"<<year<<std::endl;
	ostr<<"主显节\t\t:\t"<<ep.toString()<<std::endl;
	ostr<<"主受洗日\t\t:\t"<<bl.toString()<<std::endl;
	ostr<<"圣灰礼仪\t\t:\t"<<aw.toString()<<std::endl;
	ostr<<"复活节\t\t:\t"<<easter.toString()<<std::endl;
    ostr<<"圣神降临\t\t:\t"<<ps.toString()<<std::endl;
    ostr<<"将临期第一主日\t:\t"<<av.toString()<<std::endl;
    
    return ostr.str();
}

void LiturgicYear::init()
{
	//主显节
	LiturgicDay t(year,1,2);
	ep = t.addDays((7-t.dayOfWeek())%7);

	//主受洗日(圣诞期的结束)
	if(ep.day()>6)
	{
		bl = ep.addDays(1);
	}
	else
	{
		bl = ep.addDays(7);
	}

	//复活节
	{
		int y,c,n,k,i,j,l,m,d;
		y = year;

		c = y/100;
		n = y - 19*(y/19);
		k = (c - 17)/25;
		i = c - c/4 - (c-k)/3 + 19*n + 15;
		i = i - 30*(i/30);
		i = i - (i/28) * (1 - (i/28) * (29/(i+1)) * ((21 - n)/11));
		j = y + y/4 + i + 2 - c + c/4;
		j = j - 7*(j/7);
		l = i - j;

		m = 3 + (l+40)/44;
		d = l + 28 - 31*(m/4);

		easter.setDate(year, m, d);
	}

	//圣灰礼仪（周三 四旬期开始）
	aw = easter.addDays(-46);

	//圣神降临节瞻礼
	ps = easter.addDays(49);

	t.setDate(year,11,30);
	//将临期第一主日
	av = t.addDays(0-t.dayOfWeek());

	//圣诞节
	cm.setDate(year,12,25);
}

void LiturgicYear::testChristmas1(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<LiturgicDay(year,1,1) || ld>bl)
		return;

	ld.setSeason(CHRISTMAS);

	Date lastChristmas(year-1,12,25);
	int dayFromLC = lastChristmas.daysTo(ld);
	ld.setWeekOfSeason((dayFromLC-lastChristmas.dayOfWeek()-1)/7 + 1);
	if(ld.dayOfWeek()==SUN)
	{
		ld.appendCell(SUNDAY,WHITE,ld.toWeekdayString());
	}
	else
	{
		ld.appendCell(WEEKDAY,WHITE,ld.toWeekdayString());
	}

	if(ld<ep)
	{

	}
	else if(ld==ep)
	{
		ld.appendCell(SOLEMNITY,WHITE,"主显节");
	}
	else if(ld<bl)
	{

	}
}

void LiturgicYear::testOrdinary1(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<bl || ld>=aw)
		return;

	ld.setSeason(ORDINARY);
	int weekBegin = aw.weekNumber();
	ld.setWeekOfSeason( ld.weekNumber()-bl.weekNumber()+1 );
	if(ld.dayOfWeek()==SUN)
	{
		ld.appendCell(SUNDAY,GREEN,ld.toWeekdayString());
	}
	else
	{
		ld.appendCell(WEEKDAY,GREEN,ld.toWeekdayString());
	}


	if(ld == bl)
	{
		ld.appendCell(LORD,WHITE,"主受洗日");
	}

}

void LiturgicYear::testLent(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<aw || ld>=easter)
		return;

	ld.setSeason(LENT);
	ld.setWeekOfSeason( ld.weekNumber()-aw.weekNumber() );
	if(ld.dayOfWeek()==SUN)
	{
		if(ld.getWeekOfSeason() == 4)
			ld.appendCell(SUNDAY,ROSE,ld.toWeekdayString());
		else
			ld.appendCell(SUNDAY,PURPLE,ld.toWeekdayString());
	}
	else
	{
		ld.appendCell(WEEKDAY,PURPLE,ld.toWeekdayString());
	}

	if(ld == aw)
	{
		ld.appendCell(ASHWED,PURPLE,"圣灰礼仪");
	}
	else if(ld.getWeekOfSeason() == 6)
	{
		//圣周
		switch(ld.dayOfWeek())
		{
		case 0:
			ld.appendCell(SUNDAY,RED,"圣枝主日(基督苦难主日)");
			break;
		case 1:
			ld.appendCell(HOLYWEEK,PURPLE,"圣周一");
			break;
		case 2:
			ld.appendCell(HOLYWEEK,PURPLE,"圣周二");
			break;
		case 3:
			ld.appendCell(HOLYWEEK,PURPLE,"圣周三");
			break;
		case 4:
			ld.appendCell(TRIDUUM,WHITE,"主的晚餐");
			break;
		case 5:
			ld.appendCell(TRIDUUM,RED,"耶稣受难");
			break;
		case 6:
			ld.appendCell(TRIDUUM,WHITE,"基督安眠墓中(复活节前夕)");
			break;
		}
	}
}

void LiturgicYear::testEaster(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<easter || ld>ps)
		return;

	ld.setSeason(EASTER);
	ld.setWeekOfSeason(ld.weekNumber()-easter.weekNumber()+1);

	int dayFromEaster = easter.daysTo(ld);

	if(dayFromEaster>7)
	{
		if(ld.dayOfWeek()==SUN)
		{
			ld.appendCell(SUNDAY,WHITE,ld.toWeekdayString());
		}
		else
		{
			ld.appendCell(WEEKDAY,WHITE,ld.toWeekdayString());
		}
	}

	if(dayFromEaster<8)
	{
		if(dayFromEaster == 0)
		{
			ld.appendCell(SOLEMNITY,WHITE,"复活节");
		}
		else if(dayFromEaster < 7)
		{
			std::ostringstream ostr;
			ostr<<"复活节庆期第"<<getChineseNumStr(ld.dayOfWeek()+1)<<"日";

			ld.appendCell(SOLEMNITY,WHITE,ostr.str());
		}
		else
		{
			ld.appendCell(SOLEMNITY,WHITE,ld.toWeekdayString());
		}
	}
	else if(dayFromEaster == 39)
	{
		ld.appendCell(SOLEMNITY,WHITE,"耶稣升天");
	}
	else if(dayFromEaster == 49)
	{
		ld.appendCell(SOLEMNITY,RED,"圣神降临节");
	}
}

void LiturgicYear::testOrdinary2(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<=ps || ld>=av)
		return;

	int weekEnd = av.weekNumber() - 1;		//将临期第一主日为常年期的结束
	ld.setWeekOfSeason( 34-(weekEnd-ld.weekNumber()) );
	if(ld.dayOfWeek()==SUN)
	{
		ld.appendCell(SUNDAY,GREEN,ld.toWeekdayString());
		if(ld.getWeekOfSeason() == 34)
		{
			ld.appendCell(SOLEMNITY,WHITE,"基督普世君王节");
		}
	}
	else
	{
		ld.appendCell(WEEKDAY,GREEN,ld.toWeekdayString());
	}



	int dayFromEaster = easter.daysTo(ld);

	if(dayFromEaster == 56)
	{
		ld.appendCell(SOLEMNITY,WHITE,"圣三主日");
	}
	else if(dayFromEaster == 60)
	{
		//可移至主日庆祝 + 63
		ld.appendCell(SOLEMNITY,WHITE,"基督圣体圣血节");
	}
	else if(dayFromEaster == 68)
	{
		ld.appendCell(SOLEMNITY,WHITE,"耶稣圣心节");
	}
	else if(dayFromEaster == 69)
	{
		ld.appendCell(MEMORIAL,WHITE,"圣母无玷圣心");
	}

	ld.setSeason(ORDINARY);
}

void LiturgicYear::testAdvent(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<av || ld>=cm)
		return;

	ld.setSeason(ADVENT);
	ld.setWeekOfSeason(ld.weekNumber()-av.weekNumber()+1);
	if(ld.dayOfWeek() == SUN)
	{
		if(ld.getWeekOfSeason() == 3)
		{
			ld.appendCell(SUNDAY,ROSE,ld.toWeekdayString());
		}
		else
		{
			ld.appendCell(SUNDAY,PURPLE,ld.toWeekdayString());
		}
	}
	else
	{
		ld.appendCell(SUNDAY,PURPLE,ld.toWeekdayString());
	}
}

void LiturgicYear::testChristmas2(LiturgicDay& ld)
{
	if((!ld.isValid()) || ld<cm || ld>LiturgicDay(year,12,31))
		return;

	ld.setSeason(CHRISTMAS);
	if(cm.dayOfWeek() == SUN && ld==Date(year,12,31))
	{
		ld.setWeekOfSeason(2);
		ld.appendCell(SUNDAY,WHITE,ld.toWeekdayString());
	}
	else
	{
		ld.setWeekOfSeason(1);
		if(ld==Date(year,12,31))
		{
			ld.appendCell(WEEKDAY,WHITE,ld.toWeekdayString());
		}
	}

	switch(cm.daysTo(ld))
	{
	case 1:
	case 2:
	case 3:
	case 4:
	case 5:
	case 6:
		{
			std::ostringstream ostr;
			if(ld.dayOfWeek() == SUN)
			{
				ostr<<"圣诞节庆期第"<<getChineseNumStr(cm.daysTo(ld)+1)<<"日";
				ld.appendCell(SUNDAY,WHITE,ostr.str());
			}
			else
			{
				ostr<<"圣诞节庆期第"<<getChineseNumStr(cm.daysTo(ld)+1)<<"日";
				ld.appendCell(WEEKDAY,WHITE,ostr.str());
			}
		}
		break;
	}

	//圣家节
	if(cm.dayOfWeek() == SUN)
	{
		if(ld == Date(year,12,30))
			ld.appendCell(LORD,WHITE,"圣家节");
	}
	else
	{
		if(ld.dayOfWeek() == SUN)
			ld.appendCell(LORD,WHITE,"圣家节");
	}
}

void LiturgicYear::testProper(LiturgicDay& ld)
{
	int v = ld.month()*100+ld.day();

	std::multimap<int,CellInfo>::const_iterator iter = mapPropers.find(v);
	std::multimap<int,CellInfo>::size_type count = mapPropers.count(v);

	std::multimap<int,CellInfo>::size_type i = 0;
	while(i<count)
	{
		ld.appendCell(iter->second);

		++iter;
		++i;
	}

}
