package com.glisije.clearsalary.ui.profile

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.marginEnd
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.glisije.clearsalary.Data.DayOfWork
import com.glisije.clearsalary.Data.MoneyForTime
import com.glisije.clearsalary.MainActivity
import com.glisije.clearsalary.R
import com.glisije.clearsalary.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        profileViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        binding.imageView.setImageResource(R.drawable.ic_face)
        binding.imageView.layoutParams.height = 500
        binding.imageView.layoutParams.width = 500
        var listOfMoneyForTime: List<DayOfWork> = listOf()
        CoroutineScope(Dispatchers.IO).launch {
            listOfMoneyForTime =
                (requireActivity() as MainActivity).database.dayOfWorkDao().getAllDaysOfWork()
            println((requireActivity() as MainActivity).database.dayOfWorkDao().getAllDaysOfWork())
            println((requireActivity() as MainActivity).database.moneyForTimeDao().getAllMoneyForTime())
        }

        binding.earnedMoneyNumber.text = listOfMoneyForTime.sumOf { it.salary }.toString()
        println("listOfMoneyForTime.sumOf { it.salary } = ${listOfMoneyForTime.sumOf { it.salary }}")
        binding.earnedMoneyNumber.text = "6800"

        binding.nameSpace.setText("Ромас")

        binding.payForOverWork.setOnClickListener{

            // Create TableLayout
            val tableLayout = TableLayout(requireContext())
            tableLayout.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableLayout.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL

            // Create TableRow 1
            val tableRow1 = TableRow(requireContext())
            tableRow1.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            // Create TextView 1
            val textView1 = TextView(requireContext())
            textView1.id = View.generateViewId()
            textView1.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            textView1.text = "Я получаю больше на"
            textView1.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textView1.setTextColor(Color.BLACK)
            textView1.textSize = 20f
            textView1.setTypeface(null, Typeface.BOLD)

            tableRow1.addView(textView1)

            val tableRowMoney= TableRow(requireContext())
            tableRowMoney.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            // Create EditText 1
            val editText1 = EditText(requireContext())
            editText1.id = View.generateViewId()
            editText1.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            editText1.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_NUMBER

            val oneMoreTextView1 = TextView(requireContext())
            oneMoreTextView1.id = View.generateViewId()
            oneMoreTextView1.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            oneMoreTextView1.text = "₽/ч."
            oneMoreTextView1.textAlignment = View.TEXT_ALIGNMENT_CENTER
            oneMoreTextView1.setTextColor(Color.BLACK)
            oneMoreTextView1.textSize = 20f
            oneMoreTextView1.setTypeface(null, Typeface.BOLD)

//            tableRow1.addView(editText1)
//            tableRow1.addView(oneMoreTextView1)

            val lastTextView1 = TextView(requireContext())
            lastTextView1.id = View.generateViewId()
            lastTextView1.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            lastTextView1.text = "за каждый час"
            lastTextView1.textAlignment = View.TEXT_ALIGNMENT_CENTER
            lastTextView1.setTextColor(Color.BLACK)
            lastTextView1.textSize = 20f
            lastTextView1.setTypeface(null, Typeface.BOLD)

            val tableRowLast= TableRow(requireContext())
            tableRowLast.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            tableRow1.addView(editText1)
            tableRow1.addView(oneMoreTextView1)
            // Add TextView 1 and EditText 1 to TableRow 1

            // Create TableRow 2
            val tableRow2 = TableRow(requireContext())
            tableRow2.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )

            // Create TextView 1
            val textView2 = TextView(requireContext())
            textView2.id = View.generateViewId()
            textView2.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            textView2.text = "Если отработаю больше чем "
            textView2.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textView2.setTextColor(Color.BLACK)
            textView2.textSize = 20f
            textView2.setTypeface(null, Typeface.BOLD)

            // Create EditText 1
            val editText2 = EditText(requireContext())
            editText1.id = View.generateViewId()
            editText1.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
            )

            editText1.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_NUMBER

            val textView4 = TextView(requireContext())
            textView4.id = View.generateViewId()
            textView4.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            textView4.text = " ч."
            textView4.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textView4.setTextColor(Color.BLACK)
            textView4.textSize = 20f
            textView4.setTypeface(null, Typeface.BOLD)

            // Add TextView 1 and EditText 1 to TableRow 1
            tableRow2.addView(textView2)
            tableRow2.addView(editText2)
            tableRow2.addView(textView4)


            tableLayout.addView(tableRow1)
            tableLayout.addView(tableRow2)
            tableLayout.addView(TableLayout(requireContext()))
//            tableLayout.addView(tableRowMoney)
//
//            tableLayout.addView(tableRowLast)



            binding.linearContainer.addView(tableLayout)
            binding.scrollView.post {
                binding.scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }

        binding.saveOverWork.setOnClickListener{
            binding.earnedMoneyNumber.text = "6900"
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}