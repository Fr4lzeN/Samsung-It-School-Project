package com.example.bubble.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.bubble.R;
import com.example.bubble.UI.viewModels.FillDataViewModel;

public class FillDataActivity extends AppCompatActivity {

    static FillDataViewModel viewModel;

    public static FillDataViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_data);
        viewModel= new ViewModelProvider(this).get(FillDataViewModel.class);
        viewModel.createModel();
    }
}