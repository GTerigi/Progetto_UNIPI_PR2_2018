(*SINTASSI ASTRATTA*)
type ide = string;;

type exp =
    |Den of ide
    |Int of int
    |String of string
    |Boolean of bool
    |Sum of exp * exp
	|Dif of exp * exp
	|Molt of exp * exp
	|Div of exp * exp
    |Maggiore of exp * exp
	|Minore of exp * exp
	|Uguale of exp * exp
    |Not of exp
	|Or of exp * exp
	|And of exp * exp
    |IfThenElse of exp * exp * exp
    |Let of ide * exp * exp
    |LetRec of ide * ide list * exp * exp
    |Dict of (ide * exp) list
    |SelectDict of exp * ide
	|InsertDict of exp * ide * exp
	|RemoveDict of exp * ide
    |Fun of ide list * exp
	|FunAc of exp * exp list
    |ApplyOver of exp * exp;;


(*AMBIENTE*)

type env = (ide*evT) list

and evT =
    |EInt of int
    |EBool of bool
	|EString of string
    |Unbound
    |Funval of ide list * exp * env
	|FunRecVal of ide * ide list * exp * env
    |Dictionary of (ide * evT) list
    |EType of string ;;

(*OPERAZIONI*)

let sEnV=[];;

(*Binding di una coppia valore-identificatore nell'ambiente*)
let rec bind (ambiente,identificatore,valore) = (identificatore,valore)::ambiente;;


let rec bindList (e, i, v) = match (i,v) with   (*e=environment     i=identificatore    v=valore*)
	|([],[]) -> e
	|(x::xs,y::ys)-> bindList(bind(e,x,y),xs,ys)
	|_ -> failwith("Errore nella bindlist");;

(*Scorre l'ambiente per trovare il valore relativo a quell'identificatore i*)
let rec lookIde(e, i) = match e with
                        |(ide,valore)::envir -> if ide = i then valore else lookIde(envir,i)
                        |_ -> failwith("Nessun identificatore valido");;

(*TYPE CHECKING*)

let typechecking (t, valor) = match t with
                                |"Int" -> (match valor with
                                            |EInt u ->EBool(true)
                                            |_ -> EBool(false))
                                |"Boolean" -> (match valor with
                                            |EBool u ->EBool(true)
                                            |_ -> EBool(false))
                                |"String" -> (match valor with
                                            |EString u ->EBool(true)
                                            |_ -> EBool(false))
                                |"Dizionario" -> (match valor with
                                            |Dictionary u ->EBool(true)
                                            |_ -> EBool(false))
                                |"Fun" -> (match valor with
                                            |Funval(_,_,_) ->EBool(true)
                                            |_ -> EBool(false))
                                |"FRec" -> (match valor with
                            			    |FunRecVal(_,_,_,_) -> EBool(true)
                            			    |_ -> EBool(false))
                                |_ -> failwith("typecheck fallito");;


(*EVALUATION*)                  (**)

let rec eval ((e:exp),(r:env)):evT = match e with   (*Eval prende una coppia di valori espressione*ambiente e fa il pattern matching dell'espressione*)
        |Den(i) -> lookIde(r,i)     (*identificatore*)
        |Int(n) -> EInt(n)          (*intero*)
        |String(s) -> EString(s)    (*Stringa*)
        |Boolean(b) -> EBool(b)     (*Booleano*)
        |Sum(n1,n2) -> let n1= eval(n1,r) in        (*Somma fra due interi*)
                            let n2= eval(n2,r) in
                                (match (n1,n2) with
                                    |(EInt n1, EInt n2) -> EInt(n1+n2)
                                    |_ -> failwith("Errore, non sono interi"))

        |Dif(n1,n2) -> let n1= eval(n1,r) in        (*Differenza fra due interi*)
                            let n2= eval(n2,r) in
                                (match (n1,n2) with
                                    |(EInt n1, EInt n2) -> EInt(n1-n2)
                                    |_ -> failwith("Errore, non sono interi"))

        |Molt(n1,n2) -> let n1= eval(n1,r) in        (*Moltiplicazione fra due interi*)
                            let n2= eval(n2,r) in
                                (match (n1,n2) with
                                    |(EInt n1, EInt n2) -> EInt(n1*n2)
                                    |_ -> failwith("Errore, non sono interi"))

        |Div(n1,n2) -> let n1= eval(n1,r) in        (*Divisione fra due interi*)
                            let n2= eval(n2,r) in
                                (match (n1,n2) with
                                    |(EInt n1, EInt n2) -> EInt(n1/n2)
                                    |_ -> failwith("Errore, non sono interi"))

        |Maggiore(e1,e2) -> let v1 = eval(e1,r) in
    						let v2 = eval(e2,r) in (match (v1,v2) with
    						|(EInt v1, EInt v2) -> EBool(v1 > v2)
    						|_ -> failwith("Errore, non sono interi"))
    	|Minore(e1,e2) -> let v1 = eval(e1,r) in
    						let v2 = eval(e2,r) in (match (v1,v2) with
    						|(EInt v1, EInt v2) -> EBool(v1 < v2)
    						|_ -> failwith("Errore, non sono interi"))
    	|Uguale(e1,e2) -> let v1 = eval(e1,r) in
    						let v2 = eval(e2,r) in (match (v1,v2) with
    						|(EInt v1, EInt v2) -> EBool(v1 = v2)
    						|(EBool v1, EBool v2) -> EBool(v1 = v2)
    						|(EString v1, EString v2) -> EBool(v1 = v2)
                            |_ -> failwith("Errore, non sono confrontabili"))


        |Not(b) -> let v = eval(b,r) in (match v with      (*Negazione di un booleano*)
    						|EBool e -> if e = true then EBool(false) else EBool(true)
    						|_ -> failwith("Non è un booleano"))

    	|Or(b1,b2) -> let v1 = eval(b1,r) in               (*OR logico di due booleani*)
    						let v2 = eval(b2,r) in (match (v1,v2) with
    						|(EBool true, _) -> EBool(true)
    						|(_, EBool true) -> EBool(true)
    						|(EBool false, EBool false) -> EBool(false)
    						|_ -> failwith("Non è un booleano"))

    	|And(b1,b2) -> let v1 = eval(b1,r) in              (*AND logico di due booleani*)
    						let v2 = eval(b2,r) in (match (v1,v2) with
    						|(EBool true, EBool true) -> EBool(true)
    						|(EBool b1, EBool b2) -> EBool(false)
    						|_ -> failwith("Non è un booleano"))

        |IfThenElse(guardia,then_body,else_body) -> let e1=eval(guardia,r) in   (*Operatore If then Else, se è vera la guardia fa la valutazione del then, altrimeni l'else*)
                            (match e1 with
                                |EBool true -> eval(then_body,r)
                                |EBool false -> eval(else_body, r)
                                |_ -> failwith("Non è un booleano"))

        |Let(iden,esp1,body) -> let val1 = eval(esp1,r) in eval(body,bind(r,iden,val1))    (*Keyword Let*)

        |LetRec(ide,param,body_fun,body_let) ->eval(body_let,bind(r,ide,FunRecVal(ide,param,body_fun,r)))   (*Keyword Let ricorsiva, tiene traccia del body della funzione e i suoi parametri*)

        |Fun(nome, esp) -> Funval(nome, esp, r)

        |FunAc(esp1,esp2) -> let funname=eval(esp1,r) in (match funname with
                    |Funval(q,b,r1) -> let qe = eval_list(esp2,r) in
                                        eval(b,bindList(r1,q,qe))
                    |FunRecVal(f,q,b,r1) -> let qe = eval_list(esp2,r) in
												let new_env = bind(r1,f,funname) in
													eval(b,bindList(new_env,q,qe))
                    |_ -> failwith("Non è una funzione"))

        |Dict(e1) -> let q = eval_Dict(e1,r) in Dictionary(q)

    	|SelectDict(e1,s) -> (match eval(e1,r) with
    						|Dictionary(q) -> retrieve(q,s)
    						|_ -> failwith("Non è un dizionario"))

    	|InsertDict(e1,s,e2) -> (match eval(e1,r) with
    						|Dictionary(q) -> let v = eval(e2,r) in
    											if retrieve(q,s) = Unbound then Dictionary((s,v)::q) (*Se l'identificatore non è presente allora viene inserito*)
    											else let newq = mod_Dict(q,s,v) in Dictionary(newq)  (*Se già presente nel dizionario viene sovrascritto*)
    						|_ -> failwith("Non è un dizionario"))
    	|RemoveDict(e1,s) -> (match eval(e1,r) with
    						|Dictionary(q) -> let newq = remove_Dict(q,s) in Dictionary(newq)
    						|_ -> failwith("Non è un dizionario"))
        |ApplyOver(e1,e2) -> ( match eval(e1,r) with
    						|Dictionary(q) -> let newq = applyFun(q,e2,r) in Dictionary(newq)
    						|_ -> failwith("Non è un Dizionario"))

and
    eval_list (q,r) = (match q with
        |[] ->  []
        |x::xs -> eval(x,r) :: eval_list(xs,r))
and
	mod_Dict (q,s,v) = (match q with
	|[] -> []
	|(i,v1)::qs -> if s = i then (i,v)::mod_Dict(qs,s,v) else (i,v1)::mod_Dict(qs,s,v))
and
	remove_Dict(q,s) = (match q with
	|[] -> []
	|(i,v)::qs -> if s = i then remove_Dict(qs,s) else (i,v)::remove_Dict(qs,s))
and
	retrieve (q,s) = (match q with
	|[]->Unbound
	|(i,v)::qs -> if i = s then v else retrieve(qs,s))
and
	applyFun(q,f,r) = ( match q with
	|[] -> []
	|(i,EInt v)::qs -> (i,eval(FunAc(f,[Int(v)]),r))::applyFun(qs,f,r)
	|(i,EString v)::qs -> (i,eval(FunAc(f,[String(v)]),r))::applyFun(qs,f,r)
	|(i,EBool v)::qs -> (i,eval(FunAc(f,[Boolean(v)]),r))::applyFun(qs,f,r)
	|_ -> failwith("Non è una funzione"))
and
	eval_Dict (q,r) = (match q with
		|[]->[]
		|(iden,elem)::qs -> let v = eval(elem,r) in
						(iden,v)::eval_Dict(qs,r));;

(*TEST*)

(*Test operazioni base*)
eval(Sum(Int(5),Sum(Int(3),Int(5))),sEnV);;     (*Valutazione della somma fra 3 interi*)

eval(Dif(Int(12),Int(5)),sEnV);;                (*Valutazione della differenza fra 2 interi*)

eval(Molt(Int(3),Int(5)),sEnV);;                (*Valutazione della Moltiplicazione fra 2 interi*)

eval(Div(Int(50),Int(5)),sEnV);;                (*Valutazione della Divisione fra 2 interi*)


(*Test Booleani*)
eval(Let("a",Boolean(true),Or(Den("a"),Not(Den("a")))),sEnV);;      (*Valutazione di una tautologia a ∨ ¬a *)

eval(Let("a",Boolean(true),Let("b",Boolean(false),Let("c",Boolean(true), Or(And(Den("a"),Den("b")),And(Den("a"),Den("c")))))),sEnV);;   (*Valutazione di (a ∧ b) ∨ (a ∧ c) *)

eval(Let("a",Boolean(true),Let("b",Boolean(false),Let("c",Boolean(true), And(And(Den("a"),Den("b")),And(Den("a"),Den("c")))))),sEnV);;  (*Valutazione di (a ∧ b) ∧ (a ∧ c) *)


(*Test funzioni e IfThenElse*)

(*Esecuzione funzione: let Somma_a_tre a b c =a+b+c in Somma_a_tre 3 4 5*)
eval(Let("Somma_a_tre",Fun(["a";"b";"c"], Sum(Sum(Den("b"),Den("c")),Den("a"))),FunAc(Den("Somma_a_tre"),[Int(3);Int(4);Int(5)])),sEnV);;

(*Esecuzione funzione di Fibonacci ricorsiva: let rec Fibo n = if n < 2 then 1 else Fibo (n - 2) + Fibo (n - 1) in Fibo 10 *)
eval(LetRec("Fibo",["n"],IfThenElse(Minore(Den("n"),Int(3)),Int(1),Sum(FunAc(Den("Fibo"),[Dif(Den("n"), Int(2))]),FunAc(Den("Fibo"),[Dif(Den("n"), Int(1))]))),FunAc(Den("Fibo"),[Int(10)])),sEnV);;


(*Test Dizionario*)
eval(Let("Dictionary1",Dict([]),Den("Dictionary1")),sEnV);;

(*creazione di un dizionario con valori*)

eval(Let("Dictionary2",Dict([("nome",String("Giulio"));("matr",Int(550319))]),Den("Dictionary2")),sEnV);;

(*Accesso a un elemento del dizionario*)

eval(Let("Dictionary3",Dict([("nome",String("Giulio"));("matr",Int(445566))]),SelectDict(Den("Dictionary3"),"nome")),sEnV);;

eval(Let("Dictionary3",Dict([("nome",String("Giulio"));("matr",Int(445566))]),SelectDict(Den("Dictionary3"),"matr")),sEnV);;

(*Inserimento nel dizionario di età*)

eval(Let("MyDict",Dict([("name",String("Giovanni"));("matricola",Int(123456))]),
	 Let("MyDict2",InsertDict(Den("MyDict"),"età",Int(22)),Den("MyDict2"))),sEnV);;

(*Inserimento nel dizionario di name = "Massimo"*)

eval(Let("MyDict",Dict([("name",String("Giovanni"));("matricola",Int(123456))]),
	 Let("MyDict2",InsertDict(Den("MyDict"),"name",String("Massimo")),Den("MyDict2"))),sEnV);;

(*Eliminazione dal dizionario di name*)

eval(Let("MyDict",Dict([("name",String("Giovanni"));("matricola",Int(123456))]),
	 Let("MyDict2",InsertDict(Den("MyDict"),"età",Int(22)),
	 Let("MyDict3",RemoveDict(Den("MyDict2"),"name"),Den("MyDict3")))),sEnV);;

(*Esecuzione ApplyOver((fun x -> x+1), my_dict3)*)
eval(Let("MyDict",Dict([("name",String("Giovanni"));("matricola",Int(123456))]),
	 Let("MyDict2",InsertDict(Den("MyDict"),"età",Int(22)),
	 Let("MyDict3",RemoveDict(Den("MyDict2"),"name"),
	 Let("MyDict4",ApplyOver(Den("MyDict3"),Fun(["x"],Sum(Den("x"),Int(1)))),Den("MyDict4"))))),sEnV);;
