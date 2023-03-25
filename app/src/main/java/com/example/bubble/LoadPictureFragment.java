package com.example.bubble;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.databinding.FragmentLoadPictureBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class LoadPictureFragment extends Fragment{

    FragmentLoadPictureBinding binding;
    int changePicture = -1;
    int addPicture = 0;
    TakePictureRecyclerView adapter;
    boolean nextFragment = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLoadPictureBinding.inflate(inflater,container,false);
        adapter = new TakePictureRecyclerView(FillDataActivity.getData(), new TakePictureRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(Uri data, int position) {
                if (position==addPicture){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, addPicture);
                }
                else{
                    onCreateDialog(position);
                }
            }
        });
       binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
       binding.recyclerView.setAdapter(adapter);
       checkButton();
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });
        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextFragment){
                    sendUserData();
                }
                else{
                    Toast.makeText(getContext(), "Необходимо загрузить хотя бы одну фотографию", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data!=null) {
            if (requestCode==addPicture){
                adapter.addData(data.getData());
                checkButton();
                return;
            }
            if (requestCode==changePicture){
                adapter.changeData(data.getData());
            }
        }
    }

    public void onCreateDialog(int position){
        final  String[] actions = {"Изменить фото", String.valueOf(Html.fromHtml("<font color='#ff0000'>Удалить фото</font>"))};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        adapter.setPictureChangePosition(position);
                        startActivityForResult(intent, changePicture);
                        break;
                    case 1:
                        adapter.deleteData(position);
                        break;
                }
            }
        }).show();
    }

    void checkButton(){
        if (adapter.getItemCount()>1){
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.green));
            nextFragment=true;
        }
        else{
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
            nextFragment=false;
        }
    }

    void mainActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    void sendUserData(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(FillDataActivity.getName())
                .setPhotoUri(FillDataActivity.getData().get(1)).build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("DataBase", "UserUpdateSuccess");
                    sendData(user.getUid());
                }
                else{
                    Log.d("DataBase", "UserUpdateFailture");
                    errorToast();
                }
            }
        });
    }

    void sendData(String uid){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("userData").child(uid);
        int[] dateOfBirth = FillDataActivity.getDateOfBirth();
        reference.push().setValue(new UserInfoJSON(FillDataActivity.getName(),FillDataActivity.getInfo(),FillDataActivity.getGender(), dateOfBirth[0], dateOfBirth[1], dateOfBirth[2])).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("DataBase", "UserJsonSuccess");
                    sendPicture(uid);
                }
                else{
                    Log.d("DataBase", "UserJsonFailture");
                    errorToast();
                }
            }
        });
    }

    void sendPicture(String uid){
        List<Uri> pictures = FillDataActivity.getData();
        List<Boolean> results = new ArrayList<>();
        for (int i=1; i<pictures.size(); i++) {
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(uid+"/"+String.valueOf(i));
            int finalI = i;
            reference.putFile(pictures.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    results.add(task.isSuccessful());
                    if (results.size()==pictures.size()-1){
                        checkResult(results);
                    }
                }
            });
        }
    }

    void checkResult(List<Boolean> results){
        for (int i=0; i<results.size(); i++){
            if (!results.get(i)){
                Log.d("DataBase", "pictures");
                errorToast();
                return;
            }
        }
        mainActivity();
    }

    void errorToast(){
        Toast.makeText(getContext(), "Ошибка отправки данных", Toast.LENGTH_SHORT).show();
    }

}