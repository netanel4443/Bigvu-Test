package com.e.bigvutest.ui.recyclerviews.generics

data class VhItemSetters<T : GenericVhItem> (
    //if itemClick is not necessary , it will be null to prevent exceptions
    var clickListener: GenericItemClickListener<T>? = null,
    var createVh: Class<out CreateVh<T>>? = null,
    var layoutId:Int
)