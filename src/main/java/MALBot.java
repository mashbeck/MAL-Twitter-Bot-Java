/**
 * Created by Matthew on 12/26/2015.
 */



import org.json.JSONObject;
import twitter4j.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class MALBot {


    public static void main(String... args) throws TwitterException {
        
            try {


                Twitter twitter = TwitterFactory.getSingleton();
                MALBot malBot = new MALBot();
                String[] anime1 = malBot.getTitleandImageURL();
                System.out.println("Title: "+ anime1[0]);
                System.out.println("Image URL: " + anime1[1] +"\n");
                String[] anime2 = malBot.getTitleandImageURL();
                System.out.println("Title: "+ anime2[0]);
                System.out.println("Image URL: " + anime2[1] + "\n");
                String tweet = "If you liked " + anime1[0] + ", then you might like " + anime2[0] + "!";
                System.out.println("Tweet: " + tweet + "\n");
                String image1 = anime1[1];
                String image2 = anime2[1];


                long[] mediaIds = new long[2];
                mediaIds[0] = malBot.getMediaID(image1, twitter);
                mediaIds[1] = malBot.getMediaID(image2, twitter);
                StatusUpdate status = new StatusUpdate(tweet);
                status.setMediaIds(mediaIds);

                // set the image to be uploaded here.
                twitter.updateStatus(status);
                System.out.println("Tweet Successful!\n\n\n");
             
            } catch (Exception e) {
                e.printStackTrace();
            }
        


            /*Status status = twitter.updateStatus(tweet);
            System.out.println("Done.");*/



    }

    public String[] getTitleandImageURL() {
        try {
            String[] values = new String[2];
            String URL = "https://hummingbird.me/api/v1/anime/";
            String id = Integer.toString(((int) (Math.random() * 11670) + 1));
            URL = URL + id;
            System.out.println("URL: " + URL);
            java.net.URL website = new URL(URL);
            String s = "";
            Scanner sc = new Scanner(website.openStream());
            while (sc.hasNext()) {
                s = s + sc.nextLine();
            }
            JSONObject anime = new JSONObject(s);
            values[0] = anime.getString("title");
            values[1] = anime.getString("cover_image");
            return values;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public long getMediaID(String imageURL, Twitter twitter) throws TwitterException{
        if (imageURL.length() <= 0 || imageURL == null){
            return 0;
        }
        try {
            URL url = new URL(imageURL);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            File file = new File("meme.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(response);
            fos.close();
            UploadedMedia media = twitter.uploadMedia(file);
            file.delete();
            return media.getMediaId();
        }catch (IOException e){
            e.printStackTrace();
            return 0;
        }
    }
}