package com.mygallerydesign.sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mygallerydesign.sample.adapter.MyAdapter;
import com.mygallerydesign.sample.util.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.mygallerydesign.sample.util.Constants.REQUEST_CODE_PERMISSIONS;
import static com.mygallerydesign.sample.util.Utility.downloadImageFromServer;
import static com.mygallerydesign.sample.util.Utility.getPath;
import static com.mygallerydesign.sample.util.Utility.isNetworkAvailable;

public class MainActivity extends AppCompatActivity {
    private String mCurrentPhotoPath;
    private StorageReference mStorageRef;
    List<MyModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        list = new ArrayList<>();

        downloadImageFromServer();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        MyAdapter adapter = new MyAdapter(this, list);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void startCamera() {
        Intent cameraIntent = new
                Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 101);
    }

    public void addFirstImage(View view) {
        Utility.showDialog(MainActivity.this, getString(R.string.chooser), false);
    }

    public void captureImage(View view) {
        Utility.allPermissionsGranted(getApplicationContext(),MainActivity.this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 101 && resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                mCurrentPhotoPath = getPath(photo);

                if (isNetworkAvailable(this))
                    Utility.UploadImageToServer(mCurrentPhotoPath, null, mStorageRef);

                else
                    Toast.makeText(getApplicationContext(), R.string.internet_warning, Toast.LENGTH_SHORT).show();

            } else if (requestCode == 102 && resultCode == RESULT_OK) {
                Uri filePath = data.getData();
                Utility.UploadImageToServer(mCurrentPhotoPath, filePath, mStorageRef);
            }
        } catch (Exception e) {
        }
    }


    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 102);
    }
}
