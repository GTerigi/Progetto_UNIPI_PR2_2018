package ArrayList;

import java.util.Iterator;

public interface UserInterface<E> {
	
	/* REQUIRES: data!=null
	 * THROW: if data==null Throw NullPointerException
	 * MODIFIES: this.data
	 * EFFECTS: this.data=this.data U data2
	 */
	//Aggiunge alla collezione il dato passato come argomento.
	public void SetData(E data2);

	/* REQUIRES: data!=null
	 * THROWS: if data==null throw NullPointerException
	 * MODIFIES: this.data
	 * EFFECTS: this.data = old(this.data) - {data}
	 */
	//Rimuove dalla collezione il dato, se presente.
	public boolean RemoveData(E data);
	
	/* Restituisce true se è uno user è già presente */
	/* REQUIRES: name!=null
	 * THROWS: Se name==null lancia NullPointerException
	 * EFFECT: if(name.equals(this.user) return true
	 */
	//Controlla che il nome dell'utente passato come argomento sia uguale all'utente selezionato.
	public boolean equalsOwner(String newname);
	
	/* REQUIRES: password!=null
	 * THROWS: Se password==null lancia NullPointerException || se password!=this.password lancia una IllegalPasswordException
	 * RETURN: if(password.equals(this.password) return true 
	 */
	//Restituisce true se le due password sono identiche.
	public boolean MatchPassword(String password);
	
	/* REQUIRES: data!=null.
	 * THROWS: NullPointerException se data==null.
	 * RETURN: data.size.
	 */
	//Restituisce la dimensione del drive dell'utente corrente.
	public int GetSize();
	
	/* REQUIRES: this.user!=null
	 * THROWS: this.password==null throws NullPointerException
	 * RETURNS: Shallow copy of this.user
	 */
	//Restituisce il nome dell'utente.
	public String GetUser();
	
	/* REQUIRES: this.password!=null
	 * THROWS: this.password==null throws NullPointerException
	 * RETURNS: Shallow copy of this.password
	 */
	//Restituisce la copia della password
	public String GetPassword();
	
	/* REQUIRES: this.data!=null && this.data not empty && this.data U nomedata = this.data
	 * THROWS: Se this.data==null || this.data is empty throw new NullPointerException
	 * 		   Se this.data U nomedata != this.data throw new DataNotFoundException
	 * RETURNS: L'elemento relativo a nomedata
	 */
	//Restituisce l'elemento se presente nella collezione dati.
	public E GetData(E nomedata);
	
	/* REQUIRES: this.data!=null && this.data not empty
	 * THROWS: Se this.data==null || this.data is empty throw new NullPointerException
	 * RETURN: L'iteratore al drive selezionato.
	 */
	//Restituisce l'iteratore al Drive dell'utente.
	public Iterator<E> GetIterator();
	
}
