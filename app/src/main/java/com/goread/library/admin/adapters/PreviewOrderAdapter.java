package com.goread.library.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goread.library.R;
import com.goread.library.models.MyCart;

import java.util.List;

public class PreviewOrderAdapter extends RecyclerView.Adapter<PreviewOrderAdapter.ImageViewHolder> {


    Context mContext;
    List<MyCart> myCartList;
    int totalPrice = 0;
    int productPrice = 0;
    int totalQuantity = 0;
    int totalProductPrice = 0;


    public PreviewOrderAdapter(Context context, List<MyCart> cart) {
        this.mContext = context;
        this.myCartList = cart;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_preview_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        MyCart cartCur = myCartList.get(position);


        Glide.with(mContext)
                .load(cartCur.getImg())
                .centerCrop()
                .into(holder.cart_image);

        holder.name.setText(cartCur.getProductName());
        holder.price.setText(cartCur.getProductPrice() + "RY");
        holder.quantity.setText(cartCur.getTotalQuantity());

        totalQuantity = Integer.parseInt(cartCur.getTotalQuantity());
        productPrice = Integer.parseInt(cartCur.getProductPrice());
        totalProductPrice = totalQuantity * productPrice;
        holder.total.setText(String.valueOf(totalProductPrice));


        totalPrice = totalPrice + totalProductPrice;


    }

    @Override
    public int getItemCount() {
        return myCartList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, total, quantity;
        ImageView cart_image;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cart_name);
            price = itemView.findViewById(R.id.cart_price);
            total = itemView.findViewById(R.id.cartTotal_price);
            quantity = itemView.findViewById(R.id.quantity_cart);
            cart_image = itemView.findViewById(R.id.cart_img);

            // TODO: Update User ID Ya Moha


        }
    }
}
