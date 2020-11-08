package com.amoor.driver.ui.fragment.homeCycle;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amoor.driver.R;
import com.amoor.driver.adapter.NoteAdapter;

import com.amoor.driver.data.note.Note;
import com.amoor.driver.data.rest.ApiServices;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amoor.driver.data.rest.RetrofitClient.getClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {


    @BindView(R.id.note_fragment_recycler)
    RecyclerView noteFragmentRecycler;
    Unbinder unbinder;
    private ApiServices apiServices;
    private NoteAdapter adapter;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = getContext().getSharedPreferences("DriverData", Context.MODE_PRIVATE);
        String driver_id = preferences.getString("driver_id", "");

        apiServices = getClient().create(ApiServices.class);
        progressDialog = new ProgressDialog(getContext());


        getNotes(driver_id);

        adapter = new NoteAdapter(getContext());
        noteFragmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        noteFragmentRecycler.setAdapter(adapter);


        return view;
    }

    private void getNotes(String driver_id)
    {

        apiServices.getNotes(driver_id).enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response)
            {
                if (response.isSuccessful())
                {
                    List<Note> noteList = response.body();
                    adapter.setData(noteList);
                    adapter.notifyDataSetChanged();

                }else
                {
//                    Toast.makeText(getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
//                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
