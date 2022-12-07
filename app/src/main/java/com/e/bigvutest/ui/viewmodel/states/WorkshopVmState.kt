package com.e.bigvutest.ui.viewmodel.states

import android.view.View
import com.e.bigvutest.ui.recyclerviews.items.WorkshopVhItem

data class WorkshopVmState(
    val workshopVhItemList: List<WorkshopVhItem> = ArrayList(),
    val selectedWorkshop: WorkshopVhItem? = null,
    val isLoading:Int = View.GONE
)