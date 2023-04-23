package com.example.bubble;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PickPictureResultContract extends ActivityResultContract<Integer, Uri> {
    public PickPictureResultContract() {
        super();
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Integer integer) {
        return null;
    }

    @Nullable
    @Override
    public SynchronousResult<Uri> getSynchronousResult(@NonNull Context context, Integer input) {
        return super.getSynchronousResult(context, input);
    }

    @Override
    public Uri parseResult(int i, @Nullable Intent intent) {
        return null;
    }
}
