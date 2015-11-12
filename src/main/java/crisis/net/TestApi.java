package crisis.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class TestApi {

	public static void main(String[] args) {

		try {
			URL url = new URL(
					"http://api.crisis.net/item?apikey=EMPTY&after=2015-06-03&before=2015-06-04");
			URLConnection connection = url.openConnection();
//			connection.addRequestProperty("offset", "8");
			connection.connect();
			
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			JSONObject json = new JSONObject(builder.toString());
			System.out.println(json);
			if (json.has("total")){
				System.out.println(json.get("total"));
			}

		} catch (IOException e) {

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
