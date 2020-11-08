package com.amoor.driver.data.rest;

import com.amoor.driver.data.model.adminPhone.AdminPhone;
import com.amoor.driver.data.model.busCapacity.BusCapacity;
import com.amoor.driver.data.model.busLineAndTime.BusData;
import com.amoor.driver.data.model.lines.Line;
import com.amoor.driver.data.model.login.Login;
import com.amoor.driver.data.model.map_students.MapStudents;
import com.amoor.driver.data.model.state.State;
import com.amoor.driver.data.model.student.Student;
import com.amoor.driver.data.model.tweet.Tweet;
import com.amoor.driver.data.note.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServices {


    // Login
    @POST("login.php")
    @FormUrlEncoded
    Call<Login> login(
            @Field("driver_id") String driver_id,
            @Field("password") String password);


    //get tweets
    @POST("map_all.php")
    @FormUrlEncoded
    Call<List<MapStudents>> getStudentsOnMap(
            @Field("driver_id") String driver_id
    );

    //get Students
    @POST("get_tweets.php")
    @FormUrlEncoded
    Call<List<Tweet>> getTweets(
            @Field("line") String line
    );




    //get notes
    @POST("note.php")
    @FormUrlEncoded
    Call<List<Note>> getNotes(
            @Field("driver_id") String driver_id
    );

    // state
    @POST("state.php")
    @FormUrlEncoded
    Call<State> setState(
            @Field("access_token") String access_token,
            @Field("state") String state
    );


    // count
    @POST("count.php")
    @FormUrlEncoded
    Call<Student> getStudentsNumOnBus(
            @Field("driver_id") String driver_id
    );

    // bus capacity
    @POST("get_bus_data.php")
    @FormUrlEncoded
    Call<BusCapacity> getBusCapacity(
            @Field("driver_id") String driver_id
    );

    //get bus lines
    @GET("all_route.php")
    Call<List<Line>> getBusLines();

    //get admin phone
    @GET("admin_mob.php")
    Call<AdminPhone> getAdminPhone();



    // bus capacity
    @POST("bus_data.php")
    @FormUrlEncoded
    Call<BusData> setBusTimeAndLine
    (
            @Field("line") String line,
            @Field("time") String time,
            @Field("driver_id") String driver_id
    );


}
