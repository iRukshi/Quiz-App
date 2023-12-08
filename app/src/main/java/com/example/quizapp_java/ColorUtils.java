package com.example.quizapp_java;
import android.graphics.Color;

public class ColorUtils {
    public static int generateRandomColor() {
        // Generate random values for red, green, and blue components
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);

        // Create the color using RGB values
        return Color.rgb(red, green, blue);
    }
}

