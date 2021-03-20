package com.example.therivertask

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {
    private val leftBankItemBias = 0.05f
    private val rightBankItemBias = 0.95f
    private val leftBankBoatBias = 0.25f
    private val rightBankBoatBias = 0.75f

    private var isForInfo = false

    private lateinit var wolf: ImageView
    private lateinit var goat: ImageView
    private lateinit var cabbage: ImageView
    private lateinit var boat: ImageView

    private lateinit var wolfCurrentLayoutParams: ConstraintLayout.LayoutParams
    private lateinit var goatCurrentLayoutParams: ConstraintLayout.LayoutParams
    private lateinit var cabbageCurrentLayoutParams: ConstraintLayout.LayoutParams
    private lateinit var boatCurrentLayoutParams: ConstraintLayout.LayoutParams

    private lateinit var startButton: ImageButton
    private lateinit var refreshButton: ImageButton
    private lateinit var resetButton: ImageButton
    private lateinit var infoButton: ImageButton
    private lateinit var loseLabel: TextView

    private lateinit var winSoundPlayer: MediaPlayer
    private lateinit var loseSoundPlayer: MediaPlayer

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initInfoScreen() {
        startButton = findViewById(R.id.startButton)

        startButton.setOnClickListener {
            startState()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initMainScreen() {
        wolf = findViewById(R.id.wolf)
        goat = findViewById(R.id.goat)
        cabbage = findViewById(R.id.cabbage)
        boat = findViewById(R.id.boat)
        resetButton = findViewById(R.id.resetButton)
        infoButton = findViewById(R.id.infoButton)

        wolf.setOnClickListener {
            onItemClick(wolf)
        }
        goat.setOnClickListener {
            onItemClick(goat)
        }
        cabbage.setOnClickListener {
            onItemClick(cabbage)
        }
        boat.setOnClickListener {
            onBoatClick()
        }

        resetButton.setOnClickListener {
            resetPositions()
        }
        infoButton.setOnClickListener {
            savePositions()
            isForInfo = true
            initialState()
        }

        if (isForInfo) {
            isForInfo = false
            setCurrentPositions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initLoseScreen(message: String) {
        refreshButton = findViewById(R.id.refreshButton)
        loseLabel = findViewById(R.id.loseLabel)

        loseLabel.text = message

        refreshButton.setOnClickListener {
            startState()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initWinScreen() {
        refreshButton = findViewById(R.id.refreshButton)

        refreshButton.setOnClickListener {
            startState()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun initialState() {
        setContentView(R.layout.info_screen)
        initInfoScreen()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun startState() {
        setContentView(R.layout.activity_main)
        initMainScreen()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun winState() {
        winSoundPlayer.start()
        setContentView(R.layout.win_screen)
        initWinScreen()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun loseState(message: String) {
        loseSoundPlayer.start()
        setContentView(R.layout.lose_screen)
        initLoseScreen(message)
    }

    private fun savePositions() {
        wolfCurrentLayoutParams = wolf.layoutParams as ConstraintLayout.LayoutParams
        goatCurrentLayoutParams = goat.layoutParams as ConstraintLayout.LayoutParams
        cabbageCurrentLayoutParams = cabbage.layoutParams as ConstraintLayout.LayoutParams
        boatCurrentLayoutParams = boat.layoutParams as ConstraintLayout.LayoutParams
    }

    private fun resetPositions() {
        val wolfLayoutParams = wolf.layoutParams as ConstraintLayout.LayoutParams
        val goatLayoutParams = goat.layoutParams as ConstraintLayout.LayoutParams
        val cabbageLayoutParams = cabbage.layoutParams as ConstraintLayout.LayoutParams
        val boatLayoutParams = boat.layoutParams as ConstraintLayout.LayoutParams

        wolfLayoutParams.horizontalBias = leftBankItemBias
        goatLayoutParams.horizontalBias = leftBankItemBias
        cabbageLayoutParams.horizontalBias = leftBankItemBias
        boatLayoutParams.horizontalBias = leftBankBoatBias

        wolf.layoutParams = wolfLayoutParams
        goat.layoutParams = goatLayoutParams
        cabbage.layoutParams = cabbageLayoutParams
        boat.layoutParams = boatLayoutParams
    }

    private fun setCurrentPositions() {
        wolf.layoutParams = wolfCurrentLayoutParams
        goat.layoutParams = goatCurrentLayoutParams
        cabbage.layoutParams = cabbageCurrentLayoutParams
        boat.layoutParams = boatCurrentLayoutParams
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkGameState() {
        val wolfLayoutParams = wolf.layoutParams as ConstraintLayout.LayoutParams
        val goatLayoutParams = goat.layoutParams as ConstraintLayout.LayoutParams
        val cabbageLayoutParams = cabbage.layoutParams as ConstraintLayout.LayoutParams
        val boatLayoutParams = boat.layoutParams as ConstraintLayout.LayoutParams

        if (
                wolfLayoutParams.horizontalBias == goatLayoutParams.horizontalBias
                && kotlin.math.abs(wolfLayoutParams.horizontalBias - boatLayoutParams.horizontalBias) > 0.3f
        )
            loseState("Волк съел козу. Попробовать еще раз?")
        else if (
                goatLayoutParams.horizontalBias == cabbageLayoutParams.horizontalBias
                && kotlin.math.abs(goatLayoutParams.horizontalBias - boatLayoutParams.horizontalBias) > 0.3f
        )
            loseState("Коза съела капусту. Попробовать еще раз?")
        else if (
                wolfLayoutParams.horizontalBias == goatLayoutParams.horizontalBias
                && wolfLayoutParams.horizontalBias == cabbageLayoutParams.horizontalBias
                && wolfLayoutParams.horizontalBias == rightBankItemBias
        )
            winState()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onItemClick(item: ImageView) {
        val itemLayoutParams = item.layoutParams as ConstraintLayout.LayoutParams
        val boatLayoutParams = boat.layoutParams as ConstraintLayout.LayoutParams

        if (itemLayoutParams.horizontalBias == leftBankItemBias)
            if (boatLayoutParams.horizontalBias == leftBankBoatBias) {
                itemLayoutParams.horizontalBias = rightBankItemBias
                boatLayoutParams.horizontalBias = rightBankBoatBias
            } else
                Toast.makeText(this, "Лодка на другом берегу", Toast.LENGTH_SHORT).show()
        else
            if (boatLayoutParams.horizontalBias == rightBankBoatBias) {
                itemLayoutParams.horizontalBias = leftBankItemBias
                boatLayoutParams.horizontalBias = leftBankBoatBias
            } else
                Toast.makeText(this, "Лодка на другом берегу", Toast.LENGTH_SHORT).show()

        item.layoutParams = itemLayoutParams;
        boat.layoutParams = boatLayoutParams

        checkGameState()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onBoatClick() {
        val boatLayoutParams = boat.layoutParams as ConstraintLayout.LayoutParams

        if (boatLayoutParams.horizontalBias == leftBankBoatBias)
            boatLayoutParams.horizontalBias = rightBankBoatBias
        else
            boatLayoutParams.horizontalBias = leftBankBoatBias

        boat.layoutParams = boatLayoutParams

        checkGameState()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        winSoundPlayer = MediaPlayer.create(this, R.raw.winsound)
        loseSoundPlayer = MediaPlayer.create(this, R.raw.losesound)

        supportActionBar!!.hide()

        initialState()
    }
}