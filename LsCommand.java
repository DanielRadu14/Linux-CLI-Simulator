import java.util.*;
import java.io.*;

public class LsCommand implements Command{
    private TipFisier fisier;
    private PrintStream output;
    private PrintStream errors;
    private SistemDeFisiere fileSystem;
    private String path;
	private boolean caleAbsoluta = false;
    private boolean caleCorecta = true;
	private boolean R = false;

    public LsCommand(Scanner input, PrintStream output, PrintStream errors, SistemDeFisiere fileSystem)
    {
        this.output = output;
        this.errors = errors;
        this.fileSystem = fileSystem;
        
        path = "null";
        boolean cale = false;
                
        try
        {
            path = input.nextLine();
        }
        catch(NoSuchElementException e){}
        
	/*verific tipul argumentelor date comenzii ls*/
        int nrArgumente = (new StringTokenizer(path)).countTokens();
        if(nrArgumente == 2)
        {
            R = true;
            cale = true;
        }
        else if(nrArgumente == 1)
        {
            if(path.contains("-R"))
            {
                R = true;
            }
            else
            {
            	cale = true;
            }
        }

        Folder cautare = null;

        if(cale == true)
        {
            StringTokenizer st = new StringTokenizer(path);
            String pathAux = null;
            /*selectez corect path dintre argumentele comenzii
            deoarece pozitia argumentului -R difera*/
            while (st.hasMoreTokens())
            {
                pathAux = st.nextToken();
            }
            if(pathAux.equals("-R"))
            {
                st = new StringTokenizer(path);
                path = st.nextToken();
            }
            else
            {
                path = pathAux;
            }

            if(path.charAt(0) == '/')//incep cautarea din root sau din folderul curent
            {
                cautare = fileSystem.getRoot();
            }
            else
            {
                cautare = fileSystem.getFolderCurent();
            }
            
            ArrayList<TipFisier> lista;
            String folder;
            boolean nuCauta = false;
            st = new StringTokenizer(path, "/");
            while (st.hasMoreTokens())//verific daca path-ul este corect si retin directorul ce trebuie afisat
            {
                int ok = 0;
                folder = st.nextToken();
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
                    lista = cautare.getLista();
                    for(TipFisier l : lista)
                    {
                        if(l instanceof Folder)
                        {
                            if(l.getNume().equals(folder))
                            {
                                ok = 1;
                                cautare = (Folder)l;
                                break;
                            }
                        }
                    }
                    if(ok == 0)
                    {
                        caleCorecta = false;
                        break;
                    }
                }
            }
        }
		
	/*setez fisierul al carui continut trebuie afisat*/
        if(path.equals("."))
        {
            this.fisier = fileSystem.getFolderCurent();
        }
        if(cale == false)
        {
            this.fisier = fileSystem.getFolderCurent();
        }
        else
        {
            if(R == true)
            {
                if(path.equals("/"))
                {
                    this.fisier = fileSystem.getRoot();
                }
                else
                {
                    this.fisier = cautare;
                }
            }
            else
            {
                this.fisier = cautare;
            }
        }
    }
    public void execute()
    {
        if(fisier instanceof Fisier)
        {
            errors.println("ls: " + path + ": No such directory");
        }
        else
        {
            if(caleCorecta == true)
            {
                if(!R)
                    fisier.ls(output, errors, fileSystem);
                else 
                    fisier.lsR(output, errors, fileSystem);
            }
            else errors.println("ls: " + path + ": No such directory");
        }
    }
}
