package com.mygallerydesign.sample;

import com.google.firebase.storage.StorageReference;

public class MyModel {
    private StorageReference mImgUrl;
    public MyModel(){

    }
    public MyModel(StorageReference imgUrl){
        mImgUrl=imgUrl;
    }

    public StorageReference getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(StorageReference imgUrl) {
        mImgUrl=imgUrl;
    }
}
