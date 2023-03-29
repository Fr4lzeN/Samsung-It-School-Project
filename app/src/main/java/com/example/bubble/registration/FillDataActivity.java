package com.example.bubble.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import com.example.bubble.R;

import java.util.ArrayList;
import java.util.List;

public class FillDataActivity extends AppCompatActivity {

   static String name,info,gender;
    static int[] dateOfBirth;
    static List<Uri> data;
    static public void clear(){
        name=null;
        info = null;
        gender = null;
        dateOfBirth = null;
        data = null;
    }


   static public void setName(String name) {
        FillDataActivity.name = name;
    }

  static public void setInfo(String info) {
      FillDataActivity.info = info;
    }

    static public void setGender(String gender) {
        FillDataActivity.gender = gender;
    }

    static public void setData(List<Uri> data) {
        FillDataActivity.data = data;
    }

    static public String getName() {
        return name;
    }

    static public String getInfo() {
        return info;
    }

    static public String getGender() {
        return gender;
    }

    static public int[] getDateOfBirth() {
        return dateOfBirth;
    }

    static public List<Uri> getData() {
        return data;
    }

    public static void setDateOfBirth(int year, int month, int dayOfMonth) {
       dateOfBirth = new int[]{year, month, dayOfMonth};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_data);
        if (data==null) {
            data = new ArrayList<>();
            data.add(new Uri.Builder()
                    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .authority(getResources().getResourcePackageName(R.drawable.add_picture))
                    .appendPath(getResources().getResourceTypeName(R.drawable.add_picture))
                    .appendPath(getResources().getResourceEntryName(R.drawable.add_picture))
                    .build());
        }
    }
}