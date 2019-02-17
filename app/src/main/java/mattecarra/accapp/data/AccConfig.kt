package mattecarra.accapp.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import mattecarra.accapp.utils.AccUtils

class Cooldown(charge: Int, pause: Int, hasChanges: Boolean = false): Parcelable {
    var hasChanges: Boolean = hasChanges
        private set

    var charge: Int = charge
        set(value) {
            field = value
            hasChanges = true
        }

    var pause: Int = pause
        set(value) {
            field = value
            hasChanges = true
        }

    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readInt(), parcel.readByte() != 0.toByte())

    fun updateAcc() {
        if(hasChanges) {
            AccUtils.updateCoolDown(charge, pause)
            hasChanges = false
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(charge)
        parcel.writeInt(pause)
        parcel.writeByte(if (hasChanges) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cooldown> {
        override fun createFromParcel(parcel: Parcel): Cooldown {
            return Cooldown(parcel)
        }

        override fun newArray(size: Int): Array<Cooldown?> {
            return arrayOfNulls(size)
        }
    }
}

class Capacity(shutdownCapacity: Int, coolDownCapacity: Int, resumeCapacity: Int, pauseCapacity: Int, hasChanges: Boolean = false): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readByte() != 0.toByte())

    var hasChanges: Boolean = hasChanges
        private set

    var shutdownCapacity: Int = shutdownCapacity
        set(value) {
            field = value
            hasChanges = true
        }

    var coolDownCapacity: Int = coolDownCapacity
        set(value) {
            field = value
            hasChanges = true
        }

    var resumeCapacity: Int = resumeCapacity
        set(value) {
            field = value
            hasChanges = true
        }

    var pauseCapacity: Int = pauseCapacity
        set(value) {
            field = value
            hasChanges = true
        }

    fun updateAcc() {
        if(hasChanges) {
            AccUtils.updateCapacity(shutdownCapacity, coolDownCapacity, resumeCapacity, pauseCapacity)
            hasChanges = false
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(shutdownCapacity)
        parcel.writeInt(coolDownCapacity)
        parcel.writeInt(resumeCapacity)
        parcel.writeInt(pauseCapacity)
        parcel.writeByte(if (hasChanges) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Capacity> {
        override fun createFromParcel(parcel: Parcel): Capacity {
            return Capacity(parcel)
        }

        override fun newArray(size: Int): Array<Capacity?> {
            return arrayOfNulls(size)
        }
    }
}

class Temp(coolDownTemp: Int, pauseChargingTemp: Int, waitSeconds: Int, hasChanges: Boolean = false): Parcelable {
    var hasChanges: Boolean = hasChanges
        private set

    var coolDownTemp: Int = coolDownTemp
        set(value) {
            field = value
            hasChanges = true
        }

    var pauseChargingTemp: Int = pauseChargingTemp
        set(value) {
            field = value
            hasChanges = true
        }

    var waitSeconds: Int = waitSeconds
        set(value) {
            field = value
            hasChanges = true
        }

    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readByte() != 0.toByte())

    fun updateAcc() {
        if(hasChanges) {
            AccUtils.updateTemp(coolDownTemp, pauseChargingTemp, waitSeconds)
            hasChanges = false
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(coolDownTemp)
        parcel.writeInt(pauseChargingTemp)
        parcel.writeInt(waitSeconds)
        parcel.writeByte(if (hasChanges) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Temp> {
        override fun createFromParcel(parcel: Parcel): Temp {
            return Temp(parcel)
        }

        override fun newArray(size: Int): Array<Temp?> {
            return arrayOfNulls(size)
        }
    }
}

class AccConfig(
    val capacity: Capacity,
    var cooldown: Cooldown?,
    val temp: Temp,
    var resetUnplugged: Boolean,
    var onBootExit: Boolean,
    var onBoot: String?,
    hasChanges: Boolean = false): Parcelable {

    private var localHasChanges = hasChanges

    val hasChanges: Boolean
        get() = capacity.hasChanges || cooldown?.hasChanges == true || temp.hasChanges || localHasChanges

    constructor(parcel: Parcel) : this(
        Capacity.createFromParcel(parcel),
        parcel.readParcelable(Cooldown::class.java.classLoader),
        Temp.createFromParcel(parcel),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    )

    fun updateAcc() {
        capacity.updateAcc()

        cooldown?.updateAcc()

        temp.updateAcc()

        if(localHasChanges) {
            AccUtils.updateResetUnplugged(resetUnplugged)
            AccUtils.updateOnBootExit(onBootExit)
            AccUtils.updateOnBoot(onBoot)
            localHasChanges = false
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(capacity, flags)
        parcel.writeParcelable(cooldown, flags)
        parcel.writeParcelable(temp, flags)
        parcel.writeByte(if (resetUnplugged) 1 else 0)
        parcel.writeByte(if (onBootExit) 1 else 0)
        parcel.writeString(onBoot)
        parcel.writeByte(if (localHasChanges) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AccConfig> {
        override fun createFromParcel(parcel: Parcel): AccConfig {
            return AccConfig(parcel)
        }

        override fun newArray(size: Int): Array<AccConfig?> {
            return arrayOfNulls(size)
        }
    }
}