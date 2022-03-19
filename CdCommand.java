import java.io.*;
import java.util.*;

public class CdCommand implements Command{

    private Folder modificaFolder;
    private boolean caleCorecta = true;
    private String path;
    private SistemDeFisiere fileSystem;
    private PrintStream errors;
    
    public CdCommand(Scanner input, PrintStream errors, SistemDeFisiere fileSystem)
    {
        this.fileSystem = fileSystem;
        this.errors = errors;
        
        path = input.next();
        Folder cautare;
        ArrayList<TipFisier> lista;

        if(path.equals("/"))
        {
            this.modificaFolder = fileSystem.getRoot(); //setez curent directory in root
        }
        else
        {
            if(path.charAt(0) == '/')//incep cautarea din root sau din folderul curent
            {
                cautare = fileSystem.getRoot();
            }
            else cautare = fileSystem.getFolderCurent();


            boolean nuCauta = false;
            StringTokenizer st = new StringTokenizer(path, "/");
            while (st.hasMoreTokens())//parcurg path-ul
            {
                int ok = 0;
                String folder = st.nextToken();
                while(folder.equals(".") || folder.equals(".."))//tratez . si .. individual
                {
                    if(folder.equals("."))
                    {
                        folder = cautare.getNume();//retin numele ultimului fisier gasit pentru a nu cauta daca exista "." sau ".."
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
                        if(cautare != fileSystem.getRoot())
                        {
                            folder = cautare.getNume();
                            cautare = cautare.getParinte();//daca am citit "..", reiau cautarea din directorul parinte
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
                            this.caleCorecta = false;//directorul root nu are parinte => cale gresita
                            break;
                        }
                    }
                }
                if(!nuCauta)//caut fisierul citit in fisierul gasit anterior(parinte)
                {
                    lista = cautare.getLista();
                    for(TipFisier l : lista)
                    {
                        if(l.getNume().equals(folder))
                        {
                            ok = 1;
                            cautare = (Folder)l;
                            break;
                        }
                    }
                    if(ok == 0)//nu a fost gasit => cale gresita
                    {
                        caleCorecta = false;
                        break;
                    }
                    
                }
                this.modificaFolder = cautare;//folderul ce trebuie setat ca folder curent
                
            }
        }
    }
    public void execute()
    {
        if(caleCorecta)
        {
            fileSystem.setFolderCurent(modificaFolder);
        }
        else errors.println("cd: " + path + ": No such directory");
    }
}
