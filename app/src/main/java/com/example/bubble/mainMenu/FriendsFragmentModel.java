package com.example.bubble.mainMenu;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsFragmentModel {
    public static void createAdapters(Fragment fragment, MutableLiveData<PeopleListRecyclerView> friendsAdapter, MutableLiveData<PeopleListRecyclerView> outcomingAdapter, MutableLiveData<PeopleListRecyclerView> incomingAdapter) {
        List<String> friends = new ArrayList<>();
        List<String> outgoing = new ArrayList<>();
        List<String> incoming = new ArrayList<>();

        MutableLiveData<Map<String, UserInfoJSON>> friendsMap = new MutableLiveData<>();
        MutableLiveData<Map<String, UserInfoJSON>> outgoingMap = new MutableLiveData<>();
        MutableLiveData<Map<String, UserInfoJSON>> incomingMap = new MutableLiveData<>();

        MutableLiveData<Map<String,FriendStatus>> map = new MutableLiveData<>();
        FirebaseActions.getFriends(map);

        map.observe(fragment.getViewLifecycleOwner(), stringFriendStatusMap -> {
            if (stringFriendStatusMap!=null){
                for (Map.Entry<String,FriendStatus> i : stringFriendStatusMap.entrySet()){
                    switch (i.getValue()){
                        case FRIENDS:
                            friends.add(i.getKey());
                            break;
                        case OUTGOING_REQUEST:
                            outgoing.add(i.getKey());
                            break;
                        case INCOMING_REQUEST:
                            incoming.add(i.getKey());
                            break;
                    }
                }
                FirebaseActions.downloadUserInfo(friends, friendsMap);
                FirebaseActions.downloadUserInfo(outgoing, outgoingMap);
                FirebaseActions.downloadUserInfo(incoming, incomingMap);
                Log.d("CreateAdapters", outgoingMap.toString());
            }
        });

        friendsMap.observe(fragment.getViewLifecycleOwner(), stringUserInfoJSONMap ->
                friendsAdapter.setValue(new PeopleListRecyclerView(friends, stringUserInfoJSONMap, uid ->
                        replaceFragment(fragment, new UserProfileFragment(uid)))));

        outgoingMap.observe(fragment.getViewLifecycleOwner(), stringUserInfoJSONMap ->
                outcomingAdapter.setValue(new PeopleListRecyclerView(outgoing, stringUserInfoJSONMap, uid ->
                        replaceFragment(fragment, new UserProfileFragment(uid)))));

        incomingMap.observe(fragment.getViewLifecycleOwner(), stringUserInfoJSONMap ->
                incomingAdapter.setValue(new PeopleListRecyclerView(incoming, stringUserInfoJSONMap, uid ->
                        replaceFragment(fragment, new UserProfileFragment(uid)))));
    }


    private static void replaceFragment(Fragment current, Fragment next) {
        FragmentManager fragmentManager = current.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, next).addToBackStack(null);
        fragmentTransaction.commit();
    }

}
