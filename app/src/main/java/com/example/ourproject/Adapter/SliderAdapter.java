package com.example.ourproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ourproject.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MyHolder>{

    int[] images;

    public SliderAdapter(int[] images) {
        this.images = images;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder viewHolder, int position) {
        //viewHolder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class MyHolder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.imageView_ID);
        }
    }
}
