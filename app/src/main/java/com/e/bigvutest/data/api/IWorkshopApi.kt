package com.e.bigvutest.data.api

import com.e.bigvutest.data.dataobjects.WorkshopData
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET

interface IWorkshopApi {

    @GET("workshops.json")
    fun getWorkshopList(): Single<Response<List<WorkshopData>>>
}