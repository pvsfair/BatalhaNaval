public class Navio {

    public Navio(int posX, int posY, int tamanho, int rotacao) {
        PosX = posX;
        PosY = posY;
        Tamanho = tamanho;
        Rotacao = rotacao;
    }

    public int getPosX() {
        return PosX;
    }

    public void setPosX(int posX) {
        PosX = posX;
    }

    public int getPosY() {
        return PosY;
    }

    public void setPosY(int posY) {
        PosY = posY;
    }

    public int getTamanho() {
        return Tamanho;
    }

    public void setTamanho(int tamanho) {
        Tamanho = tamanho;
    }

    public int getRotacao() {
        return Rotacao;
    }

    public void setRotacao(int rotacao) {
        Rotacao = rotacao;
    }

    private int PosX;
    private int PosY;
    private int Tamanho;
    private int Rotacao;
}
