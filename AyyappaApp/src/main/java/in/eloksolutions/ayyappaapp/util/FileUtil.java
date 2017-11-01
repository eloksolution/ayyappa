package in.eloksolutions.ayyappaapp.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {
    public static String filename = "chatmsgs";
    public static final String DELIMITER="##@@";
    public static void writeToFile(Context ctx, List<Message> msgs,String eventId){

        FileOutputStream outputStream;

        try {
            outputStream = ctx.openFileOutput(filename+eventId, Context.MODE_PRIVATE);
            for(Message m:msgs) {
                outputStream.write(m.serialize().getBytes());
                outputStream.write("\n".getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<Message>  readFromFile(Context ctx,String eventId){
        FileInputStream fin;
        StringBuilder sb=new StringBuilder();
        List<Message> msgs=new ArrayList<>();
        try {
            fin= ctx.openFileInput(filename+eventId);
            int content;
            while ((content = fin.read()) != -1) {
                sb.append((char) content);
            }
            System.out.println("file contents "+sb);
            if (sb.length()==0)return null;
            String[] rows=sb.toString().split("\n");
            for(String row:rows){
                String fields[]=row.split(FileUtil.DELIMITER);
                msgs.add(new Message(fields[0],fields[1],fields[2],fields[3],fields[4]));
            }
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgs;
    }
}
