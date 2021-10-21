package com.example.exercise4calendar

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.exercise4calendar.adapter.ViewPagerAdapter
import com.example.exercise4calendar.fragments.CalendarFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    @RequiresApi(Build.VERSION_CODES.O)
    private var currentDate: LocalDate =LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    val fragmentList = mutableListOf(
        CalendarFragment.newInstance(currentDate.minusMonths(1)),
        CalendarFragment.newInstance(currentDate),
        CalendarFragment.newInstance(currentDate.plusMonths(1)),
    )
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPagerAdapter = ViewPagerAdapter(fragmentList,supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        viewPager.setCurrentItem(1, false)

        var focusPage = 0
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                focusPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (focusPage < 1) {
                        currentDate = currentDate.minusMonths(1)
                    } else if (focusPage > 1) {
                        currentDate = currentDate.plusMonths(1)
                    }
                    viewPagerAdapter.setCalendar(currentDate)
                    viewPager.setCurrentItem(1,false)
                }
            }
        })

    }
}