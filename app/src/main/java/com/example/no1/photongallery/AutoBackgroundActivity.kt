package com.example.no1.photongallery

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.*
import com.example.no1.photongallery.utils.JSONParser
import com.example.no1.photongallery.utils.MyAlarmReceiver
import com.example.no1.photongallery.utils.MyJsonArray
import com.example.no1.photongallery.utils.MyJsonObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_auto_background.*
import org.apache.http.NameValuePair
import java.lang.ref.WeakReference
import java.util.ArrayList

class AutoBackgroundActivity : AppCompatActivity() {
    lateinit var mAreaIcons: RecyclerView

    ///////////////////Mohammad ///////////////////
    var periodicTime = AlarmManager.INTERVAL_FIFTEEN_MINUTES
    ///////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_background)

        val font = Typeface.createFromAsset(assets, "fonts/iranyekanwebregular.ttf")
        var textView = findViewById<TextView>(R.id.lblStatus)
        textView.typeface = font

        /////////////////// Mohammad////////////////////
        val textView2 = findViewById<TextView>(R.id.txtStatus)
        textView2.typeface = font
        //TODO laod sate from SharedPref and set it into sw
        val sw = findViewById<SwitchCompat>(R.id.chkActive)
        sw?.setOnCheckedChangeListener({ _, isChecked ->
            val msg = if (isChecked) "فعال" else "غیر فعال"
            textView2.text = msg
            //  run  ChangeBackground
            if (sw.isChecked) {
                scheduleAlarm()
                //   Toast.makeText(this, "Hi there! This is a stae1.", Toast.LENGTH_SHORT).show()
            } else cancelAlarm()  //  cancel  ChangeBackground
        })
        val textView3 = findViewById<TextView>(R.id.txtSchedule)
        textView3.typeface = font
        val spinner = findViewById<Spinner>(R.id.spinner)
        // Initializing a String Array
        val colors = arrayOf("نیم ساعت", "1ساعت", "12ساعت", "24ساعت")

        // Initializing an ArrayAdapter
        val adapter = ArrayAdapter(this, // Context
                android.R.layout.simple_spinner_item, // Layout
                colors // Array
        )

        // Set the drop down view resource
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        // Finally, data bind the spinner object with dapter
        spinner.adapter = adapter;
        // Set an on item selected listener for spinner object
        //Todo save this settings in SHaredPref too
        //TODO also change intervals, Contact Naser to get interval list ;-)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //   // the Periodic_Time changes based on the spinner but becarefull sw must be on
                if (position == 0) {
                    periodicTime = 30000//AlarmManager.INTERVAL_FIFTEEN_MINUTES
                }
                if (position == 1) {
                    periodicTime = AlarmManager.INTERVAL_HOUR
                }
                if (position == 2) {
                    periodicTime = AlarmManager.INTERVAL_HALF_DAY
                }
                if (position == 3) {
                    periodicTime = AlarmManager.INTERVAL_DAY
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        /////////////////////////////////////////////////////

        textView = findViewById<TextView>(R.id.lblDescription)
        textView.typeface = font
        textView = findViewById<TextView>(R.id.lblSchedule)
        textView.typeface = font
//        textView = findViewById<TextView>(R.id.txtSchedule)
//        textView.typeface = font
        textView = findViewById<TextView>(R.id.lblGallery)
        textView.typeface = font
        textView = findViewById<TextView>(R.id.lblAllGallery)
        textView.typeface = font
        textView = findViewById<TextView>(R.id.lblSelectedGallery)
        textView.typeface = font
        textView = findViewById<TextView>(R.id.lblSpecialGallery)
        textView.typeface = font

        mAreaIcons = findViewById(R.id.areaIcons);
        LoadIcons(this).execute()
    }

    private class LoadIcons(context: Activity) : AsyncTask<String, String, String>() {

        var result: MyJsonObject? = null
        var items: MyJsonArray? = null
        var parent: WeakReference<Activity> = WeakReference(context)
        lateinit var pDialog: ProgressDialog
        lateinit var list: ArrayList<String>
        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = ProgressDialog(parent.get())
            pDialog.setMessage("در حال اتصال به سرور")
            pDialog.isIndeterminate = false
            pDialog.setCancelable(false)
            pDialog.show()
        }

        override fun doInBackground(vararg p0: String?): String? {
            val params = ArrayList<NameValuePair>()
            val jParser = JSONParser()
            val context: Context? = parent.get()
            result = MyJsonObject(jParser.makeHttpRequest(context!!.getString(R.string.server_address) +
                    "/api/galleries/", "GET", params, context))
            if (result != null && !result!!.isNull)
                items = MyJsonArray(result!!.getJSONArraySafe("results"))
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pDialog.dismiss()
            val context = parent.get()
            context!!.runOnUiThread {
                if (items != null && !items!!.isNull) {
                    list = ArrayList()
                    for (i in 0 until items!!.length()) {
                        val item = MyJsonObject(items!!.getJSONObjectSafe(i))
                        if (!item.isNull) {
                            list.add(context.getString(R.string.server_address) +
                                    item.getStringSafe("cover_pic"))
                        }
                    }
                    val adapter = AutoBGIconAdapter(context, list)
                    val activity = context as AutoBackgroundActivity
                    val llm = LinearLayoutManager(context)
                    activity.mAreaIcons.layoutManager = llm
                    activity.mAreaIcons.adapter = adapter
                }
            }
        }
    }

    override fun onBackPressed() {
        val sw = findViewById<SwitchCompat>(R.id.chkActive)
        val intent = Intent(this, MainActivity::class.java)
        if (sw.isChecked) {
            // To pass any data to next activity
            intent.putExtra("keyIdentifier", "1")
            // start your next activity
            setResult(RESULT_OK, intent);
            finish();
        } else {
            // To pass any data to next activity
            intent.putExtra("keyIdentifier", "0")
            // start your next activity
            setResult(RESULT_OK, intent);
            finish();
            // super.onBackPressed()
        }
    }

    // Setup a recurring alarm every half hour
    private fun scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        val intent = Intent(applicationContext, MyAlarmReceiver::class.java)
        // Create a PendingIntent to be triggered when the alarm goes off
        val pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Setup periodic alarm every every half hour from this point onwards
        val firstMillis = System.currentTimeMillis() // alarm is set right away
        val alarm = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                periodicTime, pIntent)

        // the Periodic_Time changes based on the spinner
    }

    private fun cancelAlarm() {
        val intent = Intent(applicationContext, MyAlarmReceiver::class.java)
        val pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarm = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.cancel(pIntent)
    }


}