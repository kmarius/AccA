package mattecarra.accapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import mattecarra.accapp.acc.Acc
import mattecarra.accapp.database.AccaRoomDatabase
import org.jetbrains.anko.doAsync

class AccBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_PROFILE_SET -> {
                if (intent.hasExtra("profile")) {
                    val profileName = intent.getStringExtra("profile")

                    doAsync {
                        val profiles = AccaRoomDatabase.getDatabase(context).profileDao().getProfileByName(profileName)

                        if (profiles.size > 0) {
                            Acc.instance.updateAccConfig(profiles[0].accConfig)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val ACTION_PROFILE_SET = "mattecarra.accapp.action.PROFILE_SET"
    }
}
