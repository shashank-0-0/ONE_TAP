package android.example.atry

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_a.*

@AndroidEntryPoint
class Save_Location_Activity : AppCompatActivity() {

    public val myviewmodel: myviewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_a)

        println("this is contained in my second commit")
        println("experimental")
        println("experimental commit 2")



        save_.setOnClickListener {
            val userEnteredname =outlinedTextField.editText?.text.toString()
            Log.i("#@#","Save location clicked")
            myviewmodel.save_location(userEnteredname){
                if(it==1){
                    Toast.makeText(this, "Location Saved", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "Some error occured ", Toast.LENGTH_LONG).show()
                }

                finish()
                overridePendingTransition(R.anim.nothing,R.anim.slide_out)
            }
        }



    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.nothing,R.anim.slide_out)
    }
}