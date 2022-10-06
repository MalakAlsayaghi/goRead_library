package com.goread.library.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.goread.library.R;
import com.goread.library.models.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ImageViewHolder> {


    Context mContext;
    List<Order> myOrderList;

    private static final SimpleDateFormat dateFormatterNew = new SimpleDateFormat("d/MM/yyyy");

    long agoDate;


    String libraryName, locationName, imgUrl;

    public OrdersAdapter(Context context) {
        this.mContext = context;
    }

    public void setOrderList(List<Order> cart) {
        this.myOrderList = cart;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.earning_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Order cartCur = myOrderList.get(position);


        holder.tvOrderPrice.setText(String.valueOf(cartCur.getTotalPrice()));
        holder.tvOrderNo.setText(String.valueOf(cartCur.getOrderNumber()));
        holder.tvBookNo.setText(String.valueOf(cartCur.getItems().size()));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date past = null;

        try {
            past = format.parse(cartCur.getOrderDate());
            holder.tvDate.setText(String.valueOf(past));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    public static interface AdapterCallback {
        void getOrderId(String orderId);

    }


    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNo, tvBookNo, tvOrderPrice, tvDate;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNo = itemView.findViewById(R.id.tv_order_id);
            tvBookNo = itemView.findViewById(R.id.tv_No_book);
            tvOrderPrice = itemView.findViewById(R.id.tv_total);
            tvDate = itemView.findViewById(R.id.tv_order_date);


            // TODO: Update User ID Ya Moha


        }
    }
}
