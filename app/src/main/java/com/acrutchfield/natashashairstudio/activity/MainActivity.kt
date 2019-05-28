package com.acrutchfield.natashashairstudio.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.acrutchfield.natashashairstudio.R
import com.acrutchfield.natashashairstudio.fragment.BookAppointmentFragment
import com.acrutchfield.natashashairstudio.fragment.ReviewFragment
import com.acrutchfield.natashashairstudio.fragment.ShopFragment
import com.acrutchfield.natashashairstudio.fragment.SocialFragment
import com.acrutchfield.natashashairstudio.utils.AppointmentReminderTask
import com.acrutchfield.natashashairstudio.utils.AppointmentReminderUtils
import com.acrutchfield.natashashairstudio.utils.SharedPrefs
import com.acrutchfield.natashashairstudio.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {  item ->
        when (item.itemId) {
            R.id.navigation_shop -> {
                replaceFragment(ShopFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_book -> {
                replaceFragment(BookAppointmentFragment.newInstance(getString(R.string.url_string)))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_review -> {
                replaceFragment(ReviewFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_social -> {
                replaceFragment(SocialFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        checkConnectionStatus()

        if (savedInstanceState != null) {
            val selectedFragment = savedInstanceState.getInt(getString(R.string.key_selected_fragment))
            navigation!!.selectedItemId = selectedFragment
        } else navigation!!.selectedItemId = R.id.navigation_shop
        

        if (SharedPrefs.getReminder(this)) AppointmentReminderUtils.scheduleAppointmentReminder(this)
        else AppointmentReminderUtils.cancelAppointmentReminder(this)
        
        handleIntent()
        fab_profile.setOnClickListener { launchProfileActivity() }

    }

    private fun handleIntent() {
        if (intent != null) {
            when (intent.action!!) {
                getString(R.string.action_social) -> navigation.selectedItemId = R.id.navigation_shop
                getString(R.string.action_book), AppointmentReminderTask.ACTION_APPOINTMENT_REMINDER -> navigation.selectedItemId = R.id.navigation_book
                getString(R.string.action_review) -> navigation.selectedItemId = R.id.navigation_review
                getString(R.string.action_social) -> navigation.selectedItemId = R.id.navigation_social
            }
        }
    }

    private fun launchProfileActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(getString(R.string.key_selected_fragment), navigation.selectedItemId)
    }

    private fun checkConnectionStatus(): Boolean {
        //Check the connection status
        val connected = Utils.isOnline(this)
        //If there is no network connection, alert the user and prompt for next action
        if (!connected) {
            val alert = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.title_offline))
                    .setMessage(getString(R.string.message_network_warning))
                    .setPositiveButton(getString(R.string.button_network_settings)) { _, _ ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) }
                    .setNegativeButton(getString(R.string.button_exit)) { _, _ ->
                        Toast.makeText(this@MainActivity, getString(R.string.toast_try_again), Toast.LENGTH_SHORT).show()
                        finish()
                    }
            val dialog = alert.create()
            dialog.show()
        }
        return connected
    }
}
