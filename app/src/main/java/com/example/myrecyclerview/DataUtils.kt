package com.example.myrecyclerview

class DataUtils {
    companion object {
        const val TAG = "wmkwmk"
        fun getData(): List<Int> {
            return arrayListOf<Int>().apply {
                repeat(20) {
                    add(it)
                }
            }
        }

        fun getStringData(): List<String> {
            return getData().map { "$it" }
        }

        fun getNewData(): List<Int> {
            return arrayListOf<Int>().apply {
//                repeat(20) {
//                    add(it)
//                }
                add(2)
                add(4)
                add(6)
            }
        }
    }
}