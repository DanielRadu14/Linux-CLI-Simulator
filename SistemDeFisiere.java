import java.io.*;

public class SistemDeFisiere {
    private final Folder root;
    private Folder folderCurent;
    private int nrComanda;
    public SistemDeFisiere()
    {
        this.root = new Folder("/");
        this.folderCurent = root;
        this.nrComanda = 0;
    }
    public Folder getRoot()
    {
        return root;
    }
    public Folder getFolderCurent()
    {
        return folderCurent;
    }
    public void setFolderCurent(Folder folderCurent)
    {
        this.folderCurent = folderCurent;
    }
    public void getNrComanda(PrintStream output, PrintStream errors)
    {
        output.println(++nrComanda);
        errors.println(nrComanda);
    }
}
