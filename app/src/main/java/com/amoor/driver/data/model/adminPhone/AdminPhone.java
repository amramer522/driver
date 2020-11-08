
package com.amoor.driver.data.model.adminPhone;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminPhone {

    @SerializedName("mob")
    @Expose
    private String mob;

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

}
