package com.axelfernandez.streaming.ui.mainMenu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.axelfernandez.streaming.R

class MainMenuFragment : Fragment() {

    companion object {
        fun newInstance() = MainMenuFragment()
    }

    private lateinit var viewModel: MainMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_menu_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainMenuViewModel::class.java)
        val view = view?:return

       val streamingVideo = view.findViewById<CardView>(R.id.card_video)
        val streamingAudio = view.findViewById<CardView>(R.id.card_radio)

        streamingAudio.setOnClickListener {
            findNavController(this).navigate(MainMenuFragmentDirections.actionMainMenuFragmentToAudioStreamingFragment())
        }
        streamingVideo.setOnClickListener {
            findNavController(this).navigate(MainMenuFragmentDirections.actionMainMenuFragmentToStreamFragment())
        }


    }

}