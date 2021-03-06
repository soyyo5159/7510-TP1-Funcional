(ns BaseDeDatos-test
    (:require 
        [clojure.test :refer :all]
        [BaseDeDatos :refer :all]
        [Premisa :refer :all]
        [Inferencia :refer :all]
        [Coleccion :refer :all]
    )
)

(def base
    (base-de-datos
        (premisa "gato" "pom")
        (premisa "gato" "tigre")
        (premisa "gato" "luigi")
        (premisa "gato" "groot")
        (premisa "perro" "leon")
        (premisa "perro" "guardian")
        (premisa "perro" "gunther")
        (premisa "jugueton" "guardian")
        (premisa "jugueton" "groot")
        (premisa "jugueton" "juancito")
        (inferencia (premisa "peludo_jugueton" "X") (conjuncion
            (premisa "peludo" "X")
            (premisa "jugueton" "X")
        ))
        (inferencia (premisa "gato_jugueton" "X") (conjuncion
            (premisa "gato" "X")
            (premisa "jugueton" "X")
        ))
        (inferencia (premisa "peludo" "X") (disyuncion
            (premisa "gato" "X")
            (premisa "perro" "X")
        ))
    )
)

(deftest base-encadenada
    (testing "premisa contenida es si"
        (is (verifica? base (premisa "gato" "pom")))
    )
    (testing "premisa no contenida es no"
        (is (not (verifica? base (premisa "perro" "pom"))))
    )
    (testing "premisa no contenida es no - aridad"
        (is (not (verifica? base (premisa "gato" "pom" "gunther"))))
    )
    (testing "disyuncion basica se cumple"
        (is (verifica? base (premisa "peludo" "pom")))
        (is (not (verifica? base (premisa "peludo" "jose"))))
    )
    (testing "conjuncion basica se cumple"
        (is (verifica? base (premisa "gato_jugueton" "groot")))
        (is (not (verifica? base (premisa "gato_jugueton" "pom"))))
    )
    (testing "conjuncion compleja se cumple"
        (is (verifica? base (premisa "peludo_jugueton" "groot")))
        (is (not (verifica? base (premisa "peludo_jugueton" "pom"))))
        (is (not (verifica? base (premisa "peludo_jugueton" "juancito"))))
    )
)
(deftest multiple-aridad
    (def bd (base-de-datos
        (premisa "letras" "a")
        (premisa "letras" "a" "b")
        (premisa "letras" "a" "b" "c")
    ))
    (testing "multiple aridad no causa problemas"
        (is (verifica? bd (premisa "letras" "a" "b")))
        (is (not (verifica? bd (premisa "letras" "c"))) )
    )
)
(deftest recursividad
    (def bd (base-de-datos
        (premisa "amigos" "juan" "jero")
        (inferencia (premisa "amigos" "X" "Y") (premisa "amigos" "Y" "X") )
    ))
    (testing "inferencia recursiva"
        (is (verifica? bd (premisa "amigos" "juan" "jero")))
        (is (verifica? bd (premisa "amigos" "jero" "juan")))
        (is (not (verifica? bd (premisa "amigos" "pinocho" "jero"))) )
    )
)
(deftest consultar-colecciones
    (def bd (base-de-datos
        (premisa "a" "x")
        (premisa "a" "y")
        (premisa "b" "x")
        (premisa "b" "y")
        (inferencia (premisa "c" "X") (conjuncion (premisa "a" "X") (premisa "b" "X")))
    ))
    (testing "consultar conjuncion"
        (is (verifica?  bd (conjuncion
            (premisa "a" "x") (premisa "b" "x") (premisa "c" "x") 
        )))
        (is (not (verifica?  bd (conjuncion
            (premisa "a" "x") (premisa "b" "x") (premisa "c" "w") 
        ))))

        (is (verifica?  bd (disyuncion
            (premisa "a" "x") (premisa "b" "x") (premisa "c" "x") 
        )))
        (is  (verifica?  bd (disyuncion
            (premisa "a" "x") (premisa "b" "x") (premisa "c" "w") 
        )))
        (is (not (verifica?  bd (disyuncion
            (premisa "a" "w") (premisa "b" "w") (premisa "c" "w") 
        ))))
        
    )
)