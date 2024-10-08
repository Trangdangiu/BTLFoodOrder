package com.example.testbotom.slide_photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.testbotom.R;

import java.util.List;

public class Photo_Adapter extends PagerAdapter {

    private Context mContext;
    private List<Photo>mListphoto;

    public Photo_Adapter(Context mContext, List<Photo> mListphoto) {
        this.mContext = mContext;
        this.mListphoto = mListphoto;
    }

    @Override
    public int getCount() {
        if(mListphoto!=null){
            return mListphoto.size();

        }
        return 0;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.items_photo,container,false);

        ImageView imgphoto= view.findViewById(R.id.img_photo);
        Photo photo=mListphoto.get(position);
        if(photo!=null){
            Glide.with(mContext).load(photo.getResourceId()).into(imgphoto);

        }
        container.addView(view);
       return  view;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }
}
