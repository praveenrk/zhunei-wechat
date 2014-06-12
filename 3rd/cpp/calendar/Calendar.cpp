#include "Calendar.h"

std::map<int,LiturgicYear*> CathAssist::Calendar::Calendar::mapLiturgicYear;

std::string CathAssist::Calendar::Calendar::getDay(const int& year, const int& month,const int& day)
{
    LiturgicYear* pYear = NULL;
    std::map<int,LiturgicYear*>::iterator iter = mapLiturgicYear.find(year);
    if(iter==mapLiturgicYear.end())
    {
        pYear = new LiturgicYear(year);
        mapLiturgicYear[year] = pYear;
    }
    else
    {
        pYear = iter->second;
    }
    
    return pYear->getDay(year,month,day);
}

void CathAssist::Calendar::Calendar::initCalendar()
{
    //do nothing
}

void CathAssist::Calendar::Calendar::releaseCalendar()
{
    std::map<int,LiturgicYear*>::iterator iter = mapLiturgicYear.begin();
    while(iter!=mapLiturgicYear.end())
    {
        delete iter->second;
        
        ++iter;
    }
    
    mapLiturgicYear.clear();
}