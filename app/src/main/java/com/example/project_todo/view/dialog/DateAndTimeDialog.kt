package com.example.project_todo.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.CalendarView
import android.widget.TableLayout
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import com.example.project_todo.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import java.text.DateFormat
import java.util.*

class DateAndTimeDialog(context: Context, private val onDeadlineSet: (String) -> Unit): AlertDialog(context) {

    private val calendar = Calendar.getInstance()

    private val calendarView: CalendarView
    private var timePicker: TimePicker

    init {
        layoutInflater.inflate(R.layout.layout_date_and_time, listView).apply {
            setView(this)
            findViewById<CalendarView>(R.id.cv_calendar_layout_date_and_time).apply {
                calendarView = this
                setOnDateChangeListener { _, year, month, day -> onDateChanged(year, month, day) }
            }
            findViewById<TimePicker>(R.id.tp_time_spinner_layout_date_and_time).apply {
                timePicker = this
                setOnTimeChangedListener { _, hour, minute -> onTimeChanged(hour, minute) }
            }
            findViewById<TabLayout>(R.id.tl_calendar_type_layout_date_and_type).apply {
                initTabLayout(this)
            }
            findViewById<MaterialButton>(R.id.mb_set_deadline_layout_date_and_time).apply {
                setOnClickListener {
                    DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)
                        .format(Date(calendar.timeInMillis)).apply { onDeadlineSet(this) }
                    dismiss()
                }
            }
        }
    }

    private fun initTabLayout(tabLayout: TabLayout) {
        tabLayout.tabMode = TabLayout.MODE_AUTO
        tabLayout.isTabIndicatorFullWidth = false
        tabLayout.setSelectedTabIndicatorColor(context.resources.getColor(R.color.colorAccent))
        tabLayout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.apply {
                    when (position) {
                        0 -> showCalendar()
                        1 -> showClock()
                    }
                }
            }
        })
    }

    private fun showCalendar() {
        calendarView.visibility = View.VISIBLE
        timePicker.visibility = View.GONE
    }

    private fun showClock() {
        calendarView.visibility = View.GONE
        timePicker.visibility = View.VISIBLE
    }


    private fun onDateChanged(year: Int, month: Int, day: Int) {
        calendar.set(year, month, day)
    }

    private fun onTimeChanged(hour: Int, minute: Int) {
        calendar.apply {
            set(Calendar.HOUR, hour)
            set(Calendar.MINUTE, minute)
        }
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        calendar.clear()
    }
}