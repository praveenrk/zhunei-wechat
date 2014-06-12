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

namespace CathAssist
{
	namespace Calendar
	{
        class LiturgicDay : public Date
        {
        public:
            LiturgicDay();
            LiturgicDay(const Date& d);
            LiturgicDay(const int& year, const int& month, const int& day);
            ~LiturgicDay();
            
        public:
            color_t getColor(){ return color; }
            void setColor(color_t c){ color = c; }
            
            rank_t getRank(){ return rank; }
            void setRank(rank_t r){ rank = r; }
            
            season_t getSeason(){ return season; }
            void setSeason(season_t s){ season = s; }
            
            std::string getCelebration()const{ return celebration; }
            void setCelebration(const std::string& c){ celebration = c; }
            
            std::string getInvitatory()const{ return invitatory; }
            void setInvitatory(const std::string& i){ invitatory = i; }
            
            std::string toLiturgicString() const;
            
        private:
            color_t color;          /* White, Red, Green, etc.	*/
            rank_t rank;			/* Feast, Solemnity, etc.	*/
            season_t season;		/* Advent, Lent, Ordinary, etc.	*/
            std::string celebration;		/* "Christmas", "Easter", etc.	*/
            std::string invitatory;		/* Invitatory of the day	*/
        };
    }
}

#endif	//__CA_CALENDAR_LITURGIC_YEAR_H__