package com.e.bigvutest.ui.viewmodel.effects

sealed class WorkshopEffect{
    object NavigateWorkshopDetailsScreen: WorkshopEffect()
    data class ShowToastMessage(val message:Int): WorkshopEffect()
}
