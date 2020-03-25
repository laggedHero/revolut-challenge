package net.laggedhero.revolut.challenge.core.livedata

class KMutableLiveData<T>(value: T) : KLiveData<T>(value) {

    public override fun postValue(value: T) {
        super.postValue(value)
    }

    public override fun setValue(value: T) {
        super.setValue(value)
    }
}