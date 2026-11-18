package gabrieljs2005.com.github.caminholivreapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        BarrierEntity::class,
        GuardianProgressEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class CaminhoLivreDatabase : RoomDatabase() {

    abstract fun barrierDao(): BarrierDao
    abstract fun guardianProgressDao(): GuardianProgressDao

    companion object {
        private const val DATABASE_NAME = "caminho_livre.db"

        @Volatile
        private var INSTANCE: CaminhoLivreDatabase? = null

        fun getInstance(context: Context): CaminhoLivreDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CaminhoLivreDatabase::class.java,
                    DATABASE_NAME,
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

