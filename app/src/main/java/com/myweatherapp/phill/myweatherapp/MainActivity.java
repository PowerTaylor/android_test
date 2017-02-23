package com.myweatherapp.phill.myweatherapp;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CityAdapter adapter;
    private List<CityInformation> cityInformationList;

    Weather weather = new Weather();

    // Future aim/enhancement is to populate this information with the users input, not hard coded.
    // This is purely to get the API displaying cities to start.
    int c = 0;
    private final String cities[] = {
            "Reading, UK",
            "Madrid, SP",
            "Las Vegas, US",
            "Moscow, RU",
            "Paris, FR",
            "Berlin, GE",
            "Vostok, AN",
            "Melbourne, AU",
    };

    private String iconImage;
    private final String sunny = "https://cdn.pixabay.com/photo/2013/07/13/12/12/sun-159392_960_720.png";
    private final String rainy = "http://www.mikeafford.com/store/store-images/tv01_example_light_rain_showers.png";
    private final String cloudy = "http://cliparts.co/cliparts/8TA/bXj/8TAbXj5Ac.png";
    private final String snow = "http://www.coolkidfacts.com/wp-content/uploads/2015/01/cloud-306404_1280.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();

        // Finds the recycler view from the xml layout.
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // States the arraylist and adds the list of items that has been created.
        cityInformationList = new ArrayList<>();
        adapter = new CityAdapter(this, cityInformationList);

        // Sets the recycler view and sets the amount of grids in view.
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        // Sets the spaces between each grid segment.
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        // Sets the animation for when the application opens.
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Adds the array list of items to the recycler view.
        recyclerView.setAdapter(adapter);

        // Uses the Glide library to set the backdrop/cover image.
        String url = "http://www.hotel-r.net/im/hotel/fr/sun-beach-2.jpg";
        try {
            Glide.with(this).load(url).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int i;
        int c = 0;
        final int countCities = cities.length;
        // Loop created to load all weather and images in city array.
        for (i = 1; i <= countCities; i++) {
            renderWeather(cities[c]);
            c++;
        }
    }

    public void renderWeather(String city){
        WeatherTask weatherTask = new WeatherTask();
        // Creates a new weather task that ensure the city is metric.
        weatherTask.execute(city + "&units=metric");
    }

    // Ensure rendering is done in the background so the app is not slow.
    public class WeatherTask extends AsyncTask<String, Void, Weather> {

        // In the background the HTTP Client is ran to collect the data.
        @Override
        protected Weather doInBackground(String... params) {
            // Calling the Weather Client class to get the string of parameters from the API.
            String apidata = ((new WeatherHttpClient()).getWeatherData(params[0]));
            weather = JSONWeatherParser.getWeather(apidata);

            return weather;
        }

        // This populates the data after the background task has finished.
        //@RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            // Needs to be here so all the information is collected before displaying.
            prepareAlbums();
        }
    }

    // This method sets up the toolbar and collapses it when the user runs down the recycler view.
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void prepareAlbums() {

        // Date formatting required otherwise you will just get a long number.
        DateFormat df = DateFormat.getTimeInstance();

        // Change the temperature to an int.
        Double doubletoInt = weather.currentCondition.getTemperature();
        int currentTemp = Integer.valueOf(doubletoInt.intValue());

        String updates = "Last updated: " + df.format(new Date(weather.place.getLastupdated()));

        String weatherCondition = weather.currentCondition.getCondition();

        // Depending on the weather condition in the city, a different image is displayed.
        if (weatherCondition.equals("Clear")) {
            iconImage = sunny;
        }
        else if(weatherCondition.equals("Clouds")){
            iconImage = cloudy;
        }
        else if(weatherCondition.equals("Drizzle") || weatherCondition.equals("Rain")){
            iconImage = rainy;
        }
        else if(weatherCondition.equals("Snow")){
            iconImage = snow;
        }

        // Get the city information and add to the list, this method is looped which
        // changes the city in the array.
        CityInformation a = new CityInformation(cities[c], currentTemp, updates, iconImage);
        cityInformationList.add(a);
        c++;

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void showInput(){
        Toast.makeText(getApplicationContext(), "Future Enhancement",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Future enhancement to add cities to the Card View instead of having hard coded places.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.add_cityID){
            showInput();
        }
        return super.onOptionsItemSelected(item);
    }
}
