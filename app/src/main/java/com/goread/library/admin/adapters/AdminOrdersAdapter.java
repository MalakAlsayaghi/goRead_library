package com.goread.library.admin.adapters;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.goread.library.R;
import com.goread.library.models.Library;
import com.goread.library.models.Order;
import com.goread.library.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.ImageViewHolder> {


    Context mContext;
    List<Order> myOrderList;
    List<Library> libraryList;
    List<User> driverList;
    private AdapterCallback adapterCallback;
    Date now = new Date();
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private BottomSheetDialog bottomSheetDialog;

    private static final SimpleDateFormat dateFormatterNew = new SimpleDateFormat("d/MM/yyyy");

    long agoDate;


    String libraryName, driverName = "No driver", locationName, imgUrl;

    public AdminOrdersAdapter(Context context) {
        this.mContext = context;
    }

    public void setOrderList(List<Order> cart) {
        this.myOrderList = cart;
    }

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.order_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Order cartCur = myOrderList.get(position);

        if (driverList != null) {
            for (int i = 0; i < driverList.size(); i++) {
                String id = driverList.get(i).getId();
                if (id.equals(cartCur.getLibraryId())) {
                    libraryName = driverList.get(i).getName();
                } else if (id.equals(cartCur.getDeliveryId())) {
                    driverName = driverList.get(i).getName();
                }
            }
        }


        holder.tvOrderPrice.setText(locationName);
        holder.tvDriver.setText(driverName);
        holder.tvLibrary.setText(libraryName);
        holder.tvNote.setText(cartCur.getDescription());
        holder.tvOrderPrice.setText(cartCur.getTotalPrice() + " RY");

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date past = null;
        try {
            past = format.parse(cartCur.getOrderDate());
            System.out.println("Your old date:" + past);
            System.out.println("today is: " + now.getTime());
            System.out.println("today is mins: " + MILLISECONDS.toMinutes(now.getTime() - past.getTime()));
            agoDate = MILLISECONDS.toMinutes(now.getTime() - past.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (agoDate > 0 && agoDate < 59) {
            holder.tvDate.setText(String.valueOf(agoDate) + "Mins Ago");
        } else if (agoDate == 0) {
            holder.tvDate.setText("Just Now");

        } else {
            holder.tvDate.setText(String.valueOf(dateFormatterNew.format(past)));

        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order cartCur = myOrderList.get(position);
                databaseReference.child("Orders").child(cartCur.getUserId()).child(cartCur.getOrderId()).removeValue();
                notifyDataSetChanged();
            }
        });


    }


    public static interface AdapterCallback {
        void getOrderId(String orderId);

    }

    public void setUserList(List<User> userList) {
        this.driverList = userList;
    }


    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView tvLibrary, tvDriver, tvNote, tvOrderPrice, tvDate;
        Button btnDelete;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNote = itemView.findViewById(R.id.tv_note);
            tvOrderPrice = itemView.findViewById(R.id.tv_price);
            tvLibrary = itemView.findViewById(R.id.tv_library);
            tvDate = itemView.findViewById(R.id.order_date);
            tvDriver = itemView.findViewById(R.id.tv_driver_name);
            tvDate = itemView.findViewById(R.id.tv_order_date);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            databaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseAuth = FirebaseAuth.getInstance();


            // TODO: Update User ID Ya Moha


        }
    }
}
