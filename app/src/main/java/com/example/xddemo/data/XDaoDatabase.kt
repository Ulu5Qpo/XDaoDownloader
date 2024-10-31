package com.example.xddemo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.xddemo.data.dao.ThreadDao
import com.example.xddemo.data.model.ReplyEntity
import com.example.xddemo.data.model.ThreadEntity

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE INDEX index_threads_content ON threads(content)")
        db.execSQL("CREATE INDEX index_replies_threadId ON replies(threadId)")
        db.execSQL("CREATE INDEX index_replies_content ON replies(content)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 添加 isDownloaded 字段到 threads 表
        db.execSQL("ALTER TABLE threads ADD COLUMN isDownloaded INTEGER NOT NULL DEFAULT 0")
    }
}

@Database(entities = [ThreadEntity::class, ReplyEntity::class], version = 3, exportSchema = false)
abstract class XDaoDatabase : RoomDatabase() {

    abstract fun threadDao(): ThreadDao

    companion object {
        @Volatile
        private var Instance: XDaoDatabase? = null

        fun getDatabase(context: Context): XDaoDatabase {
            return Instance ?: synchronized(this) {
                Instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    XDaoDatabase::class.java,
                    "xdao_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build().also { Instance = it }
            }
        }
    }
}