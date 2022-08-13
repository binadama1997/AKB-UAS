package com.rex1997.akb_uas;

import android.content.Context;
import android.content.SharedPreferences;

/*
Created at 12/08/2022
Created by Bina Damareksa (NIM: 10121702; Class: AKB-7)
*/

public class IntroManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    public IntroManager(Context context)
    {
        this.context=context;
        pref=context.getSharedPreferences("first",0);
        editor = pref.edit();
    }
    public void setFirst(boolean isFirst)
    {
        editor.putBoolean("check",isFirst);
        editor.commit();
    }
    public boolean Check()
    {
        return pref.getBoolean("check",true);
    }
}
