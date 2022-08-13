package com.rex1997.akb_uas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/*
Created at 12/08/2022
Created by Bina Damareksa (NIM: 10121702; Class: AKB-7)
*/

public class SplashViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    Button next,skip;
    private LinearLayout dotsLayout;
    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_view_pager);

        // Importing the intromanager class, which will check if this is the first time the application has been launched.
        IntroManager intromanager = new IntroManager(this);
        if(!intromanager.Check())
        {
            intromanager.setFirst(false);
            Intent i = new Intent(SplashViewPagerActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_splash_view_pager);

        //Objects
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        skip = findViewById(R.id.btn_skip);
        next = findViewById(R.id.btn_next);
        layouts = new int[]{R.layout.slider_1,R.layout.slider_2,R.layout.slider_3};
        addBottomDots(0);
        changeStatusBarColor();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);

        //For the next and previous buttons
        skip.setOnClickListener(view -> {
            Intent i = new Intent(SplashViewPagerActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        next.setOnClickListener(view -> {
            int current = getItem();
            if(current<layouts.length)
            {
                viewPager.setCurrentItem(current);
            }
            else
            {
                Intent i = new Intent(SplashViewPagerActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    //Giving the dots functionality
    private void addBottomDots(int position)
    {

        TextView[] dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for(int i = 0; i< dots.length; i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0)
            dots[position].setTextColor(colorActive[position]);
    }

    private int getItem()
    {
        return viewPager.getCurrentItem() + 1;
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener()
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
            if(position==layouts.length-1)
            {
                next.setText(R.string.proceed);
                skip.setVisibility(View.GONE);
            }
            else
            {
                next.setText(R.string.next);
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void changeStatusBarColor()
    {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
    //PagerAdapter class which will inflate our sliders in our ViewPager
    public class ViewPagerAdapter extends PagerAdapter
    {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup myContainer, int mPosition) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[mPosition],myContainer,false);
            myContainer.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View mView, @NonNull Object mObject) {
            return mView==mObject;
        }

        @Override
        public void destroyItem(ViewGroup mContainer, int mPosition, @NonNull Object mObject) {
            View v =(View)mObject;
            mContainer.removeView(v);
        }
    }
}