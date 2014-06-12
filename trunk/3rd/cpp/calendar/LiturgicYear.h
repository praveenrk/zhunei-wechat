/*
============================================================================
文件名称	:	LiturgicYear.h
公司		:	CathAssist
作者		:	李亚科
创建时间	:	2014-06-12 12:27
修改时间	:	2014-06-12 12:30
说明		:	用于计算礼仪年
============================================================================
*/
#ifndef __CA_CALENDAR_LITURGIC_YEAR_H__
#define __CA_CALENDAR_LITURGIC_YEAR_H__
#include "Date.h"

namespace CathAssist
{
	namespace Calendar
	{
		class LiturgicYear
		{
		public:
			LiturgicYear(const int& year);
			~LiturgicYear(void);

		public:
			void printSelf();

		private:
			//礼仪年中的关键日期
            Date ep;            // Epiphany of the Lord     主受洗日
			Date bl;			// End of Christmas season	上一年圣诞期的结束日（主受洗日）
			Date aw;			// Ash Wednesday			圣灰礼仪周三（四旬期开始）
			Date ps;			// Pentecost Sunday			圣神降临节（复活期结束）
			Date av;			// First Sunday of Advent	将临期第一主日，将临期的开始
		};
	}
}

#endif	//__CA_CALENDAR_LITURGIC_YEAR_H__