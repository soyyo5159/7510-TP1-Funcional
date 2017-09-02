(comment
(ns tratar-input-test
    (:require [clojure.test :refer :all]
              [tratar-input :refer :all]
              [Lifn :refer :all]
              [Espia :refer :all]))
(def ayuda "Ingrese una nueva pregunta o q para salir")

(defn- responder [pregunta]
    (case pregunta
        "llueve?" "si"
        "esta mojado?" "si"
        "hay sol?" "no"
        "playa?" "no"
    )
)

(deftest sin-preguntas
  (testing "no pregunto nada"
    (let [ 
        inputLifn (nuevoLifn "q")
        input (funcion inputLifn)
        output (fn [x] nil)

        espia (nuevoEspia)
        inputS (envuelta  espia input  "i")
        outputS (envuelta espia output "o")
        ]
        (tratar-input inputS outputS responder) 
        (is (= (registro? espia) (list
            "i"
        )))
    )
  )
)



(deftest pregunto-llueve
    (testing "pregunto si llueve y salgo"
        (let [ 
            inputLifn (nuevoLifn "llueve?" "q")
            input (funcion inputLifn)
            output (fn [x] nil)
            
            espia (nuevoEspia)
            inputS (envuelta  espia input  "i")
            outputS (envuelta espia output "o")
            ]
            (tratar-input inputS outputS responder) 
            (is (= (registro? espia) (list
                "i"
                "o" "si"
                "o" ayuda
                "i"
            )))
        )
    )
)

(deftest pregunto-llueve-varias-veces
    (testing "pregunto si llueve varias veces y salgo"
        (let [ 
            inputLifn (nuevoLifn "llueve?" "llueve?" "llueve?" "q")
            input (funcion inputLifn)
            output (fn [x] nil)
            
            espia (nuevoEspia)
            inputS (envuelta  espia input  "i")
            outputS (envuelta espia output "o")
            ]
            (tratar-input inputS outputS responder) 
            (is (= (registro? espia) (list
                "i"
                "o" "si"
                "o" ayuda
                "i"
                "o" "si"
                "o" ayuda
                "i"
                "o" "si"
                "o" ayuda
                "i"
            )))
        )
    )
)
(deftest pregunto-negativas-varias-veces
    (testing "pregunto por negativas y positivas"
        (let [ 
            inputLifn (nuevoLifn "llueve?" "hay sol?" "esta mojado?" "q")
            input (funcion inputLifn)
            output (fn [x] nil)
            
            espia (nuevoEspia)
            inputS (envuelta  espia input  "i")
            outputS (envuelta espia output "o")
            ]
            (tratar-input inputS outputS responder) 
            (is (= (registro? espia) (list
                "i"
                "o" "si"
                "o" ayuda
                "i"
                "o" "no"
                "o" ayuda
                "i"
                "o" "si"
                "o" ayuda
                "i"
            )))
        )
    )
)
)