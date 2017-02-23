package com.myweatherapp.phill.myweatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by phill on 17/02/2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

    // List created to take the city, temperature and last updated information.
    private Context mContext;
    private List<CityInformation> cityInformationList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, temp, update;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.city);
            temp = (TextView) view.findViewById(R.id.temperature);
            update = (TextView) view.findViewById(R.id.update);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public CityAdapter(Context mContext, List<CityInformation> cityInformationList) {
        this.mContext = mContext;
        this.cityInformationList = cityInformationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        return new MyViewHolder(itemView);
    }

    // Get the information on a city.
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CityInformation cityInformation = cityInformationList.get(position);
        holder.title.setText(cityInformation.getCity());
        holder.temp.setText(cityInformation.getTemp() + "°C");
        holder.update.setText(cityInformation.getUpdate());

        // Loading online image using Glide library
        try {
            Glide.with(mContext).load(cityInformation.getThumbnail()).into(holder.thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cityInformationList.size();
    }
}
