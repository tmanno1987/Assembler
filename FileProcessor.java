import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class FileProcessor<T>
{
   private BufferedWriter bw;
   private ArrayList<T> al;
   private T [] elem;
   private T item;
   private String info;
   private String fileName;
   
   public FileProcessor(String data, String fname)
   {
      info = data;
      fileName = fname;
   }
   
   public FileProcessor(String name, ArrayList<T> als)
   {
      try
      {
         bw = new BufferedWriter(new FileWriter(name));
         resetFile();
         bw.close();
         bw = null;
         bw = new BufferedWriter(new FileWriter(name, true));
         al = als;
         printAL();
         closeFile();
      }
      catch (IOException ioe)
      {
         System.err.println("Error.. File Not Found!!");
      }
   }
   
   public FileProcessor(String name, T [] item)
   {
      try
      {
         elem = item;
         bw = new BufferedWriter(new FileWriter(name));
         resetFile();
         bw.close();
         bw = null;
         bw = new BufferedWriter(new FileWriter(name, true));
         printA();
         closeFile();
      }
      catch (IOException ioe)
      {
         System.err.println("Error.. File Not Found!!" + ioe);
      }
   }
   
   private void resetFile() throws IOException
   {
      bw.write("");
   }
   
   private void printAL()
   {
      final String SAVE = "\r\n";
      String data = al.size() + SAVE;
      try
      {
         bw.write(data);
         for (T obj: al)
         {
            if (obj != null)
            {
               data = obj + SAVE;
               bw.write(data);
            }
         }
         bw.close();
      }
      catch (IOException ioe)
      {
         System.err.println("Error.. File not Found!!");
      }
   }
   
   private int findLen(int curr, T [] e)
   {
      int temp = 0;
      for (int i = 0; i < curr; i++)
      {
         if (e[i] != null)
         {
            temp++;
         }
      }
      return temp;
   }
   
   private void printA()
   {
      final String SAVE = "\r\n";
      String data = findLen(elem.length, elem) + SAVE;
      try
      {
         bw.write(data);
         for (T obj: elem)
         {
            if (obj != null)
            {
               data = obj + SAVE;
               bw.write(data);
            }
         }
         bw.close();
      }
      catch (IOException ioe)
      {
         System.err.println("Error.. File not Found!!");
      }
   }
   
   public void changeFile(String fname)
   {
      try
      {
         closeFile();
         bw = new BufferedWriter(new FileWriter(fname, true));
      }
      catch (FileNotFoundException fnfe)
      {
         System.err.println("Error..\n" + fnfe);
      }
      catch (IOException ioe)
      {
         System.err.println("Error..\n" + ioe);
      }
   }
   
   public ArrayList<T> readFile(String fname)
   {
      ArrayList<T> temp = new ArrayList<>();
      try
      {
         Scanner file = new Scanner(new File(fname));
         while (file.hasNext())
         {
            item = (T)file.next();
            temp.add(item);
         }
         file.close();         
      }
      catch (FileNotFoundException fnfe)
      {
         System.err.println("Error..\n" + fnfe);
      }
      finally
      {
         return temp;
      }
   }
   
   private void closeFile() throws IOException
   {
      bw.close();
   }
   
   public void write2File() throws IOException, FileNotFoundException
   {
      bw = new BufferedWriter(new FileWriter(fileName,true));
      bw.write(info);
      bw.close();
   }
}