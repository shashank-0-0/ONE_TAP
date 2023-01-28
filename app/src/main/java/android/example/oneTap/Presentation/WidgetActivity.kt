package android.example.oneTap.Presentation

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.example.oneTap.BaseApplication
import android.example.oneTap.Presentation.ViewModel.myviewmodel
import android.example.oneTap.R
import android.example.myapplication.Repository.location_repository
import android.example.myapplication.Repository.location_repository_implementation
import android.example.oneTap.Utils.Resource
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

//Invisible activity that saves the location

@AndroidEntryPoint
class widgetActivity: AppCompatActivity() {

    private val myviewmodel: myviewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.widget_activity_layout)
        Log.i("#@#", "invisible oncreate")

        lifecycleScope.launch {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID,buildNotification())
            myviewmodel.fetch_location()
            myviewmodel.state.collectLatest {result ->
                when(result){
                    is Resource.Success -> {
                        notificationManager.cancel(NOTIFICATION_ID)
                        Toast.makeText(this@widgetActivity,"LOCATION SAVED",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is Resource.Error ->{
                        Toast.makeText(this@widgetActivity,"Some Error Occured",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }


    //function to build a persisting notification to make sure user is aware of invisible activity
    private fun buildNotification():Notification{
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this,
            BaseApplication.CHANNEL_ID
        )
            .setContentTitle("Service")
            .setContentText("saving current location")
            .setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_baseline_add_location_24)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        return notification

    }
    companion object{
        val NOTIFICATION_ID=100
    }
}
