package com.muhammed.surat.presentation.common.component

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.muhammed.surat.data.enums.SortType
import com.muhammed.surat.data.model.FilterModel
import com.muhammed.surat.databinding.HeaderComponentViewBinding
import com.muhammed.surat.util.onClick
import com.muhammed.surat.util.onSearch
import com.muhammed.surat.util.onTextEntered
import com.muhammed.surat.util.toDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HeaderComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var filterModel = FilterModel()

    private var onFilter: ((FilterModel) -> Unit)? = null

    private var onSort: ((SortType) -> Unit)? = null

    private var mBinding: HeaderComponentViewBinding? = null
        get() {
            mBinding = field ?: inflateBinding()
            return field
        }

    private val binding: HeaderComponentViewBinding
        get() = mBinding ?: throw IllegalStateException("Binding is not initialized")

    private fun inflateBinding(): HeaderComponentViewBinding {
        return HeaderComponentViewBinding.inflate(LayoutInflater.from(context), this)
    }

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
                toggleDateIrSearchLayout(true)
                binding.rbKeyword.isChecked = true
                binding.searchEditText.setText(filter.query)
            }

            (filter.dateRange?.first != null && filter.dateRange.second != null) || binding.rbDateRange.isChecked -> {
                toggleDateIrSearchLayout(false)
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

    private fun initListeners() {
        with(binding) {
            searchEditText.onSearch { text ->
                onFilter?.invoke(filterModel.copy(query = text))
            }

            btnClear.onClick {
                startDateEditText.text?.clear()
                endDateEditText.text?.clear()
                val pair = getDatePair()
                onFilter?.invoke(filterModel.copy(dateRange = pair, query = null))
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
                    onFilter?.invoke(filterModel.copy(dateRange = pair, query = null))
                }
            }

            endDateEditText.onTextEntered {
                if (startDateEditText.text?.isNotEmpty() == true) {
                    val pair = getDatePair()
                    onFilter?.invoke(filterModel.copy(dateRange = pair, query = null))
                }
            }

            rbDateRange.onClick {
                toggleDateIrSearchLayout(false)
                rbKeyword.isChecked = false
                searchEditText.text?.clear()
                onFilter?.invoke(filterModel.copy(query = null))
            }

            rbKeyword.onClick {
                toggleDateIrSearchLayout(true)
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

    private fun toggleDateIrSearchLayout(isSearchView: Boolean) {
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
                val formattedDate =
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                editText.setText(formattedDate)
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