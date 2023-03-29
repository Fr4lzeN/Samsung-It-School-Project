package com.example.bubble.registration;

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

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentLoadPictureBinding;
import com.example.bubble.mainMenu.MainActivity;
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
    List<Boolean> results;
    TakePictureRecyclerView adapter;
    boolean nextFragment = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLoadPictureBinding.inflate(inflater,container,false);
        adapter = new TakePictureRecyclerView(FillDataActivity.getData(), (data, position) -> {
            if (position==addPicture){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, addPicture);
            }
            else{
                onCreateDialog(position);
            }
        });
       binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
       binding.recyclerView.setAdapter(adapter);
       checkButton();
        binding.backButton.setOnClickListener(view -> {
                Navigation.findNavController(binding.getRoot()).popBackStack();
        });
        binding.nextButton.setOnClickListener(view -> {
                if (nextFragment){
                    sendUserData();
                }
                else{
                    Toast.makeText(getContext(), "Необходимо загрузить хотя бы одну фотографию", Toast.LENGTH_SHORT).show();
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
            }
            if (requestCode==changePicture){
                adapter.changeData(data.getData());
            }
            checkButton();
        }
    }

    public void onCreateDialog(int position){
        final  String[] actions = {"Изменить фото", String.valueOf(Html.fromHtml("<font color='#ff0000'>Удалить фото</font>"))};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(actions, (dialog, which) -> {
            switch (which){
                case 0:
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    adapter.setPictureChangePosition(position);
                    startActivityForResult(intent, changePicture);
                    break;
                case 1:
                    adapter.deleteData(position);
                    checkButton();
                    break;
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
    void sendUserData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        int[] dateOfBirth = FillDataActivity.getDateOfBirth();
        List<Uri> pictures = FillDataActivity.getData();
        results = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DBreference = database.getReference("userData").child(uid);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(FillDataActivity.getName())
                .setPhotoUri(FillDataActivity.getData().get(1)).build();

        user.updateProfile(profileUpdates).addOnCompleteListener(databaseListener);
        DBreference.setValue(new UserInfoJSON(FillDataActivity.getName(), FillDataActivity.getInfo(), FillDataActivity.getGender(), dateOfBirth[0], dateOfBirth[1], dateOfBirth[2])).addOnCompleteListener(databaseListener);
        for (int i = 1; i < pictures.size(); i++) {
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(uid + "/" + String.valueOf(i));
            reference.putFile(pictures.get(i)).addOnCompleteListener(storageListener);
        }
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loadPictureFragment_to_loadingFragment2);
    }
    OnCompleteListener<UploadTask.TaskSnapshot> storageListener = task -> {
        results.add(task.isSuccessful());
        checkResult(results);
    };

    OnCompleteListener<Void> databaseListener = task -> {
        results.add(task.isSuccessful());
        checkResult(results);
    };

    void checkResult(List<Boolean> results){
        for (int i=0; i<results.size(); i++){
            if (!results.get(i)){
                Log.d("Data", "error");
                Toast.makeText(getContext(), "Ошибка отправки данных", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (results.size()==FillDataActivity.getData().size()+1) {
            mainActivity();
        }
    }

    void mainActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}