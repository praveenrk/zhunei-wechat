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
#include <iostream>

using namespace CathAssist::Calendar;


LiturgicYear::LiturgicYear(const int& year)
{
	//主显节
	Date y(year,1,2);
    ep = y.addDays((7-y.dayOfWeek())%7);
    
    //主受洗日(圣诞期的结束)
    if(ep.day()>6)
    {
        bl = ep.addDays(1);
    }
    else
    {
        bl = ep.addDays(7);
    }
}

LiturgicYear::~LiturgicYear(void)
{
}

void LiturgicYear::printSelf()
{
	std::cout<<"主显节:"<<ep.toString()<<std::endl;
	std::cout<<"主受洗日:"<<bl.toString()<<std::endl;
}
