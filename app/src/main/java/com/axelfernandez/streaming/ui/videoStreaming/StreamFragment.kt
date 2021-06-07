package com.axelfernandez.streaming.ui.videoStreaming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.axelfernandez.streaming.R
import com.google.android.exoplayer2.ui.StyledPlayerView


class StreamFragment : Fragment(){

    companion object {
        fun newInstance() = StreamFragment()
    }


    private lateinit var viewModel: StreamViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.stream_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StreamViewModel::class.java)
        viewModel.playerView = view?.findViewById<StyledPlayerView>(R.id.player_view)
    }

    override fun onResume() {
        super.onResume()
        viewModel.initializePlayer(requireContext())
        viewModel.player?.prepare();
        viewModel.player?.play()

    }
    override fun onStop() {
        val playerView = viewModel.playerView?:return
        playerView.onPause()
        viewModel.releasePlayer()
        super.onStop()

    }

}