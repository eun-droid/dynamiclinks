package com.google.firebase.quickstart.deeplinks.kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.google.firebase.quickstart.deeplinks.R
import com.google.firebase.quickstart.deeplinks.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // [START on_create]
    override fun onCreate(savedInstanceState: Bundle?) {
        // [START_EXCLUDE]
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linkReceiveTextView = binding.linkViewReceive

        // Validate that the developer has set the app code.
        validateAppCode()

        // Create a deep link and display it in the UI
        buildDeepLink(makeInviteLink(), 0)
        // [END_EXCLUDE]

        // [START get_deep_link]
        Firebase.dynamicLinks
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    // Get deep link from result (may be null if no link is found)
                    var deepLink: Uri? = null
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.link
                    }

                    // Handle the deep link. For example, open the linked
                    // content, or apply promotional credit to the user's
                    // account.
                    // ...

                    // [START_EXCLUDE]
                    // Display deep link in the UI
                    if (deepLink != null) {
                        Snackbar.make(findViewById(android.R.id.content),
                                "Found deep link!", Snackbar.LENGTH_LONG).show()

                        linkReceiveTextView.text = deepLink.toString()

                        when (deepLink.lastPathSegment) {
                            "invite" -> {
                                val code = deepLink.getQueryParameter("code")
                                Log.d(TAG, "Dynamic Link Open ${deepLink.lastPathSegment}, code = $code")

                                // ???????????? ????????????????
                                val intent: Intent = if (DataManager.login) {
                                    // Friends ???????????? ??????
                                    Intent(this, FriendsActivity::class.java)
                                } else {
                                    // ????????? ???????????? ??????
                                    Intent(this, LoginActivity::class.java)
                                }

                                intent.putExtra("code", code)   // ????????? ????????????
                                startActivity(intent)
                            }
                        }

                    } else {
                        Log.d(TAG, "getDynamicLink: no link found")
                    }
                    // [END_EXCLUDE]
                }
                .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
        // [END get_deep_link]
    }
    // [END on_create]

    /**
     * Build a Firebase Dynamic Link.
     * https://firebase.google.com/docs/dynamic-links/android/create#create-a-dynamic-link-from-parameters
     *
     * @param deepLink the deep link your app will open. This link must be a valid URL and use the
     * HTTP or HTTPS scheme.
     * @param minVersion the `versionCode` of the minimum version of your app that can open
     * the deep link. If the installed app is an older version, the user is taken
     * to the Play store to upgrade the app. Pass 0 if you do not
     * require a minimum version.
     * @return a [Uri] representing a properly formed deep link.
     */
    @VisibleForTesting
    fun buildDeepLink(deepLink: Uri, minVersion: Int) {
        val uriPrefix = getString(R.string.dynamic_links_uri_prefix)

        // Set dynamic link parameters:
        //  * URI prefix (required)
        //  * Android Parameters (required)
        //  * Deep link
        // [START build_dynamic_link]
        // Build the dynamic link
        Firebase.dynamicLinks.shortLinkAsync {
            link = deepLink
            domainUriPrefix = uriPrefix
            androidParameters {
                minimumVersion = minVersion
            }
        }.addOnSuccessListener {
            val shortLink = it.shortLink.toString()

            binding.linkViewSend.text = shortLink

            // Share button click listener
            binding.buttonShare.setOnClickListener { shareDeepLink(shortLink) }
        }

        val longLink = Firebase.dynamicLinks.dynamicLink {
            link = deepLink

            domainUriPrefix = uriPrefix
            androidParameters {
                minimumVersion = minVersion
            }
        }

        binding.linkViewLong.text = longLink.uri.toString()
        // [END build_dynamic_link]

        // Return the dynamic link as a URI
//        return link.uri
    }

    private fun makeInviteLink() : Uri {
        val inviteLink = getString(R.string.dynamic_links_uri_prefix) + INVITE_SEGMENT
        return Uri.parse(inviteLink)
            .buildUpon()
            .appendQueryParameter("code", "1234567")
            .build()
    }

    private fun shareDeepLink(deepLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Deep Link")
        intent.putExtra(Intent.EXTRA_TEXT, deepLink)

        startActivity(intent)
    }

    private fun validateAppCode() {
        val uriPrefix = getString(R.string.dynamic_links_uri_prefix)
        if (uriPrefix.contains("YOUR_APP")) {
            AlertDialog.Builder(this)
                    .setTitle("Invalid Configuration")
                    .setMessage("Please set your Dynamic Links domain in app/build.gradle")
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.loginStatus.text = DataManager.login.toString()
    }

    companion object {

        private const val TAG = "MainActivity"
//        private const val DEEP_LINK_URL = "https://kotlin.example.com/deeplinks"
        private const val DEEP_LINK_URL = "https://eunsub.com"
        private const val INVITE_SEGMENT = "/invite"
    }
}
