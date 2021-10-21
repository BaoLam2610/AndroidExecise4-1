package com.example.exercise4calendar.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.exercise4calendar.R
import com.example.exercise4calendar.adapter.CalendarAdapter
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val TAG = "CalendarFragment"
    private lateinit var adapter: CalendarAdapter
    lateinit var selectedDate: LocalDate
    var monthArr = mutableListOf<Int>()
    var weekdays = mutableListOf("SUN", "MON", "TUE", "WED", "THUR", "FRI", "SAT")

    @RequiresApi(Build.VERSION_CODES.O)
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstance(date: LocalDate): CalendarFragment {
            val args = Bundle()
            args.putSerializable("date", date)
            val fragment = CalendarFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var yearMonth: YearMonth? = null
    var daysInMonth: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedDate = arguments?.getSerializable("date") as LocalDate
        tvMonthYear.text = selectedDate.format(dateFormatter)
        yearMonth = YearMonth.from(selectedDate)
        daysInMonth = yearMonth!!.lengthOfMonth()
        initSpinnerWeekdays()
        rvDayOfMonth.layoutManager = GridLayoutManager(requireContext(), 7)
        rvDayOfMonth.addItemDecoration(DividerItemDecoration(
            requireContext(), DividerItemDecoration.VERTICAL
        ))
    }

    fun <T> MutableList<T>.rotate(distance: Int) =
        toList().also {
            Collections.rotate(it, distance)
        }

    var startWeekday = 0
    private fun initSpinnerWeekdays() {
        spWeekdays.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                startWeekday = position
                var temp = weekdays.rotate(-position)
                tvWeekday1.text = temp[0]
                tvWeekday2.text = temp[1]
                tvWeekday3.text = temp[2]
                tvWeekday4.text = temp[3]
                tvWeekday5.text = temp[4]
                tvWeekday6.text = temp[5]
                tvWeekday7.text = temp[6]

                monthArr = getDaysInMonth(selectedDate, startWeekday)
                adapter = CalendarAdapter(requireContext(), monthArr, daysInMonth)
                rvDayOfMonth.adapter = adapter
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDaysInMonth(date: LocalDate, start: Int): MutableList<Int> {
        var arr = mutableListOf<Int>()
        var yearMonth = YearMonth.from(date)
        var daysInMonth = yearMonth.lengthOfMonth()
        var daysInPreviousMonth = YearMonth.from(date).minusMonths(1).lengthOfMonth()
        var firstOfMonth = selectedDate.withDayOfMonth(1)
        var dayOfWeek = firstOfMonth.dayOfWeek.value - start
        var dayOfNextWeek = 1

        if (dayOfWeek != 7 && dayOfWeek >= 0)
            for (i in 1..(42)) {
                when {
                    i <= dayOfWeek -> {
                        arr.add(daysInPreviousMonth - dayOfWeek + i)
                    }
                    i - dayOfWeek <= daysInMonth -> {
                        arr.add(i - dayOfWeek)
                    }
                    i - dayOfWeek > daysInMonth -> {
                        arr.add(dayOfNextWeek)
                        dayOfNextWeek++
                    }

                }
            }
        else if (dayOfWeek == 7)
            for (i in 1..(42)) {
                when {
                    i <= daysInMonth -> {
                        arr.add(i)
                    }
                    i > daysInMonth -> {
                        arr.add(dayOfNextWeek)
                        dayOfNextWeek++
                    }
                }
            }
        else if (dayOfWeek < 0) {
            var d = 1
            for (i in 1..(42)) {
                when {
                    i <= 7 + dayOfWeek -> {
                        arr.add(daysInPreviousMonth - 7 + i - dayOfWeek)
                    }
                    i in 8 + dayOfWeek until (daysInMonth + 8 + dayOfWeek) -> {    // mon - i = 6
                        arr.add(d)
                        d++
                    }
                    else -> {
                        arr.add(dayOfNextWeek)
                        dayOfNextWeek++
                    }
                }
            }
        }
        return arr
    }
}