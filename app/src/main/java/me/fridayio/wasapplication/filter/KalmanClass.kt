package me.fridayio.wasapplication.filter

class KalmanClass {
    private var R: Float = 10F
    private var H: Float = 1F
    private var Q: Float = 10F
    private var P: Float = 0F
    private var U_hat: Float = 0F
    private var K: Float = 0F

    fun kalmanFilter(U: Float): Float{
        K = P*H/(H*P*H+R)
        U_hat = U_hat + K*(U-H*U_hat)
        P = (1-K*H)*P+Q
        return U_hat
    }
}