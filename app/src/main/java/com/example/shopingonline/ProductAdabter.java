package com.example.shopingonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdabter extends RecyclerView.Adapter<ProductAdabter.ProductViewHolder> {
    Context context;
    ArrayList<Product> Product;

    ProductAdabter(Context context, ArrayList<Product> Product){
        this.context=context;
        this.Product = Product;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(context).inflate(R.layout.products,parent,false);

   return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product= Product.get(position);
        holder.product_name.setText(product.getImageName());
        Picasso.get().load(product.getImageURL()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        return Product.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
            ImageView product_image;
            TextView product_name;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image=(ImageView)itemView.findViewById(R.id.image_view_product);
            product_name=(TextView)itemView.findViewById(R.id.textView_product);
        }
    }
}
