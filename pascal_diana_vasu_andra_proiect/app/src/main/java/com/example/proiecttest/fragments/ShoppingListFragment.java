package com.example.proiecttest.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proiecttest.R;
import com.example.proiecttest.activities.ShoppingListActivity;
import com.example.proiecttest.holders.ProductViewHolder;
import com.example.proiecttest.models.ProductModel;
import com.example.proiecttest.models.ShoppingListModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ShoppingListFragment extends Fragment {
    private String shoppingListId;
    private FirebaseFirestore rootRef;
    private CollectionReference shoppingListProductsRef;
    private Boolean izInShoppingList;
    private String userEmail, userName;
    private GoogleApiClient googleApiClient;
    private FirestoreRecyclerAdapter<ProductModel, ProductViewHolder> firestoreRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View shoppingListViewFragment = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        Bundle bundle = getArguments();
        izInShoppingList = bundle.getBoolean("izInShoppingList");

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (googleSignInAccount != null) {
            userEmail = googleSignInAccount.getEmail();
            userName = googleSignInAccount.getDisplayName();
        }

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        ShoppingListModel shoppingListModel = ((ShoppingListActivity) getActivity()).getShoppingListModel();
        shoppingListId = shoppingListModel.getShoppingListId();

        FloatingActionButton fab = shoppingListViewFragment.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                builder.setTitle("Create Product");

                final EditText editText = new EditText(getContext());
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                editText.setHint("Type a name");
                editText.setHintTextColor(Color.GRAY);
                builder.setView(editText);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String productName = editText.getText().toString().trim();
                        addProduct(productName);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        rootRef = FirebaseFirestore.getInstance();
        shoppingListProductsRef = rootRef.collection("products").document(shoppingListId).collection("shoppingListProducts");

        RecyclerView recyclerView = shoppingListViewFragment.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);

        TextView emptyView = shoppingListViewFragment.findViewById(R.id.empty_view);
        ProgressBar progressBar = shoppingListViewFragment.findViewById(R.id.progress_bar);

        Query query = shoppingListProductsRef.whereEqualTo("izInShoppingList", izInShoppingList)
                .orderBy("productName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProductModel> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel.class)
                .build();

        firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<ProductModel, ProductViewHolder>(firestoreRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductModel model) {
                        holder.setProduct(getContext(), shoppingListViewFragment, userEmail, userName, shoppingListModel, model);
                    }

                    @Override
                    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                        return new ProductViewHolder(view);
                    }

                    @Override
                    public void onDataChanged() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                        if (getItemCount() == 0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return super.getItemCount();
                    }
                };
        recyclerView.setAdapter(firestoreRecyclerAdapter);

        return shoppingListViewFragment;
    }

    private void addProduct(String productName) {
        String productId = shoppingListProductsRef.document().getId();
        ProductModel productModel = new ProductModel(productId, productName, izInShoppingList);
        shoppingListProductsRef.document(productId).set(productModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

        if (firestoreRecyclerAdapter != null) {
            firestoreRecyclerAdapter.stopListening();
        }
    }
}
