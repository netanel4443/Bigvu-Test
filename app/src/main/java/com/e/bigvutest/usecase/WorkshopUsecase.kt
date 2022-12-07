package com.e.bigvutest.usecase

import com.e.bigvutest.data.api.WorkShopApi
import com.e.bigvutest.data.dataobjects.WorkshopData
import com.e.bigvutest.ui.recyclerviews.items.WorkshopVhItem
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@ViewModelScoped
class WorkshopUsecase @Inject constructor(
    private val workShopApi: WorkShopApi
) {

    // Gets the workshop list asynchronously, then checks if we got a successful response.
    // If is successful :
    // if body is not null :
    // we sort the list  in alpha-bet order , otherwise , we return an empty list.
    fun getSortedWorkshopList(): Single<List<WorkshopData>> {
        return workShopApi.getWorkshopList()
            .map { response ->
                var list: List<WorkshopData> = ArrayList()
                if (response.isSuccessful) {
                    response.body()?.let { workshopList ->
                        list = workshopList
                    }
                }
                list
            }.map(::sortByAlphaBet)
    }

    //Sorts the list in alpha-bet order
    private fun sortByAlphaBet(list: List<WorkshopData>): List<WorkshopData> {
        val sortedList = list.sortedWith { first, second ->
            first.name.compareTo(second.name)
        }
        return sortedList
    }

}