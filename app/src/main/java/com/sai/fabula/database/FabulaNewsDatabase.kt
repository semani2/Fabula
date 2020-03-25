package com.sai.fabula.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sai.fabula.database.dao.ArticlesDao
import com.sai.fabula.database.model.Article

@Database(
    entities = [Article::class],
    version = FabulaNewsDatabase.DB_VERSION
)
abstract class FabulaNewsDatabase : RoomDatabase() {

    abstract fun getArticlesDao(): ArticlesDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "fabula_database"

        @Volatile
        private var INSTANCE: FabulaNewsDatabase? = null

        fun getInstance(context: Context): FabulaNewsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FabulaNewsDatabase::class.java,
                    DB_NAME
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
