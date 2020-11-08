
package com.amoor.driver.data.model.busCapacity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusCapacity {

    @SerializedName("bus_capacity")
    @Expose
    private String busCapacity;

    public String getBusCapacity() {
        return busCapacity;
    }

    public void setBusCapacity(String busCapacity) {
        this.busCapacity = busCapacity;
    }

}
