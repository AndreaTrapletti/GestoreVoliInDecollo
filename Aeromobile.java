package Decollo;

public class Aeromobile extends Thread {   
	
	protected TorreDiControlloIF torre;
	private String idAeromobile;
	
	/**
	 * COSTRUTTORE
	 * @param torre
	 * @param pos
	 */
	public Aeromobile(TorreDiControlloIF torre, int pos) { 
		this.torre = torre;
		pos++;
		setName(""+pos);    //setto in modo identico sia il nome del Thread che il nome dell'aeromobile stesso
		this.idAeromobile = ""+pos;
	
	}
	@Override
	public void run() {  
		while(true) {  //ciclo nel while all'infinito
			try {
				sleep((int) (Math.random()*2000));                        //aggiungo uno sleep randomico di massimo 2 secondi per aumebatre la competitività dei
				                                                          //Thread dopo l'invocazione del loro metodo run.
				
				torre.richiestaPreparazioneDecollo(this.idAeromobile);   //chiamo il metodo di TorreDiControllo
				
				sleep((int)(Math.random()*20000));                       //facccio uno sleep randomico sul thread immaginando che dopo il volo
				                                                         //esso debba prima tornare al agte di partenza e dopo inoltrare al sistema
				                                                         //un'altra richiesta di prenotazione per il decollo.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
