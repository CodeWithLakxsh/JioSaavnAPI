package com.laksh.jarvismusic

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

data class SelectionItem(val name: String, val imageUrl: String, var isSelected: Boolean = false)

class OnboardingActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var btnNext: MaterialButton
    private lateinit var tvTitle: TextView

    private val selectedItems = mutableListOf<String>()

    // --- ARTISTS LIST ---
    private val artists = listOf(
        SelectionItem("Sidhu Moose Wala", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJdEcXaCXdx1sM2bPH-_uWj2UwEHkz3jtJHOobOxAu6vMQYQbSCDDWFessB633dJ7Hk-RoO0c_tRkSW7A6TX39DXeRxJ1_KaoiFiSFr6-v&s=10"),
        SelectionItem("Karan Aujla", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9Y18c9heMbV95Veq1ilfjGeVxROeSxUMS8sTjJCXxbhF8y4oudkwkMZfndm-J25QhQQuokNBKOQSD_kk9o1zrcNyLP8vJyGHczPLY8PzS&s=10"),
        SelectionItem("Arijit Singh", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRETraqkav99oDFbUFGsbCDZl0Mteg_X51A8QGEdAE7vwEtf5WxDLa__Nl6HP71zQTFI7scIFhdXe-HvmMlZTX9grvaQBvTHDmR8-dRxiCzGQ&s=10"),
        SelectionItem("Badshah", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT2tBCiyAppsoI55CUm96M4hWI2h-kLR0jKxTnFVdhspmMCan60c7K0leP3pyObxFKjtnAivpXAHWcwqmCwsqT35YsLHFXpcGUU6LCPc0lj&s=10"),
        SelectionItem("Diljit Dosanjh", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXfY1DhaCBMBuzZvxk-gDFNsU6ECeU25uZSIdJgXPCQ6VIp5ipoLztB6Nf4XlSMSd9lkwTMa4QCx94xuXpDwV7zbgkSAIEojiM_ByHv5a6&s=10"),
        SelectionItem("Shubh", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSw6PLNP_f0QyZcsXrEpcXxHulnVgYu9PIt_s3E6scoX6U-NNV-20SNWTeXG69Am31Zh0wuoXvbYOq0Fqmi3T_j3jUi9FMgzV9DDRU5iB0A&s=10"),
        SelectionItem("Masoom Sharma", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRfAI3PVX5dpvihMDSVQTUVYgh8QuLBRFCIRpjhIJ7DmtsnfT6TGr_-d1vMJufZox9tmPeMSYLd_BjyD0hMgluvLzQrC4NoiGlVd-hQkjdh&s=10"),
        SelectionItem("KD", "https://c.saavncdn.com/artists/KD_20191130133214_500x500.jpg")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        rv = findViewById(R.id.rv_onboarding)
        btnNext = findViewById(R.id.btn_next_onboarding)
        tvTitle = findViewById(R.id.tv_onboarding_title)

        // Set Title for direct Artist selection
        tvTitle.text = "Choose 3 Artists"
        btnNext.text = "FINISH (0/3)"

        rv.layoutManager = GridLayoutManager(this, 2)
        setupAdapter(artists)

        btnNext.setOnClickListener {
            if (selectedItems.size < 3) {
                Toast.makeText(this, "Please select at least 3 artists", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save onboarding status and finish
            val prefs = getSharedPreferences("JarvisPrefs", Context.MODE_PRIVATE)
            prefs.edit().putBoolean("isFirstTime", false).apply()

            // Go back to MainActivity
            finish()
        }
    }

    private fun setupAdapter(list: List<SelectionItem>) {
        rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(p: ViewGroup, t: Int) = object : RecyclerView.ViewHolder(
                LayoutInflater.from(p.context).inflate(R.layout.item_onboarding, p, false)
            ) {}

            override fun onBindViewHolder(h: RecyclerView.ViewHolder, i: Int) {
                val item = list[i]
                val name = h.itemView.findViewById<TextView>(R.id.tv_selection_name)
                val img = h.itemView.findViewById<ShapeableImageView>(R.id.img_selection)

                name.text = item.name

                // Glide with Header fix for encrypted CDN links
                val glideUrl = GlideUrl(item.imageUrl, LazyHeaders.Builder()
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .build())

                Glide.with(this@OnboardingActivity)
                    .asBitmap()
                    .load(glideUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.ic_download)
                    .error(R.drawable.ic_play)
                    .into(img)

                // UI Selection States
                if (item.isSelected) {
                    img.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics)
                    h.itemView.scaleX = 1.08f
                    h.itemView.scaleY = 1.08f
                    h.itemView.alpha = 1.0f
                } else {
                    img.strokeWidth = 0f
                    h.itemView.scaleX = 1.0f
                    h.itemView.scaleY = 1.0f
                    h.itemView.alpha = 0.8f
                }

                h.itemView.setOnClickListener {
                    item.isSelected = !item.isSelected
                    if (item.isSelected) {
                        selectedItems.add(item.name)
                    } else {
                        selectedItems.remove(item.name)
                    }

                    val count = selectedItems.size
                    btnNext.text = "FINISH ($count/3)"
                    notifyItemChanged(i)
                }
            }
            override fun getItemCount() = list.size
        }
    }
}