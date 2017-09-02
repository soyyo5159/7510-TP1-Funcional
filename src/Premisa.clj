(ns Premisa)
;;requiere verifica y Verificable


(defrecord ^:private RecordPremisa [nombre argumentos] 
    Cumplible
    (cumple? [yo fun] (fun yo))
    Verificador
    (verifica? [yo pregunta repreguntar] (= yo pregunta))
)


(defn premisa 
    [nombre & argumentos] 
    (RecordPremisa. nombre (if (nil? argumentos) (list) argumentos))
)