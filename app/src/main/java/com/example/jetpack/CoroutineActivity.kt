package com.example.jetpack

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myrecyclerview.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class CoroutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_coroutine_activity)

        val sum = { a: Int, b: Int ->
            a + b
        }

        sum(1, 2)

        runBlocking {
            val isSuccess = copyFileTo(File(""), File(""))
        }

    }

    private suspend fun copyFileTo(oldFile: File, newFile: File): Boolean {
        val isCopySuccess = withContext(Dispatchers.IO) {
            try {
                oldFile.copyTo(newFile)
                true
            } catch (e: Exception) {
                false
            }
        }
        return isCopySuccess
    }
}