(ns inferencia)

(defrecord ^:private RecordInferencia [encabezado coleccion]
    Cumplible
    (cumple? [yo fun] (fun yo))

    Verificador
    (verifica? [yo pregunta repreguntar]
        (if (misma-forma? encabezado pregunta)
            (let [
                intercambio (emparejar-variables encabezado pregunta)
            ]
                (cumple? (mappeada coleccion intercambio) repreguntar )
            )
            false
        )

    )
)

(defn inferencia [premisa-encabezado coleccion]
    (RecordInferencia. premisa-encabezado coleccion))