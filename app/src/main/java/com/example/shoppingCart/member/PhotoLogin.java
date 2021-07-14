package com.example.shoppingCart.member;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingCart.R;
import com.example.shoppingCart.network.RemoteAccess;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class PhotoLogin extends Fragment implements View.OnClickListener {
    private static final String TAG = "TAG_PhotoLoginFragment";
    private static final int REQ_TAKE_PICTURE_LARGE = 1;
    private Activity activity;
    private File file;
    private ImageView ivPhoto;
    private TextView tvLabelName, tvLabelAccuracy;
    private Button btTakePhotoTakePhoto, btTakePhotoComplete;
    private byte[] userPhoto;
    private String account;
    private Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btTakePhotoTakePhoto = view.findViewById(R.id.btTakePhotoTakePhoto);
        btTakePhotoComplete = view.findViewById(R.id.btTakePhotoComplete);
        ivPhoto = view.findViewById(R.id.ivPhoto);
        tvLabelName = view.findViewById(R.id.tvLabelName);
        tvLabelAccuracy = view.findViewById(R.id.tvLabelAccuracy);
        bundle = getArguments();

        btTakePhotoTakePhoto.setOnClickListener(this);
        btTakePhotoComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btTakePhotoTakePhoto) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            if (dir != null && !dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.d(TAG, "DIRECTORY_PICTURES無法被創建");
                    return;
                }
            }

            file = new File(dir, "picture.jpg");
            Uri contentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

            try {
                startActivityForResult(intent, REQ_TAKE_PICTURE_LARGE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, "找不到camera app", Toast.LENGTH_SHORT).show();
            }


            if (RemoteAccess.networkConnected(activity)) {
                String url = RemoteAccess.URL_SERVER + "Member";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "MemberPhotoLogin");

//                if (userPhoto != null) {
//                    jsonObject.addProperty("InsertImageBase64", Base64.encodeToString(userPhoto, Base64.DEFAULT));
//                    Log.d(TAG, "InsertImageBase64 SUCCESS!");
//                }
                String jsonIn = RemoteAccess.getRemoteData(url, jsonObject.toString());
                Map<String, Object> retMap = new Gson().fromJson(jsonIn, new TypeToken<HashMap<String, Object>>(){}.getType());

                String label = (String) retMap.get("Label");
                double accuracy = (double) retMap.get("Accuracy");

                tvLabelName.setText(label);
                tvLabelAccuracy.setText(NumberFormat.getNumberInstance().format(accuracy));

                Log.d(TAG, "tvLabelName: " + label);
                Log.d(TAG, "tvLabelAccuracy: " + accuracy);
            }


        } else if (v == btTakePhotoComplete) {
//            if (RemoteAccess.networkConnected(activity)) {
//                String url = RemoteAccess.URL_SERVER + "Member";
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("action", "MemberImageInsert");
//                jsonObject.addProperty("account", bundle.getString("account", null));
//                if (userPhoto != null) {
//                    jsonObject.addProperty("InsertImageBase64", Base64.encodeToString(userPhoto, Base64.DEFAULT));
//                    Log.d(TAG, "InsertImageBase64 SUCCESS!");
//                }
//                String result = RemoteAccess.getRemoteData(url, jsonObject.toString());
//                Log.d(TAG, "Insert result: " + result);
//            }
//            Navigation.findNavController(btTakePhotoComplete).navigate(R.id.login);
            Navigation.findNavController(btTakePhotoComplete).navigate(R.id.homepage);
            Toast.makeText(activity, "歡迎回來，" + tvLabelName.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_TAKE_PICTURE_LARGE) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    String text = String.format(Locale.getDefault(),
                            "source image size = %d x %d", width, height);
                    Log.d(TAG, text);
                } else {
                    ImageDecoder.OnHeaderDecodedListener listener = (decoder, info, source) -> {
                        String mimeType = info.getMimeType();
                        int width = info.getSize().getWidth();
                        int height = info.getSize().getHeight();
                        String text = String.format(Locale.getDefault(),
                                "mime type: %s; source image size = %d x %d",
                                mimeType, width, height);
                        Log.d(TAG, text);
                    };
                    ImageDecoder.Source source = ImageDecoder.createSource(file);
                    try {
                        Bitmap bitmap = ImageDecoder.decodeBitmap(source, listener);
                        ivPhoto.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        userPhoto = stream.toByteArray();
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                }
            }
        }
    }


}