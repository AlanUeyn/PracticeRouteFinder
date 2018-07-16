package com.alanueyn.projects.practiceroutefinder.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CursorJoiner;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alanueyn.projects.practiceroutefinder.R;
import com.alanueyn.projects.practiceroutefinder.db.CheckpointEntity;
import com.alanueyn.projects.practiceroutefinder.util.Injection;
import com.alanueyn.projects.practiceroutefinder.viewmodel.CheckpointViewModel;
import com.alanueyn.projects.practiceroutefinder.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private ArrayList<CheckpointEntity> collection = new ArrayList<>(Arrays.asList(
            new CheckpointEntity("1-Невская губа",59.9783333333333,30.2166666666667),
            new CheckpointEntity("10-Невская губа",59.9016666666667,30.0866666666667),
            new CheckpointEntity("11-Невская губа",59.885,30.075),
            new CheckpointEntity("12-Невская губа",59.9883333333333,30.0033333333333),
            new CheckpointEntity("13-Невская губа",59.96,29.9816666666667),
            new CheckpointEntity("14-Невская губа",59.9083333333333,29.9366666666667),
            new CheckpointEntity("15-Невская губа",59.99,29.8583333333333),
            new CheckpointEntity("16-Невская губа",59.9583333333333,29.7966666666667),
            new CheckpointEntity("17-Невская губа",59.9366666666667,29.7883333333333),
            new CheckpointEntity("2-Невская губа",59.9666666666667,30.1666666666667),
            new CheckpointEntity("25-Невская губа",59.885,30.2183333333333)

    ));

    public static final String EXTRA = "EXTRA";
    public static final String TAG = MainActivity.class.getSimpleName();

    private List<CheckpointEntity> checkpoints = new ArrayList<>();
    private List<CheckpointEntity> selectedCheckpoints = new ArrayList<>();
    private CheckpointEntity c1,c2;

    Button mAddBtn;
    Button bButton;
    Button dButton;
    Button calcButton;


    private ViewModelFactory mViewModelFactory;
    private CheckpointViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddBtn = findViewById(R.id.add_btn);
        bButton = findViewById(R.id.beginning_spinner);
        dButton = findViewById(R.id.destination_spinner);
        calcButton = findViewById(R.id.calculate_btn);

        mViewModelFactory = Injection.provideViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CheckpointViewModel.class);


    }

    @Override
    public void onStart() {
        super.onStart();

        mDisposable.add(mViewModel.insertAllCheckpoints(collection)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> Toast.makeText(getApplicationContext(),"Data has been loaded!",Toast.LENGTH_SHORT).show(),
                            throwable -> Toast.makeText(getApplicationContext(),"Error loading!",Toast.LENGTH_SHORT).show())
                    );

        mDisposable.add(mViewModel.getCheckpoints()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setListOfData, throwable -> Log.e(TAG, "Unable to get checkpoints")));

        mAddBtn.setOnClickListener(v -> showMyDialog());
        bButton.setOnClickListener(v -> showBeginningDialog());
        dButton.setOnClickListener(v -> showDestinationDialog());
        calcButton.setOnClickListener(v -> startCalculating());

    }

    @Override
    protected void onStop() {
        super.onStop();

        mDisposable.clear();
        bButton.setText(R.string.start_point);
        dButton.setText(R.string.end_point);
        c1 = c2 = null;
        selectedCheckpoints.clear();
    }


    private void setListOfData(List<CheckpointEntity> entityList) {
        this.checkpoints = entityList;

    }

    private void startCalculating() {
        if(c1 == null || c2 == null) {
            Toast.makeText(getApplicationContext(), "Required fields is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        CheckpointEntity[] finalList = new CheckpointEntity[selectedCheckpoints.size() + 2];
        finalList[0] = c1;
        for(int i = 0; i < selectedCheckpoints.size(); ++i)
            finalList[i+1] = selectedCheckpoints.get(i);
        finalList[finalList.length - 1] = c2;
        Intent i = new Intent(MainActivity.this,MapsActivity.class);
        i.putExtra(EXTRA,finalList);
        startActivity(i);

    }

    private void showMyDialog() {

        int count = checkpoints.size();
        boolean checkedCheckpoints[] = new boolean[count];
        ArrayList<String> checkpointsName = new ArrayList<>();

        for(CheckpointEntity ce: checkpoints)
            checkpointsName.add(ce.getCheckpointName());


        for (int i = 0; i < count; ++i)
            checkedCheckpoints[i] = selectedCheckpoints.contains(checkpoints.get(i));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the checkpoints");
        builder.setMultiChoiceItems(checkpointsName.toArray(new String[checkpointsName.size()]), checkedCheckpoints, (dialog, which, isChecked) -> {

            ListView listView = ((AlertDialog)dialog).getListView();
            if (isChecked) {
                    if(c1.equals(checkpoints.get(which)) || c2.equals(checkpoints.get(which))) {
                        listView.setItemChecked(which,true);
                    }
                    else
                        selectedCheckpoints.add(checkpoints.get(which));

            } else {
                if(c1.equals(checkpoints.get(which)) || c2.equals(checkpoints.get(which))) {
                    listView.setItemChecked(which,true);
                } else
                    selectedCheckpoints.remove(checkpoints.get(which));
            }
        });
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Select all", (dialog, which) -> {
            ListView listView = ((AlertDialog)dialog).getListView();
            for (int i = 0; i < listView.getCount(); ++i)
            {
                listView.setItemChecked(i, true);

            }
            selectedCheckpoints.clear();
            for (CheckpointEntity ce: checkpoints) {
                if(ce != c1 && ce != c2)
                    selectedCheckpoints.add(ce);
            }

        });

        builder.show();

    }

    public void showBeginningDialog() {
        ArrayList<String> checkpointName = new ArrayList<>();

        for(CheckpointEntity ce: checkpoints)
            checkpointName.add(ce.getCheckpointName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the checkpoint");
        builder.setSingleChoiceItems(checkpointName.toArray(new String[checkpoints.size()]), 0, null)
                .setPositiveButton(R.string.ok_button, (dialog, which) -> {
                    dialog.dismiss();
                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                    c1 = checkpoints.get(selectedPosition);
                    bButton.setText(c1.getCheckpointName());
                }).show();

    }

    public void showDestinationDialog() {
        ArrayList<String> checkpointName = new ArrayList<>();

        for (CheckpointEntity ce : checkpoints)
            checkpointName.add(ce.getCheckpointName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the checkpoint");
        builder.setSingleChoiceItems(checkpointName.toArray(new String[checkpoints.size()]), 0, null)
                .setPositiveButton(R.string.ok_button, (dialog, which) -> {
                    dialog.dismiss();
                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    c2 = checkpoints.get(selectedPosition);
                    dButton.setText(c2.getCheckpointName());
                }).show();
    }
}
