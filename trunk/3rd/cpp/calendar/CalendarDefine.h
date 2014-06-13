#ifndef __CA_CALENDAR_DEFINE_H__
#define __CA_CALENDAR_DEFINE_H__
#include <string>

namespace CathAssist
{
	namespace Calendar
	{
		/*
		 * Days of the Week
		 */
		typedef enum Days
		{
		   SUN = 0, MON, TUE, WED, THU, FRI, SAT
		} day_t;

		static std::string getDayStr(day_t d)
		{
			switch(d)
			{
			case SUN:
				return "������";
				break;
			case MON:
				return "����һ";
				break;
			case TUE:
				return "���ڶ�";
				break;
			case WED:
				return "������";
				break;
			case THU:
				return "������";
				break;
			case FRI:
				return "������";
				break;
			case SAT:
				return "������";
				break;
			}
			return "Not define";
		}

		/*
		 * Months of the Year
		 */
		typedef enum Months
        {
		   PREVDEC,  JANUARY,   FEBRUARY, MARCH,
		   APRIL,    MAY,       JUNE,     JULY,
		   AUGUST,   SEPTEMBER, OCTOBER, NOVEMBER,
		   DECEMBER, NEXTJAN
		} month_t;
		/*
		 * Liturgical Colors
		 */
		typedef enum Colors
        {
		   NOCOLOR, GREEN, WHITE, RED, PURPLE, ROSE, BLACK
		} color_t;
        
        static std::string getColorStr(color_t c)
        {
            switch(c)
            {
                case NOCOLOR:
                    return "��";
                    break;
                case GREEN:
                    return "��";
                    break;
                case WHITE:
                    return "��";
                    break;
                case RED:
                    return "��";
                    break;
                case PURPLE:
                    return "��";
                    break;
                case ROSE:
                    return "õ";
                    break;
                case BLACK:
                    return "��";
                    break;
            }
            return "Not define";
        }
		/*
		 * Liturgical Ranks
		 */
		typedef enum Ranks
        {
		   WEEKDAY,		/* Plain, old weekdays			*/
		   COMMEMORATION,	/* Commemoration = Memorial in Lent 	*/
		   OPTIONAL,		/* Optional Memorials			*/
		   MEMORIAL,		/* Memorials				*/
		   FEAST,		/* Feasts (not of the Lord)		*/
		   SUNDAY,		/* Sundays 				*/
		   LORD,		/* Feasts of the Lord 			*/
		   ASHWED,		/* Ash Wednesday			*/
		   HOLYWEEK,		/* Mon, Tue, and Wed of Holy Week	*/
		   TRIDUUM,		/* The Triduum				*/
		   SOLEMNITY 		/* Solemnities 				*/
		} rank_t;
        
        static std::string getRankStr(rank_t r)
        {
            switch (r) {
                case WEEKDAY:
                    return "Weekday";
                    break;
                case COMMEMORATION:
                    return "Commemoration";
                    break;
                case OPTIONAL:
                    return "Optional";
                    break;
                case MEMORIAL:
                    return "Memorial";
                    break;
                case FEAST:
                    return "Feast";
                    break;
                case SUNDAY:
                    return "Sunday";
                    break;
                case LORD:
                    return "Lord";
                    break;
                case ASHWED:
                    return "Ashwed";
                    break;
                case HOLYWEEK:
                    return "Holyweek";
                    break;
                case TRIDUUM:
                    return "Triduum";
                    break;
                case SOLEMNITY:
                    return "Solemnity";
                    break;
            }
            return "Not define";
        }
        
		/*
		 * Liturgical Seasons
		 */
		typedef enum Seasons
        {
		   ORDINARY,
		   ADVENT,
		   CHRISTMAS,
		   LENT,
		   EASTER,
		   PASCHAL	/* Ash Wed., Holy Week & Easter Octave 	*/
		} season_t;
        
        static std::string getSeasonStr(season_t s)
        {
            switch(s)
            {
                case ORDINARY:
                    return "������";
                    break;
                case ADVENT:
                    return "������";
                    break;
                case CHRISTMAS:
                    return "ʥ����";
                    break;
                case LENT:
                    return "��Ѯ��";
                    break;
                case EASTER:
                    return "������";
                    break;
                case PASCHAL:
                    return "��Խ��";	//Ash Wed., Holy Week & Easter Octave
                    break;
            }
            return "Not define";
        }

		static std::string getChineseNumStr(const int& n)
		{
			static std::string numStr[] = 
			{
				"ʮ",
				"һ",
				"��",
				"��",
				"��",
				"��",
				"��",
				"��",
				"��",
				"��",
				"ʮ",
			};


			if(n<1||n>39)
				return "Not define";
			if(n<=10)
			{
				return numStr[n];
			}
			else if(n<20)
			{
				return "ʮ"+numStr[n%10];
			}
			else if(n<30)
			{
				return "إ"+numStr[n%10];
			}
			else if(n<40)
			{
				if(n==30)
					return "��ʮ";
				else
					return "��ʮ"+numStr[n%10];
			}
			
			return "Not define";
		}
	}
}

#endif	//__CA_CALENDAR_DEFINE_H__