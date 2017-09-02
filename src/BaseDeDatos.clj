(ns BaseDeDatos
    (:require 
        [clojure.string :as str]
        [Premisa]
        [Inferencia]
        [Regla]
    ))


(defn- verifica-pregunta? [cumplible anteriores pregunta]
    (let [
        repreguntar (fn [p] (verifica-pregunta? cumplible (cons pregunta anteriores) p))
        cumplir (fn [verificable] (verifica? verificable pregunta repreguntar))
    ]
        (if (some #{pregunta} anteriores)
            false
            (cumple? cumplible cumplir)
        )
            
    )
)

(defrecord RecordBaseDeDatos [cumplible] 
    BaseDeDatos
    (verifica? [yo otro-cumplible] 
        (cumple? otro-cumplible (partial verifica-pregunta? cumplible (list)))
    )
)