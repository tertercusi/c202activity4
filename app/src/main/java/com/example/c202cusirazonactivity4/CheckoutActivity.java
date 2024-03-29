package com.example.c202cusirazonactivity4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CheckoutActivity extends AppCompatActivity {

    FirebaseFirestore cusiRazonFirestore;
    Button cusiRazonProceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Intent cusiRazonIntent = getIntent();
        String cusiRazonOrderReferenceId = cusiRazonIntent.getStringExtra("orderReference");

        cusiRazonFirestore = FirebaseFirestore.getInstance();
        cusiRazonProceedButton = findViewById(R.id.cusiRazonProceedButton);
        EditText cusiRazonCcvField = findViewById(R.id.cusiRazonCCV);
        Button cusiRazonToMainButton = findViewById(R.id.cusiRazonMainButton);

        TextView cusiRazonFullname = findViewById(R.id.cusiRazonFullname);
        TextView cusiRazonAddress = findViewById(R.id.cusiRazonAddressDisplay);

        assert cusiRazonOrderReferenceId != null;
        cusiRazonFirestore.collection("orders").document(cusiRazonOrderReferenceId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot cusiRazonOrder = task.getResult();
                String cusiRazonUserId = String.valueOf(cusiRazonOrder.get("orderedBy"));
                cusiRazonFirestore.collection("users")
                        .document(cusiRazonUserId)
                        .get().addOnCompleteListener(userTask -> {
                            if (userTask.isSuccessful()) {
                                DocumentSnapshot cusiRazonUserData = userTask.getResult();
                                StringBuilder cusiRazonFullnameBuilder = new StringBuilder();

                                Object cusiRazonFirstName = cusiRazonUserData.get("firstName");
                                cusiRazonFullnameBuilder.append(cusiRazonFirstName);

                                cusiRazonFullnameBuilder.append(" ");

                                Object cusiRazonLastname = cusiRazonUserData.get("lastName");
                                cusiRazonFullnameBuilder.append(cusiRazonLastname);

                                cusiRazonFullname.setText(cusiRazonFullnameBuilder);
                                Object cusiRazonAddressData = cusiRazonUserData.get("address");
                                cusiRazonAddress.setText(String.valueOf(cusiRazonAddressData));

                            }
                        });

            }
        });

        cusiRazonProceedButton.setOnClickListener(v -> {
            int cusiRazonCvvCode = Integer.parseInt(String.valueOf(cusiRazonCcvField.getText()));
            if (cusiRazonCvvCode == 456) {
                Intent cusiRazonConfirmationIntent = new Intent(CheckoutActivity.this, ConfirmationActivity.class);
                cusiRazonConfirmationIntent.putExtra("referenceId", cusiRazonOrderReferenceId);
                startActivity(cusiRazonConfirmationIntent);

            } else {
                Toast.makeText(this, "Wrong CCV!", Toast.LENGTH_SHORT).show();
            }
        });

        cusiRazonToMainButton.setOnClickListener(v -> {
            Intent cusiRazonResetIntent = new Intent(CheckoutActivity.this, MainActivity.class);
            startActivity(cusiRazonResetIntent);
        });
    }
}