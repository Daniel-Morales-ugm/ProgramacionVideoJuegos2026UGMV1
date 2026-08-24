import greenfoot .*;

public class Enemigo extends Actor {
    private Estado estado = new Avanzar () ;

    public Enemigo () {
        GreenfootImage img = new GreenfootImage (24 , 24) ;
        img . setColor ( Color . RED ) ;
        img . fillOval (0 , 0 , 24 , 24) ;
        setImage ( img ) ;
    }

    public void act () {
        estado=estado.actuar(this);
        if (getX()<1) {
            getWorld().removeObject(this);
            return;
        }
        Nave nave = (Nave)getOneIntersectingObject(Nave.class);
        if (nave != null) {
            nave.recibirDaño();
            getWorld().removeObject(this);
            return;
        }
    }

    public void destruir () {
        GameManager . getInstancia () . sumarPuntos (10) ;
        if ( getWorld () != null ) {
        getWorld () . removeObject ( this ) ;
        }
    }
}
