package com.mygallerydesign.sample.util;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.view.Window;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mygallerydesign.sample.MyModel;
import com.mygallerydesign.sample.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static com.mygallerydesign.sample.util.Constants.REQUEST_CODE_PERMISSIONS;

public class Utility {

    public static void showDialog(Activity activity, String msg,boolean cancel){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);

        TextView text = dialog.findViewById(R.id.txt_dia);
        text.setText(msg);

        dialog.findViewById(R.id.btn_yes);
        dialog.findViewById(R.id.btn_no);

        dialog.show();

    }

    public static boolean isNetworkAvailable(Context context) {
        //Method to check if network is connected or not. Returns true if connected, else false
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void downloadImageFromServer() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child("images/");

        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        MyModel myModel = new MyModel();
                        myModel.setImgUrl(item);
                    }
                })
                .addOnFailureListener(e -> {
                    // Uh-oh, an error occurred!
                });
    }

    public static void UploadImageToServer(String mCurrentPhotoPath, final Uri filePath, StorageReference mStorageRef) throws FileNotFoundException {
        Uri file;
        if (mCurrentPhotoPath == null){
            file = filePath;
        }else {
            file = Uri.fromFile(new File(mCurrentPhotoPath));
        }
        StorageReference riversRef = mStorageRef.child("images/" + file.getLastPathSegment());

        riversRef.putFile(file)
                .addOnSuccessListener(taskSnapshot -> {
                    Utility.downloadImageFromServer();
                })
                .addOnFailureListener(exception -> {
                    System.out.println("exception : " + exception);
                });

    }

    public static String getPath(Bitmap photo) {
        String mCurrentPhotoPath = "";
        FileOutputStream outStream;
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/camtest");
            dir.mkdirs();

            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            File outFile = new File(dir, fileName);

            outStream = new FileOutputStream(outFile);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            mCurrentPhotoPath = outFile.getAbsolutePath().toString();

            outStream.flush();
            outStream.close();

        } catch (Exception e) {
        }
        return mCurrentPhotoPath;
    }

    public static void allPermissionsGranted(Context context, Activity activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    }, REQUEST_CODE_PERMISSIONS);
        }
    }


}
