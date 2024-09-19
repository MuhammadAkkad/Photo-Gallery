package com.muhammed.surat

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.muhammed.surat.databinding.DateComponentViewBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var onDateSelected: ((Pair<Date?, Date?>) -> Unit)? = null
    private var onQueryEntered: ((String?) -> Unit)? = null

    private var mBinding: DateComponentViewBinding? = null
        get() {
            mBinding = field ?: inflateBinding()
            return field
        }

    private val binding: DateComponentViewBinding
        get() = mBinding ?: throw IllegalStateException("Binding is not initialized")

    private fun inflateBinding(): DateComponentViewBinding {
        return DateComponentViewBinding.inflate(LayoutInflater.from(context), this)
    }

    init {
        initListeners()
        binding.rbKeyword.performClick()
    }

    fun onDateSelected(onDateSelected: (Pair<Date?, Date?>) -> Unit) {
        this.onDateSelected = onDateSelected
    }

    fun onQueryEntered(onQueryEntered: (String?) -> Unit) {
        this.onQueryEntered = onQueryEntered
    }

    private fun initListeners() {
        with(binding) {

            editTextSearch.onSearch { text ->
                onQueryEntered?.invoke(text)
            }

            btnClear.onClick {
                startDateEditText.text?.clear()
                endDateEditText.text?.clear()
                triggerFiltering()
            }

            startDateEditText.setOnClickListener {
                showDatePicker(startDateEditText)
            }

            endDateEditText.setOnClickListener {
                showDatePicker(endDateEditText)
            }

            startDateEditText.onTextEntered {
                triggerFiltering()
            }

            endDateEditText.onTextEntered {
                triggerFiltering()
            }

            rbDateRange.onClick {
                llDateFilterContainer.isVisible = true
                textInputLayout.isVisible = false
                rbKeyword.isChecked = false
                editTextSearch.text?.clear()
                onQueryEntered?.invoke(null)
            }

            rbKeyword.onClick {
                llDateFilterContainer.isVisible = false
                textInputLayout.isVisible = true
                rbDateRange.isChecked = false
                startDateEditText.text?.clear()
                endDateEditText.text?.clear()
                triggerFiltering()
            }
        }
    }

    private fun showDatePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = Calendar.getInstance().apply {
                    set(Calendar.YEAR, selectedYear)
                    set(Calendar.MONTH, selectedMonth)
                    set(Calendar.DAY_OF_MONTH, selectedDay)
                }.time
                val formattedDate =
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                editText.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun triggerFiltering() {
        val startDate =
            binding.startDateEditText.text.toString().takeIf { it.isEmpty().not() }?.toDate()
        val endDate =
            binding.endDateEditText.text.toString().takeIf { it.isEmpty().not() }?.toDate()
            onDateSelected?.invoke(Pair(startDate, endDate))
    }

    fun getDateRange(): Pair<Date?, Date?> {
        val startDate = binding.startDateEditText.text.toString().toDate()
        val endDate = binding.endDateEditText.text.toString().toDate()
        return Pair(startDate, endDate)
    }
}