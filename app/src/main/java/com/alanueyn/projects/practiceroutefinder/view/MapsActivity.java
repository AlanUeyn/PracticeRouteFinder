package com.alanueyn.projects.practiceroutefinder.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.alanueyn.projects.practiceroutefinder.R;
import com.alanueyn.projects.practiceroutefinder.db.CheckpointEntity;
import com.alanueyn.projects.practiceroutefinder.logic.Checkpoint;
import com.alanueyn.projects.practiceroutefinder.logic.GeneticAlgorithm;
import com.alanueyn.projects.practiceroutefinder.logic.Population;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.SquareCap;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String EXTRA = "EXTRA";
    private GoogleMap mMap;
    private Population population;
    private GeneticAlgorithm geneticAlgorithm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        CheckpointEntity[] entities = new CheckpointEntity[getIntent().getParcelableArrayExtra(EXTRA).length];
        for (int i = 0; i < entities.length; ++i) {
            entities[i] = (CheckpointEntity) getIntent().getParcelableArrayExtra(EXTRA)[i];
        }

        ArrayList<Checkpoint> initialRoute = new ArrayList<>();
        for (CheckpointEntity entity : entities) {

            Checkpoint c = new Checkpoint(entity.getCheckpointLong(), entity.getCheckpointLat(), entity.getCheckpointName());
            initialRoute.add(c);

        }

        population = new Population(GeneticAlgorithm.POPULATION_SIZE,initialRoute);
        population.sortRoutesByFitness();
        geneticAlgorithm = new GeneticAlgorithm(initialRoute);
        AsyncCalculate asyncCalculate = new AsyncCalculate();
        asyncCalculate.execute();



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<Checkpoint> checkpoints = population.getRoutes().get(0).getCheckpoints();

        for(Checkpoint ce: checkpoints){
            ce.setLatitude(ce.getLatitude()*Checkpoint.CONVERT_RADIANS_TO_DEGREES);
            ce.setLongtitude(ce.getLongtitude()*Checkpoint.CONVERT_RADIANS_TO_DEGREES);
        }

        ArrayList<LatLng> latLngs = new ArrayList<>();

        for(Checkpoint ce: checkpoints)
            latLngs.add(new LatLng(ce.getLatitude(),ce.getLongtitude()));

        PolylineOptions options = new PolylineOptions().addAll(latLngs)
                .startCap(new RoundCap())
                .endCap(new SquareCap())
                .width(3)
                .color(Color.MAGENTA)
                .geodesic(true);

        mMap.addPolyline(options);

        for(int i = 0; i < latLngs.size(); ++i)
            mMap.addMarker(new MarkerOptions().title(checkpoints.get(i).getName()).position(latLngs.get(i)));


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0),10));
    }


    private void calculate() {
        int generationNumber = 0;
        while (generationNumber++ < GeneticAlgorithm.NUM_OF_GENERATIONS) {
            population = geneticAlgorithm.evolve(population);
            population.sortRoutesByFitness();
        }
    }

    class AsyncCalculate extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            calculate();
            return null;
        }
    }


}
