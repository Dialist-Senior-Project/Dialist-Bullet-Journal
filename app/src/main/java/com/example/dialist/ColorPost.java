package com.example.dialist;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ColorPost {
    public int c1;
    public int c2;
    public int c3;
    public int c4;
    public int c5;

    public ColorPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public ColorPost(int c1, int c2, int c3, int c4, int c5) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
        this.c5 = c5;
    }

    @Exclude
    public Map<String, Integer> toMap() {
        HashMap<String, Integer> result = new HashMap<>();
        result.put("c1", c1);
        result.put("c2", c2);
        result.put("c3", c3);
        result.put("c4", c4);
        result.put("c5", c5);
        return result;
    }
}
