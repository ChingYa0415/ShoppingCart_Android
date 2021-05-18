package com.example.shoppingCart.homepage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppingCart.R;
import com.example.shoppingCart.bean.Category;
import com.example.shoppingCart.network.RemoteAccess;

import java.util.List;

public class Homepage extends Fragment {
    private final static String TAG = "TAG_Homepage";
    private final static String URL = RemoteAccess.URL_SERVER + "Homepage";
    private Activity activity;
    private RecyclerView rvCategory;

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
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Toolbar toolbar = view.findViewById(R.id.tbHomepage);
        rvCategory = view.findViewById(R.id.rvHomepageCategory);
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);

        rvCategory.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.ToolBarShoppingCart) {
            Navigation.findNavController(rvCategory).navigate(R.id.products);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
        private final Context context;
        private List<Category> categoryList;

        public CategoryAdapter(Context context) {
            this.context = context;
            this.categoryList = categoryList;
        }

        public void setCategoryList(List<Category> categoryList) {
            this.categoryList = categoryList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvCategory;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.Homepage_cardview_ivCategory);
                tvCategory = itemView.findViewById(R.id.Homepage_cardview_tvCategory);
            }
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.homepage_cardview_category, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull Homepage.CategoryAdapter.MyViewHolder holder, int position) {
            Category category = categoryList.get(position);
            holder.imageView.setImageResource(category.getImageId());
        }

        @Override
        public int getItemCount() {
            return 0;
        }


    }

}