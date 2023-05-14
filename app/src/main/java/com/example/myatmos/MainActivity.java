package com.example.myatmos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myatmos.dao.ApiService;
import com.example.myatmos.database.UserDatabase;
import com.example.myatmos.entities.Station;
import com.example.myatmos.entities.User;
import com.example.myatmos.instance.ApiInstance;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public Context context;
    List<Station> StationsList = new ArrayList<>();
    public RecyclerView Stations;
    public LinearLayout NoData;
    public FloatingActionButton RefreshButton;
    public FloatingActionButton InfoButton;

    public ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onStart() {
        super.onStart();
        StationAsyncTask stationAsyncTasks = new StationAsyncTask();
        stationAsyncTasks.execute();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        NoData = findViewById(R.id.empty_view);
        NoData.setVisibility(View.GONE);

        User user = UserDatabase.getDb(context).userDao().list().isEmpty() ? null : UserDatabase.getDb(context).userDao().list().get(0);

        if(user == null) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        }

        Stations = findViewById(R.id.list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        Stations.setHasFixedSize(true);
        Stations.setLayoutManager(layoutManager);

        RefreshButton = findViewById(R.id.refresh);
        RefreshButton.setImageDrawable(getResources().getDrawable(R.drawable.reload));
        RefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationAsyncTask stationAsyncTasks = new StationAsyncTask();
                stationAsyncTasks.execute();
            }
        });

        InfoButton = findViewById(R.id.info);
        InfoButton.setImageDrawable(getResources().getDrawable(R.drawable.info));
        InfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, InfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    public void addClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://atmosroad.leopold-jacquet.com/device/new"));
        startActivity(browserIntent);
    }

    public class StationAsyncTask extends AsyncTask<Nullable, Nullable, List<Station>> {
        @Override
        public List<Station> doInBackground(Nullable... nullables) {

            User user = UserDatabase.getDb(context).userDao().list().isEmpty() ? null : UserDatabase.getDb(context).userDao().list().get(0);

            if(user == null) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                return null;
            }

            String email = user.getEmail();
            String password = user.getPassword();

            ApiService apiService = ApiInstance.getRetrofitInstance().create(ApiService.class);
            apiService.getDevices(email, password).enqueue(new Callback<List<Station>>() {
                @Override
                public void onResponse(@NonNull Call<List<Station>> call, @NonNull Response<List<Station>> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        if(response.code() == 200){
                            onPostExecute(StationsList = response.body());
                        }
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        if (response.body().isEmpty()) {
                            Stations.setVisibility(View.GONE);
                            NoData.setVisibility(View.VISIBLE);
                        }
                        else {
                            Stations.setVisibility(View.VISIBLE);
                            NoData.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Erreur",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<Station>> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }

            });

            return StationsList;
        }
        @Override
        protected void onPostExecute(List<Station> stations) {
            StationAdapter stationAdapter = new StationAdapter(stations);
            Stations.setAdapter(stationAdapter);
        }
    }
}