(ns parsear 
    (:require 
        [clojure.test :refer :all]
        [Premisa :refer :all]
        [Error :refer :all]
        [clojure.string :as str]
        [BaseDeDatos :refer :all]
        [Inferencia :refer :all]
        [Coleccion :refer :all]
        [Verificador :as v]
        [Cumplible :as c]
    )
)

(defn parsear [] nil)

(defn- parser-coleccion
    
    [forma separador funcion s]
    (if (re-matches forma s)
        (let [
            separado (str/split s separador)
            parseados (map parsear separado)
        ]
            (apply fmap funcion parseados)
        )
        (error (str "no se puede parsear porque no tiene la forma correcta:" s) )
    )
)
(defn- crear-parser [reconocimiento separador funcion] 
    (partial parser-coleccion 
        (re-pattern reconocimiento) 
        (re-pattern separador) 
        funcion
    )
)

(defn validar-token [s] 
    (if (re-matches (re-pattern "[a-zA-Z0-9_]+") s)
        s
        (error (str "no es un token valido" s))
    )
)

(defn parsear-conjuncion [s]
    (if (re-matches #".*\),.*" s)
        (let [
            partes (str/split s #"\),")
            partes-completas (map (fn [parte] (str parte ")")) partes)
            parseados (map parsear partes-completas)
        ]
            (apply fmap conjuncion parseados)
        )
        (error (str "no es una conjuncion: " s))
    )
)

(def ^{:private true} parsers (list
    (crear-parser "(.*\\.)+" "\\." base-de-datos )
    (crear-parser ".*:-.*" ":-" inferencia)
    ;;la inferencia tiene variables que tienen que empezar con mayuscula, 
    ;;acá arriba HABRÍA QUE AGREGARLO

    (crear-parser ".*;.*" ";" disyuncion )
    parsear-conjuncion

    (crear-parser "^.+\\(.+\\)$" "[,\\(\\)]" premisa)
    validar-token
))

(defn parsear [s]
    "Parsea un string. Descubre qué es."
    (comment
    (let [limpio (limpiar s)]
        (some (fn [p] (let [res (p limpio) ] (if (error? res) nil res)) ) parsers)
    )
    )
    
    (let [limpio (str/replace s (re-pattern "\\s") "")
        resultado (loop [ probar (first parsers) resto (rest parsers) ]
            (let [
                resultado (probar limpio)
            ]
                (if (or   (empty? resto) (nil? resto)  (not (error? resultado))  )
                    resultado
                    (recur (first resto) (rest resto))
                )
            )
        )
        ]
        resultado
    )
    
)