package com.harshaapps.onestopforvi

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.harshaapps.onestopforvi.softwares.Software
import com.harshaapps.onestopforvi.softwares.SoftwareList
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar


private const val tag="SoftwareListAdapter"
class SoftwareListAdapter(private val mContext:Context):RecyclerView.Adapter<SoftwareListAdapter.SoftwareListHolder>() {
    private var softwares = SoftwareList.softwares
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoftwareListHolder {
Log.d(tag, "Creating recyclerview for listing. list is empty?  ${softwares.isEmpty()}")
                        val inflater = LayoutInflater.from(mContext)
                                val v = inflater.inflate(R.layout.list_item_software, parent, false)
                Log.d(tag, "View inflated.")
                                                        return SoftwareListHolder(v)
    }

    override fun onBindViewHolder(holder: SoftwareListHolder, position: Int) {
        holder.bindSoftware(softwares.get(position))
                                Log.d(tag, "Binded ${position+1}")
    }

    override fun getItemCount(): Int {
        Log.d(tag, "Size of list is: ${softwares.size}")
                return softwares.size
    }

    inner class SoftwareListHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        private var software: Software? = null
        private lateinit var softwareItem: TextView

        init {
            softwareItem = itemView.findViewById(R.id.software)
            softwareItem.setOnClickListener(this)
            softwareItem.setOnLongClickListener(this)
        }

        fun bindSoftware(software: Software) {
            this.software = software
            softwareItem.text = software.title
                    }
        override fun onClick(view: View) {
            val i = Intent(mContext, SoftwareActivity::class.java)
                        i.putExtra("extra_downloadable_software", software!!.address)
            i.putExtra("extra_downloadable_title", software!!.title)
                        mContext.startActivity(i)
        }

        override fun onLongClick(v: View?): Boolean {
            val cm = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val cd = ClipData.newUri(mContext.contentResolver, software!!.title, Uri.parse(software!!.address))
            cm.setPrimaryClip(cd)
            Snackbar.make(v!!, software!!.title+" copied to clipboard.", Snackbar.LENGTH_LONG).show()
            return true
        }
    }


}

