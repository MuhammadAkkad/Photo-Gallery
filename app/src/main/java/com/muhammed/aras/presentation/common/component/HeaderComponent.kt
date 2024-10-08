package com.muhammed.aras.presentation.common.component

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.muhammed.aras.data.enums.SortType
import com.muhammed.aras.data.model.FilterModel
import com.muhammed.aras.databinding.HeaderComponentViewBinding
import com.muhammed.aras.util.format
import com.muhammed.aras.util.onClick
import com.muhammed.aras.util.onSearch
import com.muhammed.aras.util.onTextEntered
import com.muhammed.aras.util.toDate
import java.util.Calendar
import java.util.Date

class HeaderComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var onFilter: ((FilterModel) -> Unit)? = null
    private var onSort: ((SortType) -> Unit)? = null
    private var _binding: HeaderComponentViewBinding? = null
        get() {
            _binding = field ?: inflateBinding()
            return field
        }
    private val binding: HeaderComponentViewBinding
        get() = _binding ?: throw IllegalStateException("Binding is not initialized")

    init {
        initListeners()
    }

    fun initState(filter: FilterModel, sortType: SortType) {
        when (sortType) {
            SortType.NAME -> binding.rbSortByName.isChecked = true
            SortType.DATE -> binding.rbSortByDate.isChecked = true
        }
        when {
            filter.query.isNullOrEmpty().not() || binding.rbKeyword.isChecked -> {
                toggleDateOrSearchLayout(true)
                binding.rbKeyword.isChecked = true
                binding.searchEditText.setText(filter.query)
            }

            (filter.dateRange?.first != null && filter.dateRange.second != null) || binding.rbDateRange.isChecked -> {
                toggleDateOrSearchLayout(false)
                binding.rbDateRange.isChecked = true
                binding.startDateEditText.setText(filter.dateRange?.first.toString())
                binding.endDateEditText.setText(filter.dateRange?.second.toString())
            }

            else -> binding.rbKeyword.isChecked = true
        }
    }

    fun onFilter(onFilter: ((FilterModel) -> Unit)) {
        this.onFilter = onFilter
    }

    fun onSort(onSort: ((SortType) -> Unit)) {
        this.onSort = onSort
    }

    private fun inflateBinding(): HeaderComponentViewBinding {
        return HeaderComponentViewBinding.inflate(LayoutInflater.from(context), this)
    }

    private fun initListeners() {
        with(binding) {
            searchEditText.onSearch { text ->
                onFilter?.invoke(FilterModel(query = text))
            }

            btnClear.onClick {
                startDateEditText.text?.clear()
                endDateEditText.text?.clear()
                onFilter?.invoke(FilterModel(dateRange = null, query = null))
            }

            startDateEditText.onClick {
                showDatePicker(startDateEditText)
            }

            endDateEditText.onClick {
                showDatePicker(endDateEditText)
            }

            startDateEditText.onTextEntered {
                if (endDateEditText.text?.isNotEmpty() == true) {
                    val pair = getDatePair()
                    onFilter?.invoke(FilterModel(dateRange = pair, query = null))
                }
            }

            endDateEditText.onTextEntered {
                if (startDateEditText.text?.isNotEmpty() == true) {
                    val pair = getDatePair()
                    onFilter?.invoke(FilterModel(dateRange = pair, query = null))
                }
            }

            rbDateRange.onClick {
                toggleDateOrSearchLayout(false)
                rbKeyword.isChecked = false
                searchEditText.text?.clear()
                onFilter?.invoke(FilterModel(query = null))
            }

            rbKeyword.onClick {
                toggleDateOrSearchLayout(true)
                rbDateRange.isChecked = false
                startDateEditText.text?.clear()
                endDateEditText.text?.clear()
                getDatePair()
            }

            rbSortByName.onClick {
                rbSortByDate.isChecked = false
                onSort?.invoke(SortType.NAME)
            }

            rbSortByDate.onClick {
                rbSortByName.isChecked = false
                onSort?.invoke(SortType.DATE)
            }
        }
    }

    private fun toggleDateOrSearchLayout(isSearchView: Boolean) {
        if (isSearchView) {
            binding.llDateFilterContainer.isVisible = false
            binding.SearchTextInputLayout.isVisible = true
        } else {
            binding.SearchTextInputLayout.isVisible = false
            binding.llDateFilterContainer.isVisible = true
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
                editText.setText(date.format())
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun getDatePair(): Pair<Date?, Date?> {
        val startDate =
            binding.startDateEditText.text.toString().takeIf { it.isEmpty().not() }?.toDate()
        val endDate =
            binding.endDateEditText.text.toString().takeIf { it.isEmpty().not() }?.toDate()
        return Pair(startDate, endDate)
    }
}