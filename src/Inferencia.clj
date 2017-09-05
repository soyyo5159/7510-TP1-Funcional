(ns Inferencia (:require
    [Verificador :refer :all]
    [Premisa :refer :all]
    [Cumplible :refer :all]
))

(defrecord ^:private RecordInferencia [encabezado coleccion]
    Cumplible
    (cumple? [yo fun] (fun yo))

    Verificador
    (verifica? [yo pregunta repreguntar]
        (if (misma-forma? encabezado pregunta)
            (let [
                intercambio (emparejar-variables encabezado pregunta)
                traducir-repreguntar (fn [p] (repreguntar (traducida p intercambio)))
            ]
                (cumple? coleccion traducir-repreguntar)
            )
            false
        )

    )
)

(defn inferencia [premisa-encabezado coleccion]
    {:pre [
        (satisfies? Premisa premisa-encabezado) 
        (satisfies? Cumplible coleccion)
    ]}
    (RecordInferencia. premisa-encabezado coleccion)
)