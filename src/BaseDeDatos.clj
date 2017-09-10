(ns BaseDeDatos
    (:require 
        [Cumplible :as c]
        [Coleccion :refer :all]
        [Verificador :as v]
    ))

(defprotocol BaseDeDatos
    (verifica? [yo otro-cumplible])    
)

(defn- respondedor [] "evitar problemas de nombres" false)

(defn- verificador [pregunta repreguntar]
    (fn [verificable] 
        (v/verifica? verificable pregunta repreguntar)
    )
)

(defn- verifica-pregunta? [bd negables pregunta]
    "Devuelve si el bd verifica la pregunta"
    (let [
        repreguntar (respondedor bd (cons pregunta negables))
        verifica-esta (verificador pregunta repreguntar)
    ]
        (c/cumple? bd verifica-esta)
    )
)

(defn- respondedor [bd negables]
    "Si la pregunta está en negables, el respondedor devuelve false.
    Si no, le pregunta a la base de datos si verifica la pregunta"
    (fn [p] 
        (if (some #{p} negables)
            false
            (verifica-pregunta? bd negables p)
        )
    )
)

(defn- verificador-preguntas [ bd]
    (partial verifica-pregunta? bd (list))
)

(defrecord ^:private RecordBaseDeDatos [bd] 
    
    BaseDeDatos
    (verifica? [yo otro-cumplible]

        {:pre [satisfies? c/Cumplible otro-cumplible]}
        (c/cumple? otro-cumplible (verificador-preguntas bd))
    )
)

(defn base-de-datos [ & componentes]
    {:pre [
        (every? (partial satisfies? v/Verificador) componentes)
    ]}
    (->RecordBaseDeDatos (apply disyuncion componentes))
)