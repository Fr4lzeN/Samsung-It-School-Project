package com.example.bubble.mainMenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bubble.databinding.SearchHobbyItemListBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SearchHobbiesRecyclerView extends RecyclerView.Adapter<SearchHobbiesRecyclerView.SearchHobbiesViewHolder> {

    public  interface OnItemClick {
        void onClick(String hobby);
    }

    SearchHobbyItemListBinding binding;
    ArrayList<String> data = new ArrayList<>(Arrays.asList("Автомобили", "Ароматерапия", "Астрономия", "Аэробика", "Аэрография", "Бадминтон", "Батик", "Батут", "Бег", "Бильярд", "Блоггерство", "Бодиарт", "Боевые искусства", "Бонсай", "Боулинг", "Велосипед", "Видеомонтаж", "Выращивание кристаллов", "Садоводство", "Вязание", "Гербарий", "Головоломки", "Гольф", "Горные лыжи", "Граффити", "Дайвинг", "Дартс", "Декупаж", "Дерево (выжигание и резьба)", "Диггерство", "Дизайн", "Животны", "Жонглирование", "Зентангл", "Музыка", "Игрушки и куклы", "Игры", "Кузнечное дело", "Программирование", "Икебана", "Иностранные языки", "Йога", "Исторические реконструкции", "Кайтинг", "Каллиграфия", "Карвинг", "Картинг и квадроциклы", "Квест-комнаты", "Кладоискательство", "Клубный отдых", "Коллекционирование", "Графика", "Коньки и ролики", "Косплей", "Кроссворды", "Кулинария", "Лазертаг", "Лепка", "Лошади (верховая езда, уход)", "Лыжи", "Массаж", "Моделирование", "Музеи и выставки", "Мыловарение", "Настольные игры", "Оригами", "Охота", "Паззлы", "Парашютный спорт", "Паркур", "Пейнтбол", "Пение и караоке", "Петанк", "Пикап", "Пилатес", "Писательсво", "Плавание", "Плетение", "Учеба", "Предпринимательство", "Психология и тренинги", "Путешествия", "Пчеловодство", "Радиовещание", "Рисование", "Робототехника", "Рукоделие", "Рыбалка", "Серфинг", "Силовые тренировки", "Скейтборд", "Скрапбукинг", "Сноуборд", "Спорт", "Страйкбол", "Стрельба", "Танцы", "Татуировки и пирсинг", "Театр", "Теннис", "Файер-шоу", "Фейерверки", "Фокусы", "Фотография и фотокниги", "Футбол", "Шитье и вышивание"));

    OnItemClick listener;

    public SearchHobbiesRecyclerView(OnItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchHobbiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SearchHobbyItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchHobbiesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHobbiesViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SearchHobbiesViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public SearchHobbiesViewHolder(SearchHobbyItemListBinding binding) {
            super(binding.getRoot());
            textView = binding.textView;
        }

        public void onBind(String hobby) {
            textView.setOnClickListener(v -> listener.onClick(hobby));
        }
    }
}
