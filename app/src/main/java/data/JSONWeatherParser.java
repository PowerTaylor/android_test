package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Utilitises.Utils;
import model.Place;
import model.Weather;

/**
 * Created by phill on 17/02/2017.
 */

public class JSONWeatherParser {

    public static Weather getWeather(String data) {

        Weather weather = new Weather();

        // Create JSONObject from data.
        try {
            JSONObject jsonObject = new JSONObject(data);
            Place place = new Place();

            // Get the Sys Object
            JSONObject sysObj = Utils.getObject("sys", jsonObject);
            place.setCountry(Utils.getString("country", sysObj));
            place.setLastupdated(Utils.getInt("dt", jsonObject));
            place.setCity(Utils.getString("name", jsonObject));
            weather.place = place;

            // Get the weather information. (JSON Array)
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            // Array is just one object so it can be treated like an object. Get the 1st and only object in array.
            // Get the weather from the API and then set segments of the array object to match its information.
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeather(Utils.getInt("id", jsonObject));
            weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));

            // Information from the main JSON to get the temperature, etc.
            JSONObject mainObj = Utils.getObject("main", jsonObject);
            weather.currentCondition.setTemperature(Utils.getDouble("temp", mainObj));

            // Return what is found.
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
