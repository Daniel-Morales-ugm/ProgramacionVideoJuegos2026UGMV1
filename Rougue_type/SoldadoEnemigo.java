import greenfoot .*;
import java.util.List;

public class SoldadoEnemigo extends Soldado
{
    private Jugador objetivo ;
    private int danio ;
    private int cooldownAtaque = 0;
    private int frameActual = 0;
    private int contadorAnimacion = 0;
    public SoldadoEnemigo ( Jugador objetivo ,
    int vida ,
    int velocidad ,
    int danio )
    {
        super ( vida , velocidad ) ;
        this . objetivo = objetivo ;
        this . danio = danio ;
    }
    public void act ()
    {
        if ( objetivo == null || objetivo . getWorld () == null ) return ;
        perseguir () ;
        animar();
        if ( cooldownAtaque > 0) cooldownAtaque --;
        atacar () ;
        
    }
    private void perseguir()
    {
        int dx = objetivo.getX() - getX();
        int dy = objetivo.getY() - getY();
        double distancia = Math.sqrt(dx * dx + dy * dy);

        if (distancia > 0)
        {
            int movX = (int) Math.round(velocidad * dx / distancia);
            int movY = (int) Math.round(velocidad * dy / distancia);
            setLocation(getX() + movX, getY() + movY);
        
            // --- ORIENTACIÓN DINÁMICA DEL ENEMIGO ---
            if (dx < 0) 
            {
                // El jugador está a la izquierda
                GreenfootImage img = new GreenfootImage(FabricaImagenes.ENEMIGO[frameActual]);
                img.mirrorHorizontally();
                setImage(img);
            }
            else if (dx > 0) 
            {
                // El jugador está a la derecha
                setImage(FabricaImagenes.ENEMIGO[frameActual]);
            }
        }
    }          
    private void atacar ()
    {
        if ( isTouching ( Jugador . class ) && cooldownAtaque == 0)
        {
            objetivo . recibirDanio ( danio ) ;
            cooldownAtaque = 30;
        }
    }
    protected void morir()
    {
        World mundo = getWorld();
    
        if (mundo != null)
        {
            // --- NUEVO: Drop de curación (5% de probabilidad) ---
            // Greenfoot.getRandomNumber(100) genera un número del 0 al 99
            if (Greenfoot.getRandomNumber(100) < 5)
            {
                mundo.addObject(new Curacion(), getX(), getY());
            }

            // Otorgar PX al jugador
            java.util.List<Jugador> jugadores = mundo.getObjects(Jugador.class);
            if (!jugadores.isEmpty())
            {
                jugadores.get(0).ganarExp(15);
            }
        }
    
        // Llamar al método morir() de la clase base Soldado
        super.morir(); 
    }
    private void animar()
    {
        contadorAnimacion++;
        if (contadorAnimacion >= 10) // Cambia de frame cada 10 ciclos
        {
            frameActual = (frameActual == 0) ? 1 : 0;
            setImage(FabricaImagenes.ENEMIGO[frameActual]);
            contadorAnimacion = 0;
        }
    }
}

