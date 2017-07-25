package com.r2.scau.moblieofficing.adapter;

import android.support.v7.widget.RecyclerView;

import com.r2.scau.moblieofficing.bean.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by 嘉进 on 11:24.
 */

public abstract class ContactListAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private ArrayList<Contact> items = new ArrayList<Contact>();

    public ContactListAdapter() {
        setHasStableIds(true);
    }

    public void add(Contact object) {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, Contact object) {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends Contact> collection) {
        if (collection != null) {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(Contact... items) {
        addAll(Arrays.asList(items));
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(String object) {
        items.remove(object);
        notifyDataSetChanged();
    }

    public Contact getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
