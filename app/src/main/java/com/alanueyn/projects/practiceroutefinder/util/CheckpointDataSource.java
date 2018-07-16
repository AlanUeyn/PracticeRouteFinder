package com.alanueyn.projects.practiceroutefinder.util;

import com.alanueyn.projects.practiceroutefinder.db.CheckpointEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface CheckpointDataSource {


    Flowable<List<CheckpointEntity>> getCheckpoints();

    void insertCheckpoint(CheckpointEntity checkpointEntity);

    void insertAll(ArrayList<CheckpointEntity> checkpoints);

    void deleteCheckpoint(CheckpointEntity checkpoint);

    void deleteAll();


}
