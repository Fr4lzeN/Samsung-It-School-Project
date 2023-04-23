package com.example.bubble.mainMenu;

import androidx.annotation.NonNull;

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
    public static void createAdapters() {
        List<String> friends = new ArrayList<>();
        List<String> outgoing = new ArrayList<>();
        List<String> incoming = new ArrayList<>();
        HashMap<String, FriendStatus> map = new HashMap<>();
        FirebaseActions.findUserFriends(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setMapValue(map, snapshot.getValue(new GenericTypeIndicator<HashMap<String, FriendStatus>>() {}));
                for (Map.Entry<String,FriendStatus> i : map.entrySet()){
                    switch (i.getValue()){
                        case INCOMING_REQUEST:
                            incoming.add(i.getKey());
                            break;
                        case FRIENDS:
                            friends.add(i.getKey());
                            break;
                        case OUTGOING_REQUEST:
                            outgoing.add(i.getKey());
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void setMapValue(HashMap<String, FriendStatus> map, HashMap<String, FriendStatus> data){
        map = data;
    }


}
