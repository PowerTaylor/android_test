package Utilitises;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by phill on 17/02/2017.
 */

// Utility class created to hold the API information.
public class Utils {

    // OpenWeatherMap API string required to get weather information on a city. Access requires key.
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static final String APP_ID = "&appid=ef5388eb9224accbdb11bace49e0eb52";

    // API uses JSON so a JSONObject needs to be created.
    public static JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException {
        JSONObject jObj = jsonObject.getJSONObject(tagName);
        return jObj;
    }

    public static String getString(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }

    public static double getDouble(String tagName, JSONObject jsonObject) throws JSONException {
        return (float) jsonObject.getDouble(tagName);
    }

    public static int getInt(String tagName, JSONObject jsonObject) throws JSONException {
        return  jsonObject.getInt(tagName);
    }
}