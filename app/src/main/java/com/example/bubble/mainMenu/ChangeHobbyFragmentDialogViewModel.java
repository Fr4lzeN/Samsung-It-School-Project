package com.example.bubble.mainMenu;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.registration.HobbyRecyclerView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChangeHobbyFragmentDialogViewModel extends ViewModel {

    public MutableLiveData<Boolean> hobbiesDownloaded = new MutableLiveData<>();
    public MutableLiveData<Boolean> result = new MutableLiveData<>();
    public List<String> hobbyList = Arrays.asList("Автомобили", "Ароматерапия", "Астрономия", "Аэробика", "Аэрография", "Бадминтон", "Батик", "Батут", "Бег", "Бильярд", "Блоггерство", "Бодиарт", "Боевые искусства", "Бонсай", "Боулинг", "Велосипед", "Видеомонтаж", "Выращивание кристаллов", "Садоводство", "Вязание", "Гербарий", "Головоломки", "Гольф", "Горные лыжи", "Граффити", "Дайвинг", "Дартс", "Декупаж", "Дерево (выжигание и резьба)", "Диггерство", "Дизайн", "Животны", "Жонглирование", "Зентангл", "Музыка", "Игрушки и куклы", "Игры", "Кузнечное дело", "Программирование", "Икебана", "Иностранные языки", "Йога", "Исторические реконструкции", "Кайтинг", "Каллиграфия", "Карвинг", "Картинг и квадроциклы", "Квест-комнаты", "Кладоискательство", "Клубный отдых", "Коллекционирование", "Графика", "Коньки и ролики", "Косплей", "Кроссворды", "Кулинария", "Лазертаг", "Лепка", "Лошади (верховая езда, уход)", "Лыжи", "Массаж", "Моделирование", "Музеи и выставки", "Мыловарение", "Настольные игры", "Оригами", "Охота", "Паззлы", "Парашютный спорт", "Паркур", "Пейнтбол", "Пение и караоке", "Петанк", "Пикап", "Пилатес", "Писательсво", "Плавание", "Плетение", "Учеба", "Предпринимательство", "Психология и тренинги", "Путешествия", "Пчеловодство", "Радиовещание", "Рисование", "Робототехника", "Рукоделие", "Рыбалка", "Серфинг", "Силовые тренировки", "Скейтборд", "Скрапбукинг", "Сноуборд", "Спорт", "Страйкбол", "Стрельба", "Танцы", "Татуировки и пирсинг", "Театр", "Теннис", "Файер-шоу", "Фейерверки", "Фокусы", "Фотография и фотокниги", "Футбол", "Шитье и вышивание");
    ArrayList<Boolean> currentHobbies = new ArrayList<>(Collections.nCopies(hobbyList.size(), false));
    public MutableLiveData<HobbyRecyclerView> adapter = new MutableLiveData<>();


    public  void createAdapter(Activity activity){
        ChangeHobbyFragmentDialogModel.createAdapter(activity, hobbyList, currentHobbies, adapter);
    }


    public void getCurrentHobbies (){
        ChangeHobbyFragmentDialogModel.getCurrentHobbies(FirebaseAuth.getInstance().getUid(), hobbyList, currentHobbies, hobbiesDownloaded);
    }

    public void changeHobbies(){
        ChangeHobbyFragmentDialogModel.changeHobbies(hobbyList, currentHobbies, result);
    }


}
