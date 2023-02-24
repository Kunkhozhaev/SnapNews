package ru.nurdaulet.news.data.database

import androidx.room.TypeConverter
import ru.nurdaulet.news.domain.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}