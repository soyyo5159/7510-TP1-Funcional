(ns BaseDeDatos
    (:require 
        [Cumplible :as c]
        [Coleccion :refer :all]
        [Verificador :refer :all]
    ))

(defprotocol BaseDeDatos
    (bd-verifica? [yo otro-cumplible])    
)

(defn- verifica-pregunta? [cumplible anteriores pregunta]
    (let [
        repreguntar (fn [p] (verifica-pregunta? cumplible (cons pregunta anteriores) p))
        cumplir (fn [verificable] (verifica? verificable pregunta repreguntar))
    ]
        (if (some #{pregunta} anteriores)
            false
            (c/cumple? cumplible cumplir)
        )
            
    )
)

(defrecord ^:private RecordBaseDeDatos [cumplible] 
    BaseDeDatos
    (bd-verifica? [yo otro-cumplible]
        {:pre [satisfies? c/Cumplible otro-cumplible]}
        (c/cumple? otro-cumplible (partial verifica-pregunta? cumplible (list)))
    )
)

(defn base-de-datos [ & componentes]
    {:pre [
        (every? (partial satisfies? Verificador) componentes)
    ]}
    (RecordBaseDeDatos. (apply disyuncion componentes))
)