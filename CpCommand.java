import java.io.*;
import java.util.*;

public class CpCommand implements Command{
    private TipFisier folderSource;
    private Folder folderDest;
    private PrintStream errors;
    private String source;
    private String dest;
    private boolean caleSource = true;
    private boolean caleDest = true;
    private boolean dejaExista = false;

    public CpCommand(Scanner input, PrintStream errors, SistemDeFisiere fileSystem)
    {
        this.errors = errors;
        
        source = input.next();
        dest = input.next();

        TipFisier cautareSource;
        ArrayList<TipFisier> lista;

        if(source.charAt(0) == '/')//incep cautarea din root sau din folderul curent
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
        while (st.hasMoreTokens())
        {
            int ok = 0;
            String folder = st.nextToken();
            while(folder.equals(".") || folder.equals(".."))//tratez . si .. individual
            {
                if(folder.equals("."))
                {
                    folder = cautareSource.getNume();//retin numele ultimului fisier gasit pentru a nu cauta daca exista "." sau ".."
                    if(st.hasMoreTokens())
                    {
                        folder = st.nextToken();
                    }
                    else
                    {
                        nuCauta = true;//daca "." a fost ultimul token, nu il mai caut in directorul parinte
                        break;
                    }
                }
                else if(folder.equals(".."))
                {
                    if(cautareSource != fileSystem.getRoot())
                    {
                        folder = cautareSource.getNume();
                        cautareSource = cautareSource.getParinte();//daca am citit "..", reiau cautarea din directorul parinte
                        if(st.hasMoreTokens())
                        {
                            folder = st.nextToken();
                        }
                        else
                        {
                            nuCauta = true;//daca ".." a fost ultimul token, nu il mai caut in directorul parinte
                            break;
                        }
                    }
                    else
                    {
                        this.caleSource = false;//directorul root nu are parinte => cale gresita
                        break;
                    }
                }
            }
            if(nuCauta == false)//caut fisierul citit in fisierul gasit anterior(parinte)
            {
                if(cautareSource instanceof Folder)
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
                }
                if(ok == 0)//nu a fost gasit => cale gresita
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
        while (st.hasMoreTokens())
        {
            int ok = 0;
            String folder = st.nextToken();
            while(folder.equals(".") || folder.equals(".."))//tratez . si .. individual
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
        this.folderSource = cautareSource;//folderul/fisierul ce trebuie copiat
        this.folderDest = cautareDest;//folderul in care trebuie copiat
        
        lista = cautareDest.getLista();
        for(TipFisier l : lista)//verific daca sursa deja exista in destinatie
        {
            if(l.getNume().equals(cautareSource.getNume()))
            {
                dejaExista = true;
                break;
            }
        }
    }
    public void copiazaRecursiv(Folder destinatie, TipFisier sursa)
    {
        if(sursa instanceof Folder)
        {
            Folder newfolder = new Folder(sursa.getNume());
            destinatie.add(newfolder);
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
            copiazaRecursiv(folderDest, folderSource);           
        }
        else if(!caleSource)
        {
            errors.println("cp: cannot copy " + source + ": No such file or directory");
        }
        else if(!caleDest)
        {
            errors.println("cp: cannot copy into " + dest + ": No such directory");
        }
        else if(dejaExista)
        {
            errors.println("cp: cannot copy " + source + ": Node exists at destination");
        }
    }
}
