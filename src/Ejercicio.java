class Globo{
	public String color;
	public Globo(String color){
		this.color=color;
	}
	public void setColor(String color){
		this.color=color;
	}
	public String getColor(){
		return color;
	}
}
public class Ejercicio {
	public static void main(String[] args) {
		Globo g1=new Globo("Azul");
		Globo g2=new Globo("Verde");
		Globo g3=new Globo("Blanco");
		Globo g4=new Globo("Rojo");
		tricky(g1,g2,g3,g4);
		System.out.println("Los globos son: 1,"+g1.color+"; 2,"+g2.color+"; 3,"+g3.color+"; 4,"+g4.color);
	}
	public static void tricky(Globo g1, Globo g2, Globo g3, Globo g4){
		Globo temp1=g1;
		temp1.color="Verde";
		g2.color=g1.color;
		g1=g2;
		g3=g4;
		g1.color="Azul";
		g3.color="Blanco";
	}
}
