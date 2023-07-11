package com.example.jetpack

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myrecyclerview.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class FlowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_flow)

        val sharedFlow = MutableSharedFlow<Int>(
            replay = 0, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.SUSPEND
        )

        var emitValue = 1

        runBlocking {
            launch {
                sharedFlow.onEach {
                    delay(250)
                    Log.i("wmk", "接收到的: $it")
                }.collect()
            }

            repeat(12) {
                sharedFlow.emit(emitValue)
                emitValue++
                delay(50)
            }
        }

    }

}