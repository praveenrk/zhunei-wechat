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
                    return "None";
                    break;
                case GREEN:
                    return "Green";
                    break;
                case WHITE:
                    return "White";
                    break;
                case RED:
                    return "Red";
                    break;
                case PURPLE:
                    return "Purple";
                    break;
                case ROSE:
                    return "Rose";
                    break;
                case BLACK:
                    return "Black";
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
                    return "Ordinary";
                    break;
                case ADVENT:
                    return "Advent";
                    break;
                case CHRISTMAS:
                    return "Christmas";
                    break;
                case LENT:
                    return "Lent";
                    break;
                case EASTER:
                    return "Easter";
                    break;
                case PASCHAL:
                    return "Paschal";
                    break;
            }
            return "Not define";
        }
	}
}

#endif	//__CA_CALENDAR_DEFINE_H__