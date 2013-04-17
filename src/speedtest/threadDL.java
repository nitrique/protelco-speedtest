package speedtest;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Nicolas Guillard - Nitrique Concept
 */
public class threadDL implements Runnable{
    
    /** Attributs **/
    MainFrame mainFrame;
    InputStream input = null;
    FileOutputStream writeFile = null;
    Integer numero;
        
    
    /**
     * Constructeur
     * @param  frame frame principale
     * @param  numero numéro du thread (de 0 à 4)
     */
    public threadDL(MainFrame frame, int numero){
        this.mainFrame = frame;
        this.numero = numero;
    }
    
    /**
     * Téléchargement d'un fichier
     * @param url url du fichier
     */
    private void dl(String url){
        try
        {
            URL uri = new URL(url);
            URLConnection connexion = uri.openConnection();
            int fileLength = connexion.getContentLength();
            
            if (fileLength == -1)
            {
                mainFrame.addLog("["+ numero +"] Fichier ou URL invalide.");
                return;
            }
            
            //mainFrame.addLog("Taille du fichier "+ numero +": " + (fileLength/1024)/1024 + "Mio");
            
            input = connexion.getInputStream();
            //String fileName = uri.getFile().substring(uri.getFile().lastIndexOf('/') + 1);
            
            //mainFrame.addLog("Nom du fichier : " + fileName);
            //mainFrame.addLog("Merci de patienter...");
            
            byte[] buffer = new byte[1024];
            int read;
            int nbPass = 0;
           
            mainFrame.addLog("["+ numero +"] Téléchargement...");
            
            /** Mettre le moins d'actions possibles (comptage du temps) **/
            long start = System.currentTimeMillis();

            while ((read = input.read(buffer)) > 0)
            {
                nbPass++;
                mainFrame.setProgress((nbPass*100)/(fileLength/1024), numero);
            }
            
            long end = System.currentTimeMillis();
            /*************************************************************/
            
            double speed = (fileLength/((end-start)/1000))/1024;
            
            //mainFrame.addLog("["+ numero +"] Temps de téléchargement  : " + (end-start)/1000 + " Secondes");
            mainFrame.addLog("["+ numero +"] Débit final : " + speed + " Kio/s");
            //mainFrame.addLog("["+ numero +"] Nombre de bits téléchargés (avec headers) : " + nbPass);
            
            input.close();
            //mainFrame.addLog("["+ numero +"] Fin du Téléchargement");
            mainFrame.addTotalSpeed(speed);
            
        }catch(Exception e){}
    }

    @Override
    public void run() {
        // Téléchargement d'un fichier de 4 mégas
        dl("http://test-debit.free.fr/4096.rnd");          
    }
    
}
