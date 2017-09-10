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

(defn parsear [] "esto permite llamados recursivos" nil)


(defn- parsear-separando [reconocer separar funcion s]
    "Parsea un string a partir del constructor del 
    ente que representa y funciones que permiten separar sus partes"
    (if (reconocer s);(re-matches forma s)
        (let [
            separado (separar s)
            parseados (map parsear separado)
        ]
            (apply fmap funcion parseados)
        )
        (error (str "no se puede parsear porque no tiene la forma correcta:" s) )
    )
)
(defn- parser [reconocer separar funcion]
    (partial parsear-separando reconocer separar funcion)
    
)

(defn- parser-regex [reconocimiento separador funcion]
    (let [
        reconocer (fn [s] (re-matches (re-pattern reconocimiento) s) )
        separar (fn [s] (str/split s (re-pattern separador)))
    ]
        (parser reconocer separar funcion)
    )
)

(defn validar-token [s] 
    (if (re-matches (re-pattern "[a-zA-Z0-9_]+") s)
        s
        (error (str "no es un token valido" s))
    )
)
(defn- separar-conjunciones [s]
    (map 
        (fn [s] (str s ")")) 
        (str/split s #"\),")
    )
)

(def ^:private  parser-conjuncion
    (parser
        (fn [s] (re-matches #".*\),.*" s) )
        separar-conjunciones
        conjuncion
    )
)

(def ^:private parsers (list
    (parser-regex "(.*\\.)+" "\\." base-de-datos )
    (parser-regex ".*:-.*" ":-" inferencia)
    ;;no se verifica que las variables de las inferencias tengan mayuscula

    (parser-regex ".*;.*" ";" disyuncion )
    parser-conjuncion

    (parser-regex "^.+\\(.+\\)$" "[,\\(\\)]" premisa)
    validar-token
))

(defn parsear [s]
    "Parsea un string. Descubre qué es.
    Devuelve el error del componente más pequeño que tenga error,
    o el mayor componente posible. El 'tamaño' de un 'componente' 
    está dado por su posición en la lista de parsers"
    
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