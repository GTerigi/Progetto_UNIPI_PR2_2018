package HashList;
import java.util.Hashtable;
import java.util.Iterator;

public class MyDriveHash<E> implements SecureDataContainer<E> {
	// OVERVIEW: Tipo mutabile che rappresenta una collezione di Drive relativi a degli utenti.
	// Typical Element: D[0..n] di tipo UserAccount. 
	// Rep Invariant: RI(m) = D!=null && (forall element : D => (forall x : D-{element} | element!=x)).
	
	private Hashtable<String, UserAccount<E>> Drive;

			//COSTRUTTORE
	
	public MyDriveHash() {
		Drive= new Hashtable<String, UserAccount<E>>();
	}
	
			//MODIFICATORI
	
	/* REQUIRES: Drive!=null
	 * THROWS: NullPointerException se Drive==null;
	 * EFFECTS: this.Drive= old(Drive) U newuser
	 */
	public void createUser(String Id, String passw) {
		try{
			if(Drive==null) throw new NullPointerException("Errore, Drive non inizializzato");
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
		if(Drive.isEmpty()) {
			UserAccount<E> newuser=new UserAccount<E>(Id, passw);
			Drive.put(Id, newuser);
		}
		else {
			try {
				if(!Drive.containsKey(Id)) Drive.put(Id, new UserAccount<E>(Id, passw));
				else throw new IllegalUserException("Errore, user presente\n");
			}
			catch(IllegalUserException e) {
				System.out.println(e);
			}
		}
	}

	public void removeUser(String Id, String passw) {
		try {
			if(Drive==null) throw new NullPointerException("Errore, Drive non inizializzato");
			if(Drive.isEmpty()) throw new Exception("Drive vuoto!");
			else if(Drive.containsKey(Id)) {
				UserAccount<E> tmp=Drive.get(Id);
				if(MatchUser(tmp, Id,passw)) {
					Drive.remove(tmp);
					System.out.println("L'utente di nome " + Id + " è stato rimosso");
					return;
				}
			}
			System.out.println("Nessun utente di nome " + Id + " trovato");
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/* REQUIRES: Drive!=null && Drive non vuoto
	 * THROWS: NullPointerException se Drive==null, Exception se il Drive è vuoto
	 * EFFECTS: Se Drive contiene l'User specificato inserisce il dato
	 * RETURN: True se il Dato è inserito correttamente
	 */
	public boolean put(String Owner, String passw, E data) {
		try {
			if(Drive==null) throw new NullPointerException("Errore, Drive non inizializzato");
			if(Drive.isEmpty()) throw new Exception("Drive vuoto!");
			else if(Drive.containsKey(Owner)) {
				UserAccount<E> tmp=Drive.get(Owner);
				if(MatchUser(tmp, Owner,passw)) {
					tmp.SetData(data);
					return true;
				}
			}
			System.out.println("Nessun utente di nome " + Owner + " trovato");
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
			return false;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return false;
	}
	
	/* REQUIRES: Drive!=null && Drive non vuoto
	 * THROWS: NullPointerException se Drive==null, Exception se il Drive è vuoto
	 * EFFECTS: Se Drive contiene l'User specificato rimuove il dato (se presente)
	 * RETURN: L'elemento eliminato.
	 */
	public E remove(String Owner, String passw, E data) {
		try {
			if(Drive==null) throw new NullPointerException("Errore, Drive non inizializzato");
			if(Drive.isEmpty()) throw new Exception("Drive vuoto!");
			if(Drive.containsKey(Owner)){
				UserAccount<E> tmp=Drive.get(Owner);
				if(MatchUser(tmp, Owner, passw)){					
					E tmpdata=tmp.GetData(data);
					if(tmp.RemoveData(data)) {
						System.out.println("Il dato "+ data +" è stato rimosso\nDimenzione attuale:" + getSize(Owner, passw));
						return tmpdata;
					}
				}
			}
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
			return null;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		return null;
	}
	/* REQUIRES: Drive!=null && Drive non vuoto
	 * THROWS: NullPointerException se Drive==null, Exception se il Drive è vuoto
	 * EFFECTS: Se Drive contiene l'User specificato copia il dato selezionato.
	 */
	public void copy(String Owner, String passw, E data) {
		try {
			if(Drive==null) throw new NullPointerException("Errore, Drive non inizializzato");
			if(Drive.isEmpty()) throw new Exception("Drive vuoto!");
			if(Drive.containsKey(Owner)) {
				UserAccount<E> tmp=Drive.get(Owner);
				if(MatchUser(tmp, Owner,passw)) {
					E elem=tmp.GetData(data);
					System.out.println("Il dato "+ elem +" è stato copiato\nDimenzione attuale:" + getSize(Owner, passw));
					tmp.SetData(elem);
					return;
				}
			}
			System.out.println("Nessun utente di nome " + Owner + " trovato");
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/* REQUIRES: Drive!=null && Drive non vuoto
	 * THROWS: NullPointerException se Drive==null, Exception se il Drive è vuoto
	 * EFFECTS: Se Drive contiene le due chiavi specificate copia il dato nel secondo utente.
	 * RETURN: True se il Dato è inserito correttamente
	 */
	public void share(String Owner, String passw, String Other, E data) {
		try {
			if(Drive==null) throw new NullPointerException("Errore, Drive non inizializzato");
			if(Drive.isEmpty()) throw new Exception("Drive vuoto!");
			UserAccount<E> UserPrincipale=null;
			UserAccount<E> UserSecondario=null;
            E datatocopy = null;
			if(Drive.containsKey(Other)){
				UserSecondario=Drive.get(Other);
			}
			if(Drive.containsKey(Owner)) {
				UserPrincipale=Drive.get(Owner);
				if(MatchUser(UserPrincipale, Owner, passw)) {
					datatocopy=UserPrincipale.GetData(data);
				}
			}
			if(UserSecondario!=null && datatocopy!=null) {
				System.out.println("Il dato "+ datatocopy +" è stato condiviso con "+ Other + "\nDimenzione attuale:" + getSize(Owner, passw));
				UserSecondario.SetData(datatocopy);
	            return;
			}
			else System.out.println("La condivisione non è avvenuta. Controllare i parametri in ingresso.");
            
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
			//PRODUTTORI
	
	/* REQUIRES: Drive!=null && Drive non vuoto.
	 * THROWS: NullPointerException se Drive==null, Exception se il Drive è vuoto.
	 * RETURN: La dimensione del Drive dell'utente scelto.
	 */
	public int getSize(String Owner, String passw) {
		try {
			if(Drive==null) throw new NullPointerException("Errore, Drive non inizializzato");
			if(Drive.isEmpty()) throw new Exception("Drive vuoto!");
			else if(Drive.containsKey(Owner)) {
				UserAccount<E> tmp=Drive.get(Owner);
				if(MatchUser(tmp, Owner,passw)) return tmp.GetSize();
			}
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return -1;
	}

	/* REQUIRES: Drive!=null && Drive non vuoto
	 * THROWS: NullPointerException se Drive==null, Exception se il Drive è vuoto
	 * RETURN: Il dato richiesto
	 */
	public E get(String Owner, String passw, E data) {
		try {
			if(Drive==null) throw new NullPointerException("Errore, Drive non inizializzato");
			if(Drive.isEmpty()) throw new Exception("Drive vuoto!");
			if(Drive.containsKey(Owner)){
				UserAccount<E> tmp=Drive.get(Owner);
				if(MatchUser(tmp, Owner,passw)) {
					System.out.println("Il dato "+ data + " è stato recuperato");
					return tmp.GetData(data);
				}
			}
			System.out.println("Nessun utente di nome " + Owner + " trovato");
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/* REQUIRES: Drive!=null && Drive not empty
	 * THROWS: if Drive==null throws NullPointerException, if Drive is empty throws Exception
	 * RETURNS: Un iteratore alla collezione selezionata, se superati i controlli di sicurezza
	 */
	public Iterator<E> getIterator(String Owner, String passw) {
		try {
			if(Drive==null) throw new NullPointerException("Drive non inizializzato");
			if(Drive.isEmpty()) throw new Exception("Drive vuoto!");
			if(Drive.containsKey(Owner)) {
				UserAccount<E> tmp=Drive.get(Owner);
				if(MatchUser(tmp, Owner, passw)) {
					if(tmp.GetSize()>0) {
						return tmp.GetIterator();
					}
					throw new NullPointerException("Errore, drive dell'account vuoto");
				}
			}
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
			//OSSERVATORI
	
	/* REQUIRES: obj!=null 
	 * THROWS: if obj==null throws NullPointerException
	 * RETURNS: True se l'oggetto supera i controlli di sicurezza.
	 */
	private boolean MatchUser(UserAccount<E> obj,String Owner, String passw) {
		try {
			if(obj==null) throw new NullPointerException("Errore, utente non inizializzato");
			if(obj.equalsOwner(Owner)) {
				if(obj.MatchPassword(passw)) {
					return true;
				}
			}
			return false;
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
			return false;
		}
		
	}

}