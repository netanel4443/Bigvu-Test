package com.e.bigvutest.ui.viewmodel

import android.view.View
import com.e.bigvutest.R
import com.e.bigvutest.ui.recyclerviews.items.WorkshopVhItem
import com.e.bigvutest.ui.viewmodel.effects.WorkshopEffect
import com.e.bigvutest.ui.viewmodel.generics.BaseViewModel
import com.e.bigvutest.ui.viewmodel.states.WorkshopDetailsState
import com.e.bigvutest.ui.viewmodel.states.WorkshopVmState
import com.e.bigvutest.usecase.WorkshopUsecase
import com.e.bigvutest.utils.subscribeBlock
import com.e.safety.ui.utils.livedata.MviLiveData
import com.e.safety.ui.utils.livedata.MviMutableLiveData
import com.e.safety.ui.utils.livedata.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WorkshopViewModel @Inject constructor(
    private val workshopUsecase: WorkshopUsecase
) : BaseViewModel() {

    private val _viewState = MviMutableLiveData(WorkshopVmState())
    val viewState: MviLiveData<WorkshopVmState> get() = _viewState

    private val _workshopDetailsViewState = MviMutableLiveData(WorkshopDetailsState())
    val workshopDetailsViewState: MviLiveData<WorkshopDetailsState> get() = _workshopDetailsViewState

    //default value of time duration is 0s - the start position of the video
    private val _timeDuration = BehaviorSubject.createDefault(0)
    val timeDuration get() = _timeDuration

    private val _viewEffect = SingleLiveEvent<WorkshopEffect>()
    val viewEffect get() = _viewEffect

    private val _filteredWorkshopListSubject: PublishSubject<CharSequence> = PublishSubject.create()

    private var cachedSortedWorkshopVhList: List<WorkshopVhItem> = ArrayList()

    init {
        getWorkshopList()
        subscribeFilterObservable()
    }

    private fun getWorkshopList() {
        workshopUsecase.getSortedWorkshopList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                updateIsLoading(View.VISIBLE)
            }
            .doOnError {
                _viewEffect.value = WorkshopEffect.ShowToastMessage(R.string.internet_connection)
            }
            .doAfterTerminate {
                updateIsLoading(View.GONE)
            }
            .subscribeBlock { workshopList ->

                val workshopArrayList = ArrayList<WorkshopVhItem>()

                workshopList.forEach { workshopData ->
                    val workshopVhItem = WorkshopVhItem(
                        id = UUID.randomUUID(),
                        name = workshopData.name,
                        image = workshopData.image,
                        description = workshopData.description,
                        text = workshopData.text,
                        video = workshopData.video
                    )
                    workshopArrayList.add(workshopVhItem)
                }
                cachedSortedWorkshopVhList = workshopArrayList
                updateWorkshopList(workshopArrayList)
            }.addDisposable()
    }

    private fun subscribeFilterObservable() {
        _filteredWorkshopListSubject
            .debounce(300, TimeUnit.MICROSECONDS)
            .switchMap {
                Observable.fromIterable(cachedSortedWorkshopVhList)
                    .filter { item ->
                        item.description.lowercase().contains(it)
                    }.toList().toObservable()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBlock { filteredList ->
                updateWorkshopList(filteredList)
            }.addDisposable()
    }


    fun navigateToWorkshopDetailsScreen() {
        _viewEffect.value = WorkshopEffect.NavigateWorkshopDetailsScreen
    }

    private fun updateWorkshopList(workshopList: List<WorkshopVhItem>) {
        _viewState.mviValue {
            it.copy(
                workshopVhItemList = workshopList
            )
        }
    }

    fun setSelectedWorkshop(workshopVhItem: WorkshopVhItem) {
        _workshopDetailsViewState.mviValue {
            it.copy(
                selectedWorkshop = workshopVhItem
            )
        }
    }

    fun setVideoDuration(duration: Int) {
        _timeDuration.onNext(duration)
    }

    fun filterWorkShopByText(text:CharSequence){
        _filteredWorkshopListSubject.onNext(text)
    }

    private fun updateIsLoading(visibility: Int) {
        _viewState.mviValue {
            it.copy(
                isLoading = visibility
            )
        }
    }
}