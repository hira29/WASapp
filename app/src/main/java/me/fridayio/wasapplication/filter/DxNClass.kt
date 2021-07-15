package me.fridayio.wasapplication.filter

import android.util.Log

class DxNClass {
    private var D = 15
    private var N: Float = 25F
    private var i: Int = 180
    private var j: Int = -12
    private var k: Int = 0
    private var jmax: Int = 12
    private var O = mutableListOf<Float>()
    private var Pool = mutableListOf<Float>()
    private var NewData = mutableListOf<Float>()
    private var firstRunner: Boolean = true

    fun <T> MutableList<T>.prependAll(elements: List<T>) {
        addAll(0, elements)
    }

    fun DxNFilter(Data: MutableList<Float>): MutableList<Float>{
        i = 180; j = -12; k = 0
        O.clear()
        NewData.clear()

        if (firstRunner){
            for (k in 0..179){
                Pool.add(0F)
            }
            firstRunner = false
        }

        NewData.prependAll(Data)
        NewData.prependAll(Pool)

        Pool.clear()
        k = Data.size - 181
        while( k <= Data.size - 1){
            Pool.add(Data[k])
            k++
        }

        while (i <= NewData.size - 181){
            var tmp = 0F
            j = -12
            while (j <= jmax) {
                val m = j*D
                tmp += NewData[i+m]
                j++
            }
            val Output = NewData[i] - tmp/N
            O.add(Output)
            i++
        }
        return O
    }
}