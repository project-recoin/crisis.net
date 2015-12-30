package crisis.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.naming.StringRefAddr;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ToCsv {

	static final String delimate = ";";
	static String disFile;
	static String jsonDir;

	public static void main(String[] args) {

		if (args.length == 2) {
			disFile = args[0];
			jsonDir = args[1];
		} else {
			System.out.println("two args disFile jsonDir");
			System.exit(-1);
		}
		try {

			FileWriter writer = new FileWriter(disFile);
			writer.append("sep=" + delimate + "\n");
			writer.append("id;publishedAt;locationName;geo_long;geo_lat;tags;");
			writer.append("\n");
			// read the json file
			File folder = new File(jsonDir);
			File[] listOfFiles = folder.listFiles();
			for (int f = 0; f < listOfFiles.length; f++) {
				if (listOfFiles[f].isFile() && listOfFiles[f].getAbsoluteFile().toString().contains("json")) {
					System.out.println("Processing file " + listOfFiles[f]);
					FileReader reader = new FileReader(listOfFiles[f]);
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
					JSONArray data = (JSONArray) jsonObject.get("data");
					// take the elements of the json array
					for (int i = 0; i < data.size(); i++) {
						JSONObject doc = (JSONObject) data.get(i);
						if (doc != null) {
							String id = (String) doc.get("id");
							if (id != null) {
								writer.append(removeSpecialCharachters(id) + delimate);
							} else {
								writer.append(delimate);
							}

							String publishedAt = (String) doc.get("publishedAt");
							if (publishedAt != null) {
								writer.append(removeSpecialCharachters(publishedAt) + delimate);
							} else {
								writer.append(delimate);
							}

							// String content = (String) doc.get("content");
							// if (content != null) {
							// String subString = content;
							// if (subString.length() > 20000) {
							// subString = content.substring(0, 20000);
							// }
							// writer.append(removeSpecialCharachters(subString)
							// + delimate);
							// } else {
							// writer.append(delimate);
							// }

							JSONObject geo = (JSONObject) doc.get("geo");
							if (geo != null) {
								JSONObject addressComponents = (JSONObject) geo.get("addressComponents");
								if (addressComponents != null) {
									String formattedAddress = (String) addressComponents.get("formattedAddress");
									String adminArea1 = (String) addressComponents.get("adminArea1");
									if (formattedAddress != null) {
										writer.append(removeSpecialCharachters(formattedAddress) + delimate);
									} else if (adminArea1 != null) {
										writer.append(removeSpecialCharachters(adminArea1) + delimate);
									} else {
										writer.append(delimate);
									}
								} else {
									writer.append(delimate);
								}

								JSONArray coords = (JSONArray) geo.get("coords");
								if (coords != null) {
									if (coords.get(0) != null) {
										writer.append(removeSpecialCharachters(coords.get(0).toString()) + delimate);
									} else {
										writer.append(delimate);
									}
									if (coords.get(1) != null) {
										writer.append(removeSpecialCharachters(coords.get(1).toString()) + delimate);
									} else {
										writer.append(delimate);
									}
								} else {
									writer.append(delimate + delimate);
								}
							} else {
								writer.append(delimate + delimate + delimate);
							}

							JSONArray tags = (JSONArray) doc.get("tags");
							if (tags != null) {
								StringBuilder string1 = new StringBuilder();
								for (int j = 0; j < tags.size(); j++) {
									JSONObject tag = (JSONObject) tags.get(j);
									if (tag != null) {
										String name = (String) tag.get("name");
										string1.append(removeSpecialCharachters(name) + ",");
									} else {
										writer.append(delimate);
									}
								}
								if (string1.length() > 1) {
									string1.replace(string1.length() - 1, string1.length(), "");
								}
								writer.append(removeSpecialCharachters(string1.toString()) + delimate);
							} else {
								writer.append(delimate);
							}
							writer.append("\n");
						}

					}

				}

			}
			writer.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}

	}

	private static String removeSpecialCharachters(String source) {

		source = source.replaceAll("\"", "");
		source = source.replaceAll("'", "");
		source = source.replaceAll("\n", "");
		source = source.replaceAll(delimate, "");

		return source;
	}

}
