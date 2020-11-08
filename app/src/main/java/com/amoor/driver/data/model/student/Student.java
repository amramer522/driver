
package com.amoor.driver.data.model.student;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student {

    @SerializedName("student aboard")
    @Expose
    private String studentAboard;

    public String getStudentAboard() {
        return studentAboard;
    }

    public void setStudentAboard(String studentAboard) {
        this.studentAboard = studentAboard;
    }

}
