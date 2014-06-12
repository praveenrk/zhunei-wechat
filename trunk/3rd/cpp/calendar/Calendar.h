#ifndef __CA_CALENDAR_H__
#define __CA_CALENDAR_H__
#include "CalendarDefine.h"
#include "LiturgicYear.h"
#include <map>

using namespace CathAssist::Calendar;

namespace CathAssist
{
	namespace Calendar
	{
		class Calendar
		{
		public:
            static std::string getDay(const int& year, const int& month,const int& day);
            static void initCalendar();
            static void releaseCalendar();
            
        private:
            static std::map<int,LiturgicYear*> mapLiturgicYear;
		};
	}
}

#endif	//__CA_CALENDAR_H__
