package com.glisije.clearsalary.ui.setSalary

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.glisije.clearsalary.Data.MoneyForTime
import com.glisije.clearsalary.MainActivity
import com.glisije.clearsalary.R
import com.glisije.clearsalary.databinding.FragmentSetsalaryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar


class SetSalaryFragment : Fragment() {

    private var _binding: FragmentSetsalaryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val tableList = mutableListOf<List<EditText>>() // список таблиц со спсиком edittext
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val setSalaryViewModel =
            ViewModelProvider(this).get(SetSalaryViewModel::class.java)

        _binding = FragmentSetsalaryBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val scrollView: ScrollView = binding.scrollView
//        val linearContainer: LinearLayout = binding.linearContainer
//
//        val dynamicTableView : TableLayout = binding.tableView
//
//        linearContainer.addView(dynamicTableView)
//        scrollView.post {
//            scrollView.fullScroll(View.FOCUS_DOWN)
//        }
        var listOfSalaryForMoney: List<MoneyForTime> = listOf()

        CoroutineScope(Dispatchers.IO).launch {
            listOfSalaryForMoney =
                (requireActivity() as MainActivity).database.moneyForTimeDao().getAllMoneyForTime()
        }
        for (salaryForMoney in listOfSalaryForMoney){
            addExitingTable(salaryForMoney)
        }
        val scrollView: ScrollView = binding.scrollView
        val linearContainer: LinearLayout = binding.linearContainer

        binding.addSalaryButton.setOnClickListener {


//            val parent1 = textView1.parent as? ViewGroup
//            parent1?.removeView(textView1)
//            (linearContainer.parent as?ViewGroup)?.removeView(linearContainer)
            linearContainer.addView(addTable())
            scrollView.post {
                scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }

        binding.deleteSalaryButton.setOnClickListener {
            linearContainer.removeView(linearContainer.getChildAt(linearContainer.childCount - 1))
            tableList.removeAt(tableList.size - 1)
        }

        binding.saveSalaryButton.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                listOfSalaryForMoney =
                    (requireActivity() as MainActivity).database.moneyForTimeDao().getAllMoneyForTime()
            }
            if (tableList.size < listOfSalaryForMoney.size){
                for (i in tableList.size until listOfSalaryForMoney.size){
                    CoroutineScope(Dispatchers.IO).launch {
                        (requireActivity() as MainActivity).database.moneyForTimeDao().delete(listOfSalaryForMoney[i])
                    }
                }
            }
            for (table in tableList){
                val calendarStart = Calendar.getInstance()
                calendarStart.set(Calendar.HOUR_OF_DAY, table[0].text.toString().toInt())
                calendarStart.set(Calendar.MINUTE, 0)
                val calendarStop = Calendar.getInstance()
                calendarStop.set(Calendar.HOUR_OF_DAY, table[1].text.toString().toInt())
                calendarStop.set(Calendar.MINUTE, 0)
                val moneyForTime = MoneyForTime(
                    calendarStart = calendarStart,
                    calendarStop = calendarStop,
                    salaryPerMinuet = table[2].text.toString().toInt()
                )
                CoroutineScope(Dispatchers.IO).launch {
                    (requireActivity() as MainActivity).database.moneyForTimeDao().insert(moneyForTime)
                }
            }
        }

