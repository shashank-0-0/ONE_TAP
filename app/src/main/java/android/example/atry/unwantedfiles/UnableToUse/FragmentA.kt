package android.example.atry.unwantedfiles.UnableToUse

import android.example.atry.R
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_a.*

@AndroidEntryPoint
class FragmentA : Fragment(R.layout.fragment_a) {
    // cannot create an instance because ive tried to inject applicaion context
//    public val myviewmodel: myviewmodel2 by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        save_.setOnClickListener {
//            myviewmodel.fetchAndSaveLocation()
//            Toast.makeText(this.context,"$myviewmodel",Toast.LENGTH_SHORT).show()
//            Log.i("!@!","$myviewmodel")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}