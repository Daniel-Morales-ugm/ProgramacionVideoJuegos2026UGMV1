import greenfoot.*;

public class TextoCuracion extends Actor
{
    private int vidaUtil = 35; // Dura un poco más que el daño para que se lea bien

    public TextoCuracion(int cantidad)
    {
        // Genera el texto con el "+" por delante (ejemplo: "+25")
        String texto = "+" + cantidad;
        
        // Letra tamaño 22, color verde, fondo transparente
        GreenfootImage img = new GreenfootImage(texto, 22, Color.GREEN, new Color(0,0,0,0));
        setImage(img);
    }

    public void act()
    {
        setLocation(getX(), getY() - 1); // Flota lentamente hacia arriba
        vidaUtil--;
        
        if (vidaUtil <= 0 && getWorld() != null)
        {
            getWorld().removeObject(this);
        }
    }
}