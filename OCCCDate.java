package PersonGui;

import java.io.Serializable;
import java.util.GregorianCalendar;


public class OCCCDate implements Serializable {

    private int dayOfMonth;
    private int monthOfYear;
    private int year;
    private GregorianCalendar gc;
    private boolean dateFormat;
    private boolean dateStyle;
    private boolean dateDayName;
    public static final boolean FORMAT_US = true;
    public static final boolean FORMAT_EU = false;
    public static final boolean STYLE_NUMBERS = true;
    public static final boolean STYLE_NAMES = false;
    public static final boolean SHOW_DAY_NAME = true;
    public static final boolean HIDE_DAY_NAME = false;

    // Normal Params Constructor
    public OCCCDate(int day, int month, int year) {
        gc = new GregorianCalendar(year, month - 1, day); // Subtracting 1 to match zero-based indexing

        // Check if input date is February 29th and the year is not a leap year
        if (month == 2 && day == 29 && !gc.isLeapYear(year)) {
            day = 1;
            month = 3;
        }

        // Handle overflow days beyond the number of days in a month (e.g., January 365)
        gc.set(year, month - 1, day);
        dayOfMonth = gc.get(GregorianCalendar.DAY_OF_MONTH);
        monthOfYear = gc.get(GregorianCalendar.MONTH) + 1; // Adding 1 to make it user-friendly
        this.year = gc.get(GregorianCalendar.YEAR);
        dateFormat = FORMAT_US;
        dateStyle = STYLE_NUMBERS;
        dateDayName = HIDE_DAY_NAME;
    }

    // Default Constructer
    public OCCCDate() {
        gc = new GregorianCalendar();
        dayOfMonth = gc.get(GregorianCalendar.DAY_OF_MONTH);
        monthOfYear = gc.get(GregorianCalendar.MONTH) + 1; // Adding 1 to make it user-friendly
        year = gc.get(GregorianCalendar.YEAR);
        dateFormat = FORMAT_US;
        dateStyle = STYLE_NUMBERS;
        dateDayName = HIDE_DAY_NAME;
    }

    public OCCCDate(GregorianCalendar gc) {
        this.gc = gc;
        dayOfMonth = gc.get(GregorianCalendar.DAY_OF_MONTH);
        monthOfYear = gc.get(GregorianCalendar.MONTH) + 1; // Adding 1 to make it user-friendly
        year = gc.get(GregorianCalendar.YEAR);
        dateFormat = FORMAT_US;
        dateStyle = STYLE_NUMBERS;
        dateDayName = HIDE_DAY_NAME;
    }

    public OCCCDate(OCCCDate date) {
        this.gc = date.gc;
        this.dayOfMonth = date.dayOfMonth;
        this.monthOfYear = date.monthOfYear;
        this.year = date.year;
        this.dateFormat = date.dateFormat;
        this.dateStyle = date.dateStyle;
        this.dateDayName = date.dateDayName;
    }

    /* Accessors */

    public String getDayName() {
        String dayName = "";
        switch (gc.get(GregorianCalendar.DAY_OF_WEEK)) {
            case 1:
                dayName = "Sunday";
                break;
            case 2:
                dayName = "Monday";
                break;
            case 3:
                dayName = "Tuesday";
                break;
            case 4:
                dayName = "Wednesday";
                break;
            case 5:
                dayName = "Thursday";
                break;
            case 6:
                dayName = "Friday";
                break;
            case 7:
                dayName = "Saturday";
                break;
        }
        return dayName;
    }

    /* Mutators */

    public void setDateFormat(boolean format) {
        this.dateFormat = format;
    }

    public void setStyleFormat(boolean style) {
        this.dateStyle = style;
    }

    public void setDayName(boolean dayName) {
        this.dateDayName = dayName;
    }

    public String toString() {
        String dateString = "";

        if (this.dateDayName == SHOW_DAY_NAME) {
            dateString += this.getDayName() + ", ";
        }

        if (this.dateStyle == STYLE_NUMBERS) {
            if (this.dateFormat == FORMAT_US) {
                dateString += this.monthOfYear + "/" + this.dayOfMonth + "/" + this.year;
            } else {
                dateString += this.dayOfMonth + "/" + this.monthOfYear + "/" + this.year;
            }
        } else {
            if (this.dateFormat == FORMAT_US) {
                dateString += this.getMonthName() + " " + this.dayOfMonth + ", " + this.year;
            } else {
                dateString += this.dayOfMonth + " " + this.getMonthName() + ", " + this.year;
            }
        }

        return dateString;
    }

    public String getMonthName() {
        String monthName = "";
        switch (this.monthOfYear) {
            case 1:
                monthName = "January";
                break;
            case 2:
                monthName = "February";
                break;
            case 3:
                monthName = "March";
                break;
            case 4:
                monthName = "April";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "June";
                break;
            case 7:
                monthName = "July";
                break;
            case 8:
                monthName = "August";
                break;
            case 9:
                monthName = "September";
                break;
            case 10:
                monthName = "October";
                break;
            case 11:
                monthName = "November";
                break;
            case 12:
                monthName = "December";
                break;
        }
        return monthName;
    }

    public int getDifferenceInYears() {
        GregorianCalendar today = new GregorianCalendar();
        int yearDifference = today.get(GregorianCalendar.YEAR) - this.year;

        return yearDifference;
    }

    // Difference in years between this date and another OCCCDate
    public int getDifferenceInYears(OCCCDate d) {
        int yearDifference = d.year - this.year;
        return yearDifference;
    }

    // Check if two OCCCDate objects represent the same date
    public boolean equals(OCCCDate dob) {
        return this.dayOfMonth == dob.dayOfMonth &&
               this.monthOfYear == dob.monthOfYear &&
               this.year == dob.year;
    }

    public int getDay(){
        return dayOfMonth;
    }

    public int getMonth(){
        return monthOfYear;
    }

    public int getYear(){
        return year;
    }

    public void setBirthDate(OCCCDate birthDate) {
        this.dayOfMonth = birthDate.dayOfMonth;
        this.monthOfYear = birthDate.monthOfYear;
        this.year = birthDate.year;
    }

    public OCCCDate getBirthDate() {
        return new OCCCDate(this.dayOfMonth, this.monthOfYear, this.year);
    }



}
