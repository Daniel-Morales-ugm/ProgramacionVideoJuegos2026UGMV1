import greenfoot.*;
import java.util.List;

public class Jugador extends Soldado //[cite: 2]
{
    private int cooldownDisparo = 0;
    private int nivel = 1;
    private int expActual = 0;
    private int expSiguienteNivel = 50;
    private int danioDisparo = 10;
    private int cadenciaDisparo = 15; // Intervalo de disparos en ciclos
    private int vidaMaxima = 100;
    private int frameActual = 0;
    private int contadorAnimacion = 0;
    private boolean mirandoIzquierda = false;
    
    public Jugador()
    {
        super(100, 4); //[cite: 2]
    }

    public void act()
    {
        mover(); //[cite: 2]
        dispararAutomatico();
        if (cooldownDisparo > 0) cooldownDisparo--;
        animar();
    }

    private void mover()
    {
        int dx = 0;
        int dy = 0;
    
        if (Greenfoot.isKeyDown("w")) { dy -= velocidad; }
        if (Greenfoot.isKeyDown("s")) { dy += velocidad; }
        if (Greenfoot.isKeyDown("a")) { 
            dx -= velocidad; 
            mirandoIzquierda = true;  // <--- Mira a la izquierda
        }
        if (Greenfoot.isKeyDown("d")) { 
            dx += velocidad; 
            mirandoIzquierda = false; // <--- Mira a la derecha
        }

        int nuevoX = Math.max(15, Math.min(getWorld().getWidth() - 15, getX() + dx));
        int nuevoY = Math.max(15, Math.min(getWorld().getHeight() - 15, getY() + dy));
        setLocation(nuevoX, nuevoY);
    }

    private void dispararAutomatico()
    {
        if (cooldownDisparo == 0)
        {
            SoldadoEnemigo objetivo = obtenerEnemigoMasCercano();
            if (objetivo != null)
            {
                // Cálculo del vector unitario de dirección
                double diffX = objetivo.getX() - getX();
                double diffY = objetivo.getY() - getY();
                double dist = Math.sqrt(diffX * diffX + diffY * diffY);

                if (dist > 0)
                {
                    double dirX = diffX / dist;
                    double dirY = diffY / dist;

                    int cantidadBalas = getWorld().getObjects(Bala.class).size();
                    if (cantidadBalas < 60) // Límite de presupuesto de proyectiles[cite: 2]
                    {
                        Bala bala = new Bala(dirX, dirY, danioDisparo);
                        getWorld().addObject(bala, getX(), getY());
                    }
                    cooldownDisparo = cadenciaDisparo;
                }
            }
        }
    }

    private SoldadoEnemigo obtenerEnemigoMasCercano()
    {
        List<SoldadoEnemigo> enemigos = getWorld().getObjects(SoldadoEnemigo.class);
        SoldadoEnemigo masCercano = null;
        double menorDistancia = Double.MAX_VALUE;

        for (SoldadoEnemigo e : enemigos)
        {
            double dist = Math.hypot(e.getX() - getX(), e.getY() - getY());
            if (dist < menorDistancia)
            {
                menorDistancia = dist;
                masCercano = e;
            }
        }
        return masCercano;
    }

    public void ganarExp(int cantidad)
    {
        expActual += cantidad;
        if (expActual >= expSiguienteNivel)
        {
            subirDeNivel();
        }
    }

    private void subirDeNivel()
    {
        nivel++;
        expActual -= expSiguienteNivel;
        expSiguienteNivel = (int)(expSiguienteNivel * 1.5);
        danioDisparo += 5; // Aumento del daño por nivel
        
        // Efecto opcional: recuperar un poco de vida al subir nivel
        vida = Math.min(100, vida + 20); 
    }

    public int getNivel() { return nivel; }
    public int getDanioDisparo() { return danioDisparo; }

    protected void morir() //[cite: 2]
    {
        World mundo = getWorld();
        if (mundo instanceof JuegoWorld)
        {
            ((JuegoWorld) mundo).gameOver(); //[cite: 2]
        }
        if (mundo != null) mundo.removeObject(this);
    }
    
    public void curar(int cantidad)
    {
        vida += cantidad;
        if (vida > vidaMaxima)
        {
            vida = vidaMaxima;
        }
    }
    
    private void animar()
    {
        contadorAnimacion++;
        if (contadorAnimacion >= 10)
        {
            frameActual = (frameActual == 0) ? 1 : 0;
        
            // Creamos una copia del frame actual de la fábrica para no alterar el original
            GreenfootImage img = new GreenfootImage(FabricaImagenes.JUGADOR[frameActual]);
        
            if (mirandoIzquierda)
            {
                img.mirrorHorizontally();
            }   
        
            setImage(img);
            contadorAnimacion = 0;
        }
    }
}