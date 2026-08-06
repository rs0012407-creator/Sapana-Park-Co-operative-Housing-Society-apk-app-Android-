package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ComplaintEntity
import com.example.data.model.EmergencyContactEntity
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.MaintenanceBillEntity
import com.example.data.model.NoticeEntity
import com.example.data.model.ResidentDocumentEntity
import com.example.data.model.SocietyEventEntity
import com.example.data.model.SocietyMeetingEntity
import com.example.data.model.SuggestionEntity
import com.example.data.model.UserEntity
import com.example.data.model.UtilityPaymentEntity
import com.example.data.model.VehicleEntity

@Database(
    entities = [
        UserEntity::class,
        ResidentDocumentEntity::class,
        NoticeEntity::class,
        ComplaintEntity::class,
        MaintenanceBillEntity::class,
        SocietyEventEntity::class,
        FamilyMemberEntity::class,
        VehicleEntity::class,
        SuggestionEntity::class,
        UtilityPaymentEntity::class,
        SocietyMeetingEntity::class,
        EmergencyContactEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun societyDao(): SocietyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sapana_park_chs_db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
