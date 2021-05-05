package com.example.shoppingcart.member;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.shoppingcart.R;
import com.example.shoppingcart.bean.Member;

public class UserMember extends Fragment {
    SharedPreferences preferences;
    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        statusCheck();
    }

    public void statusCheck() {
        preferences = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        int status = preferences.getInt("status", -1);

        if (status == -1) {
            Toast.makeText(activity, "請先登入", Toast.LENGTH_LONG).show();
            Navigation.findNavController(activity, R.id.main_fragment).navigate(R.id.login);
        }
    }
}
