
(ns Coleccion
    (:require 
        [Cumplible :refer :all]
    )
)

(def ^:private comb-conjuncion every?)
(defn- comb-disyuncion [fun l] 
    (if-let [ res (some fun l)]
        res
        false
    )
)

(defrecord ^:private RecordColeccion 
    [combinador cumplibles]
    Cumplible
    (cumple? [yo fun]
        (combinador (fn [x] (cumple? x fun)) cumplibles)
    )    
)

(defn- coleccion-segura [combinador & cumplibles]
    {:pre [
        (some? cumplibles)
        (not (empty? cumplibles))
        (every? (partial satisfies? Cumplible) cumplibles)
        ]
    }
    (RecordColeccion. combinador cumplibles)    
    
)

(def conjuncion (partial coleccion-segura comb-conjuncion))
(def disyuncion (partial coleccion-segura comb-disyuncion))