package com.emilioschepis.qrsync.ui.codelist

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.emilioschepis.qrsync.R
import com.emilioschepis.qrsync.model.QSCode
import java.text.DateFormat

class CodeListAdapter(private val listener: (QSCode) -> Unit) : ListAdapter<QSCode, CodeListAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_code, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), listener)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(code: QSCode, listener: (QSCode) -> Unit) = with(itemView) {
            val titleTev = findViewById<TextView>(R.id.item_code_title)
            val formattedContent = code.content.replace("\n", " ").trim()

            // If the code has a title, it is shown before the content
            titleTev.text = if (code.title.isNotBlank()) {
                String.format("(%s) %s", code.title.trim(), formattedContent)
            } else {
                formattedContent
            }

            // The time is formatted based on the phone's locale
            findViewById<TextView>(R.id.item_code_time).text = DateFormat
                    .getDateTimeInstance()
                    .format(code.timestamp.toDate())

            // The correct icon is selected from the code type
            findViewById<ImageView>(R.id.item_code_type).setImageResource(when (code.type) {
                QSCode.CodeType.UNKNOWN -> R.drawable.ic_help_outline_black_24dp
                QSCode.CodeType.TEXT -> R.drawable.ic_short_text_black_24dp
                QSCode.CodeType.URL -> R.drawable.ic_link_black_24dp
                QSCode.CodeType.CONTACT -> R.drawable.ic_person_black_24dp
                QSCode.CodeType.EMAIL -> R.drawable.ic_email_black_24dp
                QSCode.CodeType.SMS -> R.drawable.ic_sms_black_24dp
                QSCode.CodeType.PHONE -> R.drawable.ic_phone_black_24dp
                QSCode.CodeType.CALENDAR -> R.drawable.ic_event_black_24dp
                QSCode.CodeType.PRODUCT -> R.drawable.ic_shopping_cart_black_24dp
                QSCode.CodeType.ISBN -> R.drawable.ic_book_black_24dp
            })
            setOnClickListener { listener(code) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<QSCode>() {
            override fun areItemsTheSame(oldItem: QSCode?, newItem: QSCode?): Boolean = oldItem?.id == newItem?.id
            override fun areContentsTheSame(oldItem: QSCode?, newItem: QSCode?): Boolean = oldItem == newItem
        }
    }
}