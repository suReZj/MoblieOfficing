package com.r2.scau.moblieofficing.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r2.scau.moblieofficing.R;

/**
 * Created by 嘉进 on 11:12.
 */

public class NoticeUnreadFragment extends Fragment {

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice_unread, container, false);
        return view;
    }
}
