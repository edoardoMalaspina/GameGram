package it.unipi.gamegram;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cleaning {

    public static void cleanTextPrices() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("C:\\Users\\edoar\\Documents\\Università\\Large scale and Multistructured database\\datasets\\GAMES\\uncleanedPrices.csv"));
            FileWriter myWriter = new FileWriter("pricesCleaned.csv");
            String line = reader.readLine();
            while (line != null) {
                String s = line;
                Pattern p = Pattern.compile("(\\p{Sc}) *([0-9.]*)");
                Matcher m = p.matcher(s);
                if (m.find()) {
                    myWriter.write(m.group()+"\n");
                }
                else{
                    myWriter.write("$0\n");
                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFinalPrices(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("pricesCleaned.csv"));
            FileWriter myWriter = new FileWriter("pricesFinal.csv");
            String line = reader.readLine();
            while (line != null) {
                String s = line;
                if ( s.equals("$") || !s.startsWith("$") ){
                    myWriter.write("$0\n");
                }
                else{
                    myWriter.write(s + "\n");
                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args){
        cleanTextPrices();
        createFinalPrices();
    }
}
