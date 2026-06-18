package com.laksh.jarvismusic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Added the new entities and changed version to 2
@Database(entities = [LikedSong::class, Playlist::class, PlaylistSong::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    // 2. Added the Playlist command center
    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jarvis_music_db"
                )
                    .fallbackToDestructiveMigration() // 3. Prevents crashes when adding new tables!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}