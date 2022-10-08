package com.goread.library.libraries.adapters;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ImageViewHolder> {


    Context mContext;
    List<Order> myOrderList;
    List<Library> libraries;
    private AdapterCallback adapterCallback;
    Date now = new Date();
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private BottomSheetDialog bottomSheetDialog;

    String libraryId;
    private static final SimpleDateFormat dateFormatterNew = new SimpleDateFormat("d/MM/yyyy");

    long agoDate;


    String libraryName, locationName, imgUrl;

    public MyOrdersAdapter(Context context) {
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
        View v = LayoutInflater.from(mContext).inflate(R.layout.order_item_library, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Order cartCur = myOrderList.get(position);


        holder.tvOrderPrice.setText(locationName);
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
        if (cartCur.isLibraryAccepted() || cartCur.isRejected()) {
            holder.btnApprove.setVisibility(View.GONE);
            holder.btnDecline.setVisibility(View.GONE);
        }
        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order cartCur = myOrderList.get(position);
                databaseReference.child("Orders").child(cartCur.getUserId()).child(cartCur.getOrderId()).child("libraryAccepted").setValue(true);
                notifyDataSetChanged();
            }
        });

        holder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order cartCur = myOrderList.get(position);
                databaseReference.child("Order Status").child(cartCur.getUserId()).child("rejected").setValue(true);
                databaseReference.child("Orders").child(cartCur.getUserId()).child(cartCur.getOrderId()).child("rejected").setValue(true);
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order cartCur = myOrderList.get(position);

                adapterCallback.getOrderId(cartCur.getOrderId(), cartCur.getUserId());
            }
        });


    }


    public static interface AdapterCallback {
        void getOrderId(String orderId, String userId);

    }


    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView tvNote, tvOrderPrice, tvDate;
        Button btnApprove, btnDecline;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNote = itemView.findViewById(R.id.tv_note);
            tvOrderPrice = itemView.findViewById(R.id.tv_price);
            tvDate = itemView.findViewById(R.id.order_date);
            tvDate = itemView.findViewById(R.id.tv_order_date);
            btnDecline = itemView.findViewById(R.id.btnDecline);
            btnApprove = itemView.findViewById(R.id.btnApprove);

            databaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseAuth = FirebaseAuth.getInstance();
            libraryId = firebaseAuth.getCurrentUser().getUid();


            // TODO: Update User ID Ya Moha


        }
    }
}
