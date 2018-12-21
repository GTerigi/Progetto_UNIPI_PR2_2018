package ArrayList;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;

public class TestList{

	public static void main(String[] args){
		long startTime = System.currentTimeMillis();
		MyDriveList<String> test=new MyDriveList<String>();				//Il mio Drive, implemetato come Arraylist
		LinkedList<String> GetData=new LinkedList<String>();			//Lista per il recupero dati.
		
		System.out.println("Benvenuto nel tuo Drive su Terminale\n"
				+ "Digita uno dei seguenti comandi:\n"
				+ "1) Crea un nuovo User\n"
				+ "2) Aggiungi un elemento al Drive selezionato\n"
				+ "3) Restituisce l'Elemento selezionato dal Drive\n"
				+ "4) Rimuove l'elemento selezionato\n"
				+ "5) Copia l'elemento selezionato nel suo drive di origine\n"
				+ "6) Condivide l'elemento selezionato nel drive di un altro user\n"
				+ "7) Restituisce l'iteratore al Drive selezinato\n"
				+ "8) Restituisce una visione su terminale dei dati recuperati dai drive.\n"
				+ "9) Elimina l'account desiderato.");
		try {
			Scanner Filein=new Scanner(new File("Test.txt"));
			Filein.useDelimiter("\n");
			while(Filein.hasNext()) {
				int command=Filein.nextInt();
				
		        while(command<10 && command>0) {
		        		String UselessLine=Filein.nextLine();				//Recupera la newline dal file di input
		                switch(command) {
		                        case 1:{
		                                String[] user=Filein.nextLine().split(" ");
		                                test.createUser(user[0], user[1]);
		                                System.out.println("Utente "+ user[0] +" inserito"); 
		                                break;
		                        }
		                        case 2:{
		                                String[] user=Filein.next().split(" ");
		                                if(test.put(user[0],user[1],user[2])) System.out.println("Il dato " + user[2] + " è stato inserito nel drive di "+ user[0]+"\nDimenzione attuale: " + test.getSize(user[0],user[1]));
		                                break;
		                        }
		                        case 3:{
		                                String[] user=Filein.next().split(" ");
		                                String tmp=test.get(user[0],user[1],user[2]);
		                                if(tmp!=null) GetData.add(tmp);		                                
		                                break;
		                        }
		                        case 4:{
		                                String[] user=Filein.next().split(" ");
		                                test.remove(user[0],user[1],user[2]);
		                                break;
		                        }
		                        case 5:{
		                                String[] user=Filein.next().split(" ");
		                                test.copy(user[0],user[1],user[2]);
		                                break;
		                        }
		                        case 6:{
		                                String[] user=Filein.next().split(" ");
		                                test.share(user[0],user[1],user[2], user[3]);
		                                break;
		                        }
		                        case 7:{
		                                String[] user=Filein.next().split(" ");
		                                Iterator<String> iter=test.getIterator(user[0],user[1]);
		                                if(iter!=null) {
		                                        System.out.print("[");
		                                        while(iter.hasNext()) {
		                                                String tmp=iter.next();
		                                                System.out.print(tmp);
		                                                if(iter.hasNext()) System.out.print(" ");
		                                        }
		                                        System.out.println("]");
		                                }
		                                break;

		                        }
		                        case 8:{
		                                if(GetData!=null && !GetData.isEmpty()) System.out.println("Ecco i dati recuperati: "+ GetData);
		                                else System.out.println("Nessun File recuperato");
		                                break;

		                        }
		                        case 9:{
		                        	  String[] user=Filein.nextLine().split(" ");
		                              test.removeUser(user[0], user[1]);
		                              break;
		                        }
		                }
		                command=Filein.nextInt();
		        }

			}
	        Filein.close();
			
		}
		catch(FileNotFoundException e) {
			System.out.println(e);
		}
		catch(InputMismatchException e) {
			System.out.println(e);
		}
		System.out.println("Grazie per aver provato il Drive su Terminale");
		long endTime = System.currentTimeMillis();
		System.out.println("Esecuzione terminata in "+(endTime - startTime) + " ns");
		System.exit(0);
	}

}
