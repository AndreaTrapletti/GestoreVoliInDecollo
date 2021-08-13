package Decollo;

import java.util.concurrent.Semaphore;

public class TorreDiControllo implements TorreDiControlloIF{
	
	protected final int postiTotaliInPista = 4;   //settiamo a 4 il numero massimo di aeromotori che possiamo trovare in pista
	private int posAccesso;
	private Semaphore mutexPista;
	private Semaphore mutexDecollo; 
	private int[] voloPresente;
	/**
	 * COSTRUTTORE
	 */
	public TorreDiControllo() {
		this.posAccesso = 0;    //setto inizialmente la posizione di accesso alla coda della pista di decollo a 0.
		
		this.mutexPista = new Semaphore(postiTotaliInPista);  //Semaforo generalizzato settato con il numero di aeromobili massimi che si possono trovare nella coda
		                                                      //per l'accesso in pista
		this.mutexDecollo = new Semaphore(1, true);           //Semaforo binario per indicare se l'aeromotore può prendere la pista per decollare.
		this.voloPresente = new int[postiTotaliInPista];      //array della stessa grandezza degli aeromotori che possiamo trovare in coda in pista.
		for(int i = 0; i < postiTotaliInPista; i++) {         //la posizione della lista indica la posizione nella coda
			voloPresente[i] = 0;                              //l'array può avere 2 valori: 0 o 1, 0 se in quella posizione non c'è aeromotore in coda, 1 se
		}                                                     //il posto è occupato.
	}
	
	
	@Override
	public void richiestaPreparazioneDecollo(String aeromobile) {
		
		if(laCodaNonEPiena()) {   //se torna True vuol dire che in coda c'è ancora spazio.
			
			try {
				mutexPista.acquire(1);	  //Se le coda non è piena il Thread acquisisce un permesso 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//sezione Critica:
			System.out.println("L'aeromobile "+Thread.currentThread().getName()+": è entrato nell'area di preparazione al decollo...... Attesa permesso per decollo");
			voloPresente[posAccesso] = 1;     //il Thread è nella coda degli aeromotori pronti al decollo, la sua posizione viene messe a 1 (marcatore di stato occupato)
			
			
			posAccesso = (posAccesso + 1) % postiTotaliInPista;  //incremento di uno la poszione di accesso del prossimo Thread, in modo circolare, se siamo
			                                               //sull'ultima posizione esso torna a 0.
			
			
			
			try {
				mutexDecollo.acquire();                   //tentiamo di acquisire l'accesso alla pista per decollare, tendando di acquisire il permesso
				                                          //del semaforo binario. 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("                  L'aeromobile "+aeromobile+": è sulla pista di partenza.... PRONTO PER IL  DECOLLO !!!");
			
			mutexPista.release(1);  //se l'aeromotore inizia la fase di decollo rilasciamo un permesso per entarre nella coda deglia aeromotori 
			                        //pronti per decollare
			
			voloPresente[((posAccesso-1) < 0 ? 3 : posAccesso-1 )] = 0; //la posizione occupata precedentemente dall'automotore che ha iniziato la manovra di decollo viene liberata,
			                                                            //imposstato la giusta poszione dell'array a 0.
			
			try {
				Thread.currentThread().sleep(5000); //faccio perdere 5 secondi al Thread simulando che questo sia il tempo per lasciare la pista di decollo
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			decolloAvvenuto();                          //chiamo il metodo per segnalare il decollo avvenuto
			
		}
		
	}
	
	/**
	 * 
	 * @return true se la coda degli aeromotori in attesa di entrare in pista non è piena.
	 */
	private boolean laCodaNonEPiena() {
		for(int i = 0; i < postiTotaliInPista; i++) {
			if(voloPresente[i] == 0)
				return true;
		}
		return false;
	}
	
	@Override
	public void decolloAvvenuto() {
		System.out.println();
		System.out.println("                                           |*********************************|");
		System.out.println("                                              L'AEROMOBILE "+ Thread.currentThread().getName()+ ": E' IN VOLO.... ");
		System.out.println("                                           |*********************************|");
		System.out.println();
		
		mutexDecollo.release();   //rilascio il permesso per permettere a un altro Thread di entrare nella pista di decollo.
		
	}
	
	
	
}
