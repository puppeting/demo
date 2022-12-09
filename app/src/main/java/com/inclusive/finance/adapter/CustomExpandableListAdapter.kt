package com.inclusive.finance.adapter

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.hwangjr.rxbus.RxBus
import com.inclusive.finance.R
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.bean.BaseTypeBean


class CustomExpandableListAdapter internal constructor(
    private var context: Context, private var titleList: ArrayList<String>, private var dataList: LinkedHashMap<String, ArrayList<BaseTypeBean.Enum12>>
) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition].valueName
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    fun setPoss(dataList: LinkedHashMap<String, ArrayList<BaseTypeBean.Enum12>>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }


    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition).toString()
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }

        val tv_del = convertView!!.findViewById<TextView>(R.id.tv_del)

        val expandedListTextView = convertView?.findViewById<TextView>(R.id.expandedListItem)
        expandedListTextView?.text = expandedListText
        if (dataList[titleList[listPosition]]!![expandedListPosition].isCheck) {
            expandedListTextView?.setBackgroundColor(context.resources.getColor(R.color.AliceBlue))
        } else {
            expandedListTextView?.setBackgroundColor(context.resources.getColor(R.color.white))
        }
        tv_del?.setOnClickListener {
            RxBus.get().post("delsys2", titleList.get(listPosition)+","+expandedListText)


        }
        return convertView!!
    }

    override fun getChildrenCount(listPosition: Int): Int {
        Log.e("ddddddd***","***"+titleList[listPosition].toString())
        Log.e("ddddddd***","***"+dataList.size)

        return dataList.get(titleList[listPosition])?.size!!
    }

    override fun getGroup(listPosition: Int): Any {
        Log.e("ddddddd***wwww","***"+titleList[listPosition].toString())

        return this.titleList[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        Log.e("ddddddd","****fffffddddd"+getGroup(listPosition).toString())

        val listTitle = getGroup(listPosition).toString()
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listTitle)

        val tvAdd = convertView!!.findViewById<TextView>(R.id.tv_add)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        tvAdd?.setOnClickListener {
            RxBus.get().post("addsys", ""+listTitle)

        }
        val tv_del = convertView!!.findViewById<TextView>(R.id.tv_del)
        tv_del?.setOnClickListener {
            RxBus.get().post("delsys", ""+listTitle)

        }
        listTitleTextView.text = listTitle

//        dataList[listTitle]?.forEachIndexed { index, enum12 ->
//            if (enum12.isCheck) {
//                listTitleTextView.text = listTitle + "-" + enum12.valueName
//            }
//        }

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}