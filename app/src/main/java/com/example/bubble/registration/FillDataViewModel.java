package com.example.bubble.registration;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FillDataViewModel extends ViewModel{

    //Firebase
    MutableLiveData<FirebaseAuth> auth;
    MutableLiveData<FirebaseUser> user;
    MutableLiveData<DatabaseReference> database;
    MutableLiveData<StorageReference> storage;


    //User info
    MutableLiveData<String> name = new MutableLiveData<>();
    MutableLiveData<String> info = new MutableLiveData<>();
    MutableLiveData<String> gender = new MutableLiveData<>();
    MutableLiveData<Integer[]> dateOfBirth = new MutableLiveData<>();

    //Recycler view data
    MutableLiveData<List<Uri>> data = new MutableLiveData<>();
    List<String> hobbies = Arrays.asList("Автомобили", "Ароматерапия", "Астрономия", "Аэробика", "Аэрография", "Бадминтон", "Батик", "Батут", "Бег", "Бильярд", "Блоггерство", "Бодиарт", "Боевые искусства", "Бонсай", "Боулинг", "Велосипед", "Видеомонтаж", "Выращивание кристаллов", "Садоводство", "Вязание", "Гербарий", "Головоломки", "Гольф", "Горные лыжи", "Граффити", "Дайвинг", "Дартс", "Декупаж", "Дерево (выжигание и резьба)", "Диггерство", "Дизайн", "Животны", "Жонглирование", "Зентангл", "Музыка", "Игрушки и куклы", "Игры", "Кузнечное дело", "Программирование", "Икебана", "Иностранные языки", "Йога", "Исторические реконструкции", "Кайтинг", "Каллиграфия", "Карвинг", "Картинг и квадроциклы", "Квест-комнаты", "Кладоискательство", "Клубный отдых", "Коллекционирование", "Графика", "Коньки и ролики", "Косплей", "Кроссворды", "Кулинария", "Лазертаг", "Лепка", "Лошади (верховая езда, уход)", "Лыжи", "Массаж", "Моделирование", "Музеи и выставки", "Мыловарение", "Настольные игры", "Оригами", "Охота", "Паззлы", "Парашютный спорт", "Паркур", "Пейнтбол", "Пение и караоке", "Петанк", "Пикап", "Пилатес", "Писательсво", "Плавание", "Плетение", "Учеба", "Предпринимательство", "Психология и тренинги", "Путешествия", "Пчеловодство", "Радиовещание", "Рисование", "Робототехника", "Рукоделие", "Рыбалка", "Серфинг", "Силовые тренировки", "Скейтборд", "Скрапбукинг", "Сноуборд", "Спорт", "Страйкбол", "Стрельба", "Танцы", "Татуировки и пирсинг", "Театр", "Теннис", "Файер-шоу", "Фейерверки", "Фокусы", "Фотография и фотокниги", "Футбол", "Шитье и вышивание");
    List<Boolean> checked = new ArrayList<>(Collections.nCopies(hobbies.size(), false));
    //Recycler view action class
    MutableLiveData<TakePictureRecyclerView> pictureAdapter = new MutableLiveData<>();
    MutableLiveData<HobbyRecyclerView> hobbiesAdapter = new MutableLiveData<>();

    List<Boolean> results =new ArrayList<>();

    MutableLiveData<Boolean> allTasksSuccess = new MutableLiveData<>();


    OnCompleteListener<Void> createUserDBListener = task -> {
        results.add(task.isSuccessful());
    };

    OnCompleteListener<Void> updateUserInfoListener = task -> {
        results.add(task.isSuccessful());
        checkResult();
    };

    OnCompleteListener<UploadTask.TaskSnapshot> storageListener = task -> {
        Log.d("Storage", "Task result received");
        results.add(task.isSuccessful());
        checkResult();
    };


    public String getName() {
        return name.getValue();
    }

    public String getInfo() {
        return info.getValue();
    }

    public void clearResults(){
        results = new ArrayList<>();
        allTasksSuccess = new MutableLiveData<>();
    }

    public  void checkResult(){
       FirebaseResultEnum myEnum = FillDataModel.checkSuccess(results, data.getValue());
       switch (myEnum){
           case FAILTURE:
               allTasksSuccess.setValue(false);
               break;
           case WAITING:
               break;
           case SUCCESS:
               allTasksSuccess.setValue(true);
               break;
       }
    }

    public void initFirebase(){
        auth = new MutableLiveData<>(FirebaseAuth.getInstance());
        user = new MutableLiveData<>(auth.getValue().getCurrentUser());
        database = new MutableLiveData<>(FirebaseDatabase.getInstance().getReference());
        storage = new MutableLiveData<>(FirebaseStorage.getInstance().getReference());
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void setGender(String gender) {
        this.gender.setValue(gender);
    }

    public void setInfo(String info) {
        this.info.setValue(info);
    }

    public void setDateOfBirth(Integer[] dateOfBirth) {
        dateOfBirth[1]+=1;
        this.dateOfBirth.setValue(dateOfBirth);
    }

    public void sendUserInfo(){
        FillDataModel.createUser(user.getValue(), name.getValue(),
                data.getValue()).addOnCompleteListener(updateUserInfoListener);
    }

    public  void sendUser(){
        FillDataModel.createUserDB(database.getValue(),user.getValue().getUid(),
                name.getValue(), info.getValue(),
                gender.getValue(), dateOfBirth.getValue())
                .addOnCompleteListener(createUserDBListener);
    }

    public void sendPictures(){
        List<UploadTask> list = FillDataModel.sendPictures(storage.getValue(),
                user.getValue().getUid(), data.getValue());
        for (int i=0; i< list.size(); i++){
            list.get(i).addOnCompleteListener(storageListener);
        }
    }

    public void createAdapter(Activity activity){
        List<Uri> temp = new ArrayList<>();
        temp.add(FillDataModel.createAdapterData(activity));
        data.setValue(temp);
        pictureAdapter.setValue(FillDataModel.createAdapter(data.getValue()));
    }

    public void createHobbyAdapter(Activity activity){
        hobbiesAdapter.setValue(new HobbyRecyclerView(activity, hobbies, checked, (position, isChecked) -> {
            hobbiesAdapter.getValue().notifyItemChanged(position);
            Log.d("Hobby", hobbies.get(position)+" "+position+" "+ isChecked);
        }));
    }

    public void addPictureToAdapter(Intent data){
        this.data.setValue(FillDataModel.addPictureToAdapter(data));
    }

    public void changeAdapterPicture(Intent data, int position){
        this.data.setValue(FillDataModel.changeAdapterPicture(data, position));
    }

    public void deleteAdapterPicture(int position){
        data.setValue(FillDataModel.deleteAdapterPicture(position));
    }


    public void setHobbies() {
        FillDataModel.setHobbies(hobbies, checked);
    }
}
