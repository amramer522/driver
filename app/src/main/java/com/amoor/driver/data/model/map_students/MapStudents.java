
package com.amoor.driver.data.model.map_students;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapStudents {

    @SerializedName("longtude")
    @Expose
    private String longtude;
    @SerializedName("langtude")
    @Expose
    private String langtude;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("line")
    @Expose
    private String line;

    public String getLongtude() {
        return longtude;
    }

    public void setLongtude(String longtude) {
        this.longtude = longtude;
    }

    public String getLangtude() {
        return langtude;
    }

    public void setLangtude(String langtude) {
        this.langtude = langtude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

}
