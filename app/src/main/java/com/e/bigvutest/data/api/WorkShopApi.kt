package com.e.bigvutest.data.api

import com.e.bigvutest.data.dataobjects.WorkshopData
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkShopApi @Inject constructor(
    private val iWorkshopApi: IWorkshopApi
) {

    fun getWorkshopList():Single<Response<List<WorkshopData>>>{
        return iWorkshopApi.getWorkshopList()
    }

}