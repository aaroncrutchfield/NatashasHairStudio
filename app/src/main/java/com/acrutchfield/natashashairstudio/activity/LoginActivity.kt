package com.acrutchfield.natashashairstudio.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.acrutchfield.natashashairstudio.R
import com.acrutchfield.natashashairstudio.utils.GlideApp
import com.acrutchfield.natashashairstudio.utils.SharedPrefs
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {


    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar_login)

        val ab = supportActionBar!!
        ab.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()

        setupReminderSwitch()

        btn_sign_in_google.setOnClickListener { signIn() }
        btn_view_wish_list.setOnClickListener { launchWishList() }
        tv_sign_out.setOnClickListener { signOut() }

        tv_delete_account.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) promptForDeleteAccount()
            else Toast.makeText(this@LoginActivity, getString(R.string.toast_delete_warning), Toast.LENGTH_SHORT).show()
        }

        if (auth!!.currentUser != null) signedInUI()
        else signedOutUI()
    }

    private fun launchWishList() {
        val intent = Intent(this, WishListActivity::class.java)
        startActivity(intent)
    }

    private fun setupReminderSwitch() {
        val swAppointmentReminder = findViewById<Switch>(R.id.sw_appointment_reminder)

        // Restore checked state before setting the listener
        if (SharedPrefs.getReminder(this)) swAppointmentReminder.isChecked = true

        swAppointmentReminder.setOnCheckedChangeListener { _, isChecked ->
            SharedPrefs.setReminder(this, isChecked)

            if (isChecked) {
                Toast.makeText(this@LoginActivity, getString(R.string.toast_reminder_set), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@LoginActivity, getString(R.string.toast_reminder_removed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signedOutUI() {
        tv_welcome_title.visibility = View.INVISIBLE
        tv_username.visibility = View.INVISIBLE
        tv_signed_out.visibility = View.VISIBLE

        GlideApp.with(this)
                .load(R.drawable.ic_account_circle_black_124dp)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_login_picture)

        val backgroundColor = Color.parseColor(COLOR_GREY)
        val drawable = ColorDrawable(backgroundColor)

        GlideApp.with(this)
                .load(drawable)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_picture_outline)

    }

    private fun signedInUI() {
        tv_signed_out.visibility = View.INVISIBLE
        tv_welcome_title.visibility = View.VISIBLE

        tv_username.text = auth!!.currentUser!!.displayName
        tv_username.visibility = View.VISIBLE

        GlideApp.with(this)
                .load(auth!!.currentUser!!.photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_login_picture)

        val backgroundColor = Color.parseColor(COLOR_GREY)
        val drawable = ColorDrawable(backgroundColor)

        GlideApp.with(this)
                .load(drawable)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_picture_outline)
    }

    private fun signIn() {
        val providers = Arrays.asList(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo)
                        .setTheme(R.style.AppTheme)
                        .build(),
                REQUEST_SIGN_IN
        )
    }

    private fun signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener { task ->
                    signedOutUI()
                    Toast.makeText(this@LoginActivity, getString(R.string.toast_signed_out), Toast.LENGTH_SHORT).show()
                }
    }

    private fun promptForDeleteAccount() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_delete_account))
                .setMessage(getString(R.string.message_confirm_delete))
                .setPositiveButton(getString(R.string.button_confirm)) { _, _ -> deleteAccount() }
                .setNegativeButton(getString(R.string.button_cancel)) { _, _ ->
                    Toast.makeText(this@LoginActivity, getString(R.string.toast_canceled), Toast.LENGTH_SHORT).show() }
                .create()
                .show()
    }

    private fun deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener {
                    Toast.makeText(this@LoginActivity, getString(R.string.toast_signed_out), Toast.LENGTH_SHORT).show() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGN_IN && resultCode == Activity.RESULT_OK) {
            signedInUI()
            Toast.makeText(this@LoginActivity, getString(R.string.toast_signed_in), Toast.LENGTH_SHORT).show()

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.no_transition, R.anim.slide_down_animation)
            return true
        }
        return false
    }

    companion object {
        private const val REQUEST_SIGN_IN = 0
        private const val COLOR_GREY = "#676767"
    }
}
