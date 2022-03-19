import java.io.*;
import java.util.*;

public class MkDirCommand implements Command{
    private String numeFolder = null;
    private Folder folder;
    private PrintStream errors;
    private String path;
    private TipFisier folderExistent = null;
    private SistemDeFisiere fileSystem;
    private boolean caleCorecta = true;
    private boolean dejaExista = false;
    public MkDirCommand(Scanner input, PrintStream errors, SistemDeFisiere fileSystem)
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
        else cautare = fileSystem.getFolderCurent();

        StringTokenizer st = new StringTokenizer(path);
        while (st.hasMoreTokens())
        {
            path = st.nextToken();
        }
        
        st = new StringTokenizer(path, "/");
        while (st.hasMoreTokens())//verific daca path-ul este corect si retin numele folderului ce trebuie creat
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
                    else ok = 0;
                    break;
                }
                
            }
            if(ok == 0)
            {
                if(st.countTokens() != 0)
                {
                    this.caleCorecta = false;
                }
                else//daca nu a mai ramas niciun token, ultimul fisier care nu a fost gasit este cel ce trebuie creat
                {
                    this.numeFolder = folder;
                    this.folder = cautare;
                }
                break;
            }
            if(ok == 1 && st.countTokens() == 0)//daca s-a gasit si ultimul fisier din path, inseamna ca el exista deja
            {
               this.numeFolder = folder;
               if(cautare.getNume().equals(folder)) folderExistent = cautare;
               dejaExista = true;
            }   
        }
        lista = cautare.getLista();
        for(TipFisier l : lista)
        {
            if(l.getNume().equals(numeFolder))
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
            
            Folder newfolder = new Folder(numeFolder);
            folder.add(newfolder);
        }
        else if(!caleCorecta)
        {
            /*construiesc calea parinte*/
            StringTokenizer st = new StringTokenizer(path, "/");
            if(path.charAt(0) == '/') path = "/";
            else path = "";
            while (st.hasMoreTokens())
            {
                if(st.countTokens() == 1) break;
                if(st.countTokens() != 2) path = path + st.nextToken() + "/";
                else path = path + st.nextToken();
            }
            errors.println("mkdir: " + path + ": No such directory");
        }
        else if(dejaExista)
        {
            if(path.charAt(0) == '/')
                errors.println("mkdir: cannot create directory " + path + ": Node exists");
            else 
                errors.println("mkdir: cannot create directory /" + path + ": Node exists");
        }
    }
}
