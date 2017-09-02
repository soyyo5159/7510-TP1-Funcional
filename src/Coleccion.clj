(ns coleccion)

(defrecord ^:private Coleccion [combinador cumplibles]
    Cumplible
    (cumple? [yo fun]
        (combinador (fn [x] (cumple? x fun)) cumplibles)
    )    
)

(defn conjuncion [cumplibles]
    (Coleccion. every? cumplibles)    
)

(defn disyuncion [cumplibles]
    (Coleccion. some? cumplibles)    
)