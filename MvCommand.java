import java.io.*;
import java.util.*;

public class MvCommand implements Command{
    private TipFisier folderSource;
    private Folder folderDest;
    private PrintStream errors;
    private SistemDeFisiere fileSystem;
    private String source;
    private String dest;
    private boolean caleSource = true;
    private boolean caleDest = true;
    private boolean dejaExista = false;
    
    public MvCommand(Scanner input, PrintStream errors, SistemDeFisiere fileSystem)
    {
        this.fileSystem = fileSystem;
        this.errors = errors;
        
        source = input.next();
        dest = input.next();

        TipFisier cautareSource;
        ArrayList<TipFisier> lista;

        if(source.charAt(0) == '/')
        {
            cautareSource = fileSystem.getRoot();
        }
        else cautareSource = fileSystem.getFolderCurent();

        StringTokenizer st = new StringTokenizer(source);
        while (st.hasMoreTokens())
        {
            source = st.nextToken();
        }
        
        boolean nuCauta = false;
        st = new StringTokenizer(source, "/");
        while (st.hasMoreTokens())//verific daca path-ul este corect si retin numele fisierului ce trebuie mutat
        {
            int ok = 0;
            String folder = st.nextToken();
            while(folder.equals(".") || folder.equals(".."))
            {
                if(folder.equals("."))
                {
                    folder = cautareSource.getNume();
                    if(st.hasMoreTokens())
                    {
                        folder = st.nextToken();
                    }
                    else
                    {
                        nuCauta = true;
                        break;
                    }
                }
                else if(folder.equals(".."))
                {
                    if(cautareSource != fileSystem.getRoot())
                    {
                        folder = cautareSource.getNume();
                        cautareSource = cautareSource.getParinte();
                        if(st.hasMoreTokens())
                        {
                            folder = st.nextToken();
                        }
                        else
                        {
                            nuCauta = true;
                            break;
                        }
                    }
                    else
                    {
                        this.caleSource = false;
                        break;
                    }
                }
            }

            if(nuCauta == false)
            {
                lista = ((Folder)cautareSource).getLista();
                for(TipFisier l : lista)
                {
                    if(l.getNume().equals(folder))
                    {
                        ok = 1;
                        cautareSource = l;
                        break;
                    }
                }
                if(ok == 0)
                {
                    caleSource = false;
                    break;
                }
            }
        }

        Folder cautareDest;

        if(dest.charAt(0) == '/')
        {
            cautareDest = fileSystem.getRoot();
        }
        else cautareDest = fileSystem.getFolderCurent();

        st = new StringTokenizer(dest);
        while (st.hasMoreTokens())
        {
            dest = st.nextToken();
        }

        nuCauta = false;
        st = new StringTokenizer(dest, "/");
        while (st.hasMoreTokens())//verific daca path-ul este corect si retin numele folderului in care se face mutarea
        {
            int ok = 0;
            String folder = st.nextToken();
            while(folder.equals(".") || folder.equals(".."))
            {
                if(folder.equals("."))
                {
                    folder = cautareDest.getNume();
                    if(st.hasMoreTokens())
                    {
                        folder = st.nextToken();
                    }
                    else
                    {
                        nuCauta = true;
                        break;
                    }
                }
                else if(folder.equals(".."))
                {
                    if(cautareDest != fileSystem.getRoot())
                    {
                        folder = cautareDest.getNume();
                        cautareDest = cautareDest.getParinte();
                        if(st.hasMoreTokens())
                        {
                            folder = st.nextToken();
                        }
                        else
                        {
                            nuCauta = true;
                            break;
                        }
                    }
                    else
                    {
                        this.caleDest = false;
                        break;
                    }
                }
            }

            if(nuCauta == false)
            {
                lista = cautareDest.getLista();
                for(TipFisier l : lista)
                {
                    if(l.getNume().equals(folder))
                    {
                        ok = 1;
                        cautareDest = (Folder)l;
                        break;
                    }
                }
                if(ok == 0)
                {
                    caleDest = false;
                    break;
                }
            }
        }
        this.folderSource = cautareSource;//retin fisierul ce trebuie mutat
        this.folderDest = cautareDest;//retin folderul in care se face mutarea

        lista = cautareDest.getLista();
        for(TipFisier l : lista)
        {
            if(l.getNume().equals(cautareSource.getNume()))
            {
                dejaExista = true;
                break;
            }
        }
    }
    public void copiazaRecursiv(Folder destinatie, TipFisier sursa)//copiaza sursa si subdirectoarele ei in destinatie
    {
        if(sursa instanceof Folder)
        {
            Folder newfolder = new Folder(sursa.getNume());
            destinatie.add(newfolder);
            
            if(newfolder.getNume().equals(fileSystem.getFolderCurent().getNume()))
            {
                fileSystem.setFolderCurent(newfolder);
            }
            
            ArrayList<TipFisier> lista = ((Folder)sursa).getLista();
            for(TipFisier f : lista)
            {
                copiazaRecursiv(newfolder,f);
            }
        }
        else
        {
            Fisier newfolder = new Fisier(sursa.getNume());
            destinatie.add(newfolder);
        }
    }
    public void execute()
    {
        if(caleDest && caleSource && !dejaExista)
        {
            copiazaRecursiv(folderDest, folderSource);//copiez sursa in destinatie
            Folder parinteSursa = folderSource.getParinte();
            parinteSursa.remove(folderSource);//sterg sursa din locatia veche
        }
        else if(!caleSource)
        {
            errors.println("mv: cannot move " + source + ": No such file or directory");
        }
        else if(!caleDest)
        {
            errors.println("mv: cannot move into " + dest + ": No such directory");
        }
        else if(dejaExista)
        {
            errors.println("mv: cannot move " + source + ": Node exists at destination");
        }
    }
}
