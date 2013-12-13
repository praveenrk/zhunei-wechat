package daily.cathassist.org.bean;

public class CalendarDay {
	private long id;
	private String date;
	private int dayType;
	private String summary;
	private String festival;
	private int memorableDay;
	private String solarTerms;
	private String holiday;
	private String bible;
	private String pray;
	private String updateTime;

	public CalendarDay() {

	}

	public CalendarDay(long id, String date, int dayType, String summary,
			String festival, int memorableDay, String solarTerms,
			String holiday, String bible, String pray, String updateTime) {
		super();
		this.id = id;
		this.date = date;
		this.dayType = dayType;
		this.summary = summary;
		this.festival = festival;
		this.memorableDay = memorableDay;
		this.solarTerms = solarTerms;
		this.holiday = holiday;
		this.bible = bible;
		this.pray = pray;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDayType() {
		return dayType;
	}

	public void setDayType(int dayType) {
		this.dayType = dayType;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getFestival() {
		return festival;
	}

	public void setFestival(String festival) {
		this.festival = festival;
	}

	public int getMemorableDay() {
		return memorableDay;
	}

	public void setMemorableDay(int memorableDay) {
		this.memorableDay = memorableDay;
	}

	public String getSolarTerms() {
		return solarTerms;
	}

	public void setSolarTerms(String solarTerms) {
		this.solarTerms = solarTerms;
	}

	public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public String getBible() {
		return bible;
	}

	public void setBible(String bible) {
		this.bible = bible;
	}

	public String getPray() {
		return pray;
	}

	public void setPray(String pray) {
		this.pray = pray;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
