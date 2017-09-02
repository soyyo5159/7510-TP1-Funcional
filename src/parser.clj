(ns parser)



(defn- parser-coleccion
    [separador funcion s]
    (if (re-matches separador s)
        (let [
            separado (str/split s separador)
            parseados (map separado parsear)
        ]
            
            (agregar-errores (funcion parseados) parseados)
            
            
        )
        (error (funcion (list)) (str "no se puede parsear porque no se tiene el separador:" separador)
    )
)

(def parser-base [s] (partial  (re-pattern "\\.") base-de-datos))
(def parser-conjuncion [s] (partial  (re-pattern ",") conjuncion))
(def parser-disjuncion [s] (partial  (re-pattern ";") disjuncion))

(def ^{:private true} parsers (list
    parser-base
    parser-inferencia
    parser-conjuncion
    parser-disjuncion
    parser-premisa
    (error "No se puede parsear")
))

(defn parsear [s]
    "Parsea un string. Descubre qué es."
    (let [limpio (limpiar s)]
        (some (fn [p] (p s)) parsers);;como puedo hacer para quedarme solo con el ultimo error?
    )
)