import java.io.*;
import java.util.*;

public class RmCommand implements Command{
    private TipFisier fisier;
    private PrintStream errors;
    private SistemDeFisiere fileSystem;
    private String path;
    private boolean caleCorecta = true;
    private boolean contineFolderCurent = false;
    
    public RmCommand(Scanner input, PrintStream errors, SistemDeFisiere fileSystem)
    {
        this.errors = errors;
        this.fileSystem = fileSystem;
        
        path = input.nextLine();
        TipFisier cautare;
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

        boolean nuCauta = false;
        st = new StringTokenizer(path, "/");
        while (st.hasMoreTokens())
        {
            int ok = 0;
            String folder = st.nextToken();
            while(folder.equals(".") || folder.equals(".."))
            {
                if(folder.equals("."))
                {
                    folder = cautare.getNume();
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
                    if(cautare != fileSystem.getRoot())
                    {
                        folder = cautare.getNume();
                        cautare = cautare.getParinte();
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
                        this.caleCorecta = false;
                        break;
                    }
                }
            }
            if(nuCauta == false)
            {
                lista = ((Folder)cautare).getLista();
                for(TipFisier l : lista)
                {
                    if(l.getNume().equals(folder))
                    {
                        ok = 1;
                        cautare = l;
                        break;
                    }
                }
                if(ok == 0)
                {
                    this.caleCorecta = false;
                    break;
                }
            }
            this.fisier = cautare;
        }
    }
    public void verificaRecursiv(TipFisier verifica)//verifica daca folderul ce trebuie sters contine current working directory
    {
        if(verifica.getNume().equals(fileSystem.getFolderCurent().getNume()))
        {
            contineFolderCurent = true;
        }
        if(verifica instanceof Folder)
        {
            ArrayList<TipFisier> lista = ((Folder)verifica).getLista();
            for(TipFisier f : lista)
            {
                verificaRecursiv(f);
            }
        }
    }
    public void execute()
    {
        if(caleCorecta)
        {   
            verificaRecursiv(fisier);
            if(!contineFolderCurent)
            {
                Folder parinteFisier = fisier.getParinte();
                parinteFisier.remove(fisier);
            }
        }
        else
        {
            errors.println("rm: cannot remove " + path + ": No such file or directory");
        }
    }
}
