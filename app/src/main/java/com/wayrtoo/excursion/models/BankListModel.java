package com.wayrtoo.excursion.models;

import java.io.Serializable;

/**
 * Created by Ishwar on 23/12/2017.
 */
public class BankListModel implements Serializable{


    private String bank_id  ;
    private String bank_name;

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }
}

