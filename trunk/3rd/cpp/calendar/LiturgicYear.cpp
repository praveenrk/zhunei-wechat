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


LiturgicYear::LiturgicYear(const int& y)
{
    year = y;
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
    //圣灰礼仪（四旬期开始）
    aw = easter.addDays(-46);
    
    //圣神降临瞻礼
    ps = easter.addDays(49);
    
    t.setDate(year,11,30);
    av = t.addDays(0-t.dayOfWeek());
}

LiturgicYear::~LiturgicYear(void)
{
}

LiturgicDay LiturgicYear::getLiturgicDay(const Date& d)
{
    //填充数据
    LiturgicDay ld = d;
    
    return ld;
}

void LiturgicYear::printSelf()
{
    std::cout<<"年份:"<<year<<std::endl;
	std::cout<<"主显节\t\t:\t"<<ep.toString()<<std::endl;
	std::cout<<"主受洗日\t\t:\t"<<bl.toString()<<std::endl;
	std::cout<<"圣灰礼仪\t\t:\t"<<aw.toString()<<std::endl;
	std::cout<<"复活节\t\t:\t"<<easter.toString()<<std::endl;
    std::cout<<"圣神降临\t\t:\t"<<ps.toString()<<std::endl;
    std::cout<<"将临期第一主日\t:\t"<<av.toString()<<std::endl;
}
