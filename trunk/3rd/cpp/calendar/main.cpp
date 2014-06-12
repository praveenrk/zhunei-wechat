#include <iostream>
#include "Calendar.h"

using namespace std;
using namespace CathAssist::Calendar;

int main(int _Argc, char ** _Argv, char ** _Env)
{
    Calendar::initCalendar();
    
    Date d(2014,1,1);
    for(int i=0;i<366;++i)
    {
        Date t = d.addDays(i);
        LiturgicDay ld = Calendar::getLiturgicDay(t.year(), t.month(), t.day());
        cout<<ld.toLiturgicString()<<endl;
    }
    
//	for(int i=2000;i<2010;++i)
//	{
//		LiturgicYear y(i);
//		y.printSelf();
//        cout<<endl;
//	}
    
    Calendar::releaseCalendar();
	return 0;
}
