#include <iostream>
#include "Calendar.h"
#include <time.h>

using namespace std;
using namespace CathAssist::Calendar;

//打印某年全年的日子
void printYear(const int& year)
{
    Date d(year,1,1);
    int countDay = Date::isLeapYear(year) ? 366 : 365;
    for(int i=0;i<countDay;++i)
    {
        Date t = d.addDays(i);
        LiturgicDay ld = Calendar::getLiturgicDay(t.year(), t.month(), t.day());
        cout<<ld.toLiturgicString()<<endl;
    }
}


int main(int _Argc, char ** _Argv, char ** _Env)
{
    //初始化Calendar，只需在程序入口调用一次
    Calendar::initCalendar();

	time_t begin = time(0);

    //示例代码,2014年的输出
    printYear(2014);

	cout<<"Used time(s):"<<time(0)-begin<<endl;
    
    /*
	for(int i=2000;i<2010;++i)
	{
		LiturgicYear y(i);
		cout<<y.toString()<<endl;
	}
    */
    
    //释放Calendar资源，只需在程序结束时调用一次（放置内存泄露）
    Calendar::releaseCalendar();
	return 0;
}
