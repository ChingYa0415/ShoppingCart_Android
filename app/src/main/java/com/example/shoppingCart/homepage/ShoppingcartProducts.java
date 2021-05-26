package com.example.shoppingCart.homepage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingCart.R;
import com.example.shoppingCart.bean.Product;
import com.example.shoppingCart.network.RemoteAccess;

import java.util.ArrayList;
import java.util.List;


public class ShoppingcartProducts extends Fragment {
    private final static String TAG = "TAG_Homepage";
    private final static String URL = RemoteAccess.URL_SERVER + "Homepage";
    private Activity activity;
    private RecyclerView rvProduct;
    private List<Product> productList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_shoppingcart_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvProduct = view.findViewById(R.id.rvShoppingcartProducts);

        rvProduct.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));

        productList = getProductList();
        Log.d(TAG, "categoryList" + productList);
        showProductList(productList);
    }

    public List<Product> getProductList() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(R.drawable.pingles, "品客", 35));
        productList.add(new Product(R.drawable.waferpie, "新貴派", 50));
        productList.add(new Product(R.drawable.xiaoguady, "脆笛酥", 25));
        productList.add(new Product(R.drawable.lays, "樂事", 39));
        productList.add(new Product(R.drawable.milktea, "麥香", 10));
        productList.add(new Product(R.drawable.handwash, "洗手乳", 50));
        return productList;
    }

    public void showProductList(List<Product> productList) {
        if (productList == null) {
            Toast.makeText(activity, "showProductList no network", Toast.LENGTH_LONG).show();
        }
        ProductAdapter productAdapter = (ProductAdapter) rvProduct.getAdapter();
        if (productAdapter == null) {
            rvProduct.setAdapter(new ProductAdapter(activity, productList));
        } else {
            productAdapter.setProductList(productList);
            productAdapter.notifyDataSetChanged();
        }
    }

    public static class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
        private final Context context;
        private List<Product> productList;

        public ProductAdapter(Context context, List<Product> productList) {
            this.context = context;
            this.productList = productList;
        }

        public void setProductList(List<Product> productList) {
            this.productList = productList;
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivProduct;
            TextView tvName, tvPrice, tvCount;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivProduct = itemView.findViewById(R.id.ivRecommend);
                tvName = itemView.findViewById(R.id.tvRecommendName);
                tvPrice = itemView.findViewById(R.id.tvRecommendPrice);
                tvCount = itemView.findViewById(R.id.tvShoppingcartProductCount);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.shoppingcart_products_cardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ShoppingcartProducts.ProductAdapter.MyViewHolder holder, int position) {
            Product product = productList.get(position);

            holder.ivProduct.setImageResource(product.getImageId());
            holder.tvName.setText(product.getName());
            holder.tvPrice.setText(String.valueOf(product.getPrice()));
            holder.tvCount.setText("1");
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

    }

}