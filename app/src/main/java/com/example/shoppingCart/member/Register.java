package com.example.shoppingCart.member;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingCart.R;
import com.example.shoppingCart.bean.Member;
import com.example.shoppingCart.network.RemoteAccess;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Register extends Fragment {
    private static final String TAG = "TAG_RegisterFragment";
    private Activity activity;
    private EditText etNickname, etAccount, etPassword;
    private Button btNext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNickname = view.findViewById(R.id.etRegisterNickname);
        etAccount = view.findViewById(R.id.etRegisterAccount);
        etPassword = view.findViewById(R.id.etRegisterPassword);
        btNext = view.findViewById(R.id.btRegisterNext);

        initialize();

        btNext.setOnClickListener(v -> {
            String nickname = etNickname.getText().toString().trim();
            String account = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (nickname.length() <= 0) {
                Toast.makeText(activity, "請填入暱稱", Toast.LENGTH_LONG).show();
                return;
            } else if (account.length() <= 0 || password.length() <= 0) {
                Toast.makeText(activity, "請填入帳號或密碼", Toast.LENGTH_LONG).show();
                return;
            }

            if (RemoteAccess.networkConnected(activity)) {
                String url = RemoteAccess.URL_SERVER + "RegisterServlet";

                Member member = new Member(account, password, nickname, 1);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "MemberInsert");
                jsonObject.addProperty("member", new Gson().toJson(member));

                String result = RemoteAccess.getRemoteData(url, jsonObject.toString());
                Log.d(TAG, "result: " + result);
                int check = 1;
//                check = Integer.parseInt(result);
                Log.d(TAG, "check: " + check);

                if (check == 0) {
                    Toast.makeText(activity, "註冊失敗", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(activity, "註冊成功", Toast.LENGTH_LONG).show();
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    Navigation.findNavController(v).navigate(R.id.takePhoto);
                }
            } else {
                Toast.makeText(activity, "沒有網路連線", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void initialize() {
        etAccount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validAccount(etAccount);
            }

            public void validAccount(EditText et) {
                String etString = et.getText().toString().trim();
                if (etString == null) {
                    et.setError("無效的帳號");
                } else if (Patterns.EMAIL_ADDRESS.matcher(etString).matches()) {
                    et.setError("無效的帳號");
                }
            }
        });

    }
}