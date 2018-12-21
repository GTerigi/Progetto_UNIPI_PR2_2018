package ArrayList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserAccount<E> implements UserInterface<E> {
	// OVERVIEW: Tipo mutabile che rappresente un utente e i suoi dati privati.
	// Typical Element: m: < user, password, data > dove
	// user è un valore di tipo String univoco che descrive il nome dell'utente,
	// password descrive la chiave di accesso ai dati e data è il campo generico che descrive i dati.
	// Rep Invariant: RI(m) = m.user!=null && m.password!=null
	
	private String user;
	private String password;
	private ArrayList<E> data;
	
	//		COSTRUTTORI		//
	
	public UserAccount(String user, String pass, E data) {
		SetUser(user);
		SetPassword(pass);
		this.data=new ArrayList<E>();
		this.data.add(data);
	}
	public UserAccount(String user, String pass) {
		SetUser(user);
		SetPassword(pass);
		data=new ArrayList<E>();
	}
	
	//		MODIFICATORI		//
	
	/* REQUIRES: data!=null
	 * THROW: if data==null Throw NullPointerException
	 * MODIFIES: this.data
	 * EFFECTS: this.data=this.data U data2
	 */
	public void SetData(E data2) {
		try {
			if(this.data==null) throw new NullPointerException("Data not initialized.");
			this.data.add(data2);	
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/* REQUIRES: pass!=null
	 * THROWS: Se pass==null lancia IllegalPasswordException
	 * MODIFIES: this.password
	 * EFFECTS: this.password=pass
	 */
	private void SetPassword(String pass) {
		try{
			if(pass==null) throw new IllegalPasswordException("Errore: Password null not valid");
			else this.password=pass;
		}
		catch(IllegalPasswordException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/* REQUIRES: user!=null
	 * THROWS: Se user==null lancia IllegalUserException
	 * MODIFIES: this.user
	 * EFFECTS: this.user=user2
	 */
	private void SetUser(String user2) {
		try {
			if(user2==null) throw new IllegalUserException("Errore: User null non valido");
			else this.user=user2;
		}
		catch(IllegalUserException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/* REQUIRES: data!=null
	 * THROWS: if data==null throw NullPointerException
	 * MODIFIES: this.data
	 * EFFECTS: this.data = old(this.data) - {data}
	 */
	public boolean RemoveData(E data) {
		try {
			if(this.data==null || this.data.isEmpty())throw new NullPointerException("Errore: Data not Found");
			if(this.data.contains(data)) {
				this.data.remove(data);
				return true;
			}
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return false;
	}

	//		OSSERVATORI		//
	
	/* Restituisce true se è uno user è già presente */
	/* REQUIRES: name!=null
	 * THROWS: Se name==null lancia NullPointerException
	 * EFFECT: if(name.equals(this.user) return true
	 */
	
	public boolean equalsOwner(String newname) {
		try{
			if(newname==null) throw new NullPointerException("Errore, obj==null");
			if(newname.equals(this.user)) return true;
			else return false;
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	
	
	/* REQUIRES: password!=null
	 * THROWS: Se password==null lancia NullPointerException || se password!=this.password lancia una IllegalPasswordException
	 * RETURN: if(password.equals(this.password) return true 
	 */
	public boolean MatchPassword(String password) {
		try{
			if(password==null) throw new NullPointerException("La password immessa non è valida");
			if(password.equals(this.password)) return true;
			else throw new IllegalPasswordException("La password immessa non ha nessun match con l'username " +this.user);
			}
		catch(IllegalPasswordException e) {
			System.out.println(e.getMessage());
			return false;
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/* REQUIRES: data!=null.
	 * THROWS: NullPointerException se data==null.
	 * RETURN: data.size.
	 */
	public int GetSize() {
		try{
			if(data!=null) {
				return this.data.size();
			}
			else throw new NullPointerException("Driver non inizializzato!");
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
			return -1;
		}
		
	}
	
	//		PRODUTTORI		//
	
	/* REQUIRES: this.user!=null
	 * THROWS: this.password==null throws NullPointerException
	 * RETURNS: Shallow copy of this.user
	 */
	public String GetUser() {
		try {
			if(this.user!=null) {
				String usercopy=this.user;
				return usercopy;
			}
			else throw new NullPointerException("Errore, user non inizializzato");
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	
	/* REQUIRES: this.password!=null
	 * THROWS: this.password==null throws NullPointerException
	 * RETURNS: Shallow copy of this.password
	 */
	public String GetPassword() {
		try {
			if(this.password!=null) {
				String passcopy=this.password;
				return passcopy;
			}
			else throw new NullPointerException("Errore, password non inizializzata");
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/* REQUIRES: this.data!=null && this.data not empty && this.data U nomedata = this.data
	 * THROWS: Se this.data==null || this.data is empty throw new NullPointerException
	 * 		   Se this.data U nomedata != this.data throw new DataNotFoundException
	 * RETURNS: L'elemento relativo a nomedata
	 */
	public E GetData(E nomedata) {
		try {
			if(this.data==null || this.data.isEmpty()) throw new NullPointerException("Errore: Data not Found");
			if(this.data.contains(nomedata)) {
				int index=this.data.indexOf(nomedata);
				return this.data.get(index);
			}
			else {
				throw new DataNotFoundException("Il dato inserito non è presente nella collezione");
			}
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
		}
		catch(DataNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/* REQUIRES: this.data!=null && this.data not empty
	 * THROWS: Se this.data==null || this.data is empty throw new NullPointerException
	 * RETURN: L'iteratore al drive selezionato.
	 */
	public Iterator<E> GetIterator() {
		try {
			if(this.data!=null && !this.data.isEmpty()) {
				Iterator<E> itr=this.data.iterator();
				return itr;
			}
			else throw new NullPointerException("Errore, Drive vuoto o non presente");
		}
		catch(NullPointerException e) {
			System.out.println(e.getMessage());
			return null;
		}
		
	}
}
