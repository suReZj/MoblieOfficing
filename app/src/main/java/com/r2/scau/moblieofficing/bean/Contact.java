package com.r2.scau.moblieofficing.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by 嘉进 on 11:27.
 */

public class Contact implements Comparable<Contact>, Parcelable {
    private String name;
    private String phone;
    private String firstLetter;
    private boolean isSelect;
    private String photoURL;



    public Contact(){
        isSelect = false;
    }
    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
        isSelect = false;
    }

    public Contact(String name, String phone, String firstLetter) {
        this.name = name;
        this.phone = phone;
        this.firstLetter = firstLetter;
        isSelect = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    @Override
    public int compareTo(@NonNull Contact contact) {
        if(this.firstLetter.compareTo(contact.getFirstLetter()) > 0){
            return 1;
        }

        if(this.firstLetter.compareTo(contact.getFirstLetter()) < 0){
            return -1;
        }

        return 0;
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>(){

        @Override
        public Contact createFromParcel(Parcel parcel) {
            return new Contact(parcel);
        }

        @Override
        public Contact[] newArray(int i) {
            return new Contact[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(firstLetter);
        parcel.writeByte((byte) (isSelect ? 1 : 0));
        parcel.writeString(photoURL);
    }

    public Contact(Parcel in){
        name = in.readString();
        phone = in.readString();
        firstLetter = in.readString();
        isSelect = in.readByte() != 0;
        photoURL = in.readString();
    }
}
