
package spacebird;

/**
 *
 * @author Dániel
 */
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RW {
  
    public void writefile( int bestscore){
        ArrayList<String> lista =new ArrayList<String>();
        lista.add(Integer.toString(bestscore));
        try{
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter("score.txt", true));
            for(int i=0; i<lista.size();i++){
                bw.write(lista.get(i));
                bw.write("\r\n");                                               
            }   
            bw.flush();
        
        }
        catch(IOException ex){
        System.out.println("Hiba: "+ex.getMessage());            //hiba kiíratása
        }
        finally{
        System.out.println("Az irási folyamat sikeresen lefutott");
        }
     
    }
    
    
    public int readfile(){
        RandomAccessFile raf;                                //File beolvasása ezen keresztül
        String sor;                                          //Ebben tárolódik az aktuális sor
        int best = 0;
        ArrayList<String> lista =new ArrayList<String>();    //String lista létrehozása
            try{
                raf=new RandomAccessFile("score.txt","r");
                for(sor=raf.readLine(); sor!=null; sor=raf.readLine()){
                    lista.add(sor);                                         //aktuális sor hozzáadása a listához
                } 
                best = Integer.parseInt(lista.get(lista.size() - 1));       // String átalakitása Int-re és az utolso beolvasott eleme használata
            }
            catch(IOException e){
                System.out.println("Hiba: "+e.getMessage());            //hiba kiíratása
            }
            finally{
                System.out.println("Az olvasási folyamat befelyeződött");
            }
            
        return best;
    }
}

