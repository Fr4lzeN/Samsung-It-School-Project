package com.example.bubble.mainMenu;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PeopleByHobbyFragmentModel {
    public static void getUsersByHobby(int start, int end, ArrayList<String> uids, MutableLiveData<Map<String, UserInfoJSON>> data) {

    }

    public static void getUids(MutableLiveData<List<String>> uids, String hobby) {
        FirebaseActions.getUserUidByHobby(hobby, uids);
    }

    public static void getUserByUid(List<String> uids, MutableLiveData<Map<String, UserInfoJSON>> data) {
        FirebaseActions.downloadUserInfo(uids, data);
    }

    public static void getData(Fragment fragment, String hobby, MutableLiveData<List<String>> uids, MutableLiveData<Map<String, UserInfoJSON>> data, MutableLiveData<PeopleListRecyclerView> adapter) {

        getUids(uids, hobby);

        uids.observe(fragment.getViewLifecycleOwner(), strings -> {
            if (strings!=null){
                getUserByUid(uids.getValue(), data);
            }
        });

        data.observe(fragment.getViewLifecycleOwner(), stringUserInfoJSONMap -> {
            adapter.setValue(new PeopleListRecyclerView(uids.getValue(), data.getValue(), uid -> {
                if (!uid.equals(FirebaseAuth.getInstance().getUid())){
                    replaceFragment(fragment, new UserProfileFragment(uid));
                }
                else{
                    replaceFragment(fragment, new MyProfileFragment());
                }
            }));
        });

    }

    private static void replaceFragment(Fragment current, Fragment next) {
        FragmentManager fragmentManager = current.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, next).addToBackStack(null);
        fragmentTransaction.commit();
    }

}