//        val textView: TextView = binding.textNotifications
//        setSalaryViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    private fun addExitingTable(salaryForMoney: MoneyForTime): TableLayout {
        // Create TableLayout
        val tableLayout = TableLayout(requireContext())
        tableLayout.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )
        tableLayout.gravity = Gravity.CENTER

        // Create TableRow 1
        val tableRow1 = TableRow(requireContext())
        tableRow1.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )

        // Create TextView 1
        val textView1 = TextView(requireContext())
        textView1.id = View.generateViewId()
        textView1.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        textView1.text = getString(com.glisije.clearsalary.R.string.from)
        textView1.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView1.setTextColor(Color.BLACK)
        textView1.textSize = 20f
        textView1.setTypeface(null, Typeface.BOLD)

        // Create EditText 1
        val editText1 = EditText(requireContext())
        editText1.id = View.generateViewId()
        editText1.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )



        editText1.inputType =
            InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_TIME

        val hour = salaryForMoney.calendarStart.get(Calendar.HOUR_OF_DAY)
        val minute = salaryForMoney.calendarStart.get(Calendar.MINUTE)

        editText1.setText(getString(R.string.formatted_time,
            salaryForMoney.calendarStart.get(Calendar.HOUR_OF_DAY),
            salaryForMoney.calendarStart.get(Calendar.MINUTE)))

        // Add TextView 1 and EditText 1 to TableRow 1
        tableRow1.addView(textView1)
        tableRow1.addView(editText1)

        // Create TableRow 2
        val tableRow2 = TableRow(requireContext())
        tableRow2.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )

        // Create TextView 2
        val textView2 = TextView(requireContext())
        textView2.id = View.generateViewId()
        textView2.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        textView2.text = getString(com.glisije.clearsalary.R.string.to)
        textView2.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView2.setTextColor(Color.BLACK)
        textView2.textSize = 20f
        textView2.setTypeface(null, Typeface.BOLD)

        // Create EditText 2
        val editText2 = EditText(requireContext())
        editText2.id = View.generateViewId()
        editText2.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        editText1.setText(getString(R.string.formatted_time,
            salaryForMoney.calendarStop.get(Calendar.HOUR_OF_DAY),
            salaryForMoney.calendarStop.get(Calendar.MINUTE)))

        editText2.inputType =
            InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_TIME

        // Add TextView 2 and EditText 2 to TableRow 2
        tableRow2.addView(textView2)
        tableRow2.addView(editText2)

        // Create TableRow 3 similarly...
        val tableRow3 = TableRow(requireContext())
        tableRow3.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )

        val textView3 = TextView(requireContext())
        textView3.id = View.generateViewId()
        textView3.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        textView3.text = getString(com.glisije.clearsalary.R.string.equals)
        textView3.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView3.setTextColor(Color.BLACK)
        textView3.textSize = 20f
        textView3.setTypeface(null, Typeface.BOLD)

        val editText3 = EditText(requireContext())
        editText3.id = View.generateViewId()
        editText3.layoutParams = TableRow.LayoutParams(
            315, // Use specific width in pixels or TableRow.LayoutParams.WRAP_CONTENT
            TableRow.LayoutParams.WRAP_CONTENT
        )

        editText1.setText(salaryForMoney.salaryPerMinuet.toString())

        editText3.inputType = InputType.TYPE_CLASS_NUMBER

        tableRow3.addView(textView3)
        tableRow3.addView(editText3)

        // Create TableRow 4 similarly...
        val tableRow4 = TableRow(requireContext())
        tableRow4.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )
        tableRow4.gravity = Gravity.CENTER

        // ... continue creating additional TableRows as needed ...

        // Add TableRow 2, TableRow 3, and TableRow 4 to TableLayout
        tableLayout.addView(tableRow1)
        tableLayout.addView(tableRow2)
        tableLayout.addView(tableRow3)
        tableLayout.addView(tableRow4)

        tableList.add(
            listOf(
                editText1,
                editText2,
                editText3
            ))

        return tableLayout
    }

    private fun addTable(): TableLayout {
        // Create TableLayout
        val tableLayout = TableLayout(requireContext())
        tableLayout.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )
        tableLayout.gravity = Gravity.CENTER

        // Create TableRow 1
        val tableRow1 = TableRow(requireContext())
        tableRow1.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )

        // Create TextView 1
        val textView1 = TextView(requireContext())
        textView1.id = View.generateViewId()
        textView1.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        textView1.text = getString(com.glisije.clearsalary.R.string.from)
        textView1.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView1.setTextColor(Color.BLACK)
        textView1.textSize = 20f
        textView1.setTypeface(null, Typeface.BOLD)

        // Create EditText 1
        val editText1 = EditText(requireContext())
        editText1.id = View.generateViewId()
        editText1.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        editText1.inputType =
            InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_TIME

        // Add TextView 1 and EditText 1 to TableRow 1
        tableRow1.addView(textView1)
        tableRow1.addView(editText1)

        // Create TableRow 2
        val tableRow2 = TableRow(requireContext())
        tableRow2.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )

        // Create TextView 2
        val textView2 = TextView(requireContext())
        textView2.id = View.generateViewId()
        textView2.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        textView2.text = getString(com.glisije.clearsalary.R.string.to)
        textView2.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView2.setTextColor(Color.BLACK)
        textView2.textSize = 20f
        textView2.setTypeface(null, Typeface.BOLD)

        // Create EditText 2
        val editText2 = EditText(requireContext())
        editText2.id = View.generateViewId()
        editText2.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        editText2.inputType =
            InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_TIME

        // Add TextView 2 and EditText 2 to TableRow 2
        tableRow2.addView(textView2)
        tableRow2.addView(editText2)

        // Create TableRow 3 similarly...
        val tableRow3 = TableRow(requireContext())
        tableRow3.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )

        val textView3 = TextView(requireContext())
        textView3.id = View.generateViewId()
        textView3.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        textView3.text = getString(com.glisije.clearsalary.R.string.equals)
        textView3.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView3.setTextColor(Color.BLACK)
        textView3.textSize = 20f
        textView3.setTypeface(null, Typeface.BOLD)

        val editText3 = EditText(requireContext())
        editText3.id = View.generateViewId()
        editText3.layoutParams = TableRow.LayoutParams(
            315, // Use specific width in pixels or TableRow.LayoutParams.WRAP_CONTENT
            TableRow.LayoutParams.WRAP_CONTENT
        )
        editText3.inputType = InputType.TYPE_CLASS_NUMBER

        tableRow3.addView(textView3)
        tableRow3.addView(editText3)

        // Create TableRow 4 similarly...
        val tableRow4 = TableRow(requireContext())
        tableRow4.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )
        tableRow4.gravity = Gravity.CENTER

        // ... continue creating additional TableRows as needed ...

        // Add TableRow 2, TableRow 3, and TableRow 4 to TableLayout
        tableLayout.addView(tableRow1)
        tableLayout.addView(tableRow2)
        tableLayout.addView(tableRow3)
        tableLayout.addView(tableRow4)
        tableList.add(
            listOf(
                editText1,
                editText2,
                editText3
            ))
        return tableLayout
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}