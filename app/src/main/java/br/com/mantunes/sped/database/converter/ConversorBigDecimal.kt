package br.com.mantunes.sped.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal
class ConversorBigDecimal {

    @TypeConverter
    fun paraBigDecimal(valor: BigDecimal?): String {
        return valor?.toString() ?: ""
    }

    @TypeConverter
    fun paraString(valor: String?): BigDecimal {
        return valor?.let { BigDecimal(it) } ?: BigDecimal.ZERO
    }

}
