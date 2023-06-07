package com.glisije.clearsalary.ui.setDay

import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glisije.clearsalary.Data.DayOfWork
import com.glisije.clearsalary.Data.MoneyForTime
import com.glisije.clearsalary.Data.MyAppDatabase
import com.glisije.clearsalary.MainActivity
import com.glisije.clearsalary.R
import com.glisije.clearsalary.databinding.FragmentSetdayBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SetDayFragment : Fragment() {

    private var _binding: FragmentSetdayBinding? = null
    private val calendarInSetDayStart = Calendar.getInstance()
    private val calendarInSetDayStop = Calendar.getInstance()
    private var todaySalary = 0
    private var todayTime = 0
//    private var myAppDatabase =

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val setDayViewMoer(this).get(SetDayViewModel::class.java)

        _binding = FragmentSetdayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setClickListeners()

        return root
    }

    private fun setClickListeners() {
        val timePickerStart = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            calendarInSetDayStart.set(Calendar.HOUR_OF_DAY, hour)
            calendarInSetDayStart.set(Calendar.MINUTE, minute)
        }
        val timePickerStop = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            calendarInSetDayStop.set(Calendar.HOUR_OF_DAY, hour)
            calendarInSetDayStop.set(Calendar.MINUTE, minute)
        }

        binding.startTimeButton.setOnClickListener(View.OnClickListener {
            TimePickerDialog(
                this.context,
                timePickerStart,
                calendarInSetDayStart.get(Calendar.HOUR_OF_DAY),
                calendarInSetDayStart.get(Calendar.MINUTE),
                true
            ).show()
            updateView1()
        })

        binding.endTimeButton.setOnClickListener(View.OnClickListener {
            TimePickerDialog(
                this.context,
                timePickerStop,
                calendarInSetDayStop.get(Calendar.HOUR_OF_DAY),
                calendarInSetDayStop.get(Calendar.MINUTE),
                true
            ).show()
            updateView2()
        })

        binding.SaveTodayButton.setOnClickListener(View.OnClickListener {
            calendarAndTimeData()
            saveSalary()
        })
    }

    private fun updateView1() {
        val hour = calendarInSetDayStart.get(Calendar.HOUR_OF_DAY)
        val minute = calendarInSetDayStart.get(Calendar.MINUTE)
        val formattedTime = getString(R.string.formatted_time, hour, minute)

        binding.timeStartTextView.setText(formattedTime)
    }

    private fun updateView2() {
        val hour = calendarInSetDayStop.get(Calendar.HOUR_OF_DAY)
        val minute = calendarInSetDayStop.get(Calendar.MINUTE)
        println(hour.toString() +":"+ minute.toString())
        val formattedTime = getString(R.string.formatted_time, hour, minute)
        binding.timeEndTextView.setText(formattedTime)
    }

    private fun calendarAndTimeData() {
        calendarInSetDayStart.set(Calendar.YEAR, binding.datePicker.year)
        calendarInSetDayStart.set(Calendar.MONTH, binding.datePicker.month)
        calendarInSetDayStart.set(Calendar.DAY_OF_MONTH, binding.datePicker.dayOfMonth)

        calendarInSetDayStop.set(Calendar.YEAR, binding.datePicker.year)
        calendarInSetDayStop.set(Calendar.MONTH, binding.datePicker.month)
        calendarInSetDayStop.set(Calendar.DAY_OF_MONTH, binding.datePicker.dayOfMonth)

        calculateSalary()
    }

    private fun calculateSalary() {
        var listOfMoneyForTime: List<MoneyForTime> = listOf()
        CoroutineScope(Dispatchers.IO).launch {
            listOfMoneyForTime =
                (requireActivity() as MainActivity).database.moneyForTimeDao().getAllMoneyForTime()
        }
        for (moneyForTime in listOfMoneyForTime) {
            if (moneyForTime.calendarStart <= calendarInSetDayStart) {
                if (moneyForTime.calendarStop >= calendarInSetDayStop) {
                    todayTime =
                        calendarInSetDayStart.get(Calendar.HOUR_OF_DAY) * 60 + calendarInSetDayStart.get(
                            Calendar.MINUTE
                        ) -
                                calendarInSetDayStop.get(Calendar.HOUR_OF_DAY) * 60 + calendarInSetDayStop.get(
                            Calendar.MINUTE
                        )
                    todaySalary = -todayTime * moneyForTime.salaryPerMinuet
                } else if (moneyForTime.calendarStop <= calendarInSetDayStop) {
                    todayTime =
                        calendarInSetDayStart.get(Calendar.HOUR_OF_DAY) * 60 + calendarInSetDayStart.get(
                            Calendar.MINUTE
                        ) -
                                moneyForTime.calendarStop.get(Calendar.HOUR_OF_DAY) * 60 + moneyForTime.calendarStop.get(
                            Calendar.MINUTE
                        )
                    todaySalary = -todayTime * moneyForTime.salaryPerMinuet
                }
            } else if (moneyForTime.calendarStart >= calendarInSetDayStart) {
                if (moneyForTime.calendarStop >= calendarInSetDayStop) {
                    todayTime =
                        moneyForTime.calendarStart.get(Calendar.HOUR_OF_DAY) * 60 + moneyForTime.calendarStart.get(
                            Calendar.MINUTE
                        ) -
                                calendarInSetDayStop.get(Calendar.HOUR_OF_DAY) * 60 + calendarInSetDayStop.get(
                            Calendar.MINUTE
                        )
                    todaySalary = -todayTime * moneyForTime.salaryPerMinuet
                } else if (moneyForTime.calendarStop <= calendarInSetDayStop) {
                    todayTime =
                        moneyForTime.calendarStart.get(Calendar.HOUR_OF_DAY) * 60 + moneyForTime.calendarStart.get(
                            Calendar.MINUTE
                        ) -
                                moneyForTime.calendarStop.get(Calendar.HOUR_OF_DAY) * 60 + moneyForTime.calendarStop.get(
                            Calendar.MINUTE
                        )
                    todaySalary = -todayTime * moneyForTime.salaryPerMinuet
                }
            }
        }

    }

    private fun insertDayOfWork(dayOfWork: DayOfWork, context: Context?) {
        context ?: return // Handle null context if needed

        val database = MyAppDatabase.getDayOfWorkDao(context)
        database.dayOfWorkDao().insertDayOfWork(dayOfWork)
        println(database.dayOfWorkDao().getAllDaysOfWork())
    }

    private fun saveSalary() {
            calendarAndTimeData()

        CoroutineScope(Dispatchers.IO).launch {
            insertDayOfWork(
                DayOfWork(
                    todayTime,
                    calendarInSetDayStop,
                    calendarInSetDayStop,
                    todaySalary
                ),
                requireContext()
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
