package android.example.oneTap.Presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.example.oneTap.Utils.PermissionUtils
import android.example.oneTap.Presentation.ViewModel.myviewmodel
import android.example.oneTap.R
import android.example.oneTap.Room.myLocation
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LifecycleObserver, location_Adapter.OnClickHandler {

    private lateinit var madapter: location_Adapter

    private val myviewmodel: myviewmodel by viewModels()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init_recyclerview()
        adddataset()

        //making sure we have permission to access location before fetching location
        save_location_cards_view.setOnClickListener {
            when {
                PermissionUtils.isAccessFineLocationGranted(this) -> {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            startSaveLocationActivity()
                        }
                        else -> {

                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                }
                else -> {
                    PermissionUtils.requestAccessFineLocationPermission(
                        this,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }

    }

//    initializing the recycler view
    private fun init_recyclerview() {
        recyclerview_fragmentc?.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
            madapter = location_Adapter(this@MainActivity)
            adapter = madapter
        }
        addSwipeFunctionality()

    }
//    adding data to the recycler view
    private fun adddataset() {
        lifecycleScope.launch {
            myviewmodel.location.collectLatest {
                Log.i("#@#", "collected ${it.size}")
                madapter.difer.submitList(it)
            }
        }
    }

    //swipe functionality for recycler view items
    private fun addSwipeFunctionality() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {

                //Remove swiped item from list and notify the RecyclerView
                val position = viewHolder.adapterPosition
                lifecycleScope.launch {
                    myviewmodel.deleteLocation(madapter.getlocation().get(position))
                }
                Toast.makeText(
                    applicationContext,
                    "Deleted ${madapter.getlocation().get(position).name}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerview_fragmentc)
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    when {

                        PermissionUtils.isLocationEnabled(this) -> {
                            startSaveLocationActivity()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {

                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


//    handling click on the recyclerview items
    override fun onClick(m: myLocation?) {
        Log.i("!@!", "pressed travel to")
        val uri = java.lang.String.format(
            Locale.ENGLISH,
            "http://maps.google.com/maps?q=loc:%f,%f",
            m?.lattitude,
            m?.longitude
        )
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private fun startSaveLocationActivity() {
        val intent = Intent(this, Save_Location_Activity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in, R.anim.nothing)
    }


}
