package com.example.bubble.mainMenu;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.example.bubble.JSON.MessageListItem;
import com.example.bubble.MessageListComparator;
import com.example.bubble.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class MessagesFragmentModel {

    @SuppressLint("CheckResult")
    public static void createAdapter(Fragment fragment, MutableLiveData<MessageListAdapter> adapter) {
        PublishSubject<Map<String,String>> messageMap = PublishSubject.create();
        PublishSubject<MessageListItem> messageData = PublishSubject.create();
        Map<String,String> map = new HashMap<>();
        List<MessageListItem> data = new ArrayList<>();
        messageMap.subscribe(
                message -> {
                    for (Map.Entry<String, String> i : message.entrySet()) {
                        map.put(i.getKey(), i.getValue());
                    }
                    messageMap.onComplete();
                },
                t -> {

                },
                () -> {
                    FirebaseActions.getMessagesList(map, messageData);
                }
        );
        messageData.subscribe(data::add,
                throwable -> {

                },
                () -> {
                    Collections.sort(data, new MessageListComparator());
                    adapter.setValue(new MessageListAdapter(data, (userInfo, messageId, uid) ->
                            replaceFragment(fragment, new MessageFragment(uid, userInfo, messageId))));
                });
        FirebaseActions.getUserMessages(FirebaseAuth.getInstance().getUid(), messageMap);
    }

    private static void replaceFragment(Fragment current, Fragment next) {
        FragmentManager fragmentManager = current.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, next).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
