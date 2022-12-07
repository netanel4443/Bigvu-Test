package com.e.bigvutest.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.bigvutest.R
import com.e.bigvutest.databinding.WorkshopActivityBinding
import com.e.bigvutest.ui.fragments.WorkshopDetailsFragment
import com.e.bigvutest.ui.recyclerviews.createviewholder.CreateWorkshopVh
import com.e.bigvutest.ui.recyclerviews.generics.GenericItemClickListener
import com.e.bigvutest.ui.recyclerviews.generics.GenericRecyclerviewAdapter
import com.e.bigvutest.ui.recyclerviews.generics.VhItemSetters
import com.e.bigvutest.ui.recyclerviews.items.WorkshopVhItem
import com.e.bigvutest.ui.viewmodel.WorkshopViewModel
import com.e.bigvutest.ui.utils.addFragment
import com.e.bigvutest.ui.viewmodel.effects.WorkshopEffect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkshopActivity : BaseActivity() {

    private lateinit var binding: WorkshopActivityBinding
    private lateinit var recyclerviewAdapter: GenericRecyclerviewAdapter<WorkshopVhItem>
    private val workshopViewModel: WorkshopViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WorkshopActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //removes upper action bar with the name of the app
        supportActionBar?.hide()

        initUi()
        observeStateChanges()
        observeEffects()
    }

    private fun observeStateChanges() {
        workshopViewModel.viewState.observeMviLiveData(this) { prev, curr ->
            if (prev == null || prev.workshopVhItemList != curr.workshopVhItemList) {
                recyclerviewAdapter.submitList(curr.workshopVhItemList)
            }

            if (prev == null || prev.isLoading != curr.isLoading){
                setIsLoadingState(curr.isLoading)
            }
        }
    }

    private fun setIsLoadingState(visibility: Int) {
        binding.loadingProgressbarParent.visibility = visibility
    }

    private fun observeEffects() {
        workshopViewModel.viewEffect.observe(this) { effect ->
            when(effect){
                is WorkshopEffect.NavigateWorkshopDetailsScreen -> navigateToWorkshopDetailsScreen()
                is WorkshopEffect.ShowToastMessage -> showToast(effect.message)
            }

        }
    }

    private fun initUi() {
        initRecyclerview()
        initEditTextSearchListener()
    }



    private fun initRecyclerview() {
        recyclerviewAdapter = GenericRecyclerviewAdapter()
        val clickListener = object : GenericItemClickListener<WorkshopVhItem> {
            override fun onItemClick(item: WorkshopVhItem) {
                workshopViewModel.navigateToWorkshopDetailsScreen()
                workshopViewModel.setSelectedWorkshop(item)
                workshopViewModel.setVideoDuration(0) //new video needs to start from 0s
            }
        }
        val setter = VhItemSetters(
            clickListener,
            CreateWorkshopVh::class.java,
            R.layout.workshop_recyclerview_cell_design
        )

        recyclerviewAdapter.setVhItemSetter(setter)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.workshopRecyclerview.adapter = recyclerviewAdapter
        binding.workshopRecyclerview.layoutManager = layoutManager
        binding.workshopRecyclerview.setHasFixedSize(true)
    }

    private fun initEditTextSearchListener() {
        binding.searchEditText.addTextChangedListener(object :TextWatcher{
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.apply {
                    workshopViewModel.filterWorkShopByText(this)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun navigateToWorkshopDetailsScreen() {
        addFragment(
            WorkshopDetailsFragment(),
            binding.fragmentContainer.id,
            WorkshopDetailsFragment.TAG
        )
    }

    private fun showToast(message:Int){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}