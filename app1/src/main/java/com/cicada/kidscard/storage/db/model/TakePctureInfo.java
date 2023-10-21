package com.cicada.kidscard.storage.db.model;

/**
 * FileName: TakePctureInfo
 * Author: Target
 * Date: 2020/8/6 2:55 PM
 */
public class TakePctureInfo {

    private String cardNumber;
    private String capPhotoPath;


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCapPhotoPath() {
        return capPhotoPath;
    }

    public void setCapPhotoPath(String capPhotoPath) {
        this.capPhotoPath = capPhotoPath;
    }
}
