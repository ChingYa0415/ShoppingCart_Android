package com.example.shoppingCart.member;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingCart.R;
import com.example.shoppingCart.network.RemoteAccess;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class TakePhoto extends Fragment implements View.OnClickListener {
    private static final String TAG = "TAG_TakePhotoFragment";
    private static final int REQ_TAKE_PICTURE_LARGE = 1;
    private Activity activity;
    private File file;
    private Button btTakePhoto, btComplete;
    private ImageView ivPhoto;
    private byte[] userPhoto;
    private Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_take_photo, container, false);
    }

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btTakePhoto = view.findViewById(R.id.btTakePhotoTakePhoto);
        btComplete = view.findViewById(R.id.btTakePhotoComplete);
        ivPhoto = view.findViewById(R.id.ivPhoto);
        bundle = getArguments();

        btTakePhoto.setOnClickListener(this);
        btComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btTakePhoto) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (dir != null && !dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.d(TAG, "DIRECTORY_PICTURES無法被創建");
                    return;
                }
            }
            file = new File(dir, "picture.jpg");
            Uri contentUri = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            try {
                startActivityForResult(intent, REQ_TAKE_PICTURE_LARGE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, "找不到camera app", Toast.LENGTH_SHORT).show();
            }
        } else if (v == btComplete) {
            if (RemoteAccess.networkConnected(activity)) {
                String url = RemoteAccess.URL_SERVER + "Member";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "MemberImageInsert");
                jsonObject.addProperty("account", bundle.getString("account", null));
                if (userPhoto != null) {
                    jsonObject.addProperty("InsertImageBase64", Base64.encodeToString(userPhoto, Base64.DEFAULT));
                    Log.d(TAG, "InsertImageBase64 SUCCESS!");
                }
                String result = RemoteAccess.getRemoteData(url, jsonObject.toString());
                Log.d(TAG, "Insert result: " + result);
            }
            Navigation.findNavController(btComplete).navigate(R.id.login);
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