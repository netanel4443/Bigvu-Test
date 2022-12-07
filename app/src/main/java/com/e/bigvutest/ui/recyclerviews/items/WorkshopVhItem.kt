package com.e.bigvutest.ui.recyclerviews.items

import com.e.bigvutest.ui.recyclerviews.generics.GenericVhItem

data class WorkshopVhItem(
    override val id: Any,
    val name:String,
    val image:String,
    val description:String,
    val text:String,
    val video:String
):GenericVhItem


