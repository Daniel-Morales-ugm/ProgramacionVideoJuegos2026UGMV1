import greenfoot.*;

public class Plataforma extends Actor
{
    // Constructor por defecto (usa una imagen con su tamaño original o por defecto)
    public Plataforma()
    {
        setImage("plataforma_metal.png");
    }

    // Constructor dinámico: ajusta y escala la imagen al ancho y alto deseados
    public Plataforma(int ancho, int alto)
    {
        GreenfootImage img = new GreenfootImage("plataforma_metal.png");
        img.scale(ancho, alto); // Ajusta la imagen al tamaño especificado
        setImage(img);
    }
}