package com.amoor.driver.ui.fragment.homeCycle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amoor.driver.R;
import com.amoor.driver.adapter.HomeAdapter;
import com.amoor.driver.data.model.tweet.Tweet;
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
public class HomeFragment extends Fragment {

    @BindView(R.id.home_fragment_recycler)
    RecyclerView homeFragmentRecycler;
    Unbinder unbinder;
    private ApiServices apiServices;
    private HomeAdapter adapter;
    private ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        apiServices = getClient().create(ApiServices.class);
        progressDialog = new ProgressDialog(getContext());

        getTweets();

        adapter = new HomeAdapter(getContext());
        homeFragmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        homeFragmentRecycler.setAdapter(adapter);




        return view;
    }

    private void getTweets()
    {
        apiServices.getTweets("بورسعيد - جامعة حورس").enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response)
            {
                if (response.isSuccessful())
                {
                    List<Tweet> tweetList = response.body();
                    adapter.setData(tweetList);
                    adapter.notifyDataSetChanged();

                }else
                {
//                    Toast.makeText(getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t)
            {
//                Toast.makeText(getContext(), "onFailure : "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
