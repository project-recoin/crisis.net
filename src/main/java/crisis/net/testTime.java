package crisis.net;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class testTime {

	public static void main(String[] args) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:SS'Z'");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate;
		Date endDate;
		try {
			startDate = formatter.parse("2015-10-26");
			endDate = formatter.parse("2015-09-26");

			Calendar start = Calendar.getInstance();
			start.setTime(startDate);
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			for (Date date = start.getTime(); start.after(end); start.add(
					Calendar.DAY_OF_MONTH, -1), date = start.getTime()) {
				// Do your job here with `date`.
				String IOSstring = df.format(date);
				System.out.println(IOSstring);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
