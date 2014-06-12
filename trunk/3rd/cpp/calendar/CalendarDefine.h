#ifndef __CA_CALENDAR_DEFINE_H__
#define __CA_CALENDAR_DEFINE_H__

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
		typedef enum Months { 
		   PREVDEC,  JANUARY,   FEBRUARY, MARCH,
		   APRIL,    MAY,       JUNE,     JULY,
		   AUGUST,   SEPTEMBER, OCTOBER, NOVEMBER,
		   DECEMBER, NEXTJAN
		} month_t;
		/*
		 * Liturgical Colors
		 */
		typedef enum Colors {
		   NOCOLOR, GREEN, WHITE, RED, PURPLE, ROSE, BLACK
		} color_t;
		/*
		 * Liturgical Ranks
		 */
		typedef enum Ranks {
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
		/*
		 * Liturgical Seasons
		 */
		typedef enum Seasons {
		   ORDINARY,
		   ADVENT,
		   CHRISTMAS,
		   LENT,
		   EASTER,
		   PASCHAL	/* Ash Wed., Holy Week & Easter Octave 	*/
		} season_t;
	}
}

#endif	//__CA_CALENDAR_DEFINE_H__