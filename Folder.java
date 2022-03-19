import java.io.*;
import java.util.*;
public class Folder implements TipFisier{
    private final ArrayList<TipFisier> lista;
    private final String nume;
    private Folder parinte;

    public Folder(String nume)
    {
        parinte = null;
        this.nume = nume;
        this.lista = new ArrayList<>();
    }
    public void add(TipFisier fisier)
    {
        fisier.setParinte(this);
        lista.add(fisier);
        
        /*sortez lexicografic directorul in care am adaugat*/
        int i = 0;
        while(i < lista.size() - 1)
        {
            if(lista.get(i).getNume().compareTo(lista.get(i+1).getNume()) > 0)
            {
                TipFisier aux = lista.get(i);
                lista.set(i, lista.get(i+1));
                lista.set(i+1, aux);
                i = 0;
            }
            else i++;
        }
    }
    public void setParinte(Folder parinte)
    {
        this.parinte = parinte;
    }
    public Folder getParinte()
    {
        return parinte;
    }
    public void remove(TipFisier fisier)
    {
        lista.remove(fisier);
    }
    public String getNume()
    {
        return nume;
    }
    public ArrayList<TipFisier> getLista()
    {
        return lista;
    }
    public String caleAbsolutaR(TipFisier x, SistemDeFisiere fileSystem)//scriu calea absoluta pentru fisierul x
    {
        int i = 0;
        String caleAbsoluta = "";
        while(x != fileSystem.getRoot())
        {
            String aux = caleAbsoluta;
            caleAbsoluta = x.getNume() + "/";
            caleAbsoluta = caleAbsoluta + aux;
            x = ((Folder)x).getParinte();
        }
        return caleAbsoluta;
    }
    public void lsR(PrintStream output, PrintStream errors, SistemDeFisiere fileSystem)//functia ls cu argumentul -R
    {
        this.ls(output, errors, fileSystem);
        for (TipFisier y : lista)
        {
            if(y instanceof Folder)
            {
                y.lsR(output, errors, fileSystem);
            }
        }
    }
    public String caleAbsoluta(TipFisier x, SistemDeFisiere fileSystem)//scriu calea absoluta pentru fisierul x
    {
        int i = 0;
        String caleAbsoluta = "";
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
            x = ((Folder)x).getParinte();
        }
        String aux = caleAbsoluta;
        caleAbsoluta = "/";
        caleAbsoluta = caleAbsoluta + aux + ":";
        return caleAbsoluta;
    }
    public void ls(PrintStream output, PrintStream errors, SistemDeFisiere fileSystem)//functia ls
    {
        output.println(caleAbsoluta(this, fileSystem));
        for(int i = 0; i < lista.size(); i++) 
        {
            output.printf("/");
            output.printf(caleAbsolutaR(this, fileSystem) + lista.get(i).getNume());
            if(i != lista.size() - 1) output.printf(" ");
        }
        output.printf("\n\n");
    }
    public void pwd(PrintStream output)//functia pwd
    {
        Folder x = this;
        int i = 0;
        String caleAbsoluta = "";
        while(!x.getNume().equals("/"))
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
            x = ((Folder)x).getParinte();
        }
        String aux = caleAbsoluta;
        caleAbsoluta = "/";
        caleAbsoluta = caleAbsoluta + aux;
        output.println(caleAbsoluta);
    }
    
}
