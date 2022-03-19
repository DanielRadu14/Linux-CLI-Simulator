import java.io.*;
import java.util.*;

public class TouchCommand implements Command{
    private Folder folder;
    private PrintStream errors;
    private String path;
    private TipFisier folderExistent = null;
    private String numeFisier = null;
    private SistemDeFisiere fileSystem;
    private boolean caleCorecta = true;
    private boolean caleAbsoluta = false;
    private boolean dejaExista = false;
    
    public TouchCommand(Scanner input, PrintStream errors, SistemDeFisiere fileSystem)
    {
        this.errors = errors;
        this.fileSystem = fileSystem;
        
        path = input.nextLine();
        Folder cautare;
        ArrayList<TipFisier> lista;

        
        if(path.charAt(1) == '/')
        {
            cautare = fileSystem.getRoot();
        }
        else
        {
            caleAbsoluta = true;//in cazul unei erori, va trebui sa construiesc calea absoluta
            cautare = fileSystem.getFolderCurent();
        }

        StringTokenizer st = new StringTokenizer(path);
        while (st.hasMoreTokens())
        {
            path = st.nextToken();
        }

        st = new StringTokenizer(path, "/");
        while (st.hasMoreTokens())
        {
            int ok = 0;
            String folder = st.nextToken();
            while(folder.equals(".") || folder.equals(".."))
            {
                if(folder.equals("."))
                {

                    folder = st.nextToken();
                }
                if(folder.equals(".."))
                {
                    if(cautare != fileSystem.getRoot())
                    {
                        cautare = cautare.getParinte();
                        folder = st.nextToken();
                    }
                    else
                    {
                        this.caleCorecta = false;
                        break;
                    }
                }
            }
            lista = cautare.getLista();
            for(TipFisier l : lista)
            {
                if(l.getNume().equals(folder))
                {
                    ok = 1;
                    if(l instanceof Folder) cautare = (Folder)l;
                    break;
                }
            }
            if(ok == 0)
            {
                if(st.countTokens() != 0)
                {
                    this.caleCorecta = false;
                }
                else
                {
                    this.numeFisier = folder;
                    this.folder = cautare;
                }
                break;
            }
            if(ok == 1 && st.countTokens() == 0)
            {
               this.numeFisier = folder;
               if(cautare.getNume().equals(folder)) folderExistent = cautare;
               dejaExista = true;
            } 
        }
        
        lista = cautare.getLista();
        for(TipFisier l : lista)
        {
            if(l.getNume().equals(numeFisier))
            {
                folderExistent = l;
                dejaExista = true;
                break;
            }
        }
    }
    public void execute()
    {
        if(caleCorecta && !dejaExista)
        {
            Fisier newfisier = new Fisier(numeFisier);
            folder.add(newfisier);
        }
        else if(!caleCorecta)
        {
            StringTokenizer st = new StringTokenizer(path, "/");
            if(path.charAt(0) == '/') path = "/";
            else path = "";
            while (st.hasMoreTokens())
            {
                if(st.countTokens() == 1) break;
                if(st.countTokens() != 2) path = path + st.nextToken() + "/";
                else path = path + st.nextToken();
            }
            errors.println("touch: " + path + ": No such directory");
        }
        else if(dejaExista)
        {
            if(caleAbsoluta)//construiesc calea absoluta
            {
                StringTokenizer st = new StringTokenizer(path, "/");
                
                int i = 0;
                String caleAbsoluta = "";
                TipFisier x;
                if(st.countTokens() > 1)
                {
                    caleAbsoluta = "/" + path ;
                    errors.println("touch: cannot create file " + caleAbsoluta + ": Node exists");
                }
                else
                {
                    x = fileSystem.getFolderCurent();
                    while(x != fileSystem.getRoot())
                    {
                        String aux = caleAbsoluta;
                        if(i != 0)
                        {
                            caleAbsoluta = x.getNume() + "/";
                        }
                        else
                        {
                            caleAbsoluta = x.getNume();
                            i++;
                        }
                        caleAbsoluta = caleAbsoluta + aux;
                        if(x instanceof Folder) x = ((Folder)x).getParinte();
                        else break;
                    }
                    String aux = caleAbsoluta;
                    if(!caleAbsoluta.equals("")) caleAbsoluta = "/";
                    caleAbsoluta = caleAbsoluta + aux + "/" + numeFisier + ":";
                    
                    errors.println("touch: cannot create file " + caleAbsoluta + " Node exists");
                }
            }
            else 
            {
                errors.println("touch: cannot create file " + path + ": Node exists");
            }
        }
    }
}
