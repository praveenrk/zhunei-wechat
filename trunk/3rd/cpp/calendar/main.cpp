#include <iostream>
#include "Calendar.h"

using namespace std;
using namespace CathAssist::Calendar;

int main(int _Argc, char ** _Argv, char ** _Env)
{
	for(int i=2000;i<2010;++i)
	{
		LiturgicYear y(i);
		y.printSelf();
	}

	return 0;
}
