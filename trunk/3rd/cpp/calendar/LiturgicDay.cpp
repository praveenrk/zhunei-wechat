/*
============================================================================
文件名称	:	LiturgicDay.cpp
公司		:	CathAssist
作者		:	李亚科
创建时间	:	2014-06-12 20:27
修改时间	:	2014-06-12 22:30
说明		:	用于计算礼仪年
============================================================================
*/

#include "LiturgicDay.h"
#include <sstream>
#include <iomanip>

using namespace CathAssist::Calendar;

LiturgicDay::LiturgicDay()
    : Date()
    , color(NOCOLOR)
    , rank(WEEKDAY)
    , season(ORDINARY)
{
}

LiturgicDay::LiturgicDay(const Date& d)
    : Date(d)
    , color(NOCOLOR)
    , rank(WEEKDAY)
    , season(ORDINARY)
{
    
}

LiturgicDay::LiturgicDay(const int& year, const int& month, const int& day)
    : Date(year,month,day)
    , color(NOCOLOR)
    , rank(WEEKDAY)
    , season(ORDINARY)
{
    
}

LiturgicDay::~LiturgicDay()
{
    
}

std::string LiturgicDay::toLiturgicString() const
{
    //返回礼仪年日期对应的字符串，格式化输出本身
	std::ostringstream ostr;
    ostr<<"Date\t:\t"<<toString()<<std::endl;
    ostr<<"Color\t:\t"<<getColorStr(color)<<std::endl;
    ostr<<"Rank\t:\t"<<getRankStr(rank)<<std::endl;
    ostr<<"Season\t:\t"<<getSeasonStr(season)<<std::endl;
    ostr<<"Cele\t:\t"<<celebration<<std::endl;
    ostr<<"Invit\t:\t"<<invitatory<<std::endl;
    
    
    return ostr.str();
}
