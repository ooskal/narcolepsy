package com.example.narcolepsyproject.db.contact;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.narcolepsyproject.R;
import com.example.narcolepsyproject.db.RoomDB;

import java.util.List;

//데이터를 올리는 부분에서 뷰와 데이터를 연결해주는 다리역할
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{

    private List<ContactData> dataList;
    private Activity context;
    private RoomDB database;

    public ContactAdapter(Activity context, List<ContactData> dataList)
    {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //layoutInflater -> xml 미리 정의한 툴을 실제 메모리에 올리는 역할/ xml에 정의된 resource를 view 객체로 반환해줌
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_nok, parent, false);
        return new ViewHolder(view);
    }


    //뷰홀더에 데이터를 바인딩해주는 함수
    @Override
    public void onBindViewHolder(@NonNull final ContactAdapter.ViewHolder holder, int position)
    {
        final ContactData data = dataList.get(position);
        database = RoomDB.getInstance(context);


        holder.textViewName.setText(data.getName());
        holder.textViewPhoneNumber.setText(data.getPhoneNumber());



        //list_row_nok.xml
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactData mainData = dataList.get(holder.getAdapterPosition());

                final int ID = mainData.getId();
                String nameText = mainData.getName(); //이름
                String phoneNumberText = mainData.getPhoneNumber(); //전화번호

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update);

                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setLayout(width, height);

                dialog.show();

                final EditText editTextName = dialog.findViewById(R.id.dialogEditNameText);
                final EditText editTextPhoneNumber = dialog.findViewById(R.id.dialogEditPhoneNumber);
                Button bt_update = dialog.findViewById(R.id.bt_update);

                editTextName.setText(nameText);
                editTextPhoneNumber.setText(phoneNumberText);

                bt_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        String NameText = editTextName.getText().toString().trim();
                        String PhoneNumberText = editTextPhoneNumber.getText().toString().trim();

                        mainData.setName(NameText);
                        mainData.setPhoneNumber(PhoneNumberText);

                        database.mainDao().update(ID, NameText, PhoneNumberText);

                        dataList.clear(); //기존 데이터 삭제
                        dataList.addAll(database.mainDao().getAll()); //새로운 데이터 추가
                        notifyDataSetChanged(); //recyclerview의 실제 데이터와 일치하게끔 해줌
                    }
                });
            }
        });


        //list_row_nok.xml
        /* 삭제 클릭 */
        holder.btDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ContactData mainData = dataList.get(holder.getAdapterPosition());

                database.mainDao().delete(mainData);

                int position = holder.getAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewName, textViewPhoneNumber;
        Button btEdit, btDelete;

        //activity_nok
        public ViewHolder(@NonNull View view)
        {
            super(view);
            textViewName = view.findViewById(R.id.text_view_name);
            textViewPhoneNumber = view.findViewById(R.id.text_view_phoneNumber);
            btEdit = view.findViewById(R.id.bt_edit);
            btDelete = view.findViewById(R.id.bt_delete);
//            btnAdd = view.findViewById(R.id.addContact);


        }
    }
}