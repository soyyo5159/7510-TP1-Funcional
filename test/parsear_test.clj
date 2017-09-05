(ns parsear-test
    (:require [clojure.test :refer :all]
        [parsear :refer :all]
        [Premisa :refer :all]
        [Coleccion :refer :all]
        [BaseDeDatos :refer :all]
        [Inferencia :refer :all]
        [Error :refer :all])
)


(deftest parsear-premisa
    (testing "una premisa bien armada"
        (is (= 
            (parsear "hola(uno,dos)")
            (premisa "hola" "uno" "dos")
        ))
    )
    (testing "una premisa rara"
        (is (= 
            (parsear "_(1,2)")
            (premisa "_" "1" "2")
        ))
    )
    (testing "un argumento"
        (is (= 
                (parsear "gran_ciudad(buenos_aires)")
                (premisa "gran_ciudad" "buenos_aires") 
        ))
    )
    (testing "mayusculas"
        (is (= 
            (parsear "p(X,Y,Z,Q)")
            (premisa "p" "X" "Y" "Z" "Q")
        ))
    )
    (testing "una premisa mal armada"
        (is  (error? (parsear "hola(;,dos)")) )
    )
    (testing "una premisa mal armada con comas"
        (is  (error? (parsear "hola(a,,do,s,,,)")) )
    )
)

(deftest parsear-coleccion-base
    (testing "disyuncion"
        (is (= 
            (parsear "hola(p);chau(p)")
            (disyuncion (premisa "hola" "p") (premisa "chau" "p"))
        ))
    )
    (testing "conjuncion"
        (is (= 
            (parsear "hola(p),chau(p)")
            (conjuncion (premisa "hola" "p") (premisa "chau" "p"))
        ))
    )

    (testing "conjuncion muchas comas"
        (is (= 
            (parsear "hola(p,q),chau(p,q)")
            (conjuncion (premisa "hola" "p" "q") (premisa "chau" "p" "q"))
        ))
    )

    (testing "conjuncion demasiadas comas"
        (is (error?
            (parsear "hola(p,q),chau(p,q),,,,")
        ))
    )

    (testing "conjuncion-disjuncion"
        (is (= 
            (parsear "h(p),h(q);c(p),c(q)")
            (disyuncion (parsear "h(p),h(q)") (parsear "c(p),c(q)"))
        ))
    )
    
    (testing "base"
        (let [
            inferencia (parsear "saludo(X):-chau(X);hola(X)")
        ]
            (is (= 
                (parsear "hola(p).chau(p).saludo(X):-chau(X);hola(X).")
                
                (base-de-datos (premisa "hola" "p") (premisa "chau" "p") inferencia)
            ))
        )
    )
    (testing "coleccion mal armada casi"
        (is  (error? (parsear "h(p).h(q),x(k);y(m).")) )
    )
    (testing "coleccion mal armada cualquier cosa"
        (is  (error? (parsear "buenas(amigos) soy Obi(Wan,kenobi). No me caen bien.")) )
    )
)

(deftest parsear-inferencia
    (testing "inferencia con disyuncion"
        (is (= 
            (parsear "saludo(X):-hola(X);chau(X)")
            (inferencia 
                (premisa "saludo" "X" )
                (disyuncion (premisa "hola" "X") (premisa "chau" "X"))
            )
        ))
    )
    (testing "inferencia con conjuncion"
        (is (= 
            (parsear "saludo(X):-hola(X),chau(X)")
            (inferencia 
                (premisa "saludo" "X" )
                (conjuncion (premisa "hola" "X") (premisa "chau" "X"))
            )
        ))
    )
    (testing "inferencia con conjuncion y mas args"
        (is (= 
            (parsear "saludo(X,Y):-hola(X,Y),chau(X,Y)")
            (inferencia 
                (premisa "saludo" "X" "Y" )
                (conjuncion (premisa "hola" "X" "Y") (premisa "chau" "X" "Y"))
            )
        ))
    )
)