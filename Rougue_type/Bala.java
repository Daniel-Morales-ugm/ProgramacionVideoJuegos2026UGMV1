import greenfoot.*;

public class Bala extends Actor
{
    private double dx, dy;
    private int velocidad = 8;
    private int vidaUtil = 80;
    private int danio;

    public Bala(double dirX, double dirY, int danio)
    {
        this.dx = dirX;
        this.dy = dirY;
        this.danio = danio;
        setImage(FabricaImagenes.BALA); 
        setRotation((int) Math.toDegrees(Math.atan2(dy, dx)));
    }

    public void act()
    {
        if (getWorld() == null) return;

        setLocation((int)(getX() + dx * velocidad), (int)(getY() + dy * velocidad));
        vidaUtil--;

        SoldadoEnemigo enemigo = (SoldadoEnemigo) getOneIntersectingObject(SoldadoEnemigo.class);
        if (enemigo != null)
        {
            // 1. Guardar las coordenadas ANTES de aplicar el daño
            int enemigoX = enemigo.getX();
            int enemigoY = enemigo.getY();
        
            // 2. Aplicar el daño (esto ejecuta morir() y lo elimina si su vida llega a 0)
            enemigo.recibirDanio(danio); 
        
            // 3. Crear el texto flotante usando las coordenadas que guardamos previamente
            if (getWorld() != null)
            {
                getWorld().addObject(new TextoDanio(danio), enemigoX, enemigoY - 15);
                getWorld().removeObject(this); // Eliminar la bala
            }
        
            return; // Salir del ciclo actual
        }

        // 4. Si la bala cumple su vida útil o sale del mundo, se destruye
        if (vidaUtil <= 0 || fueraDelMundo())
        {
            if (getWorld() != null) getWorld().removeObject(this);
        }
    }

    private boolean fueraDelMundo()
    {
        return getX() <= 2 || getX() >= getWorld().getWidth() - 2 ||
               getY() <= 2 || getY() >= getWorld().getHeight() - 2;
    }
}
