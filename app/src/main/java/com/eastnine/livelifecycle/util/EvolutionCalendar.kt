package com.eastnine.livelifecycle.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class EvolutionCalendar(timeZone: TimeZone = TimeZone.getDefault()) : GregorianCalendar(timeZone) {
    enum class DateType(private val typeValue: Int) {
        NULL(0), PAST_MONTH(1), THIS_MONTH(2), NEXT_MONTH(3);

        fun getValue(): Int {
            return typeValue
        }
    }

    enum class DayOfTheWeek(private val dayNum: Int) {
        SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(7);

        fun getNum(): Int {
            return dayNum
        }
    }

    var dateType: DateType = DateType.NULL
    var isHoliday = false

    init {
        set(Calendar.HOUR_OF_DAY, 0)
    }

    var year: Int = get(Calendar.YEAR)
        set(value) {
            field = value

            set(Calendar.YEAR, value)
        }
        get() {
            return get(Calendar.YEAR)
        }

    var month: Int = get(Calendar.MONTH)
        set(value) {
            field = value

            set(Calendar.MONTH, value)
        }
        get() {
            return get(Calendar.MONTH)
        }

    var day: Int = get(Calendar.DATE)
        set(value) {
            field = value

            set(Calendar.DATE, value)
        }
        get() {
            return get(Calendar.DATE)
        }

    var hour: Int = get(Calendar.HOUR_OF_DAY)
        set(value) {
            field = value

            set(Calendar.HOUR_OF_DAY, value)
        }
        get() {
            return get(Calendar.HOUR_OF_DAY)
        }

    var minute: Int = get(Calendar.MINUTE)
        set(value) {
            field = value

            set(Calendar.MINUTE, value)
        }
        get() {
            return get(Calendar.MINUTE)
        }

    var second: Int = get(Calendar.SECOND)
        set(value) {
            field = value

            set(Calendar.SECOND, value)
        }
        get() {
            return get(Calendar.SECOND)
        }

    fun setDate(date: Date): EvolutionCalendar {
        timeInMillis = date.time
        return this
    }

    fun setDate(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0): EvolutionCalendar {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DATE, day)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, second)
        set(Calendar.MILLISECOND, 0)

        return this
    }

    fun setTime(hour: Int, minute: Int, second: Int): EvolutionCalendar {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, second)
        set(Calendar.MILLISECOND, 0)

        return this
    }

    fun resetTime() {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    fun getTimeString(): String {
        return "${get(Calendar.HOUR_OF_DAY)}:${get(Calendar.MINUTE)}:${get(Calendar.SECOND)}"
    }

    //해당 월 1일의 요일
    fun getFirstDayOfWeekToMonth(): Int {
        val dayOfYear = get(DAY_OF_MONTH) %7
        var firstDayOfWeek = get(DAY_OF_WEEK) - dayOfYear
        if(firstDayOfWeek < 0) {
            firstDayOfWeek += 8
        } else if(firstDayOfWeek > 6) {
            firstDayOfWeek = 1
        } else {
            firstDayOfWeek += 1
        }

        return firstDayOfWeek
    }

    //해당 달의 최대 일
    fun getMaximumDayOfMonth(): Int {
        return getActualMaximum(DAY_OF_MONTH)
    }

    //1월 1일의 요일
    fun getFirstDayOfWeekToYear(): Int {
        val tempCalendar = EvolutionCalendar(timeZone)
        tempCalendar.setDate(year, 0, 1)
        return tempCalendar.get(DAY_OF_WEEK)
    }

    //해당 년도 몇번째 주
    fun getWeekOfYear(): Int {
        val calibrationWeekOfYear = (getFirstDayOfWeekToYear() -1) + get(DAY_OF_YEAR)
        return ceil(calibrationWeekOfYear.toFloat() / 7f).toInt()
    }

    //12월31일의 요일
    fun getMaximumDayOfWeek(): Int {
        return getActualMaximum(DAY_OF_WEEK)
    }

    //해당 년도 주의 수
    fun getMaximumWeekOfYear(): Int {
        val calibrationWeekOfYear = getActualMaximum(DAY_OF_YEAR)
        return ceil(calibrationWeekOfYear.toDouble() / 7.toDouble()).toInt()
    }

    //해당 주의 몇번째 날짜 milliseconds
    fun getfirstDayOfWeekMilliseconds(): Long {
        val tempCalendar = EvolutionCalendar(timeZone)
        tempCalendar.setDate(year, month, day - getDayOfWeek() + 1)

        return tempCalendar.timeInMillis
    }

    //해당 주의 몇번째 날
    fun getDayOfWeek(): Int {
        return get(DAY_OF_WEEK)
    }

    //해당 달의 몇번째 주
    fun getWeekOfMonth(): Int {
        val dayOfMonth: Int = get(DAY_OF_MONTH)
        val lastDateOfFirstWeek = 7 -getFirstDayOfWeekToMonth() +1
        if(dayOfMonth <= lastDateOfFirstWeek) {
            return 1
        } else {
            return ceil((dayOfMonth -lastDateOfFirstWeek).toDouble() /7.0).toInt() +1
        }
    }

    //해당 달의 마지막 날짜
    fun getLastDayOfMonth(): Int {
        return getActualMaximum(DAY_OF_MONTH)
    }

    //해당 달의 주의 개수
    fun getWeeks(): Int {
        return ceil((getLastDayOfMonth() + getFirstDayOfWeekToMonth() -1).toFloat() / 7f).toInt()
    }

    //해당 년도 일 수
    fun getMaximumDayOfYear(): Int {
        var days = 0
        val tempCalendar = EvolutionCalendar(timeZone)

        for(month in 0 until 12) {
            tempCalendar.setDate(year, month, 1)
            days += tempCalendar.getMaximumDayOfMonth()
        }

        return days
    }

    //1주일 이동
    fun moveDay(i: Int): EvolutionCalendar {
        set(Calendar.DATE, day + i)
        return this
    }

    //1주일 이동
    fun moveWeeks(i: Int): EvolutionCalendar {
        set(Calendar.DATE, day + (7 * i))
        return this
    }

    //1달 이동
    fun moveMonth(i: Int): EvolutionCalendar {
        set(Calendar.MONTH, month + i)
        return this
    }

    //해당 주의 i번째 요일로 이동
    fun withDayOfWeek(i: Int): EvolutionCalendar {
        set(Calendar.DATE, day + (i - getDayOfWeek()))
        return this
    }

    //ZonedDateTime()
    fun getZonedDateTime(zoneDateStyle: String, locale: Locale = Locale.US): String {
        val dateFormat = SimpleDateFormat(zoneDateStyle, locale)
        dateFormat.timeZone = timeZone
        return dateFormat.format(timeInMillis)
    }

    override fun toString(): String {
        val decimalFormat = DecimalFormat()
        decimalFormat.applyPattern("00")
        return year.toString() + "-" + decimalFormat.format(month.toLong()) + "-" + decimalFormat.format(day.toLong())
    }

    override fun hashCode(): Int {
        return year *1000000 + month *100 + day
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (other is EvolutionCalendar) {
            if (other.year == year && other.month == month && other.day == day) {
                return true
            }
        }
        return false
    }
}