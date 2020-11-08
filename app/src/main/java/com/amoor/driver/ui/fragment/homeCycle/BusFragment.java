package com.amoor.driver.ui.fragment.homeCycle;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amoor.driver.R;
import com.amoor.driver.data.model.adminPhone.AdminPhone;
import com.amoor.driver.data.model.busCapacity.BusCapacity;
import com.amoor.driver.data.model.busLineAndTime.BusData;
import com.amoor.driver.data.model.lines.Line;
import com.amoor.driver.data.model.state.State;
import com.amoor.driver.data.model.student.Student;
import com.amoor.driver.data.rest.ApiServices;
import com.amoor.driver.data.rest.RetrofitClient;
import com.amoor.driver.helper.HelperMethod;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amoor.driver.helper.HelperMethod.getTextFromSpinner;
import static com.amoor.driver.helper.HelperMethod.setSpinnerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusFragment extends Fragment {


    @BindView(R.id.bus_fragment_switch)
    Switch busFragmentSwitch;
    @BindView(R.id.note_fragment_bus_line)
    Spinner noteFragmentBusLine;
    Unbinder unbinder;
    @BindView(R.id.bus_fragment_available_student)
    TextView busFragmentAvailableStudent;
    @BindView(R.id.bus_fragment_rest_student)
    TextView busFragmentRestStudent;
    @BindView(R.id.bus_fragment_bus_capacity)
    TextView busFragmentBusCapacity;
    @BindView(R.id.Bus_Fragment_Tv_driver_name)
    TextView BusFragmentTvDriverName;
    @BindView(R.id.note_fragment_tv_time_moving)
    TextView noteFragmentTvTimeMoving;
    private ApiServices apiServices;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String mob;
    private String driver_id;

    public BusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bus, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = getContext().getSharedPreferences("DriverData", Context.MODE_PRIVATE);
        apiServices = RetrofitClient.getClient().create(ApiServices.class);

        String access_token = preferences.getString("access_token", "");
        String driver_state = preferences.getString("driver_state", "");
        driver_id = preferences.getString("driver_id", "");
        String driver_name = preferences.getString("driver_name", "");
        BusFragmentTvDriverName.setText(BusFragmentTvDriverName.getText() + "  " + driver_name);
        if (driver_state.equals("on")) {
            busFragmentSwitch.setChecked(true);
        } else if (driver_state.equals("off")) {
            busFragmentSwitch.setChecked(false);
        }
        setState(access_token);
        getNumbersAndSetThemToTextView(driver_id);
        getLines();

        return view;
    }

    private void getLines() {
        apiServices.getBusLines().enqueue(new Callback<List<Line>>() {
            @Override
            public void onResponse(Call<List<Line>> call, Response<List<Line>> response) {
                if (response.isSuccessful()) {
                    List<Line> busLineList = response.body();
                    String[] lines = new String[busLineList.size()];

                    for (int i = 0; i < busLineList.size(); i++)
                    {
                        lines[i] = busLineList.get(i).getRoute();
                    }

                    setSpinnerAdapter(getActivity(), lines, noteFragmentBusLine);

                } else {
//                    Toast.makeText(getContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Line>> call, Throwable t) {
//                Toast.makeText(getContext(), "onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNumbersAndSetThemToTextView(final String driver_id) {
        apiServices.getStudentsNumOnBus(driver_id).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    int studentsOnBus = Integer.parseInt(response.body().getStudentAboard());
                    busFragmentAvailableStudent.setText(studentsOnBus + "");
                    getBusCapacity(driver_id, studentsOnBus);

                } else {
//                    Toast.makeText(getContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
//                Toast.makeText(getContext(), "onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBusCapacity(String driver_id, final int studentsOnBus) {
        apiServices.getBusCapacity(driver_id).enqueue(new Callback<BusCapacity>() {
            @Override
            public void onResponse(Call<BusCapacity> call, Response<BusCapacity> response) {
                if (response.isSuccessful()) {
                    String busCapacity1 = response.body().getBusCapacity();
                    if (busCapacity1 != null) {
                        int busCapacity = Integer.parseInt(response.body().getBusCapacity());
                        int availableRequests = busCapacity - studentsOnBus;
                        busFragmentBusCapacity.setText(busCapacity + "");
                        busFragmentRestStudent.setText(availableRequests + "");
                    }

                } else {
//                    Toast.makeText(getContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BusCapacity> call, Throwable t) {
//                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setState(final String access_token) {
        busFragmentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = preferences.edit();
                if (isChecked) {
                    editor.putString("driver_state", "on");
                    setStateRequest(access_token, "on");

                    Toast.makeText(getContext(), "on", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("driver_state", "off");

                    setStateRequest(access_token, "off");
                    Toast.makeText(getContext(), "off", Toast.LENGTH_SHORT).show();

                }
                editor.apply();
            }
        });

    }

    private void setStateRequest(String access_token, String driver_state) {
        apiServices.setState(access_token, driver_state).enqueue(new Callback<State>() {
            @Override
            public void onResponse(Call<State> call, Response<State> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getContext(), "" + response.message(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<State> call, Throwable t) {
//                Toast.makeText(getContext(), "onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.bus_fragment_call, R.id.bus_fragment_btn_save, R.id.note_fragment_tv_time_moving})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bus_fragment_call:
                getAdminPhone();

                break;
            case R.id.bus_fragment_btn_save:
                String line = getTextFromSpinner(noteFragmentBusLine);
                if (!noteFragmentTvTimeMoving.getText().toString().contains(":"))
                {
                    Toast.makeText(getContext(), "حدد وقت التحرك الاول", Toast.LENGTH_SHORT).show();
                }else
                {
                    sendTimeAndLine(noteFragmentTvTimeMoving.getText().toString(), line);
                }

                break;
            case R.id.note_fragment_tv_time_moving:
                HelperMethod.showTimeDialog(getActivity(),noteFragmentTvTimeMoving);
                break;

        }
    }

    private void getAdminPhone() {
        apiServices.getAdminPhone().enqueue(new Callback<AdminPhone>() {
            @Override
            public void onResponse(Call<AdminPhone> call, Response<AdminPhone> response) {
                if (response.isSuccessful()) {
                    mob = response.body().getMob();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + mob));
//                    Toast.makeText(getContext(), ""+mob, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
//                    Toast.makeText(getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AdminPhone> call, Throwable t) {
//                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendTimeAndLine(String time, String line)
    {
//        Toast.makeText(getContext(), ""+driver_id, Toast.LENGTH_SHORT).show();
        if (driver_id!=null)
        {
            apiServices.setBusTimeAndLine(line,time,driver_id).enqueue(new Callback<BusData>() {
                @Override
                public void onResponse(Call<BusData> call, Response<BusData> response)
                {
                    if (response.isSuccessful())
                    {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }else
                    {
                        Toast.makeText(getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<BusData> call, Throwable t)
                {

                }
            });

        }


    }


}
