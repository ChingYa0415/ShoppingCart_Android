package com.example.shoppingCart.member;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.shoppingCart.R;
import com.example.shoppingCart.bean.Member;
import com.example.shoppingCart.network.RemoteAccess;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import static android.content.Context.MODE_PRIVATE;

public class Login extends Fragment {
    private final static String TAG = "TAG_LoginFragment";
    private Activity activity;
    private EditText etAccount, etPassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etAccount = view.findViewById(R.id.etLoginAccount);
        etPassword = view.findViewById(R.id.etLoginPassword);

        view.findViewById(R.id.btLoginLogin).setOnClickListener(v -> {
            String account = etAccount.getText().toString();
            String password = etPassword.getText().toString();

            if (account.length() <= 0 || password.length() <= 0) {
                Toast.makeText(activity, "帳號或密碼為空值", Toast.LENGTH_SHORT).show();
                return;
            }

            Member member = new Member(account, password);
            if (RemoteAccess.networkConnected(activity)) {
                String url = RemoteAccess.URL_SERVER + "LoginServlet";
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD HH:mm:ss").create();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "memberLogin");
                jsonObject.addProperty("member", gson.toJson(member));

                String jsonIn = RemoteAccess.getRemoteData(url, jsonObject.toString());
                JsonObject jsonObjectIn = gson.fromJson(jsonIn, JsonObject.class);
                Log.d(TAG, "jsonObjectIn: " + jsonObjectIn);
                String result = jsonObjectIn.get("result").getAsString();

                switch (result) {
                    case "Fail":
                        Toast.makeText(activity, "帳號或密碼輸入錯誤", Toast.LENGTH_SHORT).show();
                        return;
                    case "OK":
                        String memberStr = jsonObject.get("member").getAsString();
                        Log.d(TAG, "memberStr: " + memberStr);
                        Member memberIn = gson.fromJson(memberStr, Member.class);
                        String nickname = memberIn.getNickname();
                        int id = memberIn.getId();
                        Log.d(TAG, "Nickname: " + nickname);
                        Log.d(TAG, "ID: " + id);

                        int status = memberIn.getStatus();
                        StringBuilder text = new StringBuilder();
                        text.append("歡迎回來")
                                .append(", ")
                                .append(nickname);
                        SharedPreferences pref = activity.getSharedPreferences("preferences", MODE_PRIVATE);
                        pref.edit()
                                .putBoolean("login", true)
                                .putInt("id", id)
                                .putString("nickname", nickname)
                                .putString("password", password)
                                .putInt("status", status)
                                .apply();

                        Log.d(TAG, "pref: " + pref.getString("nickname", "no values"));
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                        Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
                        Navigation.findNavController(v).navigate(R.id.userMember);
                }
            } else {
                Toast.makeText(activity, "無網路連線", Toast.LENGTH_LONG).show();
            }
        });

        view.findViewById(R.id.btLoginSignUp).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.register)
        );
    }
}