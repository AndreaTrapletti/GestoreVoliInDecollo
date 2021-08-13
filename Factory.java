package Decollo;
/**
 * 
 * @author Andrea_Trapletti
 *
 */

public class Factory {
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		final int numeroAerei = 10;   //costante del numero totale di aeromotori presenti
		System.out.println("COMPUTER DELLA TORRE DI CONTROLLO PER LA GESTIONE DI UN AEROPORTO.... TOTALE AEROMOBILI PRESENTI: "+numeroAerei+".");
		
		TorreDiControlloIF torre = new TorreDiControllo();    //creo l'oggetto TorreDiControllo per la gestione e la sincronizzazione dei voli.
		
		Aeromobile[] listAeromobili = new Aeromobile[numeroAerei];
		
		for(int i = 0; i < numeroAerei; i++) {
			Aeromobile aeromobile = new Aeromobile(torre, i);   //Creo oggetti di tipo Aeromobili, che estendono direttamente la classe Thread.
			listAeromobili[i] = aeromobile; 
		}
		for(int i = 0; i < listAeromobili.length; i++) {
			listAeromobili[i].start();       //invoco il metodo start dei Thread per rendere Runnable il thread stesso.
		}
		
	}

}
