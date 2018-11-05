package com.example.mediateka.presentation.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mediateka.presentation.main.MainActivity;


public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initToolbar();
        initViews();
        initDagger();
        initPresenter();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : {
                onBackPressed();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void navigateToMainActivity(Context context){
        Intent intent = new Intent(context , MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    protected void changeStatusBarColor(int color){
        if (isAboveLollipop()){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this , color));
        }
    }

    protected void initToolbar(){
        mToolbar = findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        if (mToolbar != null){
            setSupportActionBar(mToolbar);
        }
    }

    protected void showToast(String text){
        Toast.makeText(this , text , Toast.LENGTH_SHORT).show();
    }

    protected static boolean isAboveLollipop(){
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    }

    protected void addOffsetChangeListener(AppBarLayout appBarLayout , String title){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isVisible = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset <= 100) {
                    setCollapsingToolbarTitle(title);
                    isVisible = true;
                } else if(isVisible) {
                    setCollapsingToolbarTitle("");
                    isVisible = false;
                }
            }
        });
    }

    protected void setCollapsingToolbarTitle(String title){
        mCollapsingToolbarLayout.setTitle(title);
    }

    protected CollapsingToolbarLayout getCollapsingToolbarLayout(){
        return mCollapsingToolbarLayout;
    }

    protected Toolbar getToolbar(){
        return mToolbar;
    }

    protected abstract int getLayoutId();
    protected abstract void initViews();
    protected abstract void initPresenter();
    protected void initDagger(){/*/optional*/}
}
