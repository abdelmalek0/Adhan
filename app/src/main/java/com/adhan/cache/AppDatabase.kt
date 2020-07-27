package com.adhan.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.adhan.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(entities = [Prayer::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao
    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "Sample.db")
                // prepopulate the database after onCreate was called
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                            val PREPOPULATE_DATA = listOf(
                                Prayer(designation = "Fajr",name = context.resources.getString(R.string.salatSob7),time = "",uid = 0),
                                Prayer(designation = "Dhuhr",name = context.resources.getString(R.string.salatDohr),time = "",uid = 0),
                                Prayer(designation = "Asr",name = context.resources.getString(R.string.salat3asr),time = "",uid = 0),
                                Prayer(designation = "Maghrib",name = context.resources.getString(R.string.salatMaghreb),time = "",uid = 0),
                                Prayer(designation = "Isha",name = context.resources.getString(R.string.salatAishaa),time = "",uid = 0))
                            CoroutineScope(IO).launch {
                                for(i in PREPOPULATE_DATA){
                                    getInstance(context).prayerDao().insert(i)
                                }
                            }

                    }
                })
                .build()


    }
}