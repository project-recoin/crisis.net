package crisis.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class TestApi2 {

	static String http = "http://api.crisis.net/item?apikey=EMPTY";

	public static void main(String[] args) {
		HttpURLConnection urlConnection = null;
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(http);
			
			System.out.println(url);
			urlConnection = (HttpURLConnection) url.openConnection();
//			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
//			urlConnection.setRequestMethod("GET");
//			 urlConnection.setUseCaches(false);
			// urlConnection.setConnectTimeout(10000);
			// urlConnection.setReadTimeout(10000);
//			urlConnection
//					.setRequestProperty("Content-Type", "application/json");

			urlConnection.setRequestProperty("limit", "2");

			// Create JSONObject here
			

			int HttpResult = urlConnection.getResponseCode();
			System.out.println(HttpResult);
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();

				System.out.println("" + sb.toString());

			} else {
				System.out.println("some error");
				System.out.println(urlConnection.getResponseMessage());
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

}
