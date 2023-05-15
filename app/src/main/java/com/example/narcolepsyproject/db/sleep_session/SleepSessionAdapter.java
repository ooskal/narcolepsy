package com.example.narcolepsyproject.db.sleep_session;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narcolepsyproject.R;
import com.example.narcolepsyproject.db.RoomDB;
import com.example.narcolepsyproject.db.contact.ContactAdapter;
import com.example.narcolepsyproject.db.contact.ContactData;

import java.util.List;

//public class SleepSessionAdapter extends RecyclerView.Adapter<SleepSessionAdapter.ViewHolder>{
//    private List<SleepSessionData> dataList;
//    private Activity context;
//    private RoomDB database;
//
//    public SleepSessionAdapter(Activity context, List<SleepSessionData> dataList)
//    {
//        this.context = context;
//        this.dataList = dataList;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public SleepSessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//    {
//        //layoutInflater -> xml 미리 정의한 툴을 실제 메모리에 올리는 역할/ xml에 정의된 resource를 view 객체로 반환해줌
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_nok, parent, false);
//        return new ContactAdapter.ViewHolder(view);
//    }

//}
