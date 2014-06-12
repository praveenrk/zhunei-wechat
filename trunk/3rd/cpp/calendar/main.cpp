#include <iostream>
#include "Calendar.h"

using namespace std;
using namespace CathAssist::Calendar;

int main(int _Argc, char ** _Argv, char ** _Env)
{
    /*
     示例代码,2014年的输出
     
    //初始化Calendar，只需在程序入口调用一次
    Calendar::initCalendar();
    
    Date d(2014,1,1);
    for(int i=0;i<366;++i)
    {
        Date t = d.addDays(i);
        LiturgicDay ld = Calendar::getLiturgicDay(t.year(), t.month(), t.day());
        cout<<ld.toLiturgicString()<<endl;
    }
    
    //释放Calendar资源，只需在程序结束时调用一次（放置内存泄露）
    Calendar::releaseCalendar();
    */
    
    
	for(int i=2000;i<2010;++i)
	{
		LiturgicYear y(i);
		cout<<y.toString()<<endl;
	}
    
	return 0;
}
