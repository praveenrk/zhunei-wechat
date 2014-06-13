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
    , season(ORDINARY)
{
}

LiturgicDay::LiturgicDay(const Date& d)
    : Date(d)
    , season(ORDINARY)
{
    
}

LiturgicDay::LiturgicDay(const int& year, const int& month, const int& day)
    : Date(year,month,day)
    , season(ORDINARY)
{
    
}

LiturgicDay::~LiturgicDay()
{
    
}

std::list<CellInfo> LiturgicDay::getCellInfos() const
{
    std::list<CellInfo> cells;
    
    CellMap::const_reverse_iterator iter = mapCells.rbegin();
    while (iter!=mapCells.rend())
    {
        cells.push_back(iter->second);
        ++iter;
    }
    
    return cells;
}

void LiturgicDay::appendCell(const CellInfo& c)
{
    mapCells.insert(std::make_pair(c.rank, c));
}

void LiturgicDay::appendCell(rank_t r,color_t c,const std::string& cele)
{
	CellInfo cell = {r,c,cele};
    appendCell(cell);
}

std::string LiturgicDay::toLiturgicString() const
{
    //返回礼仪年日期对应的字符串，格式化输出本身
	std::ostringstream ostr;
    ostr<<"Date\t:\t"<<toString()<<std::endl;
    ostr<<"Season\t:\t"<<CathAssist::Calendar::getSeasonStr(season)<<std::endl;
    
    
    CellMap::const_reverse_iterator iter = mapCells.rbegin();
    ostr<<"Cells\t:\t"<<std::endl;
    while (iter!=mapCells.rend())
    {
        ostr<<"("<<CathAssist::Calendar::getColorStr(iter->second.color);
        ostr<<"\t"<<CathAssist::Calendar::getRankStr(iter->second.rank);
        ostr<<"\t"<<iter->second.celebration<<");";
        ++iter;
    }
    
    
    //ostr<<"Invit\t:\t"<<invitatory<<std::endl;
    
    
    return ostr.str();
}
