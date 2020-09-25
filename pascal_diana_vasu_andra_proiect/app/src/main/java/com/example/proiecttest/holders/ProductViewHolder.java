package com.example.proiecttest.holders;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proiecttest.R;
import com.example.proiecttest.models.ProductModel;
import com.example.proiecttest.models.ShoppingListModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProductViewHolder extends RecyclerView.ViewHolder  {
    private TextView productNameTextView;

    public ProductViewHolder(View itemView) {
        super(itemView);
        productNameTextView = itemView.findViewById(R.id.product_name_text_view);
    }

    public void setProduct(Context context, View shoppingListViewFragment, String userEmail, String userName, ShoppingListModel shoppingListModel, ProductModel productModel) {
        String shoppingListId = shoppingListModel.getShoppingListId();
        String shoppingListName = shoppingListModel.getShoppingListName();
        String productId = productModel.getProductId();
        String productName = productModel.getProductName();
        Boolean isInShoppingList = productModel.getIzInShoppingList();
        productNameTextView.setText(productName);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference productIdRef = rootRef.collection("products").document(shoppingListId)
                .collection("shoppingListProducts").document(productId);

        itemView.setOnClickListener(view -> {
            Map<String, Object> map = new HashMap<>();
            if (isInShoppingList) {
                map.put("izInShoppingList", false);
            } else {
                map.put("izInShoppingList", true);
            }
            productIdRef.update(map);
        });

        itemView.setOnLongClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Edit / Delete Product");

            EditText editText = new EditText(context);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            editText.setText(productName);
            editText.setSelection(editText.getText().length());
            editText.setHint("Type a name");
            editText.setHintTextColor(Color.GRAY);
            builder.setView(editText);

            builder.setPositiveButton("Update", (dialogInterface, i) -> {
                String newProductName = editText.getText().toString().trim();
                Map<String, Object> map = new HashMap<>();
                map.put("productName", newProductName);
                productIdRef.update(map);
            });

            builder.setNegativeButton("Delete", (dialogInterface, i) -> {
                productIdRef.delete().addOnSuccessListener(aVoid -> Snackbar.make(shoppingListViewFragment, "Product deleted!", Snackbar.LENGTH_LONG).show());
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            return true;
        });
    }

}
