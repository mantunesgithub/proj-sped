package br.com.mantunes.sped.extensions

import java.text.SimpleDateFormat
import java.time.Period
import java.util.*
import java.util.concurrent.TimeUnit

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun exibeDataHoje() {
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())
}
//Convertendo uma data para uma string formatada
fun Date.dateToString(format: String = "yyyy-MM-dd HH:mm:ss"): String =
    SimpleDateFormat(format, Locale.getDefault()).format(this)

//Convertendo uma string formatada para data
fun String.stringToDate(format: String = "yyyy-MM-dd HH:mm:ss"): Date =
    SimpleDateFormat(format, Locale.getDefault()).parse(this)

//Recuperando a diferença entre dois dados
//Diferença em horas
infix fun Date.difHoursTo(dateTo: Date): Long =
    TimeUnit.MILLISECONDS.toHours(dateTo.time - this.time)

//Diferença em minutos
infix fun Date.difMinutesTo(dateTo: Date): Long =
    TimeUnit.MILLISECONDS.toMinutes(dateTo.time - this.time)

//Diferença em segundos
infix fun Date.difSecondsTo(dateTo: Date): Long =
    TimeUnit.MILLISECONDS.toSeconds(dateTo.time - this.time)

//Adicionando anos, meses, dias, horas, minutos ou segundos em uma data
//Função de extensão usada demais
fun Date.add(field: Int, amount: Int): Date {
    Calendar.getInstance().apply {
        time = this@add
        add(field, amount)
        return time
    }
}
//Adicionando anos
fun Date.addYears(years: Int): Date =
    add(Calendar.YEAR, years)

//Adicionando meses
fun Date.addMonths(months: Int): Date =
    add(Calendar.MONTH, months)

//Adicionando dias
fun Date.addDays(days: Int): Date =
    add(Calendar.DAY_OF_MONTH, days)

//Adicionando horas
fun Date.addHours(hours: Int): Date =
    add(Calendar.HOUR_OF_DAY, hours)

//Adicionando minutos
fun Date.addMinutes(minutes: Int): Date =
    add(Calendar.MINUTE, minutes)

//Adicionando segundos
fun Date.addSeconds(seconds: Int): Date =
    add(Calendar.SECOND, seconds)