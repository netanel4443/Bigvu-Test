package com.e.bigvutest.ui.recyclerviews.createviewholder

import android.net.Uri
import android.view.View
import com.e.bigvutest.databinding.WorkshopRecyclerviewCellDesignBinding
import com.e.bigvutest.ui.recyclerviews.generics.CreateVh
import com.e.bigvutest.ui.recyclerviews.generics.GenericItemClickListener
import com.e.bigvutest.ui.recyclerviews.items.WorkshopVhItem
import com.squareup.picasso.Picasso

class CreateWorkshopVh: CreateVh<WorkshopVhItem>() {
    private var binding: WorkshopRecyclerviewCellDesignBinding? = null
    private var _itemClickListener:GenericItemClickListener<WorkshopVhItem>? = null

    override fun onInitVh(view: View) {
        binding = WorkshopRecyclerviewCellDesignBinding.bind(view)

        binding!!.workshopItemMainParent.setOnClickListener {
            _itemClickListener!!.onItemClick(cachedItem!!)
        }
    }

    override fun setClickListener(itemClickListener: GenericItemClickListener<WorkshopVhItem>?) {
        _itemClickListener = itemClickListener
    }

    override fun bindData(item: WorkshopVhItem) {
        binding!!.author.text = item.name
        binding!!.description.text = item.description
        Picasso.get()
            .load(item.image)
            .fit()
            .into(binding!!.authorImage)

    }


}