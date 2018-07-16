package com.alanueyn.projects.practiceroutefinder.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

@Dao
public interface CheckpointDao {

    @Query("SELECT * FROM CheckpointEntity")
    Flowable<List<CheckpointEntity>> getListOfCheckpoints();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCheckpoint(CheckpointEntity... checkpointEntities);

    @Delete
    void deleteCheckpoint(CheckpointEntity checkpoint);

    @Query("DELETE FROM CheckpointEntity")
    void deleteAllCheckpoints();

}
