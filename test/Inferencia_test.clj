
(ns Inferencia-test
    (:require [clojure.test :refer :all]
    
        [Inferencia :refer :all]
        [Premisa :refer :all]
        [Verificador :refer :all]
        [Coleccion :refer :all]
    )
)

(defn siempretrue [& args] true)
(defn siemprefalse [& args] false)
(defn jerosi-juanno [p]
    (= p (premisa "amigo" "jero"))
)
(defn jerojuan [p]
    (some #{p} (list
        (premisa "amigo" "jero")
        (premisa "amigo" "juan")
    ))
)

(deftest inferencia-verifica
    (let [
        amigos-conjuntos (inferencia (premisa "amigos" "X" "Y") (conjuncion 
            (premisa "amigo" "X") 
            (premisa "amigo" "Y")
        ))
        amigos-disjuntos (inferencia (premisa "amigos" "X" "Y") (disyuncion 
            (premisa "amigo" "X") 
            (premisa "amigo" "Y")
        ))
    ]
   (testing "no coincide encabezado"
        (is (not (verifica? 
            amigos-conjuntos
            (premisa "calido" "naranja")
            siempretrue
        )))
    )
    (testing "coincide encabezado, varia repreguntar"
        (is (not (verifica? 
            amigos-conjuntos
            (premisa "amigos" "juan" "jero")
            siemprefalse
        )))
        (is  (verifica? 
            amigos-conjuntos
            (premisa "amigos" "juan" "jero")
            siempretrue
        ))
    )
    (testing "preguntas correctas verifican conjuncion"
        (is (not (verifica? 
            amigos-conjuntos
            (premisa "amigos" "juan" "jero")
            jerosi-juanno
        )))
        (is  (verifica? 
            amigos-conjuntos
            (premisa "amigos" "juan" "jero") 
            jerojuan
        ))
    )
    (testing "preguntas correctas verifican disyunción"
        (is (verifica? 
            amigos-disjuntos
            (premisa "amigos" "juan" "jero")
            jerosi-juanno
        ))
        (is  (verifica?
            amigos-disjuntos
            (premisa "amigos" "juan" "jero")
            jerojuan
        ))
    )
    (testing "preguntas correctas verifican conjuncion aunque mantenga responder"
        (is (verifica?
            amigos-conjuntos
            (premisa "amigos" "jero" "jero")
            jerosi-juanno
        ))
    )
    )
)