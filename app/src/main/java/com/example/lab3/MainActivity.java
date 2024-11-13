package com.example.lab3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS Одногруппники;");
        db.execSQL("CREATE TABLE Одногруппники (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "last_name TEXT NOT NULL," +
                "first_name TEXT NOT NULL," +
                "middle_name TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");");
        db.execSQL("INSERT INTO Одногруппники (last_name, first_name, middle_name)" +
                " VALUES ('Сизов', 'Матвей', 'Алексеевич')," +
                "('Желтиков', 'Глеб', 'Евгеньевич')," +
                "('Абдулов', 'Абдул', 'Абдулович')," +
                "('Именной', 'Павел', NULL)," +
                "('Последних', 'Игорь', NULL);");
    }

    public void showData(View view) {
        try {
            Cursor query = db.rawQuery("SELECT last_name, first_name, middle_name, created_at FROM Одногруппники;", null);
            StringBuilder result = new StringBuilder("Last Name, First Name, Middle Name, Created At\n");
            while (query.moveToNext()) {
                result.append(query.getString(0)).append(", ")
                        .append(query.getString(1)).append(", ")
                        .append(query.getString(2)).append(", ")
                        .append(query.getString(3)).append("\n");
            }
            query.close();

            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("data", result.toString());
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void addData(View view) {
        db.execSQL("INSERT INTO Одногруппники (last_name, first_name, middle_name) VALUES ('Новый', 'Матвей', 'Алексеевич');");
        Toast.makeText(this, "Добавлена запись", Toast.LENGTH_SHORT).show();
    }

    public void updateData(View view) {
        db.execSQL("UPDATE Одногруппники SET " +
                "last_name = 'Иванов'," +
                "first_name = 'Иван'," +
                "middle_name = 'Иванович'" +
                "WHERE created_at = (SELECT MAX(created_at) FROM Одногруппники)");
        Toast.makeText(this, "Обновлена последняя запись", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }
}