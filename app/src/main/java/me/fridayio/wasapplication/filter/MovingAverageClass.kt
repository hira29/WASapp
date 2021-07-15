package me.fridayio.wasapplication.filter

import android.util.Log

class MovingAverageClass {
    private var N: Float = 15F
    private var i: Int = 7
    private var j: Int = -7
    private var k: Int = 0
    private var jmax: Int = 7
    private var O = mutableListOf<Float>()
    private var Pool = mutableListOf<Float>()
    private var NewData = mutableListOf<Float>()
    private var firstRunner: Boolean = true

    fun <T> MutableList<T>.prependAll(elements: List<T>) {
        addAll(0, elements)
    }

    fun MovAvg(Data: MutableList<Float>): MutableList<Float>{
        i = 7; j = -7; k = 0
        O.clear()
        NewData.clear()

        if (firstRunner){
            for (k in 0..6){
                Pool.add(0F)
            }
            firstRunner = false
        }

        NewData.prependAll(Data)
        NewData.prependAll(Pool)

        Pool.clear()
        k = Data.size - 8
        while( k <= Data.size - 1){
            Pool.add(Data[k])
            k++
        }

        while (i <= NewData.size - 8){
            var tmp = 0F
            j = -7
            while (j <= jmax) {
                val n = i+j
                tmp += NewData[n]
                j++
            }
            val m = tmp/N
            O.add(m)
            i++
        }
        return O
    }
}