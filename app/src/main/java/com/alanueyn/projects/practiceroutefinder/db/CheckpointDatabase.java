package com.alanueyn.projects.practiceroutefinder.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {CheckpointEntity.class}, version = 1)
public abstract class CheckpointDatabase extends RoomDatabase{

    private static volatile CheckpointDatabase INSTANCE;

    public abstract CheckpointDao checkpointDao();

    public static CheckpointDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (CheckpointDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CheckpointDatabase.class,"checkpoints.db")
                            .build();
                }
            }
        }

        return INSTANCE;
    }

}
