package com.e.bigvutest.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.e.bigvutest.databinding.WorkshopDetailsBinding
import com.e.bigvutest.ui.viewmodel.WorkshopViewModel
import com.e.bigvutest.utils.subscribeBlock
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class WorkshopDetailsFragment : Fragment() {

    companion object {
        const val TAG = "WorkshopDetailsFragment"
    }

    // for cleaning rxjava subscribers
    private val compositeDisposable = CompositeDisposable()

    private var binding: WorkshopDetailsBinding? = null
    private val workshopViewModel by activityViewModels<WorkshopViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = WorkshopDetailsBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUi()
        observeStateChanges()
    }

    private fun initUi() {
        initVideoPlayer()
    }

    private fun initVideoPlayer() {
        val videoView = binding!!.videoView
        val mediaController = MediaController(requireActivity())

        mediaController.setAnchorView(videoView)
        mediaController.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)

    }


    private fun observeStateChanges() {
        workshopViewModel.workshopDetailsViewState.observeMviLiveData(viewLifecycleOwner) { prev, curr ->
            val selectedWorkshop = curr.selectedWorkshop
            if (prev == null || prev.selectedWorkshop != curr.selectedWorkshop) {
                //todo requested to add a title , but there is no title from the given json, that's why
                //todo it was not added.
                binding!!.author.text = selectedWorkshop?.name
                binding!!.text.text = selectedWorkshop?.text
                binding!!.description.text = selectedWorkshop?.description
                setVideo(selectedWorkshop!!.video)
            }
        }
    }


    private fun setVideo(videoUrl: String) {
        val uri = Uri.parse(videoUrl)
        binding!!.videoView.setVideoURI(uri)
    }


    override fun onResume() {
        super.onResume()
        compositeDisposable.add(workshopViewModel.timeDuration.observeOn(Schedulers.io())
            .subscribeBlock { duration ->
                binding!!.videoView.seekTo(duration)// selects the started time position of the video
                binding!!.videoView.start()
            })
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
        val duration =
            binding!!.videoView.currentPosition //gets the current time position of the video
        //From the docs , we understand that, changes in lifecycle affects the video.
        //So we need to save the current time position of the video,
        //in order to resume the video from his last time position.
        workshopViewModel.setVideoDuration(duration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}