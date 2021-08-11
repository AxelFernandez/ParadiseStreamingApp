package com.axelfernandez.paradise.ui.audioStreaming

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.axelfernandez.paradise.R
import com.axelfernandez.paradise.services.PlayerNotificationService
import kotlinx.android.synthetic.main.audio_streaming_fragment.*


class AudioStreamingFragment : Fragment() {

    companion object {
        fun newInstance() = AudioStreamingFragment()
    }

    private lateinit var viewModel: AudioStreamingViewModel
    private lateinit var mService: PlayerNotificationService
    private var mBound: Boolean = false

    lateinit var intent : Intent

    private val connection = object: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerNotificationService.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.audio_streaming_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AudioStreamingViewModel::class.java)
        val constraintLayout: ConstraintLayout = view?.findViewById(R.id.layout) as ConstraintLayout
        val animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()


        intent = Intent(requireContext(), PlayerNotificationService::class.java)
        requireContext().startService(intent)




        playerImage.setOnClickListener {
            if (mBound) {
                when (mService.isPlaying){
                    false ->{
                        mService.play()
                        //Picasso.with(requireContext()).load(R.drawable.exo_icon_pause).into(it.playerImage)
                        playerImage.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)

                    }
                    true ->{
                        mService.pause()
                        //Picasso.with(requireContext()).load(R.drawable.exo_icon_play).into(it.playerImage)
                        playerImage.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)


                    }
                }

            }
        }

    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(requireContext(), PlayerNotificationService::class.java).also { intent ->
            requireContext().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        requireContext().unbindService(connection)
        mBound = false
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().stopService(intent)

    }
}