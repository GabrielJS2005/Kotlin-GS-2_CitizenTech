package gabrieljs2005.com.github.caminholivreapp.data.local

import androidx.room.TypeConverter
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierCategory
import gabrieljs2005.com.github.caminholivreapp.domain.model.BarrierStatus

class Converters {

    @TypeConverter
    fun fromBarrierCategory(category: BarrierCategory): String = category.label

    @TypeConverter
    fun toBarrierCategory(label: String): BarrierCategory = BarrierCategory.fromLabel(label)

    @TypeConverter
    fun fromBarrierStatus(status: BarrierStatus): String = status.label

    @TypeConverter
    fun toBarrierStatus(label: String): BarrierStatus = BarrierStatus.fromLabel(label)
}

