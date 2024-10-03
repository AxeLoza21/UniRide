package com.example.uniride.fragments;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatListFragment extends Fragment {
    private RecyclerView recyclerView;
    //private ChatListAdapter adapter;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String currentUserId;

}
