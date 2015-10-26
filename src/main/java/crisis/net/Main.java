package crisis.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {

	public static String endPoint = "http://api.crisis.net/item?apikey=562e16afd15eddf7785a5a15";
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:SS'Z'");
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	static int resultLimit = 500;

	public static void usage() {

		System.out.println("Main starData endDate");
		System.out.println("Dates should have the formate yyyy-MM-dd");
	}

	public static void main(String[] args) {

		if (args.length != 2) {
			usage();
			System.exit(0);
		}

		File dis = new File("filtered");
		dis.mkdirs();

		Date startDate;
		Date endDate;
		try {
			startDate = formatter.parse(args[0]);
			endDate = formatter.parse(args[1]);
			// endDate = formatter.parse("2015-06-01");

			Calendar start = Calendar.getInstance();
			start.setTime(startDate);
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			for (Date date = start.getTime(); start.after(end); start.add(
					Calendar.DAY_OF_MONTH, -1), date = start.getTime()) {
				String IOSstring = df.format(date);
				JSONObject json = runJson(endPoint, date, 0);
				if (json == null) {
					continue;
				}

				if (json.has("total")) {
					int totalQueryContent = json.getInt("total");
					if (totalQueryContent == 0) {
						continue;
					}

					if (totalQueryContent <= resultLimit) {
						String day = formatter.format(date);
						PrintWriter writer = new PrintWriter(dis + "/" + day
								+ ".json", "UTF-8");
						writer.println(json.toString());
						writer.close();

					} else {

						for (int i = 0; i < totalQueryContent; i += resultLimit) {
							JSONObject jsonMultibe = runJson(endPoint, date, i);
							String day = formatter.format(date);
							int fragment;
							if (i == 0) {
								fragment = 1;
							} else {
								fragment = (i / resultLimit) + 1;
							}
							PrintWriter writer = new PrintWriter(dis + "/"
									+ day + "_" + fragment + ".json", "UTF-8");
							writer.println(jsonMultibe.toString());
							writer.close();
						}
					}
				}

			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static JSONObject runJson(String stringurl, Date date, int offset) {
		JSONObject json = null;

		try {
			String beforeString = formatter.format(date);
			Calendar end = Calendar.getInstance();
			end.setTime(date);
			end.add(Calendar.DAY_OF_MONTH, -1);
			String afterString = formatter.format(end.getTime());

			String urlWithParams = stringurl + "&after=" + afterString
					+ "&before=" + beforeString + "&offset=" + offset
					+ "&limit=" + resultLimit;

			System.out.println(urlWithParams);
			URL url = new URL(urlWithParams);
			URLConnection connection = url.openConnection();

			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			json = new JSONObject(builder.toString());

		} catch (IOException e) {
			e.printStackTrace();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}
