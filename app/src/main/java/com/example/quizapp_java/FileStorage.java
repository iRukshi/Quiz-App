package com.example.quizapp_java;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {

    private static final String fileName = "result.dat";

    public static void saveResultList(Context context, List<List<ResultModel>> resultList){
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
             oos.writeObject(resultList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static void resetResultList(Context context){
        List<List<ResultModel>> emptyList = new ArrayList<>();
        saveResultList(context,emptyList);
    }
    public static List<List<ResultModel>> retrieveResultList(Context context){
        List<List<ResultModel>> resultList = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis))
        {
            Object object =ois.readObject();
            if (object instanceof List<?>) {
                resultList = (List<List<ResultModel>>) object;
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
    public static void addNewResult(Context context, List<ResultModel> result){
        List<List<ResultModel>> existingResultList = retrieveResultList(context);
        existingResultList.add(result);
        saveResultList(context,existingResultList);
    }
    public static boolean checkIfFileExist(Context context){
        File file = new File(context.getFilesDir(), fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

}
