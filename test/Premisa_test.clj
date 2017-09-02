(ns Premisa-test
  (:require [clojure.test :refer :all]
    [Premisa :refer :all]
    [Regla :refer :all]))
;;no respeto la regla de prolog, 
;;permito que los identificadores sean todo lo que cumple [a-zA-Z0-9_]+

(deftest un-argumento
  (testing "un solo argumento"
    (let [premisa (parsear "cumple(jeronimo)")]
        
        (is (= (nombre premisa) "cumple"))
        (is (= (argumentos premisa) (list "jeronimo")))
        (is (= (aridad premisa) 1))
    )
  )
)

(deftest dos-argumentos
    (testing "dos argumentos"
      (let [premisa (parsear "cumple(jeronimo,juan)")]
          
          (is (= (nombre premisa) "cumple"))
          (is (= (argumentos premisa) (list "jeronimo" "juan")))
          (is (= (aridad premisa) 2))
      )
    )
)

(deftest todos-numeros
    (testing "todos son numeros"
      (let [premisa (parsear "21(22,23)")]
          
          (is (= (nombre premisa) "21"))
          (is (= (argumentos premisa) (list "22" "23")))
          (is (= (aridad premisa) 2))
      )
    )
)

(deftest cinco-argumentos
    (testing "5 argumentos"
      (let [premisa (parsear "soy(1,buen,tipo,0,quilombo)")]
          
          (is (= (nombre premisa) "soy"))
          (is (= (argumentos premisa) (list "1" "buen" "tipo" "0" "quilombo")))
          (is (= (aridad premisa) 5))
      )
    )
)

(deftest con-espacios
    (testing "dos argumentos"
      (let [premisa (parsear "cumple ( jeronimo   , juan) ")]
          
          (is (= (nombre premisa) "cumple"))
          (is (= (argumentos premisa) (list "jeronimo" "juan")))
          (is (= (aridad premisa) 2))
      )
    )
)

(deftest erroneo-sin-parentesis-final
    (testing "sin parentesis final"
      (let [premisa (parsear "cumple(jeronimo,juan")]
          
          (is (= premisa nil))
      )
    )
)

(deftest supermal
    (testing "cualquier cosa"
      (let [premisa (parsear "como están muchachos, todo bien ustedes?")]
        (is (= premisa nil))
      )
    )
)

(deftest bien-con-simbolos
    (testing "bien pero con simbolos"
      (let [premisa (parsear "+3jjk?78(¿¿?,97,pe!rro;)")]
        (is (= premisa nil))
      )
    )
)

(deftest casi-bien-nombre
    (testing "el nombre casi bien"
      (let [premisa (parsear "josé(45,27)")]
        (is (= premisa nil))
      )
    )
)


(deftest reemplazo-todos
    (testing "Reemplazo todos los argumentos"
        (let [
            premisa (parsear "asd(a,b,c,d)")
            premisa-nueva (reemplazar-args (zipmap (list "a" "b" "c" "d") (list 1 2 3 4)) premisa)
        ]
            (is (= (argumentos premisa-nueva) (list 1 2 3 4)))
        )
    )    
)

(deftest reemplazo-ninguno
    (testing "Reemplazo ningún argumento"
        (let [
            premisa (parsear "asd(a,b,c,d)")
            premisa-nueva (reemplazar-args (zipmap (list "x" "y" "z" 3) (list 1 2 3 4)) premisa)
        ]
            (is (= (argumentos premisa-nueva) (list "a" "b" "c" "d")))
        )
    )    
)

(deftest reemplazo-algunos
    (testing "Reemplazo algunos argumentos"
        (let [
            premisa (parsear "asd(a,b,c,d)")
            premisa-nueva (reemplazar-args (zipmap (list "a" "b" "z" 3) (list "1" "2" "3" "4")) premisa)
        ]
            (is (= (argumentos premisa-nueva) (list "1" "2" "c" "d")))
        )
    )    
)