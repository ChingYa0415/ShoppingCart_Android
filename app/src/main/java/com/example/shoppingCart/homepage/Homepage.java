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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingCart.R;
import com.example.shoppingCart.bean.Category;
import com.example.shoppingCart.bean.Product;
import com.example.shoppingCart.network.RemoteAccess;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends Fragment {
    private final static String TAG = "TAG_Homepage";
    private final static String URL = RemoteAccess.URL_SERVER + "Homepage";
    private Activity activity;
    private RecyclerView rvCategory, rvFoodRecommend, rvDrinkRecommend, rvDailyNecessitiesRecommend;

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
        rvFoodRecommend = view.findViewById(R.id.rvHomepageFoodRecommend);
        rvDrinkRecommend = view.findViewById(R.id.rvHomepageDrinkRecommend);
        rvDailyNecessitiesRecommend = view.findViewById(R.id.rvHomepageDailyNecessitiesRecommend);

        ((AppCompatActivity) activity).setSupportActionBar(toolbar);

        rvCategory.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        rvFoodRecommend.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        rvDrinkRecommend.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        rvDailyNecessitiesRecommend.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));

        List<Category> categoryList = getCategoryList();
        List<Product> foodRecommendList = getFoodRecommendList();
        List<Product> drinkRecommendList = getDrinkRecommendList();
        List<Product> dailyNecessitiesRecommendList = getDailyNecessitiesRecommendList();
        showCategoryList(categoryList);
        showFoodRecommendList(foodRecommendList);
        showDrinkRecommendList(drinkRecommendList);
        showDailyNecessitiesRecommendList(dailyNecessitiesRecommendList);
    }

    public List<Category> getCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(R.drawable.box, "全部"));
        categoryList.add(new Category(R.drawable.snack, "零食"));
        categoryList.add(new Category(R.drawable.softdrink, "飲料"));
        categoryList.add(new Category(R.drawable.dairyproducts, "生活用品"));
        return categoryList;
    }

    public List<Product> getFoodRecommendList() {
        List<Product> foodRecommendList = new ArrayList<>();
        foodRecommendList.add(new Product(R.drawable.food1, "炭火燒肉包", 30));
        foodRecommendList.add(new Product(R.drawable.food2, "麻辣關東煮", 55));
        foodRecommendList.add(new Product(R.drawable.food3, "味噌關東煮", 50));
        foodRecommendList.add(new Product(R.drawable.food4, "鮭魚豆腐味噌湯", 60));
        foodRecommendList.add(new Product(R.drawable.food5, "蔬菜關東煮", 45));
        foodRecommendList.add(new Product(R.drawable.food6, "完熟蕃茄蔬菜關東煮", 50));
        foodRecommendList.add(new Product(R.drawable.food7, "剝皮辣椒雞湯", 65));
        foodRecommendList.add(new Product(R.drawable.food8, "地瓜", 30));
        return foodRecommendList;
    }

    public List<Product> getDrinkRecommendList() {
        List<Product> drinkRecommendList = new ArrayList<>();
        drinkRecommendList.add(new Product(R.drawable.drink1, "開喜烏龍茶", 25));
        drinkRecommendList.add(new Product(R.drawable.drink2, "每朝健康", 25));
        drinkRecommendList.add(new Product(R.drawable.drink3, "黑松茶花", 25));
        drinkRecommendList.add(new Product(R.drawable.drink4, "分解茶", 25));
        return drinkRecommendList;
    }

    public List<Product> getDailyNecessitiesRecommendList() {
        List<Product> dailyNecessitiesRecommendList = new ArrayList<>();
        dailyNecessitiesRecommendList.add(new Product(R.drawable.dailynecessities1, "肥皂", 100));
        dailyNecessitiesRecommendList.add(new Product(R.drawable.dailynecessities2, "沐浴乳", 110));
        dailyNecessitiesRecommendList.add(new Product(R.drawable.dailynecessities3, "衛生紙", 140));
        dailyNecessitiesRecommendList.add(new Product(R.drawable.dailynecessities4, "洗衣粉", 80));
        dailyNecessitiesRecommendList.add(new Product(R.drawable.dailynecessities5, "牙膏", 35));
        dailyNecessitiesRecommendList.add(new Product(R.drawable.dailynecessities6, "牙刷", 30));
        return dailyNecessitiesRecommendList;
    }

    public void showCategoryList(List<Category> categoryList) {
        if (categoryList == null) {
            Toast.makeText(activity, "showCategoryList no network", Toast.LENGTH_LONG).show();
        }
        CategoryAdapter categoryAdapter = (CategoryAdapter) rvCategory.getAdapter();
        if (categoryAdapter == null) {
            rvCategory.setAdapter(new CategoryAdapter(activity, categoryList));
        } else {
            categoryAdapter.setCategoryList(categoryList);
            categoryAdapter.notifyDataSetChanged();
        }
    }

    public void showFoodRecommendList(List<Product> foodRecommendList) {
        if (foodRecommendList == null) {
            Toast.makeText(activity, "showFoodRecommendList no network", Toast.LENGTH_LONG).show();
        }
        FoodRecommendAdapter foodRecommendAdapter = (FoodRecommendAdapter) rvFoodRecommend.getAdapter();
        if (foodRecommendAdapter == null) {
            rvFoodRecommend.setAdapter(new FoodRecommendAdapter(activity, foodRecommendList));
        } else {
            foodRecommendAdapter.setFoodRecommendList(foodRecommendList);
            foodRecommendAdapter.notifyDataSetChanged();
        }
    }

    public void showDrinkRecommendList(List<Product> drinkRecommendList) {
        if (drinkRecommendList == null) {
            Toast.makeText(activity, "showDrinkRecommendList no network", Toast.LENGTH_LONG).show();
        }
        DrinkRecommendAdapter drinkRecommendAdapter = (DrinkRecommendAdapter) rvDrinkRecommend.getAdapter();
        if (drinkRecommendAdapter == null) {
            rvDrinkRecommend.setAdapter(new FoodRecommendAdapter(activity, drinkRecommendList));
        } else {
            drinkRecommendAdapter.setDrinkRecommendList(drinkRecommendList);
            drinkRecommendAdapter.notifyDataSetChanged();
        }
    }

    public void showDailyNecessitiesRecommendList(List<Product> dailyNecessitiesRecommendList) {
        if (dailyNecessitiesRecommendList == null) {
            Toast.makeText(activity, "showDailyNecessitiesRecommendList no network", Toast.LENGTH_LONG).show();
        }
        DailyNecessitiesRecommendAdapter dailyNecessitiesRecommendAdapter = (DailyNecessitiesRecommendAdapter) rvDailyNecessitiesRecommend.getAdapter();
        if (dailyNecessitiesRecommendAdapter == null) {
            rvDailyNecessitiesRecommend.setAdapter(new FoodRecommendAdapter(activity, dailyNecessitiesRecommendList));
        } else {
            dailyNecessitiesRecommendAdapter.setDailyNecessitiesRecommendList(dailyNecessitiesRecommendList);
            dailyNecessitiesRecommendAdapter.notifyDataSetChanged();
        }
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
        private final Context context;
        private List<Category> categoryList;

        public CategoryAdapter(Context context, List<Category> categoryList) {
            this.context = context;
            this.categoryList = categoryList;
        }

        public void setCategoryList(List<Category> categoryList) {
            this.categoryList = categoryList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivCategory;
            TextView tvCategory;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivCategory = itemView.findViewById(R.id.Homepage_cardview_ivCategory);
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
            int categoryType;

            holder.ivCategory.setImageResource(category.getImageId());
            holder.tvCategory.setText(category.getCategory());
            Log.d(TAG, "Size " + categoryList.size());

            switch (category.getCategory()) {
                case "全部":
                    categoryType = 1;
                    break;
                case "零食":
                    categoryType = 2;
                    break;
                case "飲料":
                    categoryType = 3;
                    break;
                case "生活用品":
                    categoryType = 4;
                    break;
                default:
                    categoryType = 0;
                    break;
            }

            Bundle bundle = new Bundle();
            bundle.putInt("category", categoryType);

            holder.ivCategory.setOnClickListener(v ->
                Navigation.findNavController(rvCategory).navigate(R.id.productMenu, bundle));

            holder.tvCategory.setOnClickListener(v ->
                Navigation.findNavController(rvCategory).navigate(R.id.productMenu, bundle));
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }

    }

    public static class FoodRecommendAdapter extends RecyclerView.Adapter<FoodRecommendAdapter.MyViewHolder> {
        private final Context context;
        private List<Product> foodRecommendList;

        public FoodRecommendAdapter(Context context, List<Product> foodRecommendList) {
            this.context = context;
            this.foodRecommendList = foodRecommendList;
        }

        public void setFoodRecommendList(List<Product> foodRecommendList) {
            this.foodRecommendList = foodRecommendList;
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivFoodRecommend;
            TextView tvFoodRecommendName, tvFoodRecommendPrice;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivFoodRecommend = itemView.findViewById(R.id.ivRecommend);
                tvFoodRecommendName = itemView.findViewById(R.id.tvRecommendName);
                tvFoodRecommendPrice = itemView.findViewById(R.id.tvRecommendPrice);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.homepage_cardview_recommend, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull Homepage.FoodRecommendAdapter.MyViewHolder holder, int position) {
            Product product = foodRecommendList.get(position);

            holder.ivFoodRecommend.setImageResource(product.getImageId());
            holder.tvFoodRecommendName.setText(product.getName());
            holder.tvFoodRecommendPrice.setText(String.valueOf(product.getPrice()));
        }

        @Override
        public int getItemCount() {
            return foodRecommendList.size();
        }
    }

    public static class DrinkRecommendAdapter extends RecyclerView.Adapter<DrinkRecommendAdapter.MyViewHolder> {
        private final Context context;
        private List<Product> drinkRecommendList;

        public DrinkRecommendAdapter(Context context, List<Product> drinkRecommendList) {
            this.context = context;
            this.drinkRecommendList = drinkRecommendList;
        }

        public void setDrinkRecommendList(List<Product> drinkRecommendList) {
            this.drinkRecommendList = drinkRecommendList;
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivDrinkRecommend;
            TextView tvDrinkRecommendName, tvDrinkRecommendPrice;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivDrinkRecommend = itemView.findViewById(R.id.ivRecommend);
                tvDrinkRecommendName = itemView.findViewById(R.id.tvRecommendName);
                tvDrinkRecommendPrice = itemView.findViewById(R.id.tvRecommendPrice);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.homepage_cardview_recommend, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull Homepage.DrinkRecommendAdapter.MyViewHolder holder, int position) {
            Product product = drinkRecommendList.get(position);

            holder.ivDrinkRecommend.setImageResource(product.getImageId());
            holder.tvDrinkRecommendName.setText(product.getName());
            holder.tvDrinkRecommendPrice.setText(String.valueOf(product.getPrice()));
        }

        @Override
        public int getItemCount() {
            return drinkRecommendList.size();
        }

    }

    public static class DailyNecessitiesRecommendAdapter extends RecyclerView.Adapter<DailyNecessitiesRecommendAdapter.MyViewHolder> {
        private final Context context;
        private List<Product> dailyNecessitiesRecommendList;

        public DailyNecessitiesRecommendAdapter(Context context, List<Product> dailyNecessitiesRecommendList) {
            this.context = context;
            this.dailyNecessitiesRecommendList = dailyNecessitiesRecommendList;
        }

        public void setDailyNecessitiesRecommendList(List<Product> dailyNecessitiesRecommendList) {
            this.dailyNecessitiesRecommendList = dailyNecessitiesRecommendList;
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivDailyNecessitiesRecommend;
            TextView tvDailyNecessitiesRecommendName, tvDailyNecessitiesRecommendPrice;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivDailyNecessitiesRecommend = itemView.findViewById(R.id.ivRecommend);
                tvDailyNecessitiesRecommendName = itemView.findViewById(R.id.tvRecommendName);
                tvDailyNecessitiesRecommendPrice = itemView.findViewById(R.id.tvRecommendPrice);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.homepage_cardview_recommend, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull Homepage.DailyNecessitiesRecommendAdapter.MyViewHolder holder, int position) {
            Product product = dailyNecessitiesRecommendList.get(position);

            holder.ivDailyNecessitiesRecommend.setImageResource(product.getImageId());
            holder.tvDailyNecessitiesRecommendName.setText(product.getName());
            holder.tvDailyNecessitiesRecommendPrice.setText(String.valueOf(product.getPrice()));
        }

        @Override
        public int getItemCount() {
            return dailyNecessitiesRecommendList.size();
        }

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
            Navigation.findNavController(rvCategory).navigate(R.id.shoppingcartProducts);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

}