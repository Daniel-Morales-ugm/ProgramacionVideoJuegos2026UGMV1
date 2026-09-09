import greenfoot.*;

public class TextoDanio extends Actor
{
    private int vidaUtil = 25; // Duración en pantalla

    public TextoDanio(int danio)
    {
        GreenfootImage img = new GreenfootImage(String.valueOf(danio), 20, Color.YELLOW, new Color(0,0,0,0));
        setImage(img);
    }

    public void act()
    {
        setLocation(getX(), getY() - 1); // Flota hacia arriba
        vidaUtil--;
        if (vidaUtil <= 0 && getWorld() != null)
        {
            getWorld().removeObject(this);
        }
    }
}