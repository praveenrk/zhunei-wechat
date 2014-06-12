/*
============================================================================
文件名称	:	LiturgicDay.h
公司		:	CathAssist
作者		:	李亚科
创建时间	:	2014-06-12 21:27
修改时间	:	2014-06-12 22:30
说明		:	用于计算礼仪年
============================================================================
*/
#ifndef __CA_CALENDAR_LITURGIC_DAY_H__
#define __CA_CALENDAR_LITURGIC_DAY_H__
#include "Date.h"
#include <list>
#include <map>

namespace CathAssist
{
	namespace Calendar
	{
        typedef struct CellInfo
        {
            rank_t rank;                //优先级
            color_t color;              //祭衣颜色
            std::string celebration;    //节日
        } CellInfo_t;
        
        typedef std::multimap<rank_t,CellInfo> CellMap;
        
        class LiturgicDay : public Date
        {
        public:
            LiturgicDay();
            LiturgicDay(const Date& d);
            LiturgicDay(const int& year, const int& month, const int& day);
            ~LiturgicDay();
            
        public:
            season_t getSeason(){ return season; }
            void setSeason(season_t s){ season = s; }
            
            std::list<CellInfo> getCellInfos() const;
            void appendCell(const CellInfo& c);
            void appendCell(rank_t r,color_t c,const std::string& cele);
            
//            rank_t getRank(){ return rank; }
//            void setRank(rank_t r){ rank = r; }
            
//            std::string getCelebration()const{ return celebration; }
//            void setCelebration(const std::string& c){ celebration = c; }
            
//            std::string getInvitatory()const{ return invitatory; }
//            void setInvitatory(const std::string& i){ invitatory = i; }
            
            std::string toLiturgicString() const;
            
        private:
            season_t season;            // Advent, Lent, Ordinary, etc.
            
            //多个数据
            CellMap mapCells;
            
//            std::string invitatory;		// Invitatory of the day, 好像是当天选用的读经，暂时未使用
        };
    }
}

#endif	//__CA_CALENDAR_LITURGIC_YEAR_H__