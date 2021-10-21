package com.example.exercise4calendar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.exercise4calendar.R
import kotlinx.android.synthetic.main.item_day_of_month.view.*
import java.util.*


class CalendarAdapter(
    var mContext: Context,
    var dayOfMonth: MutableList<Int>,
    var month: Int,
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_day_of_month,
                parent,
                false
            )
        )
    }

    var clicked = false
    var currentPosition = 0
    var checkSingle = false
    var checkDouble = false
    var tempPosition = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(
        holder: CalendarViewHolder,
        @SuppressLint("RecyclerView") position: Int,
    ) {
        holder.itemView.apply {
            tvDay.text = dayOfMonth[position].toString()
            changeTextColorToDefault(position, tvDay)
            changeBackgroundToDefault(position,tvDay)
            if (clicked) {
                if (position == currentPosition) {
                    if (!checkSingle && !checkDouble) {
                        background = null
                        if (currentPosition != tempPosition) {
                            tvDay.setBackgroundResource(R.drawable.custom_selected_day)
                            changeTextColorToDefault(position, holder.itemView.tvDay)
                        }
                    } else if (checkSingle)
                        tvDay.setBackgroundResource(R.drawable.custom_selected_day)
                    else if (checkDouble) {
                        var rnd = Random()
                        val color = Color.argb(255,
                            rnd.nextInt(256),
                            rnd.nextInt(256),
                            rnd.nextInt(256))
                        val drawable = DrawableCompat.wrap(
                            ContextCompat.getDrawable(mContext, R.drawable.custom_selected_day)!!)
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            drawable.colorFilter = BlendModeColorFilterCompat
                                .createBlendModeColorFilterCompat(
                                    color,
                                    BlendModeCompat.SRC_ATOP
                                )
//                        } else{
//                            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
//                        }
                        tvDay.background = drawable
                    }
                } else {
                    background = null
                }
            }
            val gestureDetector =
                GestureDetector(mContext, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {

                        tempPosition = currentPosition
                        currentPosition = position
                        clicked = true
                        checkSingle = !checkSingle
                        if (checkDouble) {
                            checkDouble = false
                            checkSingle = false
                        }
                            notifyDataSetChanged()
                        return true
                    }

                    override fun onDoubleTap(e: MotionEvent?): Boolean {

                        tempPosition = currentPosition

                        currentPosition = position
                        clicked = true
                        if (!checkDouble) {
                            checkDouble = !checkDouble
                            checkSingle = false
                        }
                            notifyDataSetChanged()

                        return true
                    }
                })
            setOnTouchListener { _, motionEvent ->
                gestureDetector.onTouchEvent(motionEvent)
                true
            }
        }
    }

    private fun changeTextColorToDefault(position: Int, textView: TextView) {
        if(dayOfMonth.indexOf(1) < dayOfMonth.lastIndexOf(month))
            if (position >= dayOfMonth.indexOf(1) && position <= dayOfMonth.lastIndexOf(month))
                textView.setTextColor(Color.BLACK)
            else
                textView.setTextColor(Color.GRAY)
        else
            if (position <= dayOfMonth.lastIndexOf(month))
                textView.setTextColor(Color.BLACK)
            else
                textView.setTextColor(Color.GRAY)
    }

    private fun changeBackgroundToDefault(position: Int, textView: TextView) {
        if(dayOfMonth.indexOf(1) < dayOfMonth.lastIndexOf(month))
            if (position < dayOfMonth.indexOf(1) || position > dayOfMonth.lastIndexOf(month))
                textView.background = null
        else
            if (position < dayOfMonth.lastIndexOf(month))
                textView.background = null
    }

    override fun getItemCount(): Int = dayOfMonth.size

}