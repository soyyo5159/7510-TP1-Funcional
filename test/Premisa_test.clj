(ns Premisa-test
  (:require [clojure.test :refer :all]
    [Premisa :refer :all]
))
;;no respeto la regla de prolog, 
;;permito que los identificadores sean todo lo que cumple [a-zA-Z0-9_]+

(deftest creacion
  (testing "un solo argumento"
    (let [premisa (premisa "cumple" "jeronimo")]
        (is (= (nombre premisa) "cumple"))
        (is (= (argumentos premisa) (list "jeronimo")))
        (is (= (aridad premisa) 1))
    )
  )
    (testing "5 argumentos"
      (let [premisa (premisa "soy" 1 "buen" "tipo" "0" "?")]
          (is (= (nombre premisa) "soy"))
          (is (= (argumentos premisa) (list 1 "buen" "tipo" "0" "?")))
          (is (= (aridad premisa) 5))
      )
    )
)

(deftest probar-misma-forma
    (testing "misma forma"
        (is (misma-forma? 
            (premisa "amigos" "luis" "arnaldo") 
            (premisa "amigos" "mauri" "cristi")
        ))
    )
    (testing "misma forma mayusculas"
        (is (misma-forma?
            (premisa "amigos" "luis" "arnaldo") 
            (premisa "amigos" "X" "Y")
        ))
    )
    (testing "mismo nombre otra aridad"
        (is (not (misma-forma? 
            (premisa "amigos" "luis" "arnaldo") 
            (premisa "amigos" "luis" "arnaldo" "jose" "juan" 7 8 9)
        )))
    )
    (testing "misma aridad otro nombre"
        (is (not (misma-forma?
            (premisa "amigos" "luis" "arnaldo") 
            (premisa "enemigos" "luis" "arnaldo") 
        )))
    )
)

(deftest traduccion
    (testing "reemplazo todas funcion"
        (is (=
            (premisa "siguiente" 2 3)
            (traducida (premisa "siguiente" 1 2) inc)
        ))
    )
    (testing "reemplazo todas mapa"
        (is (=
            (premisa "siguiente" 1 2)
            (traducida (premisa "siguiente" "1" "2") {"1" 1, "2" 2})
        ))
    )

    (testing "reemplazo algunas"
        (is (=
            (premisa "siguiente" 1 2 3 4 5 6)
            (traducida (premisa "siguiente" "1" "2" 3 4 5 6) {"1" 1, "2" 2})
        ))
    )

    (testing "reemplazo ninguna"
        (is (=
            (premisa "siguiente" 10 20 30 40)
            (traducida (premisa "siguiente" 10 20 30 40) {"1" 1, "2" 2})
        ))
    )   
)

(deftest emparejamiento
    (testing "emparejamiento"
        (is (=
            (emparejar-variables (premisa 1 2 3) (premisa 4 5 6))
            {2 5, 3 6}
        ))
    )
)