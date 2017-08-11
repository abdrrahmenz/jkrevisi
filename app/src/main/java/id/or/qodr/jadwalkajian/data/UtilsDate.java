package id.or.qodr.jadwalkajian.data;

import android.content.Context;

/**
 * Created by adul on 01/08/17.
 */

public class UtilsDate {

    private Context context;

    public UtilsDate(Context context) {
        this.context = context;
    }

    public String selectMonth(int monthAsInt) {
        String monthString = null;
        // the java switch/case statement
        switch (monthAsInt) {
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:
                monthString = "March";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "June";
                break;
            case 7:
                monthString = "July";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "October";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "December";
                break;
            default:
                monthString = "None";
        }
        return monthString;
    }

    public String selectPekan(String montAsString) {
        String monthString = null;
        // the java switch/case statement
        switch (montAsString) {
            case "01":
                monthString = "January";
                break;
            case "02":
                monthString = "February";
                break;
            case "03":
                monthString = "Maret";
                break;
            case "04":
                monthString = "April";
                break;
            case "05":
                monthString = "May";
                break;
            case "06":
                monthString = "Juni";
                break;
            case "07":
                monthString = "July";
                break;
            case "08":
                monthString = "Agustus";
                break;
            case "09":
                monthString = "September";
                break;
            case "10":
                monthString = "October";
                break;
            case "11":
                monthString = "November";
                break;
            case "12":
                monthString = "December";
                break;
            default:
                monthString = "None";
        }
        return monthString;
    }

    public String monthPekan(String montAsString) {
        String monthString = null;
        // the java switch/case statement
        switch (montAsString) {
            case "01":
                monthString = "Jan";
                break;
            case "02":
                monthString = "Feb";
                break;
            case "03":
                monthString = "Mar";
                break;
            case "04":
                monthString = "Apr";
                break;
            case "05":
                monthString = "May";
                break;
            case "06":
                monthString = "Jun";
                break;
            case "07":
                monthString = "Jul";
                break;
            case "08":
                monthString = "Aug";
                break;
            case "09":
                monthString = "Sep";
                break;
            case "10":
                monthString = "Oct";
                break;
            case "11":
                monthString = "Nov";
                break;
            case "12":
                monthString = "Dec";
                break;
            default:
                monthString = "None";
        }
        return monthString;
    }

    public String showDayByID(int dayID) {
        String stringDay = null;

        switch (dayID) {
            case 1:
                stringDay = "Ahad";
                break;
            case 2:
                stringDay = "Senin";
                break;
            case 3:
                stringDay = "Selasa";
                break;
            case 4:
                stringDay = "Rabu";
                break;
            case 5:
                stringDay = "Kamis";
                break;
            case 6:
                stringDay = "Jumat";
                break;
            case 7:
                stringDay = "Sabtu";
                break;
        }
        return stringDay;
    }
}
